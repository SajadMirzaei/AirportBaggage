package exception;
/**
 * 
 * @author Sajad
 * Class for handling Input Reading Exceptions
 * 
 */
public class InputException extends Exception{
	public InputException(String message) {
		super("Input Error: " + message);
	}
	
	public static InputException invalidFormat(String part, int line){
		return new InputException("Invalid input format in '" + part + "' Section (Not enough parameters)- Line:" + line);
	}
	
	public static InputException invalidTravelTime(int line){
		return new InputException("Invalid travel time entered (Should be a positive integer) - Line:" + line);
	}
	
	public static InputException baggageClaim(){
		return new InputException("BaggageClaim node is missing in Conveyor System");
	}
	
	public static InputException nodeNotExist(int line){
		return new InputException("There is no such gate defined in Conveyor System - Line:" + line);
	}
	
	public static InputException departureNotExist(int line){
		return new InputException("There is no such departure defined in Departure Section - Line:" + line);
	}
	
	public static InputException repeatingDepartue(int line){
		return new InputException("Departure information already exists - Line:" + line);
	}
	
	public static InputException noConveyor(){
		return new InputException("Conveyor System Section should be defined");
	}
	
	public static InputException noDeparture(){
		return new InputException("Departure Section should be defined");
	}
}
