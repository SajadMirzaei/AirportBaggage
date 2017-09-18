package object;

/**
 * 
 * @author Sajad
 * Departure Class representing the flight information
 * 
 */
public class Departure {
	
	private String flightId;
	private Node gate;
	private String destinationAirport;
	private String flightTime;

	public Departure(String flightId, Node gate, String destinationAirport, String flightTime) {
		this.flightId = flightId;
		this.gate = gate;
		this.destinationAirport = destinationAirport;
		this.flightTime = flightTime;
	}

	public String getFlightId() {
		return flightId;
	}

	public void setFlightId(String flightId) {
		this.flightId = flightId;
	}

	public Node getGate() {
		return gate;
	}

	public void setGate(Node gate) {
		this.gate = gate;
	}

	public String getDestinationAirport() {
		return destinationAirport;
	}

	public void setDestinationAirport(String destinationAirport) {
		this.destinationAirport = destinationAirport;
	}

	public String getFlightTime() {
		return flightTime;
	}

	public void setFlightTime(String flightTime) {
		this.flightTime = flightTime;
	}
}
