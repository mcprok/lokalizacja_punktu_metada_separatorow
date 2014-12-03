package main.java;

import gogui.Line;
import gogui.Point;


public class Edge{
	private int start;
	private int end;
	private Line line;
	private int weight;
	private int separatorId;
	public double a;
	public double b;

	public Edge(Point point1, Point point2, int start, int end) {
		line = new Line(point1, point2);
		this.start = start;
		this.end = end;
		this.weight = 1;
		calculateFunction(point1, point2);
	}
	private void calculateFunction(Point start, Point end) {
		this.a = (end.y - start.y) / (end.x - start.x);
		this.b = start.y - a * start.x;
	}

	public boolean isOnLine(Point p) {
		return p.y == (a*p.x + b);
	}

	public boolean isAbove(Point p) {
		return p.y < this.line.getPoint1().y;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public Line getLine() {
		return line;
	}

	public int getSeparatorId() {
		return separatorId;
	}

	public void setSeparatorId(int separatorId) {
		this.separatorId = separatorId;
	}

	@Override
	public String toString() {
		return "Edge{" +
			"start=" + start +
			", end=" + end +
			", weight=" + weight +
			'}';
	}
}
