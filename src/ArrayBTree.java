import java.util.ArrayList;

/**
 * Created by princekannah on 4/20/16.
 * Working project: P4_Bioinfomatics.
 */
public class ArrayBTree {
    public static final int INT_BYTES = 4;
    public static final int LONG_BYTES = 8;
    public static final int TREEOBJECT_BYTES = 12;
    private final int MAX_NUM_KEYS; //the maximum number of keys per node in this BTree
    private final int MAX_NUM_CHILDREN; //the maximum number of children per node
    private final int DEGREE;

    private BTreeNode root;
    private int height, debug, sequenceLength, degree;

    public ArrayBTree(int degree, int sequenceLength, int debug) {
        DEGREE = degree;
        this.sequenceLength = sequenceLength; //the DNA sequence length this BTree is for
        MAX_NUM_KEYS = ((2 * DEGREE) - 1);
        MAX_NUM_CHILDREN = MAX_NUM_KEYS + 1;
        this.debug = debug;

        //creating the root
        root = new BTreeNode(false); //root is initially empty with no children
//        BTreeNode temp = new BTreeNode(false);
        //TODO: write temp to disk (i.e binary file)
//        root = temp;
    }

    private class BTreeNode {
        private int numberOfKeys, keysIndexer;
        private boolean isLeaf;
        private int parent, nodeKey;									// parent of node
        //		private int nodeKey;								// Index/key of node
//        private ArrayList<TreeObject> keys;			// ArrayList of TreeObjects
//        private ArrayList<Integer> children;		// ArrayList of children
        private TreeObject[] keys;
        private BTreeNode[] children;

        BTreeNode(boolean leaf) {
            keys = new TreeObject[MAX_NUM_KEYS];
            numberOfKeys = keysIndexer = 0;
            isLeaf = leaf;

            if(isLeaf())
                children = null;
            else
                children = new BTreeNode[MAX_NUM_CHILDREN];
        }

        public boolean isLeaf() {return isLeaf;}

        public boolean isFull() {
            return keysIndexer == MAX_NUM_KEYS;
        }

        public void insertNonFull(TreeObject k) {
            int i = numberOfKeys - 1;

            if(isLeaf())
            {
                //move all keys greater than k to the right by one
                while (i >= 0 && keys[i].getKey() > k.getKey())
                {
                    keys[i + 1] = keys[i];
                    i--;
                }

                keys[i + 1] = k;
                numberOfKeys++;
                //TODO write node to disk
                //TODO return handle to where we dropped k (index?)
            }
            else
            {
                //find the child to descend into
                while (i >= 0 && keys[i].getKey() > k.getKey()) i--;

                i++;
                //TODO perform disk read on children[i]
                //TODO children[i].diskread();

                if(children[i].isFull())
                {
                    children[i].splitChild(null, i); //TODO come back after fixing splitChild()
                    if(keys[i].getKey() < k.getKey()) i++;
                }

                //TODO children[i].insertnonfull(k)
            }
        }

//        public void insert(TreeObject k)
//        {
//            if(BTree.this.root.isFull()) {
//                BTree.BTreeNode temp = new BTree.BTreeNode();
//                BTree.this.root = temp;
////                children.set(0,root); TODO TALK ABOUT STORING CHILDREN AS BTNodes
//                BTree.this.root.splitChild();
//            }
//            else
//                return;
//            BTree.this.root.insertNonFull(k);
//
//        }

        public void splitChild(BTreeNode parent, int i) {
            BTreeNode z = new BTreeNode(true);

            for (int j = 0; j < DEGREE - 1; j++){
                z.keys[j] = keys[j + DEGREE];
                keys[j + DEGREE] = null;
            }

            if(!isLeaf())
            {
                for (int j = 0; j < DEGREE ; j++){
                    z.children[j] = children[j + DEGREE];
                    children[j + DEGREE] = null;
                }
            }

            numberOfKeys = DEGREE - 1;

            for (int j = parent.numberOfKeys; j >= i + 1 ; j--) {
                parent.children[j + 1] = parent.children[j];
            }
            parent.children[i + 1] = z;

            for (int j = parent.numberOfKeys - 1; j >= i; j--) {
                parent.keys[j+1] = parent.keys[j];
            }

            parent.keys[i] = keys[DEGREE - 1];
            keys[DEGREE - 1] = null;

            //TODO write node to disk
            //TODO write z to disk
            //TODO write parent to disk
        }
    }

}
