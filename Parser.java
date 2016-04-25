import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;


/**
 * @author Jayden Weaver
 * Parses dna string from text files.
 */
public class Parser extends ConvertDNAToLong {

	private long longRepresentation;
	private Scanner scan, realScanner;
	private int offSetNum;

	/**
	 * Attempts to open a file with a scanner and then calls the parseIt method.
	 * @param file
	 */
	public ArrayList parse(File file, int offSet){
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
	 * Does the actual parsing of the dna strings and calls the convertToLong method, inside of the ConvertDNAToLong class.
	 * @return 
	 */
	private ArrayList parseIt(){
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

		ArrayList longArray = new ArrayList();
		current = newSec;
		String respekOnMyName = "brrrrrr";
		while(current != "" && !(current.length() < offSetNum)){
			respekOnMyName = current.substring(0, offSetNum);
			current = current.substring(offSetNum - (offSetNum - 1), current.length());

			if (!respekOnMyName.contains("N") && !respekOnMyName.contains("O") && !respekOnMyName.contains("I")){
				System.out.println(respekOnMyName);
				System.out.println(convertToLong(respekOnMyName));
				longArray.add(convertToLong(respekOnMyName));						
			}
		}
		return longArray;
	}

	/**
	 * Returns the long representation of the DNA sequence.
	 * @return longRepresentation 
	 */
	public long getLongRepresentation(){
		return this.longRepresentation;
	}

}