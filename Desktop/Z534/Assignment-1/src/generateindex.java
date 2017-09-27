import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;

import java.io.File;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
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
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

public class generateindex {

	public static void main(String args[]) throws IOException {
		String indexPath = "C:\\Users\\cool\\Desktop\\Z534\\Assignment-1\\index";
		Directory dir = FSDirectory.open(Paths.get(indexPath));
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		iwc.setOpenMode(OpenMode.CREATE);
		IndexWriter indwriter = new IndexWriter(dir, iwc);

		String filepath = "C:\\Users\\cool\\Desktop\\Z534\\Assignment-1\\corpus";
		File[] files = new File(filepath).listFiles();

		for (File filep : files) {

			String fileinstring = FileUtils.readFileToString(filep, "UTF-8");

			Document document = new Document();
			String reg1 = "<DOC>";
			Pattern p1 = Pattern.compile(reg1);
			Matcher m1 = p1.matcher(fileinstring);

			String reg2 = "</DOC>";
			Pattern p2 = Pattern.compile(reg2);
			Matcher m2 = p2.matcher(fileinstring);

			ArrayList<String> docarr = new ArrayList<String>();
			int k = 0;
			while (m1.find()) {
				int i = m1.end();
				if (m2.find()) {
					int j = m2.start();
					docarr.add(fileinstring.substring(i++, j));

				}
			}
			/* for each Field a regx and a string to store it's value is created */
			// for DOCNO
			String docno = "";
			String regdoc = "<DOCNO>([\\s\\w]*)</DOCNO>";
			Pattern pdoc = Pattern.compile(regdoc);

			// for HEAD
			String head = "";
			String reghead = "<HEAD>([\\s\\w]*)</HEAD>";
			Pattern phead = Pattern.compile(reghead);

			// for BYLINE
			String bline = "";
			String regbline = "<BYLINE>([\\s\\w]*)</BYLINE>";
			Pattern pbline = Pattern.compile(regbline);

			// for DATELINE
			String dline = "";
			String regdline = "<DATELINE>([\\s\\w]*)</DATELINE>";
			Pattern pdline = Pattern.compile(regdline);

			// for TEXT
			String text = "";
			String regtext = "<DOCNO>([\\s\\w]*)</DOCNO>";
			Pattern ptext = Pattern.compile(regtext);

			int l = 0;
			while (l < docarr.size()) {
				String st = docarr.get(l);
				int x = 0;

				Matcher mdoc = pdoc.matcher(st);
				Matcher mhead = phead.matcher(st);
				Matcher mbline = pbline.matcher(st);
				Matcher mdline = pdline.matcher(st);
				Matcher mtext = ptext.matcher(st);

				// in this loop matched filed is checked

				while (x < st.length()) {

					if (mdoc.find()) {
						docno = docno.concat(mdoc.group(1));
						x = mdoc.end() + 1;
					} else if (mhead.find()) {
						head = head.concat(mhead.group(1));
						x = mhead.end() + 1;
					} else if (mbline.find()) {
						bline = bline.concat(mbline.group(1));
						x = mbline.end() + 1;
					} else if (mdline.find()) {
						dline = dline.concat(mdline.group(1));
						x = mdline.end() + 1;
					} else if (mtext.find()) {
						text = text.concat(mtext.group(1));
						x = mtext.end() + 1;
					} else {
						System.out.println("no match to tag");
						break;
					}

				}
			}

			int check = 0; // to check if any field is added

			if (docno != null) {
				document.add(new StringField("DOCNO", docno, Field.Store.YES));
				check++;
			}
			if (head != null) {
				document.add(new TextField("HEAD", head, Field.Store.YES));
				check++;
			}
			if (dline != null) {
				document.add(new TextField("DATELINE", dline, Field.Store.YES));
				check++;
			}
			if (bline != null) {
				document.add(new TextField("BYLINE", bline, Field.Store.YES));
				check++;
			}
			if (text != null) {
				document.add(new TextField("TEXT", text, Field.Store.YES));
				check++;
			}
			if (check != 0) {
				indwriter.addDocument(document);
			}

		}

		indwriter.close();
		checkindex();
	}

	

	public static void checkindex() throws IOException
 {
	 String PathtoIndex = "C:\\Users\\cool\\Desktop\\Z534\\Assignment-1\\index";
	 IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get( (PathtoIndex))));

	 

	//Print the total number of documents in the corpus

	System.out.println("Total number of documents in the corpus: "+reader.maxDoc());                            

	                               

	                //Print the number of documents containing the term "new" in <field>TEXT</field>.

	                System.out.println("Number of documents containing the term \"new\" for field \"TEXT\": "+reader.docFreq(new Term("TEXT", "new")));

	                               

	                //Print the total number of occurrences of the term "new" across all documents for <field>TEXT</field>.

	                System.out.println("Number of occurrences of \"new\" in the field \"TEXT\": "+reader.totalTermFreq(new Term("TEXT","new")));                                                       

	                                                               

	                Terms vocabulary = MultiFields.getTerms(reader, "TEXT");

	                               

	                //Print the size of the vocabulary for <field>TEXT</field>, applicable when the index has only one segment.

	                System.out.println("Size of the vocabulary for this field: "+vocabulary.size());

	                               

	                //Print the total number of documents that have at least one term for <field>TEXT</field>

	                System.out.println("Number of documents that have at least one term for this field: "+vocabulary.getDocCount());

	                               

	                //Print the total number of tokens for <field>TEXT</field>

	                System.out.println("Number of tokens for this field: "+vocabulary.getSumTotalTermFreq());

	                               

	                //Print the total number of postings for <field>TEXT</field>

	                System.out.println("Number of postings for this field: "+vocabulary.getSumDocFreq());      

	                               

	                //Print the vocabulary for <field>TEXT</field>

	                TermsEnum iterator = vocabulary.iterator();

	       BytesRef byteRef = null;

	       System.out.println("\n*******Vocabulary-Start**********");

	       while((byteRef = iterator.next()) != null) {

	           String term = byteRef.utf8ToString();

	           System.out.print(term+"\t");

	       }

	       System.out.println("\n*******Vocabulary-End**********");        

	                reader.close();
 }
}
