import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.search.similarities.LMJelinekMercerSimilarity;
import org.apache.lucene.store.FSDirectory;

public class compareAlgorithms {

	IndexSearcher searcher;
	Analyzer analyzer = new StandardAnalyzer();
	QueryParser parser = new QueryParser("TEXT", analyzer);

	compareAlgorithms(String querytype, String querystring) throws IOException, ParseException {
		String pathToIndex = "C:\\Users\\cool\\Desktop\\Z534\\Assignment 2\\index\\index";
		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(pathToIndex)));
		searcher = new IndexSearcher(reader);

		BM25(querytype, querystring);
		VSM(querytype, querystring);
		LMD(querytype, querystring);
		LMJ(querytype, querystring);
		reader.close();

	}

	public void BM25(String querytype, String queryString) throws IOException, ParseException {

		// setting file to write output result
		String file=null;
		switch (querytype) {
		case "shortquery":
			file = "BM25shortQuery.txt";
			break;
		case "longquery":
			file = "BM25longQuery.txt";
			break;
		default:
			System.out.println("error");
		}
		File fout = new File(file);
		if (!fout.exists()) {
			fout.createNewFile();
		}
		FileWriter fos =new FileWriter(fout,true);
		BufferedWriter osw =new BufferedWriter(fos);
		PrintWriter pw = new PrintWriter(osw);

		Query query = parser.parse(QueryParser.escape(queryString));
		searcher.setSimilarity(new BM25Similarity());
		TopDocs topDocs = searcher.search(query, 1000);
		ScoreDoc[] docs = topDocs.scoreDocs;
        
		for (int i = 0; i < docs.length; i++) {
			Document doc = searcher.doc(docs[i].doc);
			pw.printf("%d   Q0  %s   0   %f   run-1",searchTRECtopics.queryid,doc.get("DOCNO"),docs[i].score);
			pw.println();
				
			
		}
		
		pw.close();
	    fos.close();
	    osw.close();
	}

	public void VSM(String querytype, String queryString) throws IOException, ParseException {

		// setting file to write output result
		String file = null;
		switch (querytype) {
		case "shortquery":
			file = "VSMshortQuery.txt";
			break;
		case "longquery":
			file = "VSMlongQuery.txt";
			break;
		default:
			System.out.println("error");
		}
		File fout = new File(file);
		if (!fout.exists()) {
			fout.createNewFile();
		}
		FileWriter fos =new FileWriter(fout,true);
		BufferedWriter osw =new BufferedWriter(fos);
		PrintWriter pw = new PrintWriter(osw);

		Query query = parser.parse(QueryParser.escape(queryString));
		searcher.setSimilarity(new ClassicSimilarity());
		TopDocs topDocs = searcher.search(query, 1000);
		ScoreDoc[] docs = topDocs.scoreDocs;

		for (int i = 0; i < docs.length; i++) {
			Document doc = searcher.doc(docs[i].doc);
			pw.printf("%d   Q0   %s  0   %f   run-1",searchTRECtopics.queryid,doc.get("DOCNO"),docs[i].score);
			pw.println();
			
		}
		
		pw.close();
	    fos.close();
	    osw.close();

	}

	public void LMD(String querytype, String queryString) throws IOException, ParseException {

		// setting file to write output result
		String file = null;
		switch (querytype) {
		case "shortquery":
			file = "LMDshortQuery.txt";
			break;
		case "longquery":
			file = "LMDlongQuery.txt";
			break;
		default:
			System.out.println("error");
		}
		File fout = new File(file);
		if (!fout.exists()) {
			fout.createNewFile();
		}
		FileWriter fos =new FileWriter(fout,true);
		BufferedWriter osw =new BufferedWriter(fos);
		PrintWriter pw = new PrintWriter(osw);

		Query query = parser.parse(QueryParser.escape(queryString));
		searcher.setSimilarity(new LMDirichletSimilarity());
		TopDocs topDocs = searcher.search(query, 1000);
		ScoreDoc[] docs = topDocs.scoreDocs;
		for (int i = 0; i < docs.length; i++) {
			Document doc = searcher.doc(docs[i].doc);
			pw.printf("%d   Q0   %s   0   %f    run-1",searchTRECtopics.queryid,doc.get("DOCNO"),docs[i].score);
			pw.println();
			
		}
		
		pw.close();
	    fos.close();
	    osw.close();
	}

public	void LMJ(String querytype,String queryString) throws IOException, ParseException
{
   	// setting file to write output result
    String file=null;
	switch(querytype)
	{
		case "shortquery":
			 file= "LMJshortQuery.txt";
			break;
		case "longquery":
			 file= "LMJlongQuery.txt";
			break;
		default:
			System.out.println("error");
	}
	File fout =new File (file);
	   if(!fout.exists()) {
		   fout.createNewFile();
	   }
    FileWriter fos =new FileWriter(fout,true);
	BufferedWriter osw =new BufferedWriter(fos);
	PrintWriter pw= new PrintWriter(osw);
	
	Query query = parser.parse(QueryParser.escape(queryString));
	searcher.setSimilarity(new LMJelinekMercerSimilarity((float) 0.7));	
	TopDocs topDocs = searcher.search(query, 1000);
	ScoreDoc[] docs = topDocs.scoreDocs;
	for (int i = 0; i < docs.length; i++) {
	 Document doc = searcher.doc(docs[i].doc);
	 
	 pw.printf("%d  Q0   %s  0   %f    run-1",searchTRECtopics.queryid,doc.get("DOCNO"),docs[i].score);
		pw.println();
	}		
   
	pw.close();
    fos.close();
    osw.close();
 }

}
