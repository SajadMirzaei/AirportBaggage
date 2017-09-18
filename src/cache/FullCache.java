package cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import object.Node;
import object.Path;

/**
 * 
 * @author Sajad
 * Implementation of a simple cache which saves all the queries and their answers in a map
 * It is useful for small graphs or when there is no memory limit
 * Here the Dijkstra algorithm returns all paths found and saves them in the map
 *
 */
public class FullCache implements Cache{
	private Map<String, Node> nodeMap;
	private Map<String, Path> pathMap = new HashMap<>();
	int calls = 0;

	public void setNodeMap(Map<String, Node> nodeMap) {
		this.nodeMap = nodeMap;
	}

	@Override
	public String getPath(Node src, Node dest) {
		Path p = pathMap.get(Path.getKey(src, dest));
		if (p != null) return p.toString();
		p = pathMap.get(Path.getKey(dest, src));
		if (p != null) return p.reverseString();
		List<Path> newPaths = dijkstra(src, dest);
		String result = "";
		for(Path path: newPaths){
			if (path == null){
				return src.getId() + " " + dest.getId() + " : no path";
			}
			if (path.getStart() == src && path.getEnd() == dest) result = path.toString();
			pathMap.put(path.getKey(), path);
		}
		return result;
	}
	
	private List<Path> dijkstra(Node src, Node dest) {
		class QElement implements Comparable<QElement>{
			Node node;
			int distance = Integer.MAX_VALUE;
			
			public QElement(Node node) {
				this.node = node;
			}
			
			@Override
			public int compareTo(QElement o) {
				if (this == o) 
					return 0;
				if (distance == o.distance)
					return node.getId().compareTo(o.node.getId())<0 ? -1:1;
				return distance > o.distance ? 1:-1;
			}
		}
		List<Path> pathList = new ArrayList<>();
		TreeSet<QElement> minHeap = new TreeSet<>();
		Map<Node, QElement> minHeapMap = new HashMap<>(); // A relation between each Dijkstra Node and the actual Node
		Map<Node, Path> currentPathMap = new HashMap<>(); // A map to save the path from source to each node
		// Adding all the nodes to the heap with initial value of Infinity except the source with value 0.
		for (Node node: nodeMap.values()){
			QElement qElement = new QElement(node);
			if (node.equals(src)){
				qElement.distance = 0;
				minHeap.add(qElement);
				minHeapMap.put(node, qElement);
				currentPathMap.put(src, new Path(src)); // First path is the source to itself.
			}else{
				minHeap.add(qElement);
				minHeapMap.put(node, qElement);
			}
		}
		//In the following loop, minHeap contains all nodes whose shortest distance to source is not finalized (Initially infinity for all Nodes)
		while (!minHeap.isEmpty()){
			QElement minElement = minHeap.pollFirst(); // Extracting the node with minimum value
			minHeapMap.remove(minElement.node);
			if (minElement.distance == Integer.MAX_VALUE){
				// There is no path between src and dest because the graph is not connected
				pathList.add(null);
				return pathList;
			}
			// Traverse through all adjacent vertices of dNode and update their distance values and their best path
			for (Node neighbor: minElement.node.getNeighbors().keySet()){
				if (!minHeapMap.containsKey(neighbor))
					continue;
				int distanceToNeighbor = minElement.node.getNeighbors().get(neighbor);
				int distFromHere = minElement.distance + distanceToNeighbor;
				int existingDist = minHeapMap.get(neighbor).distance;
				if (distFromHere < existingDist){
					// decrease key for the neighbor node.
					QElement neighorDNode = minHeapMap.get(neighbor);
					minHeap.remove(neighorDNode);
					neighorDNode.distance = distFromHere;
					minHeap.add(neighorDNode);
					// Updating the corresponding path for the node that got its key decreased;
					Path currentPath = currentPathMap.get(minElement.node).copy();
					currentPath.add(neighbor, distanceToNeighbor);
					currentPathMap.put(neighbor, currentPath);
				}
			}
			pathList.add(currentPathMap.get(minElement.node));
//			if (minElement.node == dest){
//				return pathList;
//			}
		}
		return pathList;
	}
}
