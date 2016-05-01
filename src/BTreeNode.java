import java.util.ArrayList;

public class BTreeNode {
    ArrayList<TreeObject> keys;
    ArrayList<Integer> children;
    static int NUM_NODES; //use to keep track of the total number of nodes
    private int nodeIndex, parentIndex, maxKeyCount, degree;
    private boolean isRoot, isLeaf; //use to indicate if  a node is either a leaf and  root

    public BTreeNode(int degree, boolean isRoot, boolean isLeaf, int nodeIndex) {
        this.nodeIndex = nodeIndex;
        this.isLeaf = isLeaf;
        this.degree = degree;
        this.isRoot = isRoot;

        if (isRoot()) {
            parentIndex = -1;
        }

        maxKeyCount = ((degree * 2) - 1);
        keys = new ArrayList<>(maxKeyCount);
        children = new ArrayList<>(maxKeyCount + 1);

        NUM_NODES++;
    }

    public boolean isFull() {
        return keys.size() == maxKeyCount;
    }

    public void setIndex(int index){nodeIndex = index;}

    public boolean isRoot() {
        return isRoot;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public int getNodeIndex() {
        return nodeIndex;
    }

    public int getMaxKeyCount() {
        return maxKeyCount;
    }


    public void setParent(int parentIndex) {
        this.parentIndex = parentIndex;
    }

    public void setRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }

    public void setIsLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public int getParentIndex() {
        return parentIndex;
    }


    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("KEYS:\n");
        int count = 0;
        for (int i = 0; i < keys.size() ; i++) {
            if(count % 4 == 0)
                builder.append("\n");
            builder.append(keys.get(i));
        }
        return builder.toString();
    }
}
