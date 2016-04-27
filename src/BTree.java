import java.io.File;
import java.io.IOException;

public class BTree {
    BTreeNode root, current, next;
    private TreeWriter writer;
    private int degree, numNodes, seqLength;

    public BTree(int degree, int length, String fileName, TreeObject o) {
        this.degree = degree;
        seqLength = length;
        File file = new File(fileName);
        if(file.exists())
            file.delete();

        root = new BTreeNode(degree, true, true, 0);
        root.keys.add(0,o);
        numNodes++;

        System.out.println("Created BTree with root node: " +root.getNodeIndex()
        +" Seq: " + ConvertDNAToLong.convertFromLong(o.getKey()));
        writer = new TreeWriter(fileName, degree, length); //setup file to write to
    }

    public BTree(File file, int degree, int seqLength) {
        writer = new TreeWriter(file.getName(), degree, seqLength);
        try {
            this.degree = writer.readDegree();
            int rootIndex = writer.readRootIndex();
            this.seqLength = writer.readSeqLength();

            if (degree != this.degree && seqLength != this.seqLength)
                root = writer.diskRead(rootIndex,degree);

            root.setRoot(true);
        } catch (IOException e) {
            System.out.println("can't open file");
        }
    }



    private void splitChild(BTreeNode parent, BTreeNode splitNode, int index) {
        if(parent.isFull() || !splitNode.isFull())
            throw new IllegalStateException();

        System.out.println("Child split with parent " + parent.getNodeIndex() + ", split indexes: " +
                splitNode.getNodeIndex() +" & " + index);

        BTreeNode newChild = new BTreeNode(degree, false, splitNode.isLeaf(), numNodes);
        numNodes++;

        System.out.println("New child created: " + newChild.getNodeIndex() + " -> Parent: " +parent.getNodeIndex());
        for (int p = splitNode.keys.size() - 1; p >= degree - 1; p--) {
            newChild.keys.add(0, splitNode.keys.remove(p));
        }

        if (!splitNode.isFull()) {
            for (int j = splitNode.children.size() - 1; j >= degree; j--) {
                newChild.children.add(0, splitNode.children.remove(j));
            }
        }

        parent.children.add(index + 1, newChild.getNodeIndex());
        newChild.setParent(parent.getNodeIndex());

        writer.diskWrite(parent, false);
        writer.diskWrite(splitNode, false);
        writer.diskWrite(newChild, false);
    }

    private void insertNonfull(TreeObject o) throws IOException
    {
        current = root;
        Long treeKey = o.getKey();
            int index = current.keys.size() - 1;
            if (current.isLeaf()) {
                while (index >= 0 && treeKey.compareTo(current.keys.get(index).getKey()) <= 0) {
                    if (treeKey.compareTo(current.keys.get(index).getKey()) == 0) {
                        current.keys.get(index).increaseFrequency();
                        writer.diskWrite(current, false);
                        //TODO PRINT DEBUG INFO
                        System.out.println("Seq: " +
                                ConvertDNAToLong.convertFromLong(treeKey) +" Freq: " + o.getFrequency()
                        + " Node:" + current.getNodeIndex());
                        return;
                    }
                    index--;
                }
                current.keys.add(index + 1, o);
                //TODO print DEBUG INFO
                writer.diskWrite(current, false);
            } //END OF current.isLef()
            else {
                while (index >= 0 && treeKey.compareTo(current.keys.get(index).getKey()) <= 0) {
                    if (treeKey.compareTo(current.keys.get(index).getKey()) == 0) {
                        current.keys.get(index).increaseFrequency();
                        writer.diskWrite(current, false);
                        //TODO PRINT DEBUG INFO
                        return;
                    }
                    index--;
                }
                index++;
                next = writer.diskRead(current.children.get(index), degree);
                if (next.isFull()) {
                    splitChild(current, next, index);
                    if (treeKey.compareTo(current.keys.get(index).getKey()) == 0) {
                        current.keys.get(index).increaseFrequency();
                        writer.diskWrite(current, false);
                        //TODO PRINT DEBUG INFO
                        return;
                    } else if (treeKey.compareTo(current.keys.get(index).getKey()) > 0) {
                        next = writer.diskRead(current.children.get(index + 1), degree);
                    }
                }
                current = next;
            } //END of else block
//        }
    }

    public void insert(TreeObject o) throws IOException {
        if (root.isFull()) {
            next = root;

            root = new BTreeNode(degree, true, false, numNodes++);
            root.children.add(0, next.getNodeIndex());
            next.setParent(root.getNodeIndex());
            next.setRoot(false);

            System.out.println("New root created: " + root.getNodeIndex());

            splitChild(root, next, 0);
            insertNonfull(o);
        } else
            insertNonfull(o);
    }

    private int nodeSearch(BTreeNode node, TreeObject searchKey)
            throws IOException {
        current = node;
            int i = 0;

            while (i < current.keys.size()
                    && current.keys.get(i).getKey().compareTo(searchKey.getKey()) < 0) {
                i++;
            }

            if (i < current.keys.size()
                    && current.keys.get(i).getKey().compareTo(searchKey.getKey()) == 0) {
                return current.keys.get(i).getFrequency();
            }

            if (current.isLeaf()) {
                return 0; // case where sequence is not found
            } else {
                current = writer.diskRead(current.children.get(i),degree);
            }
        return -1;
    }

    public TreeWriter getWriter(){return writer;}
    public int search(TreeObject o) throws IOException {
        return nodeSearch(root, o);
    }

    public static void main(String[] args) {
        BTree tree = new BTree(5, 5, "TEST", new TreeObject(1240));
        System.out.println(tree.numNodes);

        try {
            for (int i = 0; i < 5; i++) {
                tree.insert(new TreeObject((long)i));
            }
            tree.insert(new TreeObject(ConvertDNAToLong.convertToLong("atcga")));
            tree.insert(new TreeObject(ConvertDNAToLong.convertToLong("atcag")));
            tree.insert(new TreeObject(ConvertDNAToLong.convertToLong("attga")));
            tree.insert(new TreeObject(ConvertDNAToLong.convertToLong("atcga")));
            tree.insert(new TreeObject(ConvertDNAToLong.convertToLong("aaaaa")));
            tree.insert(new TreeObject(ConvertDNAToLong.convertToLong("atcta")));
            tree.insert(new TreeObject(ConvertDNAToLong.convertToLong("atgga")));
            tree.insert(new TreeObject(ConvertDNAToLong.convertToLong("attga")));
            tree.insert(new TreeObject(ConvertDNAToLong.convertToLong("accga")));
            tree.insert(new TreeObject(ConvertDNAToLong.convertToLong("atcga")));
            tree.insert(new TreeObject(ConvertDNAToLong.convertToLong("atcga")));
            tree.insert(new TreeObject(ConvertDNAToLong.convertToLong("atcga")));
            tree.insert(new TreeObject(ConvertDNAToLong.convertToLong("atcga")));
            tree.insert(new TreeObject(ConvertDNAToLong.convertToLong("atcga")));

//            System.out.println("SEARCH RESULTS: " + tree.search(t));
            System.out.println("SIZE AFTER: " + tree.writer.getF().length());
        } catch (IOException E) {}
//        BTree bTree = new BTree(new File("TEST"),5,12);
    }
}
