import java.util.ArrayList;

public class BTree {
	
	BTreeNode root;
	int height;
	int degree;
	int nodeKey;
	
	BTree(int degree){
		this.root = new BTreeNode(this.degree, 0);
		this.root.isLeaf = true;
	}
	
	public void BTreeInsert(TreeObject key){
		
	}
	
	public void BTreeInsertNonFull(TreeObject key){
		
	}
	
	public void BTreeSplitChild(BTreeNode parent, BTreeNode child){
		
	}
	
	private class BTreeNode{
		private int numKeys;						// # of keys in Node
		private int maxNumKeys;						// maximum # of keys per node
		private int numChildren;					// # of children
		private int maxNumChildren;					// maximum # of children
		private int parent;							// parent of node
		private ArrayList<TreeObject> keys;			// ArrayList of TreeObjects
		private ArrayList<Integer> children;		// ArrayList of children
		
		private boolean isLeaf;
		
		BTreeNode(){
			this.numKeys = 0;
			
		}
		
		BTreeNode(int degree, int nodeKey){
			this.numKeys = 0;
			this.maxNumChildren = maxNumKeys + 1;
			this.keys = new ArrayList<TreeObject>(maxNumKeys);
			this.children = new ArrayList<Integer>(maxNumChildren);
			this.isLeaf = false;
		}
	}

}
