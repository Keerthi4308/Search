
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.File;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Terms;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;

public class indexcomparison {

	private static IndexWriter indwriter;

	public static void main(String args[]) throws CorruptIndexException, LockObtainFailedException, IOException ,IllegalArgumentException{
		
				
		String inputfile ="C:\\Users\\cool\\Desktop\\Z534\\Assignment-1\\corpus";
		
		
		index_standard(inputfile);   /*indexing using simple analyzer */
		index_stop(inputfile);        /*indexing using stop analyzers */
		index_simple(inputfile);    /*indexing using simple analyzer */
		index_key(inputfile);          /*indexing using key analyzer */
	}

	public static void index_standard(String filepath) throws IOException {

		
		
		String restand = "C:\\Users\\cool\\Desktop\\Z534\\Assignment-1\\restand";
		
		Directory dir = FSDirectory.open(Paths.get(restand));
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		iwc.setOpenMode(OpenMode.CREATE);
		indwriter = new IndexWriter(dir, iwc);
		
		File[] files = new File(filepath).listFiles();

		for (File filep : files) {

			String fileinstring = FileUtils.readFileToString(filep, "UTF-8");

			Document document = new Document();

			// for TEXT
			String text = "";
			String regtext = "<TEXT>([^<>]*)</TEXT>";
			Pattern ptext = Pattern.compile(regtext);
			Matcher mtext = ptext.matcher(fileinstring);

			while (mtext.find()) {
				text = text.concat(mtext.group(1));
			}

			int check = 0;

			if (text != "") {
				document.add(new TextField("TEXT", text, Field.Store.YES));
				check++;
			}

			if (check != 0) {
				indwriter.addDocument(document);
			}

		}
		indwriter.forceMerge(1);
		indwriter.commit();
		System.out.println(indwriter.numDocs());
		indwriter.close();
		System.out.println("Stat of stand");
		getstat(restand);

	}

	public static void index_stop(String filepath) throws IOException {

		String restop = "C:\\Users\\cool\\Desktop\\Z534\\Assignment-1\\restop";
		Directory dir = FSDirectory.open(Paths.get(restop));
		Analyzer analyzer = new StopAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		iwc.setOpenMode(OpenMode.CREATE);
		indwriter = new IndexWriter(dir, iwc);
		
		File[] files = new File(filepath).listFiles();

		for (File filep : files) {

			String fileinstring = FileUtils.readFileToString(filep, "UTF-8");

			Document document = new Document();

			// for TEXT
			String text = "";
			String regtext = "<TEXT>([^<>]*)</TEXT>";
			Pattern ptext = Pattern.compile(regtext);
			Matcher mtext = ptext.matcher(fileinstring);

			while (mtext.find()) {
				text = text.concat(mtext.group(1));
			}

			int check = 0;

			if (text != "") {
				document.add(new TextField("TEXT", text, Field.Store.YES));
				check++;
			}

			if (check != 0) {
				indwriter.addDocument(document);
			}

		}

		indwriter.forceMerge(1);
		indwriter.commit();
		System.out.println(indwriter.numDocs());
		indwriter.close();
		System.out.println("Stat of stop");
		getstat(restop);

	}

	public static void index_key(String filepath) throws IOException {

		String rekey = "C:\\Users\\cool\\Desktop\\Z534\\Assignment-1\\rekey";
		
		Directory dir = FSDirectory.open(Paths.get(rekey));
		Analyzer analyzer = new KeywordAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		iwc.setOpenMode(OpenMode.CREATE);
		indwriter = new IndexWriter(dir, iwc);
		

		File[] files = new File(filepath).listFiles();

		for (File filep : files) {

			String fileinstring = FileUtils.readFileToString(filep, "UTF-8");

			Document document = new Document();

			// for TEXT
			String text = "";
			String regtext = "<TEXT>([^<>]*)</TEXT>";
			Pattern ptext = Pattern.compile(regtext);
			Matcher mtext = ptext.matcher(fileinstring);

			while (mtext.find()) {
				text = text.concat(mtext.group(1));
			}

			int check = 0;

			if (text != "") {
				document.add(new StringField("TEXT", text, Field.Store.NO));
				check++;
			}

			if (check != 0) {
				indwriter.addDocument(document);
			}

		}

		indwriter.forceMerge(1);
		indwriter.commit();
		System.out.println(indwriter.numDocs());
		indwriter.close();

		System.out.println("Stat of key");
		getstat(rekey);

	}

	public static void index_simple(String filepath) throws IOException {

		String resimple = "C:\\Users\\cool\\Desktop\\Z534\\Assignment-1\\resimple";
		Directory dir = FSDirectory.open(Paths.get(resimple));
		Analyzer analyzer = new SimpleAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		iwc.setOpenMode(OpenMode.CREATE);
		indwriter = new IndexWriter(dir, iwc);
		

		File[] files = new File(filepath).listFiles();

		for (File filep : files) {

			String fileinstring = FileUtils.readFileToString(filep, "UTF-8");

			Document document = new Document();

			// for TEXT
			String text = "";
			String regtext = "<TEXT>([^<>]*)</TEXT>";
			Pattern ptext = Pattern.compile(regtext);
			Matcher mtext = ptext.matcher(fileinstring);

			while (mtext.find()) {
				text = text.concat(mtext.group(1));
			}

			int check = 0;

			if (text != "") {
				document.add(new TextField("TEXT", text, Field.Store.YES));
				check++;
			}

			if (check != 0) {
				indwriter.addDocument(document);
			}

		}
        
		indwriter.forceMerge(1);
		indwriter.commit();
		System.out.println(indwriter.numDocs());
		indwriter.close();
       
		System.out.println("Stat of simple");
		getstat(resimple);

	}

	public static void getstat(String pathtoindex) throws IOException {

		
		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(pathtoindex)));
		
		// Print the total number of documents in the corpus

		System.out.println("Total number of documents in the corpus: " + reader.maxDoc());

		
	Terms vocabulary = MultiFields.getTerms(reader, "TEXT");

		System.out.println("size of vocabulary: " + vocabulary.size());
		
 //Print the total number of tokens for <field>TEXT</field>

	System.out.println("Number of tokens for this field: " + vocabulary.getSumTotalTermFreq());

//	 Print the total number of postings for <field>TEXT</field>

	System.out.println("Number of postings for this field: " + vocabulary.getSumDocFreq());
		
		
		

		reader.close();

	}
}
