package main;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 * 
 * @author Sajad
 * Simple Test class for different Scenarios
 * Please ignore
 *
 */

public class Test {

	public Test() {
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
			          new FileOutputStream("res/input1.txt")));
			writer.write("# Section: Conveyor System");
			writer.newLine();
			int nodes = 1000;
			writer.write("BaggageClaim A1 1");
			writer.newLine();
			for (int i=0; i<nodes; i++) {
//				writer.write("A" + i + " A" + (i+1) + " " + 1);
//				writer.newLine();
				for (int j=i+1; j<nodes; j+=2){
					writer.write("A" + i + " " + "A" + j + " " + 1);
					writer.newLine();
				}
			}
			writer.write("# Section: Departures");
			writer.newLine();
			for (int i=0; i<nodes; i++) {
				writer.write("UA" + i + " A" + i + " B" + i + " 00:00");
				writer.newLine();
			}
			writer.write("# Section: Bags");
			writer.newLine();
			int queries = 1000000;
			writer.write("0001 A9 ARRIVAL");
			writer.newLine();
			for (int i=0; i<queries; i++) {
				writer.write(i + " A" + (int) (Math.random()*nodes) + " UA" + (int) (Math.random()*nodes));
//				writer.write(i + " A1" + " UA" + (int) (Math.random()*nodes));
				writer.newLine();
//				for (int j=0; j<50; j++){
//					writer.write(i + " A" + 1 + " UA999");
//					writer.newLine();
//				}
//				for (int j=0; j<20; j++){
//					writer.write(i + " A" + 1 + " UA997");
//					writer.newLine();
//				}
//				for (int j=0; j<5; j++){
//					writer.write(i + " A" + 1 + " UA998");
//					writer.newLine();
//				}
//				writer.write(i + " A0 UA999");
//				writer.newLine();
//				writer.write(i + " A111 UA999");
//				writer.newLine();
//				writer.write(i + " A103 UA996");
//				writer.newLine();
//				writer.write(i + " A104 UA995");
//				writer.newLine();
			}
			writer.flush();
			writer.close();
			System.out.println();
			new BaggageHandler(new String[] {"-c" , "res/input1.txt"});
//			new AirportBaggage(new String[] {"res/input.txt"});
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Test();
	}

}
