import java.io.File;

public class driver {
	public static void main(String[] args){
		Parser parse = new Parser();
		File file = new File(args[0]);
		parse.parse(file);
		
		
	}

}
