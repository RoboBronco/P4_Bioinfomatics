import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class TreeWriter {
    public static final int INT = 4, LONG = 8, BOOLEAN = 4;
    public static final int META_DATA = INT * 3;
    private RandomAccessFile outfile;
    private int degree, length;

    public TreeWriter(String fileName, int degree, int seqLength) {
        length = seqLength;
        this.degree = degree;

        try {
            outfile = new RandomAccessFile(new File((fileName)), "rw");
            System.out.println("SIZE: " +outfile.length());
        } catch (IOException e){}
    }

    public long getLength() throws IOException{return outfile.length();}


    public BTreeNode diskRead(int index, int degree) throws  IOException {
        int numKeys;
        BTreeNode temp = new BTreeNode(1, false, false, index);
        temp.setDegree(degree);
        temp.setIndex(index);
        temp.setRoot(false);
        temp.setMaxNumKeys((2 * degree) - 1);
        ByteBuffer buff = ByteBuffer.allocate(4 + 4 + 4 + temp.getMaxNumKeys()
                * (8 + 4) + (temp.getMaxNumKeys() + 1) * (4));

        outfile.seek(getNodeOffSet(index));
        outfile.read(buff.array());

		// Read meta data
        temp.setParent(buff.getInt());

        if (buff.getInt() == 1) {
            temp.setIsLeaf(true);
        } else {
            temp.setIsLeaf(false);
        }
        numKeys = buff.getInt();

		/* Read keys */
        temp.keys = new ArrayList<>(numKeys);

        for (int k = 0; k < temp.getMaxNumKeys(); k++) {
			/* Read in each key and store it in the list. */
            if (k < numKeys) {
                temp.keys.add(k, new TreeObject(buff));
            }
			//Skip past each empty key
            else {
                buff.getLong();
                buff.getInt();
            }
        }
        temp.children = new ArrayList<>(numKeys + 1);

        if (!temp.isLeaf()) {
            for (int c = 0; c < temp.getMaxNumKeys() + 1; c++) {
				/* Read in each child and store it in the list. */
                if (c < numKeys + 1) {
                    temp.children.add(c, buff.getInt());
                }
                else
                    buff.getInt();
            }
        }
        BTreeNode returnNode = temp;
        if (temp != null)
            diskWrite(temp, false);
        return returnNode;
    }

    public void diskWrite(BTreeNode treeNode, boolean writeRoot) {
        if (treeNode.isRoot() && !writeRoot) return;

        ByteBuffer buff = ByteBuffer.allocate(4 + 4 + 4 + treeNode.getMaxNumKeys()
                * (8 + 4) + (treeNode.getMaxNumKeys() + 1) * (4));
//        ByteBuffer buff = ByteBuffer.allocate(
//                META_DATA + treeNode.getMaxNumKeys() * (META_DATA) + (treeNode.getMaxNumKeys() + 1) * (INT));
        try {
            outfile.seek(getNodeOffSet(treeNode.getNodeIndex()));
        } catch (IOException e) {
        }

        buff.putInt(treeNode.getParentIndex());

        if (treeNode.isLeaf())
            buff.putInt(1);
        else
            buff.putInt(0);
        buff.putInt(treeNode.keys.size());

        //writing all keys in the node
        for (int i = 0; i < treeNode.getMaxNumKeys(); i++) {
            if (i < treeNode.keys.size()) {
                //TODO get tree object in here
                buff.putLong(treeNode.keys.get(i).getKey());
                buff.putInt(treeNode.keys.get(i).getFrequency());
            } else {
                buff.putLong(-1);
                buff.putInt(-1);
            }
        }

        //writing children
        for (int i = 0; i < treeNode.getMaxNumKeys() + 1; i++) {
            if (i < treeNode.children.size()) {
                buff.putInt(treeNode.children.get(i));
            } else
                buff.putInt(-1);
        }

        try {
            outfile.write(buff.array());
        } catch (IOException e) {}
    }

    public int getNodeSize() {
        int numMaxKeys = (2 * degree) - 1;
        int sizeOfMetaData = (INT * 2) + BOOLEAN;
        int sizeOfKey = (12) * numMaxKeys;
        int childSize = INT * (numMaxKeys + 1);
        return sizeOfMetaData + sizeOfKey + childSize;
    }

    public int getNodeOffSet(int index) {
        return META_DATA + (getNodeSize()) * index;
    }

    public void writeMetaData(int rootIndex) {
        try {
            outfile.seek(0L);
            outfile.writeInt(degree);
            outfile.writeInt(rootIndex);
            outfile.writeInt(length);
        } catch (IOException e) {
        }
    }

    public void flushTree(BTreeNode current, BTreeNode next, BTreeNode root) {
//        TODO assert (root != null);
        if (current != null) {
            diskWrite(current, true);
        }
        if (next != null)
            diskWrite(next, true);

        diskWrite(root, true);
        writeMetaData(root.getNodeIndex());
    }

    public RandomAccessFile getF(){return outfile;}

    public int readDegree() throws IOException {
        outfile.seek(4L);
        return outfile.readInt();
    }

    public int readRootIndex() throws IOException {
        outfile.seek(4L);
        return outfile.readInt();
    }

    public int readSeqLength() throws IOException {
        outfile.seek(8L);
        return outfile.readInt();
    }
}
