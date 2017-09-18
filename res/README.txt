INTRODUCTION :  This program is a java program that is an Airport baggage system handler to route bags to their flight or appropriate baggage claim
                
                The information about the graph, the gates as nodes and their travel time to other gates are given in an input file
                The queries of the minimal path from a node to another is given in the same file. 
                
                This program uses the Dijkstra algorithm to find the shortest path between nodes. 
                The running time for the algorithm for each query is O(|V + E|log |V|) here |V| is the number of nodes in the graph while |E| is the number of edges. 
                Since the number of queries can be extremely large the program uses cache to avoid repetition of algorithm call.
                Two forms of caches are used to avoid repeating runs of the algorithms. 
                1. A simple cache which saves all the queries and their answers in a map
                     * It is useful for small graphs or when there is no memory limit and can be faster
                     * For large graphs, it may run out of memory at some point
                     * Unless specified in the program options this method is the default method for caching
                2. A LFRU cache which saves all the queries and their answers in a combination of LRU cache (privileged set) and an LFU cache (unprivileged set)
                     * It is useful for large graphs or when there is memory limit.
                     * It never runs out of memory and may be slower than the first method when the number of nodes are small.
                     * For larger graphs it becomes faster than the first method as it runs more queries.
                

---------------------------------------------------------------------------

SYNOPSIS     : java -jar BaggageHandler.jar [<options>] <input-file Path> 

OPTIONS      : (Optional) use "-c" to use LFRU cache

INPUT        :  Input: The input consists of several sections.  The beginning of each section is marked by a line starting: “# Section:”
                
                Section 1: A weighted bi-directional graph describing the conveyor system.
                Format: <Node 1> <Node 2> <travel_time>

                Section 2: Departure list Format:
                <flight_id> <flight_gate> <destination> <flight_time>
                
                Section 3: Bag list Format:
                <bag_number> <entry_point> <flight_id>

                
OUTPUT       :  The optimized route for each bag
                <Bag_Number> <point_1> <point_2> [<point_3>, …] : <total_travel_time>

                The output is in the same order as the Bag list section of the input.

EXAMPLE      :  

                Example Input:
                # Section: Conveyor System
                Concourse_A_Ticketing A5 5
                A5 BaggageClaim 5
                A5 A10 4
                A5 A1 6
                A1 A2 1
                A2 A3 1
                A3 A4 1
                A10 A9 1
                A9 A8 1
                A8 A7 1
                A7 A6 1
                # Section: Departures
                UA10 A1 MIA 08:00
                UA11 A1 LAX 09:00
                UA12 A1 JFK 09:45
                UA13 A2 JFK 08:30
                UA14 A2 JFK 09:45
                UA15 A2 JFK 10:00
                UA16 A3 JFK 09:00
                UA17 A4 MHT 09:15
                UA18 A5 LAX 10:15
                # Section: Bags
                0001 Concourse_A_Ticketing UA12
                0002 A5 UA17
                0003 A2 UA10
                0004 A8 UA18
                0005 A7 ARRIVAL
                ```

                Example Output:
                0001 Concourse_A_Ticketing A5 A1 : 11
                0002 A5 A1 A2 A3 A4 : 9
                0003 A2 A1 : 1
                0004 A8 A9 A10 A5 : 6
                0005 A7 A8 A9 A10 A5 BaggageClaim : 12
		   
CONTACT      :  Please send bug reports and technical questions to Sajad Mirzaei at 
                <mirzaei.sajad@gmail.com>.
