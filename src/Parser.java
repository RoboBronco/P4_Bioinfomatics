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
	private String current = "";

	/**
	 * Attempts to open a file with a scanner and then calls the parseIt method.
	 * @param file
	 */
	public void parseNext(File file){
		if (current == ""){ //don't want to make a scanner again each time lol.
			try {
				this.scan = new Scanner(file);
			} catch (FileNotFoundException e) {
				System.out.println("lel. file not found.");
			}
		}
		parseIt();
	}
//<<<<<<< f3a8f9232d2ed67db221a050e636f7f2d170311d

	/**
	 * Does the actual parsing of the dna strings and calls the convertToLong method, inside of the ConvertDNAToLong class.
	 */
	private Collection<Character> parseIt(){

		//get to the DNA part...
		if (current == ""){
			while(!current.equals("ORIGIN      ")){
				current = scan.nextLine();
			}
		}
		dnaSeq.clear();
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
			break;
		}
		return dnaSeq;

		//		//multiple dna set capabilities
		//		if (scan.hasNext()){
		//			parseIt();
		//		}

	}

	/**
	 * Returns the long representation of the DNA sequence.
	 * @return longRepresentation
	 */
	public long getLongRepresentation(){
		return this.longRepresentation;
	}

	/**
	 * Returns the DNA Sequence Collection.
	 * @return DNA Sequence
	 */
	public Collection<Character> DNASequence(){
		return dnaSeq;
	}

}
//=======
//
//
//
//
//
//
//}
//>>>>>>> Midsession commit, updated argument and error handling for CreateBTree.
