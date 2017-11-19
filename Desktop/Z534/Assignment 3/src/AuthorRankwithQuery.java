import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections15.BidiMap;
import org.apache.commons.collections15.functors.MapTransformer;
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

	public static void PageRankPriors(Hypergraph<Integer,Integer> h, BidiMap<Integer, Integer> bmapid, String querystring) throws IOException, ParseException, ClassCastException{
		
		Analyzer analyzer = new StandardAnalyzer();
	    QueryParser parser = new QueryParser("content", analyzer);
		String pathToIndex = "C:\\Users\\cool\\Desktop\\Z534\\author.index\\author_index";			
		IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(pathToIndex)));
		IndexSearcher searcher = new IndexSearcher(reader);
        Query query = parser.parse(QueryParser.escape(querystring));
		searcher.setSimilarity(new BM25Similarity());
		
		//Top 300 publications are retrieved 
		
		TopDocs topDocs = searcher.search(query,300);
		ScoreDoc[] docs = topDocs.scoreDocs;
	    HashMap <Integer,Double> hprior = new HashMap<Integer, Double>(); 
		
	    //For each author in the ranked documents, score is added to prior
	    
	    for (int i = 0; i < docs.length; i++) {
			
		        Document doc = searcher.doc(docs[i].doc);
		        int aid= bmapid.get(Integer.parseInt(doc.get("authorid")));
		        if(hprior.containsKey(aid))
		        {
		        	hprior.replace(aid,(double) hprior.get(aid)+docs[i].score);
		        }
		        else
		        {
		        	hprior.put(aid, (double) docs[i].score);
		        }
		        
		}
		
		//Calculating prior probability for each of the vertex/ author
		
		Double np=(double) 0;
		Map<Integer, Double> nprior =new HashMap<Integer, Double>();       //normalized prior for each author is stored in this hashmap
			
		for(int aid : h.getVertices())
		{
		
			if(hprior.containsKey(aid)) 
			{	
			  if(h.inDegree(aid)!=0)
			  {	
			
				for(int prev : h.getPredecessors(aid))
			    {
			       if(hprior.containsKey(prev))
			       {  
			    	 np= np+ hprior.get(prev);
			    	 
			       }
			       else
			       {
			    	   continue;
			       }   
				}
			
			    Double newValue= (hprior.get(aid)/np);           //author prior is normalized 
		
		        nprior.put(aid, newValue);       
		      }
			}
			else
			{
				nprior.put(aid, (double) 0);
			}
			
   	}
		
		//Calculating pagerank with priors
		MapTransformer<Integer, Double> mp =(MapTransformer<Integer, Double>) MapTransformer.getInstance(nprior);
		
		
		PageRankWithPriors<Integer, Integer> pgprior = new PageRankWithPriors<Integer, Integer>(h, mp,0.1);
		
		pgprior.setTolerance(0.85);
		
		pgprior.evaluate();
		
		HashMap<Integer, Double> res =new HashMap<Integer, Double>();
		
		for(int vertex: h.getVertices())
	 	{
	 	   res.put(vertex,pgprior.getVertexScore(vertex));
	 	}
		
		//Top 10 authors
		
		List<Double> lp = new ArrayList<Double>(res.values());
		lp.sort(Collections.reverseOrder());
		lp = lp.subList(0,10);
		int count =0;		
		System.out.println("----------------------------------------------------- ");
		
		System.out.println("TOP TEN AUTHORS" + " For Query " + querystring);
				
		while(count!=10)
		{
			for(int author: res.keySet())
		    {
			      if(res.get(author)==lp.get(count))
			      {
			    	  
		         	  System.out.println("For author: "+ bmapid.getKey(author) + " Score "+ lp.get(count));
			    	  break;
			      }
	       	}
			
			count++;
	    }
		
		
	 	reader.close();

	}

}
