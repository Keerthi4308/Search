import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.queryparser.classic.ParseException;

import edu.uci.ics.jung.algorithms.scoring.PageRank;
import edu.uci.ics.jung.graph.Hypergraph;
import edu.uci.ics.jung.graph.SetHypergraph;


public class AuthourRank{
	


	public static void main(String args[]) throws IOException, ParseException {
		
		
	   //extract vertice and edges from the file authors.net
		
		//read line by line so one line is input and find space in i it using /s as regex 
		//implies bfr first space is node identifier, ....in next case bfr
		//second space is second node identifier
		
		//or read line by line and save into array using line.split() and use first parameter,
		//case 2 first 2 parameters
	    
		String filename = "C:\\Users\\cool\\Desktop\\Z534\\author.net";

		File newfile = new File(filename);

		String totalip = FileUtils.readFileToString(newfile, "UTF-8");
		int Edgestart=0,i=1;
		//String[] nodeid = new String[2000];
		
		Hypergraph<Integer, Integer> g= new SetHypergraph<Integer, Integer>();
		
		String[] lines = totalip.split("\\r?\\n");
	    for (String line : lines) {
	         //System.out.print(line);
	         String[] content = line.split(" ");
	        // System.out.println(content[0]);
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
	        		HashSet<Integer>h =new HashSet<Integer>();
	        		h.add(Integer.parseInt(content[0]));
	        		h.add(Integer.parseInt(content[1]));
	        		g.addEdge(i, h);
	        		i++;
	         }
	         else
	        	 g.addVertex(Integer.parseInt(content[0]));
	         
	         
	      }
	      
	 	System.out.println("Vertices: " + g.getVertexCount() + "Edges: " +g.getEdgeCount()); 
	 	
	 	PageRank<Integer, Integer> pg= new PageRank<Integer,Integer>(g,0.1);
	 	
	 	pg.setTolerance(0.85);
	 	
	 	//pg.setMaxIterations(3);
	 	
	 	pg.evaluate();
	 	
	 	
	 	HashMap<Integer, Double>hresult =new HashMap<Integer, Double>();
	 	
	 	for(int vertex: g.getVertices())
	 	{
	 	   hresult.put(vertex, pg.getVertexScore(vertex));
	 	}
	 	
	 	System.out.println(hresult.entrySet());
	 	
	 	//Page rank with prior
	 	
	 	AuthorRankwithQuery.PageRankPriors(g);
				
	}

}
