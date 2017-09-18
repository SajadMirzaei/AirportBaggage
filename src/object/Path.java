package object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Sajad
 * Representing a path between two nodes
 * Includes a list of Nodes to represent the path
 * Also saves other information to be able to contain subPath information used in LFRU Cache
 * 
 */
public class Path implements Comparable<Path>{
	private int hits = 0;
	private int totalTravelTime = 0;
	private List<Node> path = new ArrayList<>();
	private List<Integer> travelTimeList = new ArrayList<>();
	private Map<Node, Integer> nodeMap = new HashMap<>();
	private Node start;
	private Node end;
	
	public Path() {
	}
	
	public Path(Node start){
		this.start = start;
		path.add(start);
		nodeMap.put(start,0);
		travelTimeList.add(0);
	}

	public void add (Node node, int travelTime){
		path.add(node);
		nodeMap.put(node,path.size()-1);
		travelTimeList.add(travelTime);
		totalTravelTime += travelTime;
		end = node;
	}
	
	public Path copy(){
		Path newPath = new Path();
		newPath.start = start;
		newPath.end = end;
		newPath.path.addAll(path);
		for (Node node: nodeMap.keySet()){
			newPath.nodeMap.put(node, nodeMap.get(node));
		}
		newPath.travelTimeList.addAll(travelTimeList);
		newPath.totalTravelTime = this.totalTravelTime;
		return newPath;
	}
	
	public static String getKey(Node start, Node end){
		return start.getId()+"|-|"+end.getId();
	}
	
	public String getKey(){
		String key = start.getId()+"|-|";
		if (end != null) key += end.getId();
		return key;
	}
	
	public List<Node> getNodes() {
		return path;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Node node: path){
			sb.append(node.getId() + " ");
		}
		sb.append(": " + totalTravelTime);
		return sb.toString();
	}
	
	public String reverseString() {
		StringBuilder sb = new StringBuilder();
		for (int i=path.size()-1; i>=0; i--){
			sb.append(path.get(i).getId() + " ");
		}
		sb.append(": " + totalTravelTime);
		return sb.toString();
	}

	@Override
	public int compareTo(Path o) {
		if (this == o) return 0;
		if (getKey().compareTo(o.getKey()) == 0) return 0;
		if (getScore() == o.getScore()) return getKey().compareTo(o.getKey());
		return Integer.compare(o.getScore(), getScore());
	}
	
	public boolean contains(Node source, Node dest){
		return nodeMap.containsKey(source) && nodeMap.containsKey(dest);
	}

	private int getScore() {
		return hits*path.size();
	}
	
	public void hit(){
		hits++;
	}

	public String getSubpath(Node src, Node dest) {
		int srcIndex = nodeMap.get(src);
		int destIndex = nodeMap.get(dest);
		int travelTime = 0;
		StringBuilder sb = new StringBuilder();
		if (srcIndex < destIndex){
			for(int i=srcIndex; i<destIndex; i++){
				sb.append(path.get(i) + " ");
				travelTime += travelTimeList.get(i+1);
			}
			sb.append(path.get(destIndex) + " : " + travelTime);
		}else {
			for(int i = srcIndex; i > destIndex; i--){
				sb.append(path.get(i) + " ");
				travelTime += travelTimeList.get(i);
			}
			sb.append(path.get(destIndex) + " : " + travelTime);
		}
		return sb.toString();
	}
	
	public Node getStart() {
		return start;
	}
	
	public Node getEnd() {
		return end;
	}
	
}
