import java.util.ArrayList;
import java.util.*;

public class BTree {

	private BTreeNode root, current, lChild, rChild;
	private int degree;									// Degree of BTree
	private int k;										// Sequence length
	private int numNodes;								// Number of nodes
	private int maxNumKeys;								// maximum # of keys per node
	private int maxNumChildren;							// maximum # of children
	private int currentNodeIndex;
	
	public BTree(int degree, int k){
		currentNodeIndex = 0;
		this.degree = degree;
		this.k = k;
        BTreeNode node = new BTreeNode();
        node.isLeaf = true;
		root = node;
		maxNumKeys = (2*degree) - 1;
		maxNumChildren = maxNumKeys + 1;
		numNodes = 1;
	}

	public void BTreeInsert(TreeObject key){
        BTreeNode node = root;
        if(node.getNumTreeObjects() == maxNumKeys)
        {
            BTreeNode newNode = new BTreeNode();
            
            newNode.isLeaf = false;
            root = newNode;
            BTreeSplitChild(newNode,1,node);
            BTreeInsertNonFull(newNode,key);
        }
        else
        {
            BTreeInsertNonFull(node,key);
        }
	}

	public void BTreeInsertNonFull(BTreeNode node, TreeObject key){
		int i = node.numKeys;	       
		if(node.isLeaf)
		{
			while(i >= 1 && key.getKey() < node.getTreeObject(i).getKey())
			{
				long tmpkey = node.getTreeObject(i).getKey();
				//node.getTreeObject(i+1).getKey() = node.getTreeObject(i).getKey();
				{
					node.getTreeObject(i).increaseFrequency();

					//write to disk;
				}
				i--;
			}
		}
		else
		{
			while(i >= 1 && key.getKey() < node.getTreeObject(i).getKey())
			{
				if(key.getKey() == node.getTreeObject(i-1).getKey())
				{

					node.getTreeObject(i-1).increaseFrequency();
					//write to disk

				}
				i--;
			}

		}
	}

	public void BTreeSplitChild(BTreeNode parent, int childKey, BTreeNode node){

	}

    public int getMaxNumChildren(){
    	return maxNumChildren;
    }
    
    public int getMaxNumKeys(){
    	return maxNumKeys;
    }
	
    public int getDegree()
    {
        return degree;
    }

    public BTreeNode getRoot()
    {
        return root;
    }
    
	/*public FoundNode Search(BTreeNode node, TreeObject key){
		int i = 1;

		if(node == null || node.numKeys < 1){
			return null;
		}

		while(i <= node.keys.size() && key.getKey() > node.keys.get(i).getKey()){
			i++;
		}
		if(i <= node.keys.size() && key.getKey() == node.keys.get(i).getKey()){
			return new FoundNode(node,i);
		}
		if(node.isLeaf){
			return null;
		}
		//return ;	
	}*/

	void diskRead(){
		
	}
	
	void diskWrite(){
		
	}
	
	public class FoundNode{

		BTreeNode foundnode;
		int key;

		public FoundNode(BTreeNode node, int key){
			this.foundnode = node;
			this.key = key;
		}

	}

	public class BTreeNode{

		private int numKeys;								// # of keys in Node
		private int numChildren;							// # of children
		private int parentNodeKey;							// parent of node
		private int nodeIndex;								// Index/key of node
		private ArrayList<TreeObject> keys;					// ArrayList of TreeObjects
		private ArrayList<Integer> children;				// ArrayList of children
		private boolean isLeaf;

		public BTreeNode(){
			numKeys = 0;
			numChildren = 0;
			nodeIndex = currentNodeIndex;
			keys = new ArrayList<TreeObject>(maxNumKeys);
			children = new ArrayList<Integer>(maxNumChildren);
			isLeaf = true;
			currentNodeIndex++;
		}
		
        public BTreeNode (int nodeIndex){
        	children = new ArrayList<Integer>();
            keys  = new ArrayList<TreeObject>();
            this.nodeIndex = nodeIndex;
        }
		public boolean isLeaf(){
			return isLeaf;
		}
		
        public int getNodeIndex()
        {
            return nodeIndex;
        }
		
		public void setTreeObject(TreeObject key, int index)
		{
			keys.add(index,key);
		}

		public TreeObject getTreeObject(int index)
		{
			return (TreeObject) keys.get(index);
		}

		public int getNumTreeObjects()
		{
			return keys.size();
		}

		public int getChild(int index)
		{
			return children.get(index);
		}
		
        public void setChild(int nodeKey, int index)
        {
            children.add(index, nodeKey);
        }

		public boolean isFull(){
			return (numKeys == maxNumKeys);
		}
	}

}
