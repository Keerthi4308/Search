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
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;

public class generateindex {

	private static IndexWriter indwriter;

	public static void main(String args[]) throws CorruptIndexException,LockObtainFailedException, IOException{
		
		String indexPath = "C:\\Users\\cool\\Desktop\\Z534\\Assignment-1\\indexp";
		Directory dir = FSDirectory.open(Paths.get(indexPath));
	    Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		iwc.setOpenMode(OpenMode.CREATE);
		indwriter = new IndexWriter(dir, iwc);

      String filepath = "C:\\Users\\cool\\Desktop\\Z534\\Assignment-1\\corpus";
	  File[] files = new File(filepath).listFiles();

	  
		for (File filep : files) {

			/* for each file in corpus */
			
		String fileinstring = FileUtils.readFileToString(filep,"UTF-8"); 
  		
			String reg1 = "<DOC>";
			Pattern p1 = Pattern.compile(reg1);
			Matcher m1 = p1.matcher(fileinstring);

			String reg2 = "</DOC>";
			Pattern p2 = Pattern.compile(reg2);
			Matcher m2 = p2.matcher(fileinstring);

			ArrayList<String> docarr = new ArrayList<String>();
		
			/*extracting text between doc tags*/
			
			while (m1.find()) {
				int i = m1.end()+1;
				if (m2.find()) {
					int j = m2.start();
					docarr.add(fileinstring.substring(i, j));

				}
			}
			
			int l = 0;
			while (l < docarr.size()) {
			
				String st = docarr.get(l);
				int x = 0;
				/* for each document */
				
				Document document = new Document();	
				
				/* for each Field a regx and a string to store it's value is created */
				// for DOCNO
				String docno = "";
				String regdoc = "<DOCNO>([^<>]*)</DOCNO>";
				Pattern pdoc = Pattern.compile(regdoc);
				Matcher mdoc = pdoc.matcher(st);
				
				// for HEAD
				String head = "";
				String reghead = "<HEAD>([^<>]*)</HEAD>";
				Pattern phead = Pattern.compile(reghead);
				Matcher mhead = phead.matcher(st);
				
				// for BYLINE
				String bline = "";
				String regbline = "<BYLINE>([^<>]*)</BYLINE>";
				Pattern pbline = Pattern.compile(regbline);
				Matcher mbline = pbline.matcher(st);
				
				// for DATELINE
				String dline = "";
				String regdline = "<DATELINE>([^<>]*)</DATELINE>";
				Pattern pdline = Pattern.compile(regdline);
				Matcher mdline = pdline.matcher(st);
				
				// for TEXT
				String text = "";
				String regtext = "<TEXT>([^<>]*)</TEXT>";
				Pattern ptext = Pattern.compile(regtext);
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
						
						break; 						
					}

				}
				

			int check = 0; // to check if any field is added

			if (docno != "") {
				document.add(new StringField("DOCNO", docno, Field.Store.YES));
				check++;
			}
			if (head != "") {
				document.add(new TextField("HEAD", head, Field.Store.YES));
				check++;
			}
			if (dline != "") {
				document.add(new TextField("DATELINE", dline, Field.Store.YES));
				check++;
			}
			if (bline != "") {
				document.add(new TextField("BYLINE", bline, Field.Store.YES));
				check++;
			}
			if (text != "") {
				document.add(new TextField("TEXT", text, Field.Store.YES));
				check++;
			}
			if (check != 0) {
				indwriter.addDocument(document);
				
			}

			l++;  //next doc
		}
	
	     
	}	
		  indwriter.forceMerge(1);
		  indwriter.commit();  
	       System.out.println(indwriter.numDocs());
		   indwriter.close();
		   
		   getstat();
	}
	public static void getstat() throws IOException {
		String pathtoindex = "C:\\Users\\cool\\Desktop\\Z534\\Assignment-1\\indexp";
		IndexReader reader =DirectoryReader.open(FSDirectory.open(Paths.get(pathtoindex)));
		//Print the total number of documents in the corpus

		System.out.println("Total number of documents in the corpus: "+reader.maxDoc());                            

		                               

		                //Print the number of documents containing the term "new" in <field>TEXT</field>.

		                System.out.println("Number of documents containing the term \"new\" for field \"TEXT\": "+reader.docFreq(new Term("TEXT", "new")));

		                               

		                //Print the total number of occurrences of the term "new" across all documents for <field>TEXT</field>.

		                System.out.println("Number of occurrences of \"new\" in the field \"TEXT\": "+reader.totalTermFreq(new Term("TEXT","new")));                                                       

		                                                               

		                Terms vocabulary = MultiFields.getTerms(reader, "TEXT");
		                
		                System.out.println("size of vocabulary: " + vocabulary.size());
                                                            
		                //Print the total number of documents that have at least one term for <field>TEXT</field>

		                System.out.println("Number of documents that have at least one term for this field: "+vocabulary.getDocCount());

		                            

		                //Print the total number of tokens for <field>TEXT</field>

		                System.out.println("Number of tokens for this field: "+vocabulary.getSumTotalTermFreq());

		                               

		                //Print the total number of postings for <field>TEXT</field>

		                System.out.println("Number of postings for this field: "+vocabulary.getSumDocFreq());      

		          reader.close();
		
	}
}


