
import java.io.File;

public class driver {
	public static void main(String[] args){
		Parser parse = new Parser();
		File file = new File(args[0]);
		for(int i = 0; i < 100; i++){
			parse.parseNext(file);
			System.out.println(parse.DNASequence());
			System.out.println(parse.convertToLong(parse.DNASequence()));
		}
	}

}