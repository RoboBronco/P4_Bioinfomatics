import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;


/**
 *@author Jayden Weaver
 */

//currently only parses one set of DNA. Will add more soon.

public class Parser extends ConvertDNAToLong {

	private long longRepresentation;
	private Scanner scan;
	private Collection<Character> dnaSeq = new ArrayList<Character>();

	public void parse(File file){
		try {
			this.scan = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("lel. file not found.");
		}
		
		String current = "";

		//get to the DNA part...
		while(!current.equals("ORIGIN      ")){
			current = scan.nextLine();
		}
		//get dna
		char currentChar = ' ';
		while (!current.equals("//")){
			current = scan.next();
			for (int k = 0; k < current.length(); k++){
				currentChar = current.charAt(k);
				if (currentChar > 64){ //To exclude numbers from being added to collection, ascii table values. if we don't exclude numbers....convertToLong will fail.
					dnaSeq.add(currentChar);
				}

			}
			this.longRepresentation = convertToLong(dnaSeq); //convert to long.
			//For Testing
			System.out.println(longRepresentation); //print the long.
			System.out.println(convertFromLong(this.longRepresentation)); //confirm we get what we expect back.

			//Clear the collection.
			dnaSeq.clear();

		}

	}

	public long getLongRepresentation(){
		return this.longRepresentation;
	}

}