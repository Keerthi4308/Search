import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public class searchTRECtopics {
	public static int queryid = 51;

	public static void main(String args[]) throws Exception {

		String filename = "C:\\Users\\cool\\Desktop\\Z534\\topics.51-100";

		File newfile = new File(filename);

		String totalip = FileUtils.readFileToString(newfile, "UTF-8");
       //using regular expression extracting short query and long query
		
		String reg1 = "<title> Topic:";
		Pattern p1 = Pattern.compile(reg1);
		Matcher m1 = p1.matcher(totalip);

		String reg2 = "<desc> Description:([^<>]*)";
		Pattern p2 = Pattern.compile(reg2);
		Matcher m2 = p2.matcher(totalip);

		while (m1.find()) {
			int i = m1.end() + 1;
			if (m2.find()) {
				int j = m2.start();

				// call to my algorithm

				easySearch.Easysearch("shortquery", totalip.substring(i, j));
				easySearch.Easysearch("longquery", m2.group(1));

				// call to compare Algorithms

				new compareAlgorithms("shortquery",totalip.substring(i, j));
				new compareAlgorithms("longquery",m2.group(1));

				

			}
			System.out.println("Query: "+ queryid);
			queryid++;
			
		}

	}

	
}
