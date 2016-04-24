import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by pkannah on 4/22/16.
 * Working project: P4_Bioinfomatics.
 */
public class TreeWriter{
    int seqLength;
    private RandomAccessFile fOut;
    private File file;

    //Creates the file based on the specified name and length
    public TreeWriter(String filename) {
//        seqLength = sequenceLength;
        file = new File(filename);
        try {
            fOut = new RandomAccessFile(filename, "rwd"); //everyupdate to the file is also written to disk
            //TODO write metadata
        }
        catch (IOException e) {
            System.out.println("something up");
        }
    }

    public TreeWriter(File file){
        try {
            System.out.println("insdie of construct 2212312");
            fOut = new RandomAccessFile(file, "rwd");
            //TODO write metadata
            fOut.write(7); //sequence length
            fOut.write(4); //number of nodes, including root
            //TODO write root
        }
        catch (IOException e){
            System.out.println("something is up");
        }
    }

    //TODO parameter should be a node not a TreeObject
    public void writeToDisk(Object object){

    }

    //TODO should return a BTreeNode (index?) instead of void
    public void readFromDisk() throws IOException{
        fOut.seek(0);
        for(int i = 0; i < fOut.length(); i++) {
            System.out.println(fOut.read());
        }

    }


    public static void main(String[] args) throws Exception{
        TreeWriter writer = new TreeWriter(new File("TREE_WRITER_TEST"));
        TreeWriter writer1 = new TreeWriter("test1.gbk.btree.data.1");

        writer.readFromDisk();
    }

}
