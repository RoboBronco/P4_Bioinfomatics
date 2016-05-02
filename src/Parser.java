import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Jayden Weaver
 * Parses dna string from text files.
 */
public class Parser extends ConvertDNAToLong {

    private Scanner scan, realScanner;
    private int offSetNum;

    /**
     * Attempts to open a file with a scanner and then calls the parseIt method.
     * Returns an ArrayList of longs received from child method.
     * @param file
     * @return longArray
     */
    public ArrayList<Long> parse(File file, int offSet){
        try {
            this.scan = new Scanner(file);
            this.realScanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("lel. file not found.");
        }
        this.offSetNum = offSet;
        return parseIt();
    }

    /**+
     * Does actual parsing of file. Returns ArrayList of longs to parent method.
     * @return longArray
     */
    private ArrayList<Long> parseIt(){
        String current = "";
        String section = "";
        //get to the DNA part...
        while(!current.equals("ORIGIN      ")){
            current = scan.nextLine();
            section = realScanner.nextLine();
        }
        //get dna section.
        char currentChar = ' ';

        while (!current.equals("//")){
            section += realScanner.nextLine();
            current = scan.nextLine();
        }

        String newSec = "";
        for (int i = 0; i < section.length(); i++){
            currentChar = section.charAt(i);
            if (currentChar > 64){
                newSec += currentChar;
            }
        }

        ArrayList<Long> longArray = new ArrayList<Long>();
        current = newSec;
        String respekOnMyName;
        while(current != "" && !(current.length() < offSetNum)) {
            respekOnMyName = current.substring(0, offSetNum).toUpperCase();
            current = current.substring(offSetNum - (offSetNum - 1), current.length());

            if (!respekOnMyName.contains("N") && !respekOnMyName.contains("O") && !respekOnMyName.contains("I") &&
                    !respekOnMyName.contains("R")){
                    longArray.add(convertToLong(respekOnMyName));
            }
        }

        //parses multiple sections
        if (scan.hasNext()){
            parseIt();
        }
        return longArray;
    }
}
