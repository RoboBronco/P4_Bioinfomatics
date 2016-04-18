import java.util.ArrayList;

public class BTree {
	
	public static final int INT_BYTES = 4;
	public static final int LONG_BYTES = 8;
	public static final int TREEOBJECT_BYTES = 12;
	
	BTreeNode root;
	int height;				// Height of BTree
	int degree;				// Degree of BTree
	private int debug;		// Debug level
	int k;					// Sequence length
	int numNodes;
	
	BTree(int degree, int k, int debug, String DNASequence){
		this.root = new BTreeNode(this.degree, 0);
		this.degree = degree;
		this.k = k;
        numNodes = 1;
	}
	
	public void BTreeInsert(TreeObject key){
		
	}
	
	public void BTreeInsertNonFull(TreeObject key){
		
	}
	
	public void BTreeSplitChild(BTreeNode parent, BTreeNode node, int nodeIndex){
		
	}
	
	public int Search(TreeObject key){
		return 1;
	}
	
	private class BTreeNode{
		int numKeys;								// # of keys in Node
		int maxNumKeys;								// maximum # of keys per node
		int numChildren;							// # of children
		int maxNumChildren;							// maximum # of children
		int parent;									// parent of node
		int nodeKey;								// Index/key of node
		int degree;
		private ArrayList<TreeObject> keys;			// ArrayList of TreeObjects
		private ArrayList<Integer> children;		// ArrayList of children
		
		private boolean isLeaf;
		
		BTreeNode(int degree, int nodeKey){
			this.numKeys = 0;
			this.maxNumKeys = (2*degree) - 1;
			this.maxNumChildren = maxNumKeys + 1;
			this.degree = degree;
			this.isLeaf = isLeaf;
			this.keys = new ArrayList<TreeObject>(maxNumKeys);
			this.children = new ArrayList<Integer>(maxNumChildren);
		}
		
		public boolean isLeaf(BTreeNode node){
			if(node.numChildren == 0){
				return true;
			}
			return false;
		}
		
		public boolean isFull(BTreeNode node){
			if(node.numKeys == maxNumKeys){
				return true;
			}
			return false;
		}
	}

}
