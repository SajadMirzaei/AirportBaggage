package cache;

import java.util.Map;

import object.Node;

public interface Cache {
	public String getPath(Node src, Node dest); // returns the minimum path between two nodes
	public void setNodeMap(Map<String,Node> map); // should be passed the graph nodes to run the algorithm
}

