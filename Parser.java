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

	private long binary;
	private Scanner scan;
	private Collection<Character> dnaSeq = new ArrayList<Character>();

	public void parse(File file){
		try {
			this.scan = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("lel. file not found.");
		}
		String current = "";
		int i = 0;
		//get to the DNA part...
		while(!current.equals("ORIGIN      ")){
			current = scan.nextLine();
			i++;
		}
		//get dna
		char currentChar = ' ';
		while (!current.equals("//")){
			current = scan.next();
			for (int k = 0; k < current.length(); k++){
				currentChar = current.charAt(k);
				if (currentChar > 64){ //To exclude numbers from being added to collection, ascii table values
					dnaSeq.add(currentChar);
				}
//				System.out.println(currentChar);
			}

		}
		System.out.println(dnaSeq.toString());






		this.binary = convertToLong(dnaSeq);
		

	}


	public long getBinary(){
		return this.binary;
	}











}