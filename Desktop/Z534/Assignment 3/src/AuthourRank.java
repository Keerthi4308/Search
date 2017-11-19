import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections15.BidiMap;
import org.apache.commons.collections15.bidimap.TreeBidiMap;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.queryparser.classic.ParseException;

import edu.uci.ics.jung.algorithms.scoring.PageRank;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;

public class AuthourRank{
	
	public static void main(String args[]) throws IOException, ParseException {

		String filename = "C:\\Users\\cool\\Desktop\\Z534\\author.net";

		File newfile = new File(filename);

		String totalip = FileUtils.readFileToString(newfile, "UTF-8");
		int Edgestart=0,i=1;		

		DirectedGraph<Integer, Integer> g= new DirectedSparseMultigraph<Integer, Integer>();
		
		BidiMap <Integer, Integer> bmapid = new TreeBidiMap<Integer, Integer> ();
		
		
		String[] lines = totalip.split("\\r?\\n");
	    for (String line : lines) {
	         String[] content = line.split(" ");
	         if(content[0].compareTo("*Vertices")==0)
	         {
	        	 continue;
	         }
	        if(content[0].compareTo("*Edges")==0)
	         {
	        	Edgestart++;
	        	 continue;      	 
	         }
	         if(Edgestart!=0)
	         {
	        		g.addEdge(i, Integer.parseInt(content[0]), Integer.parseInt(content[1]), EdgeType.DIRECTED);
	     
	        		i++;
	         }
	         else
	         {
	        	 g.addVertex(Integer.parseInt(content[0]));
	         
                 String label= content[1].replace("\"", " ").trim();
                 bmapid.put(Integer.parseInt(label), Integer.parseInt(content[0]));	     
	         }
	      }
	     	    
	 	PageRank<Integer, Integer> pg= new PageRank<Integer,Integer>(g,0.1);
	 	
	 	pg.setTolerance(0.85);
	 	
	 	pg.evaluate();
	 	
       HashMap<Integer, Double>hresult =new HashMap<Integer, Double>();
	
	 	 	
	 	for(int vertex: g.getVertices())
	 	{
	 	   hresult.put(vertex, pg.getVertexScore(vertex));
	 	}
	 			
		//Top 10 authors
		
		List<Double> l = new ArrayList<Double>(hresult.values());
		l.sort(Collections.reverseOrder());
		l = l.subList(0,10);
		int count =0;
		System.out.println("TOP TEN RANKED AUTHORS");
		while(count!=10)
		{
			for(int author: hresult.keySet())
		    {
			      if(hresult.get(author)==l.get(count))
			      {
			    	  
		         	  System.out.println("For author: "+ bmapid.getKey(author) + " Score "+ l.get(count));
			    	  break;
			      }
	       	}
			
			count++;
	    }
		
	
	 	//Page rank with prior
	 	
	 	AuthorRankwithQuery.PageRankPriors(g,bmapid,"Data Mining");
	 	
	 	AuthorRankwithQuery.PageRankPriors(g,bmapid,"Information Retrieval");
				
	}

}
