import java.util.ArrayList;
import java.io.File;

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
	private String filename;
	private File outputFile;

	public BTree(int degree, int k, String filename){

		this.degree = degree;

		this.k = k;

		BTreeNode node = new BTreeNode();
		node.isLeaf = true;
		root = node;
		currentNodeIndex = 0;
		maxNumKeys = (2*degree) - 1;
		maxNumChildren = maxNumKeys + 1;
		numNodes = 1;
		writer = new TreeWriter("TEST_FILE");
		//		System.out.println("INSDIE BTREE CONSTRUCTOR");
		writer.writeTreeMetaData(numNodes,k,maxNumChildren,maxNumKeys);

		this.filename = filename;
		outputFile = new File("" + filename + ".btree.data." + k + "." + degree);
	}

	public TreeWriter getFile(){
		return writer;
	}


	public void BTreeSplitChild(BTreeNode parent, BTreeNode toSplit, int iChild) throws IllegalStateException{
		if (parent.isFull() || !toSplit.isFull())
			throw new IllegalStateException();

		// BOOK pg. 494
		BTreeNode newChild = new BTreeNode();
		newChild.isLeaf = toSplit.isLeaf;
		for(int i = 0; i <= degree - 2; i++)
		{
			newChild.keys.add(i,toSplit.keys.get(degree));
			toSplit.keys.remove(degree);
		}
		if(!toSplit.isLeaf)
		{
			for(int i = 0; i <= degree-1; i++)
			{
				newChild.children.add(i,toSplit.children.get(degree));
				toSplit.children.remove(degree);
			}
		}

		if(parent.children.size() < 2){
			parent.children.add(newChild.nodeIndex, iChild);
		}else{
			parent.children.add(newChild.nodeIndex, iChild+1);
		}

		if(parent.keys.size() == 0)
		{
			parent.keys.add(iChild-1, toSplit.keys.get(degree-1));
		}
		else
		{
			parent.keys.add(iChild, toSplit.keys.get(degree-1));
		}
		toSplit.keys.remove(degree-1);

		diskWrite(toSplit);
		diskWrite(parent);
		diskWrite(newChild);
	}	

	public void BTreeInsert(TreeObject key){
		// BOOK pg. 495
		BTreeNode rootNode = root;
		if(rootNode.keys.size() == maxNumKeys)
		{
			// ALLOCATE NODE
			BTreeNode node = new BTreeNode();
			root = node;
			node.isLeaf = false;
			node.children.add(rootNode.nodeIndex,0);
			BTreeSplitChild(node,rootNode,1);
			BTreeInsertNonFull(node,key);
		}
		else
		{
			BTreeInsertNonFull(rootNode,key);
		}
	}


	public void BTreeInsertNonFull(BTreeNode node, TreeObject key){
		boolean found = false;

		// BOOK pg. 496
		int i = node.keys.size() - 1;

		if(node.isLeaf)
		{
			while(i >= 0 && key.getKey() <= node.keys.get(i).getKey())
			{
				if(key.getKey() == node.keys.get(i).getKey())
				{
					found = true;
					node.keys.get(i).increaseFrequency();

					//diskWrite(node);
					return;
				}
				i--;
			}
			if(!found)
			{
				node.keys.add(i+1,key);
				//diskWrite(node);
				//found = true;
			}
		}
		else
		{
			while(i >= 0 && key.getKey() <= node.keys.get(i).getKey())
			{
				if(key.getKey() == node.keys.get(i).getKey())
				{

					node.keys.get(i).increaseFrequency();
					found = true;
					//diskWrite(node);
					return;
				}
				i--;
			}
			if(!found){
				BTreeNode child = diskRead(node.children.get(i));
				if(child.keys.size() == maxNumKeys)
				{
					//BTreeSplitChild(node,index);
					if(key.getKey() == node.keys.get(i).getKey())
					{
						node.keys.get(i).increaseFrequency();
						diskWrite(node);
						found = true;
					}
					if(key.getKey() > node.keys.get(i).getKey() && !found)
					{
						i++;
						child = diskRead(node.children.get(i));
					}
				}
				if(!found){
					BTreeInsertNonFull(child,key);
				}
			}
		}
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
			node = diskRead(node.children.get(i));
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
		ArrayList<TreeObject> keys;					// ArrayList of TreeObjects
		ArrayList<Integer> children;				// ArrayList of children
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
		
	}

}