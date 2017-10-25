import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Set;
import java.util.TreeMap;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.FSDirectory;
public class easySearch {
	 	
public static void Easysearch(String querytype, String queryString )throws Exception {
	
	String index ="C:\\Users\\cool\\Desktop\\Z534\\Assignment 2\\index\\index";
	IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
	IndexSearcher searcher =new IndexSearcher(reader);
	Analyzer analyzer =new StandardAnalyzer();
	QueryParser parser= new QueryParser("TEXT",analyzer);
	Query query =parser.parse(QueryParser.escape(queryString));  
	Set<Term> queryTerms = new LinkedHashSet<Term>();
	searcher.createNormalizedWeight(query, false).extractTerms(queryTerms);
	ClassicSimilarity dSimi = new ClassicSimilarity();
	
	HashMap<Float, String> hmap =new HashMap<Float, String>();
	/*int adoc[];
	int aterm[];
	float aperterm[][];*/
	int docTotal = reader.numDocs();
	for (Term t : queryTerms) {
		
       	int docFreq = reader.docFreq(t);
       	float idf;
		if(docFreq==0) {idf =0;} //to avoid getting exception, when docFreq=0, in idf calculation
		else
		{	idf= (float) Math.log(1+(docTotal/docFreq));}
		
		hmap.put(idf, t.text());
				
	}
	/*
	 * Sorting terms based of key- idf
	 */
	Map<Float, String>tempmap =new TreeMap<Float,String>(hmap);
	Map<Float, String>map =new TreeMap<Float, String>(Collections.reverseOrder());
	map.putAll(tempmap);
	Set<Entry<Float, String>> set2 =map.entrySet();
	
	HashMap<Float, String> hmapscore =new HashMap<Float, String>();
	
	//int temdocId=0; //this is used for adoc that holds DOCNO for all documents in index
	
	// Get the segments of the index
	List<LeafReaderContext> leafContexts = reader.getContext().reader()
				.leaves();
		// Processing each segment
	for (int i = 0; i < leafContexts.size(); i++) {
	    LeafReaderContext leafContext = leafContexts.get(i);
			// Get normalized length (1/sqrt(numOfTokens)) of the document
	    int startDocNo = leafContext.docBase;
		int numberOfDoc = leafContext.reader().maxDoc();
	for (int docId = 0; docId < numberOfDoc; docId++) {
		float Totalscore=0;
		float normDocLeng = dSimi.decodeNormValue(leafContext.reader().getNormValues("TEXT").get(docId));
				// Get length of the document
		float docLeng = 1 / (normDocLeng * normDocLeng);
		//int tp=0;
		
		for(Entry <Float,String> me2 : set2)
			{
				float totalscore=0;
				String reg = me2.getValue();
				Pattern p = Pattern.compile(reg);
				String input= searcher.doc(docId+startDocNo).toString();
				Matcher m = p.matcher(input);
				if(m.find())
				{	
						int termCount=0;
				 do {
						termCount++;
					}while(m.find());
				 
				//Caluculate Term frequency for this term in the document
				float tf= (termCount/docLeng);
				

				//now calculate score
				totalscore= (tf*me2.getKey());
				
				//store relvance score of each term
				
				//aperterm[temdocId][tp]=totalscore;  //a 2D array created for this
				
				//aterm[tp]=me2.getValue();        //keeping track of query term
				 
				//tp++;
			
				//add that score to the total score of the query for given document
				Totalscore+=totalscore;
			 }	
		}
		hmapscore.put(Totalscore,searcher.doc(docId+startDocNo).get("DOCNO"));
		//adoc[temdocId]= searcher.doc(docId+startDocNo).get("DOCNO"); //keeping track of DOCNO for docId
	  }
    }
		

	
	// setting file to write output result
	String file= null;
	//String file2= null;
	switch(querytype)
	{
		case "shortquery":
			
			 file= "myshortQuery.txt";
			 //file2="myshortTerm.txt";
			break;
		case "longquery":
			
			 file= "mylongQuery.txt";
			// file2="mylongTerm.txt";
			break;
		default:
			System.out.println("error");
			return;
	}
	File fout =new File (file);
	//File fout2 =new File (file2);
	
	   if(!fout.exists()) {
		   fout.createNewFile();
	   }
	   
	 /*  if(!fout2.exists()) {
		   fout2.createNewFile();
	   }*/
	//FileOutputStream fos =new FileOutputStream(fout);
	//OutputStreamWriter osw =new OutputStreamWriter(fos);
	   
	FileWriter fos = new FileWriter(fout, true);
	BufferedWriter osw = new BufferedWriter(fos);
	PrintWriter pw= new PrintWriter(osw);
	
	/*FileWriter fos2 = new FileWriter(fout2, true);
	BufferedWriter osw2 = new BufferedWriter(fos2);
	PrintWriter pw2= new PrintWriter(osw2);*/
	
	/*
	 * Sort hpscore
	 * print first 1000 documents
	 */
	
	Map<Float, String>tempmap2 =new TreeMap<Float, String>(hmapscore);
	Map<Float, String>map2 =new TreeMap<Float, String>(Collections.reverseOrder());
	map2.putAll(tempmap2);
	Set<Entry<Float, String>> set3 =map2.entrySet();
	int count=1;
	
	for(Entry<Float,String> me3 :set3)
	{
		
		pw.printf("%d  Q0   %s   0   %f   run-1",searchTRECtopics.queryid,me3.getValue(),me3.getKey());
		pw.println();
		
		// generating results for each term in query
		/*int i=0;
		
		while(me3.getValue()!=adoc[i])
		{
			i++;
		}
		for(int k=1;k<=aterm.length;k++)
		{
			pw2.printf("%d  %s   %s   0   %f   run-1",searchTRECtopics.queryid,aterm[k],me3.getValue(),aperterm[i][k]);
			pw2.println();
		}*/
		
		count++;
		if(count==1000)
		{
			break;
		}
	}
	
	pw.close();
    fos.close();
    osw.close();
	reader.close();
	
	}
   
}
