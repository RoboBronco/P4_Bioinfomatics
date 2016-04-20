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
	private Scanner scan;
	private Collection<Character> dnaSeq = new ArrayList<Character>();

	/**
	 * Attempts to open a file with a scanner and then calls the parseIt method.
	 * @param file
	 */
	public void parse(File file){
		try {
			this.scan = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("lel. file not found.");
		}
		parseIt();		
	}
	
	/**
	 * Does the actual parsing of the dna strings and calls the convertToLong method, inside of the ConvertDNAToLong class.
	 */
	private void parseIt(){
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
		
		//multiple dna set capabilities
		if (scan.hasNext()){
			parseIt();
		}
		
	}

	/**
	 * Returns the long representation of the DNA sequence.
	 * @return longRepresentation 
	 */
	public long getLongRepresentation(){
		return this.longRepresentation;
	}

}