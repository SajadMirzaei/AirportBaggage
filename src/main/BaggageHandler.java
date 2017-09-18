package main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import cache.Cache;
import cache.FullCache;
import cache.LFRUCache;
import exception.InputException;
import object.Departure;
import object.Node;

/**
 * 
 * @author Sajad
 * Main Class
 * Starts by initiating the appropriate cache, then reads the input file and sets the nodes and departures in two maps. 
 * Since queries can be extremely large class answers queries while reading the input file in a StreamReader.  
 * 
 */
public class BaggageHandler {
	
	private Map<String, Node> nodeMap; // the graph represented by a map of nodes
	private Map<String, Departure> departureMap; // representing the departure information
	private Cache cache;

	public BaggageHandler(String[] args) {
		nodeMap = new HashMap<>(); 
		departureMap = new HashMap<>();
		// Evaluating the input arguments
		String url = "";
		if (args.length == 1){
			cache = new FullCache();
			url = args[0];
		}else if (args.length == 2){
			if ("-c".equals(args[0].toLowerCase())){
				cache = new LFRUCache(1000);
				url = args[1];
			}else{
				System.out.println("Invalid input arguments");
				System.exit(0);
			}
		}else{
			System.out.println("Invalid input arguments");
			System.exit(0);
		}
		cache.setNodeMap(nodeMap);
		// Starts reading the input file and printing the query results while reading the Bags section
		readInput(url);
	}
	
	private void readInput(String url) {
		FileInputStream fis= null;
		InputStreamReader reader = null;
		BufferedReader br = null;
		try {
			fis = new FileInputStream(url);
			reader = new InputStreamReader(fis);
			br = new BufferedReader(reader);
			String line = br.readLine();
			int lineNumber = 1;
			while (line != null) {
				// Reading the Conveyor System Section
				if (line.contains("# Section: Conveyor System")){
					line = br.readLine();
					lineNumber++;
					while(line != null && !line.contains("# Section")){
						String[] part = line.split(" ");
						if (part.length < 3) throw InputException.invalidFormat("Conveyor System", lineNumber);
						
						// Retrieving/Initiating Source and Destinations nodes
						Node source = null;
						if (nodeMap.containsKey(part[0]))
							source = nodeMap.get(part[0]);
						else{
							source = new Node(part[0]);
							nodeMap.put(part[0], source);
						}
						Node dest = null;
						if (nodeMap.containsKey(part[1]))
							dest = nodeMap.get(part[1]);
						else{
							dest = new Node(part[1]);
							nodeMap.put(part[1], dest);
						}
						
						// Setting the travelTime
						int travelTime = 1;
						try{
							travelTime = Integer.valueOf(part[2]);
							if (travelTime <= 0) throw new NumberFormatException();
						}catch (NumberFormatException nfe){
							throw InputException.invalidTravelTime(lineNumber);
						}
						source.addNeighbor(dest, travelTime);
						dest.addNeighbor(source, travelTime);
						line = br.readLine();
						lineNumber++;
					}
					if (!nodeMap.containsKey("BaggageClaim")) throw InputException.baggageClaim();
					if (cache instanceof LFRUCache){ // Increasing the cache size to the number of nodes if the graph is large
						if (((LFRUCache) cache).getCapacity() < nodeMap.size()){
							((LFRUCache) cache).setCapacity(nodeMap.size());
						}
					}
				// Reading the Departure Section
				}else if (line.contains("# Section: Departures")){
					if(nodeMap.isEmpty()) throw InputException.noConveyor();
					line = br.readLine();
					lineNumber++;
					while(line != null && !line.contains("# Section")){
						String[] part = line.split(" ");
						if (part.length < 4) throw InputException.invalidFormat("Departures", lineNumber);
						
						if (!nodeMap.containsKey(part[1])) throw InputException.nodeNotExist(lineNumber);
						if (departureMap.containsKey(part[0])) throw InputException.repeatingDepartue(lineNumber);
						Departure departure = new Departure(part[0], nodeMap.get(part[1]), part[2], part[3]);
						departureMap.put(part[0], departure);
						line = br.readLine();
						lineNumber++;
					}
					departureMap.put("ARRIVAL", new Departure("ARRIVAL", nodeMap.get("BaggageClaim"), "", ""));
				// Reading the Bags Section	
				}else if (line.contains("# Section: Bags")){
					if(departureMap.isEmpty()) throw InputException.noDeparture();
					line = br.readLine();
					lineNumber++;
					while(line != null && !line.contains("# Section")){
						String[] part = line.split(" ");
						try {
							if (part.length < 3) throw InputException.invalidFormat("Bags", lineNumber);
							if (!nodeMap.containsKey(part[1])) throw InputException.nodeNotExist(lineNumber);
							if (!departureMap.containsKey(part[2])) throw InputException.departureNotExist(lineNumber);
							query(part[0],nodeMap.get(part[1]),departureMap.get(part[2]).getGate());
						}catch(InputException e){
							System.out.println(e.getMessage());
						}
						line = br.readLine();
						lineNumber++;
					}
				}else{
					line=br.readLine();
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InputException e){
			System.out.println(e.getMessage());
		} finally{
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void query(String bagNumber, Node src, Node dest) {
		String path = cache.getPath(src, dest);
		System.out.println(bagNumber + " " + path);
	}

	public static void main(String[] args) {
		new BaggageHandler(args);
	}

}
