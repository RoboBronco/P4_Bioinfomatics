import java.io.*;
import java.util.ArrayList;
import java.util.Stack;

public class BTree {
    BTreeNode root, current, next;
    private TreeWriter writer;
    private int degree, numNodes, seqLength;
    private int debug;

    public BTree(int degree, int length, String fileName, int debug)
    {
        this.degree = degree;
        seqLength = length;
        this.debug = debug;
        File f = new File(fileName);
        f.delete();

        try {
            writer = new TreeWriter(fileName, this.degree, this.seqLength); //setup file to write to
        }catch (IOException ignored){}

        root = new BTreeNode(this.degree, true, true, 0);
        numNodes = 1;
    }

    public BTree(File bTreeFile, int degree, int seqLength, int debug) {
        try {
            writer = new TreeWriter(bTreeFile.getName(), degree, seqLength);
        }catch (IOException ignored){}

        this.debug = debug;

        try
        {
            this.degree = writer.readDegree();
            int rootIndex = writer.readRootIndex();
            this.seqLength = writer.readSeqLength();

            if (this.degree != degree && seqLength != this.seqLength)
                throw new IllegalStateException("The specified degree & sequence length doesn't match the ones in the file");

            root = writer.diskRead(rootIndex,this.degree);
            root.setRoot(true);
        } catch (IOException e) {System.out.println("can't open bTreeFile");}
    }



    private void splitChild(BTreeNode parent, BTreeNode nodeToSplit, int index) {
        if(parent.isFull() || !nodeToSplit.isFull())
            throw new IllegalStateException();

        if(debug == 0)
            System.out.println("Child split with parent node " + parent.getNodeIndex() + ", split indexes: " +
                nodeToSplit.getNodeIndex() +" & " + index);

        BTreeNode newChild = new BTreeNode(degree, false, nodeToSplit.isLeaf(), numNodes);
        numNodes++;

        if(debug == 0)
            System.out.println("New child node: " + newChild.getNodeIndex() + " -> Parent: " +parent.getNodeIndex());

        for (int p = nodeToSplit.keys.size() - 1; p > degree - 1; p--) {
            newChild.keys.add(0, nodeToSplit.keys.remove(p));
        }

        if (!nodeToSplit.isLeaf()) {
            for (int j = nodeToSplit.children.size() - 1; j >= degree; j--) {
                newChild.children.add(0, nodeToSplit.children.remove(j));
            }
        }

        parent.children.add(index + 1, newChild.getNodeIndex());
        newChild.setParent(parent.getNodeIndex());
        parent.keys.add(index, nodeToSplit.keys.remove(degree - 1));

        try {
            writer.diskWrite(parent, false);
            writer.diskWrite(nodeToSplit, false);
            writer.diskWrite(newChild, false);
        } catch (IOException e){e.printStackTrace();}
    }

    private void insertNonfull(TreeObject o) throws IOException
    {
        current = root;
        Long treeKey = o.getKey();

        while (true)
        {
            int index = current.keys.size() - 1;
            if (current.isLeaf())
            {
                while (index >= 0 && treeKey.compareTo(current.keys.get(index).getKey()) <= 0)
                {
                    if (treeKey.compareTo(current.keys.get(index).getKey()) == 0)
                    {
                        current.keys.get(index).increaseFrequency();
                        writer.diskWrite(current, false);
                        //TODO PRINT DEBUG INFO
                        if(debug == 0)
                            System.out.println("Seq: " +
                                ConvertDNAToLong.convertFromLong(treeKey) + ", Freq: "
                                    + current.keys.get(index).getFrequency()
                                + ", Node: " + current.getNodeIndex());
                        return;
                    }
                    index--;
                }
                current.keys.add(index + 1, o);
                if(debug == 0)
                    System.out.println("Inserted " + ConvertDNAToLong.convertFromLong(treeKey) +
                 " at node " + current.getNodeIndex() );

                writer.diskWrite(current, false);
                break;
            } //END OF current.isLef()
            else
            {
                while (index >= 0 && treeKey.compareTo(current.keys.get(index).getKey()) <= 0)
                {
                    if (treeKey.compareTo(current.keys.get(index).getKey()) == 0)
                    {
                        current.keys.get(index).increaseFrequency();
                        writer.diskWrite(current, false);

                        //TODO PRINT DEBUG INFO
                        if(debug == 0)
                            System.out.println("Seq: " +
                                    ConvertDNAToLong.convertFromLong(treeKey) + ", Freq: " +
                                    current.keys.get(index).getFrequency()
                                    + ", Node: " + current.getNodeIndex());

                        return;
                    }
                    index--;
                }
                index++;
                next = writer.diskRead(current.children.get(index), degree);
                if (next.isFull())
                {
                    splitChild(current, next, index);
                    if (treeKey.compareTo(current.keys.get(index).getKey()) == 0)
                    {
                        current.keys.get(index).increaseFrequency();
                        writer.diskWrite(current, false);
                        //TODO PRINT DEBUG INFO
                        if(debug == 0)
                            System.out.println("Seq: " +
                                    ConvertDNAToLong.convertFromLong(treeKey) + ", Freq: " +
                                    current.keys.get(index).getFrequency()
                                    + ", Node: " + current.getNodeIndex());

                        return;
                    } else if (treeKey.compareTo(current.keys.get(index).getKey()) > 0)
                        next = writer.diskRead(current.children.get(index + 1), degree);
                }
                current = next;
            } //END of else block
        }
    }

    public void insert(TreeObject o) throws IOException {
        if (root.isFull()) {
            next = root;

            root = new BTreeNode(degree, true, false, numNodes++);
            root.children.add(0, next.getNodeIndex());
            next.setParent(root.getNodeIndex());
            next.setRoot(false);

            if(debug == 0)
                System.out.println("New root node: " + root.getNodeIndex());

            splitChild(root, next, 0);
            insertNonfull(o);
        } else
            insertNonfull(o);
    }

    private TreeObject nodeSearch(BTreeNode node, TreeObject searchKey)
            throws IOException {
        current = node;
        while (true) {
            int i = 0;
            while (i < current.keys.size()
                    && current.keys.get(i).getKey().compareTo(searchKey.getKey()) < 0){
                i++;
            }

            if (i < current.keys.size()
                    && current.keys.get(i).getKey().compareTo(searchKey.getKey()) == 0){
                return current.keys.get(i);
            }

            if (current.isLeaf()) {
                return null; // case where sequence is not found
            } else
                current = writer.diskRead(current.children.get(i), degree);
        }
    }

    public void writeTree() {
        try{
            writer.flushTree(current,next,root);
        } catch (IOException e){e.printStackTrace();}
    }

    public TreeObject search(TreeObject o) throws IOException {
        return nodeSearch(root, o);
    }

    public void writeDumpFile() throws IOException
    {
        boolean traversing = true;
        current = root;
        Stack<Integer> children = new Stack<>();
        Stack<Integer> treeNodes = new Stack<>();
        FileOutputStream outFileStream = new FileOutputStream(new File("dump.txt"));
        PrintWriter outStream = new PrintWriter(outFileStream);
        int childIndex = 0;

        while (traversing)
        {
            if (childIndex == current.children.size() && !current.isLeaf())
            {
                if (treeNodes.isEmpty() && children.isEmpty())
                {
                    traversing = false;
                    continue;
                }
                else
                {
                    current = this.writer.diskRead(treeNodes.pop(), degree);
                    childIndex = children.pop();

                    if (childIndex < current.keys.size())
                    {
                        outStream.write(current.keys.get(childIndex).toString());
                        outStream.println();
                    }
                    childIndex++;
                    continue;
                }
            }

            if (current.isLeaf())
            {
                for (int i = 0; i < current.keys.size() ; i++)
                {
                    outStream.write(current.keys.get(i).toString());
                    outStream.println();
                }

                if (current == root) break;
                current = this.writer.diskRead(treeNodes.pop(), degree);
                childIndex = children.pop();

                if (childIndex < current.keys.size())
                {
                    outStream.write(current.keys.get(childIndex).toString());
                    outStream.println();
                }
                childIndex++;
            }
            else
            {
                treeNodes.push(current.getNodeIndex());
                children.push(childIndex);
                current = writer.diskRead(current.children.get(childIndex),degree);
                childIndex = 0;
            }
        }
        outStream.close();
    }

    public static void main(String[] args) throws IOException {
        BTree tree = new BTree(3, 10, "TEST", 0);
        Parser p = new Parser();
        ArrayList<Long> nums = p.parse(new File("test1.gbk"),10);

        for (int i = 0; i < nums.size() ; i++) {
            tree.insert(new TreeObject(nums.get(i)));
        }

        tree.writeTree();
        tree.writeDumpFile();

        TreeObject t = new TreeObject(nums.get(0));

        TreeObject result = tree.search(t);

        System.out.println("\nRESULT: " + result);


    }
}
