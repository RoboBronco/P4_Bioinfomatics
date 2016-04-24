import java.util.ArrayList;
import java.util.*;

public class BTree {

	private BTreeNode root, current;
	private int degree;									// Degree of BTree
	private int k;										// Sequence length
	private int numNodes;								// Number of nodes
	private int maxNumKeys;								// maximum # of keys per node
	private int maxNumChildren;							// maximum # of children
	private int currentNodeIndex;
	private TreeWriter writer;
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
		writer = new TreeWriter("TEST_FILE");
		writer.writeTreeMetaData(numNodes,k,maxNumChildren,maxNumKeys);
	}


	public TreeWriter getFile(){
		return writer;
	}

	public void BTreeInsert(TreeObject key){
		// BOOK pg. 495
		BTreeNode node = root;
		if(node.getNumTreeObjects() == maxNumKeys)
		{
			// ALLOCATE NODE
			BTreeNode alNode = new BTreeNode();
			alNode.isLeaf = false;
			root = alNode;
			alNode.setChild(node.nodeIndex,0);
			BTreeSplitChild(alNode,1);
			BTreeInsertNonFull(alNode,key);
		}
		else
		{
			BTreeInsertNonFull(node,key);
		}
	}

	public void BTreeInsertNonFull(BTreeNode node, TreeObject key){
		// BOOK pg. 496
		int i = node.numKeys;
		boolean found = false;
		if(node.isLeaf)
		{
			while(i >= 1 && key.getKey() <= node.getTreeObject(i-1).getKey())
			{
                if(key.getKey() == node.getTreeObject(i-1).getKey())
                {
                	node.getTreeObject(i-1).increaseFrequency();
                    found = true;
                    //diskWrite(node);
                }
				i--;
			}
            if(!found)
            {
                node.setTreeObject(i,key);
                //diskWrite(node);
            }
		}
		else
		{
			while(i >= 1 && key.getKey() <= node.getTreeObject(i-1).getKey())
			{
				if(key.getKey() == node.getTreeObject(i-1).getKey())
				{

					node.getTreeObject(i-1).increaseFrequency();
					found = true;
					//diskWrite(node);

				}
				i--;
			}
			if(!found){
				BTreeNode child = diskRead(node.getChild(i));
                if(child.getNumTreeObjects() == maxNumKeys)
                {
                    //BTreeSplitChild();
                    if(key.getKey() == node.getTreeObject(i).getKey())
                    {
                    	node.getTreeObject(i).increaseFrequency();
                    	diskWrite(node);
                      	found = true;
                    }
                    if(key.getKey() > node.getTreeObject(i).getKey() && !found)
                    {
                        i++;
                        child = diskRead(node.getChild(i));
                    }
                }
                if(!found){
                	BTreeInsertNonFull(child,key);
                }
            }
			}
		}

	public void BTreeSplitChild(BTreeNode parent, int i){

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

	public FoundNode BTreeSearch(BTreeNode node, TreeObject key){
		
		if(node == null || node.numKeys < 1){
			return null;
		}

		// BOOK pg.492
		int i = 1;
		while(i <= node.keys.size() && key.getKey() > node.keys.get(i).getKey()){
			i++;
		}
		if(i <= node.keys.size() && key.getKey() == node.keys.get(i).getKey()){
			return new FoundNode(node,i);
		}else if(node.isLeaf){
			return null;
		}else{
			diskRead(node.getChild(i));
			return BTreeSearch(node, key);
		}
	}
	
	public class FoundNode{

		BTreeNode foundnode;
		int key;

		public FoundNode(BTreeNode node, int key){
			this.foundnode = node;
			this.key = key;
		}

	}

	// JUST A PLACEHOLDER 
	public BTreeNode diskRead(int nodeIndex){
		BTreeNode a = new BTreeNode();
		return a;
	}

	// JUST A PLACEHOLDER 
	void diskWrite(BTreeNode a){

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
			numKeys = keys.size();
			numChildren = children.size();
			keys  = new ArrayList<TreeObject>();
			children = new ArrayList<Integer>();
			this.nodeIndex = nodeIndex;	
		}
		
		public boolean isLeaf(){
			return isLeaf;
		}

		public boolean isFull(){
			return (numKeys == maxNumKeys);
		}
		
		public int getNodeIndex()
		{
			return nodeIndex;
		}
		
		public int getNumTreeObjects()
		{
			return keys.size();
		}

		public TreeObject getTreeObject(int index)
		{
			return keys.get(index);
		}
		
		public void setTreeObject(int index, TreeObject key)
		{
			keys.add(index,key);
		}

		public int getChild(int index)
		{
			return children.get(index);
		}

		public void setChild(int index, int nodeKey)
		{
			children.add(index, nodeKey);
		}


	}

}
