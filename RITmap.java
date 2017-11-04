
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

/**
 * 
 * Class RITmap builds a graph based on map representation of the RIT campus, runs aStar, BFS, or backtracking based on the specified method.
 * @author Chris Grate - Clg9100
 * @version 27-Mar-2017
 */
 
public class RITmap {
	
	//Number of edges in our graph.
	private int edgeCount = 0;
	
	//Hashmap of the building numbers
	private static HashMap<String, Integer> buildingNums = new HashMap<String, Integer>();
	
	//Hashmap of our Heuristic Representation.
	private static HashMap<String, Integer> defaultHeuristic = new HashMap<String, Integer>();
	
	//Node class used to represent a node in our graph
	public static class Node{
		public String name; //Name representation of the building
		public ArrayList<Edge> connections; //An edge, this includes the node itself, and its neighbors
		public Node parent; //This nodes parent node
		public int heuristicCost; //The heuristic cost of this node
		public int pathCost; //The path cost associated with this node
		
		public Node(String name, ArrayList<Edge> connections){
			this.name = name;
			this.connections = connections;
			this.parent = null;
			this.heuristicCost = 0;
			this.pathCost = 0;
		}
	}

	//Edge class used to represent an edge in our graph
	public static class Edge {
		public Node n1; //First node of the edge
		public Node n2; //Second node of the edge
		public int weight; //The weight of the edge
		

		public Edge(Node n1, Node n2, int weight){
			this.n1 = n1;
			this.n2 = n2;
			this.weight = weight;
		}
		//Method used to print this edges node members and their collective weight
		public void p(){
			System.out.println("n1="+this.n1.name);
			System.out.println("n2="+this.n2.name);
			System.out.println("weight="+this.weight);
		}
	}

	//Class used to create an isntance of our overall graph being constructed
	public static class Graph{
		ArrayList<Node> nodes;
		
		public Graph(){
			nodes = new ArrayList<Node>();
		}
	}
	
	public static void main(String[] args) {
		
		//usage error
		if(args.length != 2){
			System.err.println("Usage: java RITmap <methodName> <filename>");
			System.exit(0);
		}
		
		String methodName = args[0]; //Which method we want to use, BFS, Backtracking, or aStar
		String filename = args[1]; //The file we're pulling our map information from
		
		File file = new File(filename);
		Scanner scan = null;
		String start = null;
		String end = null;
		try {
			scan = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
		//Get input from the user
		Scanner userInput = new Scanner(System.in);
		System.out.println("Enter a start node: ");
		start = userInput.next();
		System.out.println("Enter an end node: ");
		end = userInput.next();
		
		Graph graph = new Graph();
		
		//Filling the graph from the text file
		while (scan.hasNext()){
			
			int createdN1 = -1; //integer flag to see if n1 was already created
								//-1 if it hasn't been created
								//otherwise it contains the index where it is in the list
			int createdN2 = -1; //integer flag to see if n2 was already created
								//-1 if it hasn't been created
								//otherwise it contains the index where it is in the list
			
			String firstNode = scan.next();
			String secondNode = scan.next();
			int edgeWeight = scan.nextInt();
			
			//checking if the node is already in the list
			for(int i = 0; i < graph.nodes.size(); i++){
				Node n = graph.nodes.get(i);
				if(n.name.equals(firstNode)){
					createdN1 = i;
				}
				if(n.name.equals(secondNode)){
					createdN2 = i;
				}
			}
			
			Node n1 = null;
			Node n2 = null;
			if(createdN1 == -1){
				n1 = new Node(firstNode, new ArrayList<Edge>());
				graph.nodes.add(n1);
			}else{
				n1 = graph.nodes.get(createdN1);
			}
			if(createdN2 == -1){
				n2 = new Node(secondNode, new ArrayList<Edge>());
				graph.nodes.add(n2);
			}else{
				n2 = graph.nodes.get(createdN2);
			}
			Edge newEdge = new Edge(n1, n2, edgeWeight);
			
			n1.connections.add(newEdge);
			n2.connections.add(newEdge);

		}//For End
		
		Node pathEnd = null;
		Node pathEnd2 = null; //for our heuristic in aStar
		Node startNode = null;
		Node endNode = null;
		
		//float startTime = 0;
		//float endTime = 0;
		//float startTime2 = 0;
		//float endTime2 = 0;
		//find the start and end nodes in the graph
		
		for(int i = 0; i < graph.nodes.size(); i++ ){
			if(graph.nodes.get(i).name.equals(start)){
				startNode = graph.nodes.get(i);
			}
			if(graph.nodes.get(i).name.equals(end)){
				endNode = graph.nodes.get(i);
			}
		}
		
		if(methodName.equals("BFS")){
			//startTime = System.currentTimeMillis();
			pathEnd = BFS(graph.nodes, startNode, endNode);
			//endTime = System.currentTimeMillis();
		}
		
		if(methodName.equals("Backtracking")){
			//startTime = System.currentTimeMillis();
			pathEnd = Backtracking(graph.nodes, startNode, endNode);
			//endTime = System.currentTimeMillis();
		}
		
		if(methodName.equals("aStar")){
			
			//fill the maps with the appropriate heuristic values
			defaultHeuristic.put("GVE", 90);
			defaultHeuristic.put("GVD", 75);
			defaultHeuristic.put("GVC", 112);
			defaultHeuristic.put("GVP", 53);
			defaultHeuristic.put("CRS", 61);
			defaultHeuristic.put("HLC", 89);
			defaultHeuristic.put("COL", 65);
			defaultHeuristic.put("ROS", 119);
			defaultHeuristic.put("LOW", 118);
			defaultHeuristic.put("GOS", 85);
			defaultHeuristic.put("WAL", 105);
			defaultHeuristic.put("LBR", 107);
			defaultHeuristic.put("BOO", 92);
			defaultHeuristic.put("UNI", 99);
			defaultHeuristic.put("VIG", 103);
			defaultHeuristic.put("GAN", 123);
			defaultHeuristic.put("BLC", 140);
			defaultHeuristic.put("SAN", 95);
			defaultHeuristic.put("CAR", 94);
			defaultHeuristic.put("CBT", 41);
			defaultHeuristic.put("INS", 37);
			defaultHeuristic.put("ENG", 10);
			defaultHeuristic.put("GLE", 20);
			defaultHeuristic.put("ORN", 26);
			defaultHeuristic.put("GOL", 0);
			defaultHeuristic.put("ENT", 13);
			defaultHeuristic.put("USC", 15);
			defaultHeuristic.put("SIH", 22);
			defaultHeuristic.put("SLA", 47);
			defaultHeuristic.put("SUS", 39);
			defaultHeuristic.put("MON", 131);
			defaultHeuristic.put("EAS", 129);
			defaultHeuristic.put("POL", 143);
			defaultHeuristic.put("SAU", 142);
			defaultHeuristic.put("CPC", 150);
			defaultHeuristic.put("SMT", 162);
			defaultHeuristic.put("CLK", 158);
			defaultHeuristic.put("RIA", 173);
			defaultHeuristic.put("HAC", 160);
			defaultHeuristic.put("AUG", 168);
			defaultHeuristic.put("GWH", 211);
			defaultHeuristic.put("FMS", 1027);
			defaultHeuristic.put("GOR", 170);
			defaultHeuristic.put("RSC", 219);
			defaultHeuristic.put("CSD", 226);
			defaultHeuristic.put("LBJ", 231);
			
			//heuristic |Golisano's Building Number - Curr. Building Number|
			
			//Under the assumption that it would be 405
			buildingNums.put("GVE", Math.abs(70-405));
			buildingNums.put("GVD", Math.abs(70-404));
			buildingNums.put("GVC", Math.abs(70-403));
			buildingNums.put("GVP", Math.abs(70-400));
			buildingNums.put("CRS", Math.abs(70-89));
			buildingNums.put("HLC", Math.abs(70-14));
			buildingNums.put("COL", Math.abs(70-18));
			buildingNums.put("ROS", Math.abs(70-10));
			buildingNums.put("LOW", Math.abs(70-12));
			buildingNums.put("GOS", Math.abs(70-8));
			buildingNums.put("WAL", Math.abs(70-5));
			buildingNums.put("LBR", Math.abs(70-6));
			buildingNums.put("BOO", Math.abs(70-7));
			buildingNums.put("UNI", Math.abs(70-7));
			buildingNums.put("VIG", Math.abs(70-7));
			buildingNums.put("GAN", Math.abs(70-7));
			buildingNums.put("BLC", Math.abs(70-77));
			buildingNums.put("SAN", Math.abs(70-7));
			buildingNums.put("CAR", Math.abs(70-76));
			buildingNums.put("CBT", Math.abs(70-75));
			buildingNums.put("INS", Math.abs(70-73));
			buildingNums.put("ENG", Math.abs(70-17));
			buildingNums.put("GLE", Math.abs(70-9));
			buildingNums.put("ORN", Math.abs(70-13));
			buildingNums.put("GOL", Math.abs(70-70));
			buildingNums.put("ENT", Math.abs(70-82));
			buildingNums.put("USC", Math.abs(70-87));
			buildingNums.put("SIH", Math.abs(70-87));//SIH is part of USC but listed on the map differently
			buildingNums.put("SLA", Math.abs(70-78));
			buildingNums.put("SUS", Math.abs(70-81));
			buildingNums.put("MON", Math.abs(70-15));
			buildingNums.put("EAS", Math.abs(70-1));
			buildingNums.put("POL", Math.abs(70-22));
			buildingNums.put("SAU", Math.abs(70-4));
			buildingNums.put("CPC", Math.abs(70-3));
			buildingNums.put("SMT", Math.abs(70-16));
			buildingNums.put("CLK", Math.abs(70-3));
			buildingNums.put("RIA", Math.abs(70-2));
			buildingNums.put("HAC", Math.abs(70-23));
			buildingNums.put("AUG", Math.abs(70-23));
			buildingNums.put("GWH", Math.abs(70-25));
			buildingNums.put("FMS", Math.abs(70-99));
			buildingNums.put("GOR", Math.abs(70-24));
			buildingNums.put("RSC", Math.abs(70-53));
			buildingNums.put("CSD", Math.abs(70-55));
			buildingNums.put("LBJ", Math.abs(70-60));
			
			//startTime = System.currentTimeMillis();
			setHeuristic(graph, defaultHeuristic);
			pathEnd = aStar(graph.nodes, startNode, endNode);
			//endTime = System.currentTimeMillis();
			
			//startTime2 = System.currentTimeMillis();
			setHeuristic(graph, buildingNums);
			pathEnd2 = aStar(graph.nodes, startNode, endNode);
			//endTime2 = System.currentTimeMillis();
		}
		
		ArrayList<String> path = getPath(pathEnd, startNode);
		
		for(String s : path){
			System.out.print( s + " ");
		}
		System.out.println();
		System.out.println("Path Size: "+ path.size());
		int distance = getDist(path,endNode);
		System.out.println("Total Cost: "+ distance);
		//System.out.println("Time Taken: " + (endTime - startTime));
		
		//Print out the path generated, Size of path, and the cost
		if(methodName.equals("aStar")){
			ArrayList<String> ourPath = getPath(pathEnd2, startNode);
			System.out.println("\n\n---Using Our Heuristic---");
			for(String s : ourPath){
				System.out.print( s + " ");
			}
			System.out.println();
			System.out.println("Path Size: "+ ourPath.size());
			int distance2 = getDist(ourPath, endNode);
			System.out.println("Total Cost: "+ distance2);
			//System.out.println((endTime2 - startTime2));
		}
	}//Main End
	
	/**
	 * Method used to return the path cost to the current endNode
	 * @param path - the path we've taken in our graph
	 * @param endNode - the last node in our graph
	 * @return the path cost
	 */
	private static int getDist(ArrayList<String> path,Node endNode) {
			int toCost = 0;
			int i = path.size()-1;
			Node currentNode = endNode;
			while ( !currentNode.name.equals(path.get(0))){
				for(Edge e : currentNode.connections){
				
					if(e.n1.name.equals( path.get(i)) && e.n1.name.equals(currentNode.parent.name)){
						toCost+=e.weight;
						currentNode = e.n1;
					}
					if(e.n2.name.equals( path.get(i)) && e.n2.name.equals(currentNode.parent.name)){

						toCost+=e.weight;
						currentNode = e.n2;
					}
					
				}
				i-=1;
			}
		return toCost;
	}

	//Adapted from https://en.wikipedia.org/wiki/Breadth-first_search
	/**
	 * Function used to create a graph and fid the shortest path from our start position, to our end position
	 * Using the BFS algorithm
	 * 
	 * @param theGraph - The graph we're performing BFS on
	 * @param start -Our specified starting node
	 * @param end - our specified end node
	 * @return null if there is no path between our start and end node.
	 */
	public static Node BFS (ArrayList<Node> theGraph , Node start, Node end){
		LinkedList<Node> theStack = new LinkedList<Node>();
		start.parent = start;
		theStack.push(start);
		while(theStack.size() !=0){
			Node v  = theStack.pop();
			//if v is goal return
			if(v.name.equals(end.name)){
				return v;
			}
			//else
			//somehow mark v as visited
			//find neighbors that haven't been visited
			for (Edge e: v.connections) {
				if (!e.n1.name.equals(v.name)){
					if(e.n1.parent == null) {
						e.n1.parent = v;
						//put neighbors in stack
						theStack.add(e.n1);			
					}
				}
				if (!e.n2.name.equals(v.name)){
					if(e.n2.parent == null) {
						e.n2.parent = v;
						//put neighbors in stack
						theStack.add(e.n2);			
					}
				}
			}
		}	
	return null; //No goal found	
	}// End BFS method
	
	/**
	 * Perform the backtracking algorithm on our graph to find the shortest path form our given start node to our given end node
	 * @param theGraph - the graph we are traversing
	 * @param start - our user specified start node
	 * @param end - our user specified end node
	 * @return null if no path exists between our two nodes
	 */
	public static Node Backtracking (ArrayList<Node> theGraph , Node start, Node end){
		LinkedList<Node> theStack = new LinkedList<Node>();
		start.parent = start;
		theStack.push(start);
		while(theStack.size() !=0){
			Node v  = theStack.pop();
			//if v is goal return
			if(v.name.equals(end.name)){
				return v;
			}
			//else
			//mark v as visited
			//find neighbors that haven't been visited
			for (Edge e: v.connections) {
				if (!e.n1.name.equals(v.name)){
					if(e.n1.parent == null) {
						e.n1.parent = v;
						//put neighbors in stack
						theStack.add(0,e.n1);			
					}
				}
				if (!e.n2.name.equals(v.name)){
					if(e.n2.parent == null) {
						e.n2.parent = v;
						//put neighbors in stack
						theStack.add(0,e.n2);			
					}
				}
			}
		}	
		return null;		
	}
	
	/**
	 * Perform the aStar algorithm to find the shortest path between our two nodes
	 * @param theGraph - the graph we're traversing
	 * @param start - our start node
	 * @param end - our end node
	 * @return null if no path exists between our two nodes
	 */
	public static Node aStar(ArrayList<Node> theGraph , Node start, Node end){
		// The set of nodes to be evaluated
		ArrayList<Node> open = new ArrayList<Node>();
		
		//The set of nodes that have already been evaluated
		ArrayList<Node> closed = new ArrayList<Node>();
		
		Node current = null;
		open.add(start);
		int lowestCost = Integer.MAX_VALUE;
		
		
		//Keep looping until we don't have anymore nodes to evaluate
		
		while( !open.isEmpty() ){
			//get the node with the lowest f cost
			//For each node in OPEN
			for(int i = 0; i < open.size();i++){
				if(open.get(i).pathCost < lowestCost){
					lowestCost = open.get(i).pathCost ;
						
				}
			}
			for(int j = 0; j < open.size();j++){
				//if the heuristicCost == the currently lowest noted heuristicCost
				if(open.get(j).pathCost == lowestCost){
					current = open.get(j);
					open.remove(j);
					closed.add(current);
				}
				
			}
			
			current.pathCost -= current.heuristicCost;

			if(current.name.equals(end.name)){ ///ficx
				return current;
			}
			
		
			
			//For each neighbor of the current Node
			for(Edge e: current.connections){
				//If the neighbor (edge member) currently being looked at is not our current node
				Node half = null; // what half of edge we care about
				
				if(!e.n1.name.equals(current.name)){
					half = e.n1;
					//Check every node in closed, see if our current exists in closed
					
				}
				if(!e.n2.name.equals(current.name)){
					//Check every node in closed, see if our current exists in closed
					half = e.n2;
					
				}
				boolean goOn = true;
				for(Node n: closed){
					//If it is in closed, break (essentially skip to next neighbor)
					if(n.name.equals(half.name)){
						goOn = false;
					}
				}
				
				if (goOn) {
				
					Node neighbor = half;

					neighbor.pathCost = current.pathCost + neighbor.heuristicCost+e.weight;
					if (!open.contains(neighbor)) {
						neighbor.parent = current;

						open.add(neighbor);
					}
					else {
						for (Node n: open) {
							if (n.name.equals(neighbor.name)) {
								if (neighbor.pathCost < n.pathCost) {
									neighbor.parent = current;
									open.add(neighbor);
								}
							}
						}
					}
					
				}
			}
			lowestCost = Integer.MAX_VALUE;
		}
		System.out.println("sorry");
		return null;
	}
	
	/**
	 * Method used to return the path between our start and end node
	 * @param end - our ending node
	 * @param start - our starting node
	 * @return path - the path between our start and end node
	 */
	public static ArrayList<String> getPath(Node end, Node start){
		ArrayList<String> path = new ArrayList<String>();
		path.add(end.name);
		while(!end.name.equals(start.name)){//if the end is the same as the start we've finished the path (Changed)
			path.add(0, end.parent.name);//add the name to the beginning of the list
			end = end.parent;
			//System.out.println(end.parent.name);
		}
		
		return path;
	}
	
	/**
	 * Method used to set the heuristic values of our nodes for use in aStar algorithm
	 * @param graph - our graph
	 * @param hvals - the heuristic values of our building nodes
	 */
	public static void setHeuristic(Graph graph, Map<String,Integer> hvals){
		for(Node n : graph.nodes ){
			int hval = hvals.get(n.name).intValue();
			n.heuristicCost = hval;
		}
	}
}// RITmap class end
