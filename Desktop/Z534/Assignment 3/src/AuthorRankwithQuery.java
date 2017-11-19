import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.collections15.Transformer;
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
import org.apache.lucene.store.FSDirectory;

import edu.uci.ics.jung.algorithms.scoring.PageRankWithPriors;
import edu.uci.ics.jung.graph.Hypergraph;

public class AuthorRankwithQuery {

	public static void PageRankPriors(Hypergraph<Integer,Integer> h) throws IOException, ParseException {
		
		Analyzer analyzer = new StandardAnalyzer();
	    QueryParser parser = new QueryParser("content", analyzer);
		String pathToIndex = "C:\\Users\\cool\\Desktop\\Z534\\author.index\\author_index";			
		IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(pathToIndex)));
		
		System.out.println(reader.document(990));
		IndexSearcher searcher = new IndexSearcher(reader);
        String querystring= "Data Mining"; 
        Query query = parser.parse(QueryParser.escape(querystring));
		searcher.setSimilarity(new BM25Similarity());
		TopDocs topDocs = searcher.search(query,300);
		ScoreDoc[] docs = topDocs.scoreDocs;
	    HashMap <Integer,Double> hprior = new HashMap<Integer, Double>();
		
		for (int i = 0; i < docs.length; i++) {
		        Document doc = searcher.doc(docs[i].doc);
		        int aid=Integer.parseInt(doc.get("authorid"));
		        if(hprior.containsKey(aid))
		        {
		        	hprior.replace(aid,(double) hprior.get(aid)+docs[i].score);
		        }
		        else
		        {
		        	hprior.put(aid, (double) docs[i].score);
		        }
		        
		}
		
		reader.close();
		
		//Calculating prior probability for each of the vertex/ author
		
		Double np=(double) 0;
		
			
		for(int aid : hprior.keySet())
		{
			if(h.inDegree(aid)!=0)
			{	
			  for(int prev : h.getPredecessors(aid))
			  {
			    np= np+ hprior.get(prev);
			 }
			
			  Double newValue= hprior.get(aid)/np;
		
			hprior.replace(aid, newValue);
			}	
		}
		
		//Calculating pagerank with priors
		
		@SuppressWarnings("unchecked")
		PageRankWithPriors<Integer, Integer> pgprior = new PageRankWithPriors<Integer, Integer>(h,(Transformer<Integer, Double>) hprior,0.1);
		
		pgprior.setTolerance(0.85);
		
		pgprior.evaluate();
		
		//Top ten ranked authours
		
		Map<Integer, Double> res =new TreeMap<Integer, Double>(Collections.reverseOrder());
		
		for(int vertex: h.getVertices())
	 	{
	 	   res.put(vertex,pgprior.getVertexScore(vertex));
	 	}
		
		System.out.println(res.entrySet());
	 	
	 	

	}

}
