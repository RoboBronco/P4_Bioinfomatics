import java.io.File;
import java.io.FileNotFoundException;

public class GeneBankCreateBTree {
    public static void main (String[] args){


            if (args.length() < 4 || args.length() > 6 ){
            //print usage
                system.out.println("Usage: java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]");
				system.out.println("[<cache size>] should only be specified if you are using a cache.");
            //exit
            }
     		else{
				if (args[0] == 0){
					cachelessBTree();
				}
				else if (args[0] == 1){
					cachedBTree();
				}
				else{
				system.out.println("First argument must be a 0 or 1 to determine if cache is used");
				system.out.println("Usage: java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]");
				system.out.println("[<cache size>] should only be specified if you are using a cache.");
				}

            }
	}//end main	

		
	public static <T> void cachelessBTree(){ // generic? static necessary?
		
		int degree = Integer.parseInt(args[1]);
		String gbName = args[2];
		int sequence = Integer.parseInt(args[3]);
		int debug = 0;
		
		if (args.length == 5){ 
			debug = args[4];
		}

		if (degree == 0){
		//generate opitmal degree
		system.out.println("Generating optimal degree based on disc size"); 
		degree = OptimalDegreeGenerator.generate();		
		}
		
		if (!sequence <= 31 || !sequence >= 1){
		//TODO error handling for improper sequence length values 	
		}

		try {
			File gbFile = new File(gbName); // TODO double check that this is working. 
		}
		catch(FileNotFoundException fnfe){
			system.out.println("Genebank File Not Found: Program Terminated.");
			exit(1);
		}	
	
	}//end Cacheless




	public static <T> void cachedBTree(){
	
	
	
	}//end Cached
						
}

