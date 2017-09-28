import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sum {
	
	
    
   
	public static void main(String[] args) {
		//int
		String regex = "[-+]*\\d+";
		Pattern p = Pattern.compile(regex);
		int count = 0;
		
		for (int i = 0; i < args.length; i++) {
			Matcher m = p.matcher(args[i]);
			while (m.find()) {
				count += Integer.valueOf(args[i].substring(m.start(),m.end()));
			}		
		}
		System.out.print(count);
	}
}