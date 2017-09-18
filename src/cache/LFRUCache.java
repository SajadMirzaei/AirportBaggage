package cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeSet;

import object.Node;
import object.Path;

/**
 * 
 * @author Sajad
 * Implementation of a LFRU cache which saves all the queries and their answers in an LRU cache (privileged set) and an LFU cache (unprivileged)
 * It is useful for large graphs or when there is memory limit
 * The Dijkstra algorithm here only returns one path from the source to the destination
 * 
 */
public class LFRUCache implements Cache{
	int size = 0;
	int privilegeCapacity;
	int capacity;
	TreeSet<Path> unprivileged;
	LinkedList<Path> privileged;
	Map<String, Node> nodeMap;
	
	public LFRUCache(int capacity) {
		this.capacity = capacity;
		privilegeCapacity = Math.min(100,capacity/2);
		unprivileged = new TreeSet<>();
		privileged = new LinkedList<>();
	}
	
	public String getPath(Node src, Node dest){
		Path hitPath = null;
		String result = "";
		boolean hit = false;
		// We first check the privileged cache which is a LRU cache
		// If there is a hit the hit element will go on top
		for(int i=0; i<privileged.size(); i++){
			Path path = privileged.get(i);
			if (path.contains(src, dest)){
				hitPath = path;
				result = path.getSubpath(src,dest);
				hit = true;
				hitPath.hit();
				privileged.remove(i);
				privileged.addFirst(hitPath);
				break;
			}
		}
		// If there is no hit in the LRU cache we check the unprivileged cache which is a LFU cache
		if (!hit){
			Iterator<Path> it = unprivileged.iterator();
			while(it.hasNext()){
				Path path = it.next();
				if (path.contains(src, dest)){
					hitPath = path;
					result = path.getSubpath(src,dest);
					hit = true;
					break;
				}
			}
			// If there is a hit, the hit element will go to the top of the privileged list
			// The least recent node in privileged list will go to unprivileged list which is sorted
			if (hit){
				Path lastPrivilegedPath = privileged.removeLast();
				privileged.addFirst(hitPath);
				unprivileged.remove(hitPath);
				hitPath.hit();
				unprivileged.add(lastPrivilegedPath);
				return result;
			}
			// if There is no hit in the cache we call the Dijkstra's algorithm to get new paths
			// The same happens and new paths go to the privileged list and lease recent paths in privileged list downgrade to unprivileged list
			// The lease frequent paths in unprivileged list will be removed. 
			Path path = dijkstra(src, dest);
			// There is no path between src and dest
			if (path == null){
				return src.getId() + " " + dest.getId() + " : no path";
			}
			if (capacity > 0){
				Path lastPrivilegedPath = size >= privilegeCapacity ? privileged.pollLast():null;
				privileged.addFirst(path);
				size++;
				if (lastPrivilegedPath != null) {
					boolean added = false;
					added = unprivileged.add(lastPrivilegedPath);
					if (size > capacity && added){
						unprivileged.remove(unprivileged.last());
						size--;
					}
				}
			}
			path.hit();
			result = path.getSubpath(src, dest);
			
		}
		return result;
	}
	
	private Path dijkstra(Node src, Node dest) {
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
				return null;
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
			if (minElement.node == dest){
				return currentPathMap.get(minElement.node);
			}
		}
		return null;
	}

	public void setNodeMap(Map<String, Node> nodeMap) {
		this.nodeMap = nodeMap;
	}
	
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public int getCapacity() {
		return capacity;
	}

}
