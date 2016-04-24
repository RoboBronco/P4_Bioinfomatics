import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

/**
 * Created by pkannah on 4/22/16.
 * Working project: P4_Bioinfomatics.
 */

public class TreeWriter {
    int seqLength;
    private RandomAccessFile fOut;
    private File file;
    //Creates the file based on the specified name and length

    public TreeWriter(String filename) {
        file = new File(filename);
        try {
            fOut = new RandomAccessFile(file, "rwd"); //everyupdate to the file is also written to disk
        }
        catch (IOException e) {
            System.out.println("something up");
        }
    }

    public void writeTreeMetaData(int numNodes,

                                  int k, int maxNumChildren, int maxNumKeys){
        try {
//            System.out.println("WRITING METADATA");
            fOut.write(k);
            fOut.write(numNodes);
            fOut.write(maxNumChildren);
            fOut.write(maxNumKeys);
        }
        catch (IOException e) {
            System.out.println("FILE DOESN'T WORK");
        }

    }

    //TODO parameter should be a node not a TreeObject
    public void writeToDisk(Object object) {
        BTree.BTreeNode node = (BTree.BTreeNode) object;
        try {

//            System.out.println("WHAT IS HAPPENING!!!!!");
//            fOut.write(111111111);
//            fOut.write(node.getNodeIndex());

            for (int i = 0; i < node.getChildren().size() ; i++){
                System.out.println("inside ot writeToDisk()");
                fOut.write(node.getChildren().get(i));
            }
        }
        catch (IOException e){
            System.out.println("someihting");
        }
    }

    //TODO should return a BTreeNode (index?) instead of void
    public void readFromDisk() throws IOException {
        fOut = new RandomAccessFile(file, "rwd");
        for(long i = 0;i < fOut.length(); i++) {
            System.out.print(fOut.read() + " ");
        }

        System.out.println("\nLENGTH = " + fOut.length());
    }


    public static void main(String[] args) throws Exception {
        System.out.println("MAIN!!!!");
//        TreeWriter writer1 = new TreeWriter("test1.gbk.btree.data.1");

        BTree tree = new BTree(2,7);
        System.out.println("BTree has been created!!!!");

        for (int i = 0; i < Integer.MAX_VALUE ; i++){
//            System.out.print("inside loop..inserting..." + i+ " ");
            tree.BTreeInsert(new TreeObject(12345));
        }

        tree.getFile().readFromDisk();
    }
}
