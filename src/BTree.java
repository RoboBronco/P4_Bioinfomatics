import java.util.ArrayList;

public class BTree {
	public static final int INT_BYTES = 4;
	public static final int LONG_BYTES = 8;
	public static final int TREEOBJECT_BYTES = 12;
    private final int MAX_NUM_KEYS; //the maximum number of keys per node in this BTree
    private final int MAX_NUM_CHILDREN; //the maximum number of children per node

    private BTreeNode root;
	private int height, debug, sequenceLength;
	
	public BTree(int degree, int sequenceLength, int debug) {
		this.sequenceLength = sequenceLength; //the DNA sequence length this BTree is for
        MAX_NUM_KEYS = ((2 * degree) - 1);
        MAX_NUM_CHILDREN = MAX_NUM_KEYS + 1;

        //creating the root
        BTreeNode temp = new BTreeNode();
        //TODO: write temp to disk (i.e binary file)
        root = temp;
	}

//
//    public BTree(int degree) {
//        MAX_NUM_KEYS = ((2 * degree) - 1); //the maximum number of keys per node in this BTree
//        MAX_NUM_CHILDREN = MAX_NUM_KEYS + 1; //the maximum number of children per node
//    }
	
	public void BTreeInsert(TreeObject key) {
//		if(root == null)
//            root = new BTreeNode(key);
	}
	
	private void BTreeInsertNonFull(TreeObject key) {
		
	}
	
	private void BTreeSplitChild(BTreeNode parent, BTreeNode node, int nodeIndex){
//		BTreeNode tempNode = new BTreeNode(TreeObject)
	}

    private void BTreeSplitChild(TreeObject k, BTree x, int index){
//        BTreeNode z = new BTreeNode(k);
    }
	
	public int Search(TreeObject key){
		return 1;
	}
	
    private void splitChild(BTreeNode x, int nodeInex){

    }


    private class BTreeNode {
        private int numberOfKeys;
        private int parent, nodeKey;									// parent of node
        //		private int nodeKey;								// Index/key of node
        private ArrayList<TreeObject> keys;			// ArrayList of TreeObjects
        private ArrayList<Integer> children;		// ArrayList of children
        private boolean isLeaf;

        BTreeNode() {
            keys = new ArrayList<>(BTree.this.MAX_NUM_KEYS);
            children = new ArrayList<>(BTree.this.MAX_NUM_CHILDREN);
            numberOfKeys = keys.size();
        }

        public boolean isLeaf() {
            if(children.size() == 0)
                isLeaf = true;
            else
                isLeaf = true;

            return isLeaf;
//			if(node.numChildren == 0){
//				return true;
//				return false;
//			}
        }

        public boolean isFull() {
            return numberOfKeys == BTree.this.MAX_NUM_KEYS;
// if(node.numberOfKeys == maxNumKeys){
//				return true;
//			}
//			return false;
        }

        public void insertNonFull(TreeObject k) {
            int i = keys.size()  - 1;

            if(isLeaf())
            {
                //move all keys greater than k to the right by one
                while (i >= 0 && keys.get(i).getKey() > k.getKey())
                {
                    keys.set(i+1, keys.get(i));
                    i--;
                }
                keys.set(i+1, k);
                numberOfKeys = keys.size();
                //TODO write node to disk
                //TODO return handle to where we dropped k
            }
            else
            {
                //find the child to descend into
                while (i >= 0 && keys.get(i).getKey() > k.getKey()) i--;

                i++;
                children.get(i);
            }
        }

        public void insert(TreeObject k)
        {
            if(BTree.this.root.isFull()) {
                BTreeNode temp = new BTreeNode();
                BTree.this.root = temp;
//                children.set(0,root); TODO TALK ABOUT STORING CHILDREN AS BTNodes
                BTree.this.root.splitChild();
            }
            else
                return;
            BTree.this.root.insertNonFull(k);

        }


        public void splitChild(){}
    }

}
