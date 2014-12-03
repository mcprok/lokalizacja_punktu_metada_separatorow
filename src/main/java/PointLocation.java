package main.java;

import gogui.GoGui;
import gogui.Point;

import java.io.IOException;


public class PointLocation {

	public static void main(String[] args) throws IOException {
		Solver graph = new Solver();
		graph.initializeGraph("src/main/resources/" + args[0]);


		graph.findPointLocation(new Point(Double.valueOf(args[1]), Double.valueOf(args[2])));

		GoGui.saveJSON("src/main/resources/result_ " + args[0] + ".txt");
	}



}
