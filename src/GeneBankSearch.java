import java.io.*;

public class GeneBankSearch 
{
	private static BTree btree;
	private static int t;
	private static int k;
	private static int debug;	
	private static int cacheSize;
	// args[1]
	private static String btreeFile;
	// args[2]
	private static String queryFile;
	
	private static TreeObject search;

	public static void main(String[] args) 
	{
		/* If the argument count is not 3 or 4 exit the program */
		if(args.length < 3 || args.length > 5) 
		{
			printUsage();
			System.exit(1);
		}

		if((Integer.parseInt(args[0]) == 0)){
			btreeFile = args[1];
			queryFile = args[2];
			k = Integer.parseInt(btreeFile.substring(btreeFile.length() - 1));
			t = Integer.parseInt(btreeFile.substring(btreeFile.length() - 3));
			if((args.length == 4)){
				debug = Integer.parseInt(args[3]);
			}
		}else if((Integer.parseInt(args[0]) == 1)){
			btreeFile = args[1];
			queryFile = args[2];
			if(args.length == 4){
				cacheSize = Integer.parseInt(args[3]);
			}
			if(args.length == 5){
				cacheSize = Integer.parseInt(args[3]);
				debug = Integer.parseInt(args[4]);
			}

		}else{
			printUsage();
		}

		String queryInput;
		BufferedReader reader;
		try
		{
			btree = new BTree(new File(btreeFile), t, k);
			reader = new BufferedReader(new FileReader(queryFile));

			while((queryInput = reader.readLine()) != null) 
			{
				queryInput = queryInput.toUpperCase();
				
				if (debug == 0){
					search = new TreeObject(ConvertDNAToLong.convertToLong(queryInput));
					System.out.println(btree.search(search) + queryInput);
				}
				else{ 
					search = new TreeObject(ConvertDNAToLong.convertToLong(queryInput));
					btree.search(search);
				}
			}
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
			System.exit(1);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private static void printUsage() 
	{
		System.out.println("Usage...");
	}
}
