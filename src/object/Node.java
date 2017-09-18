package object;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Sajad
 * Graph node represented by and adjacency map
 * 
 */
public class Node {
	
	private String id;
	private Map<Node, Integer> neighbors;

	public Node(String id) {
		this.id = id;
		neighbors = new HashMap<>();
	}
	
	public void addNeighbor(Node neighbor, int travelTime){
		neighbors.put(neighbor, travelTime);
	}
	
	public Map<Node, Integer> getNeighbors() {
		return neighbors;
	}
	
	public String getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return id;
	}
}
