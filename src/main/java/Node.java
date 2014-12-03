package main.java;

import gogui.Point;

import java.util.ArrayList;
import java.util.List;


public class Node extends Point {

	private int id;
	private final List<Integer> neighbours;

	private List<Edge> inEdges;
	private List<Edge> outEdges;

	private int in;
	private int out;

	public Node(int id,Point node) {
		super(node.x, node.y);
		inEdges = new ArrayList<>();
		outEdges = new ArrayList<>();
		neighbours = new ArrayList<>();
		this.id = id;
		in = 0;
		out = 0;
	}

	public void addNeighbour(int neighbourId) {
		neighbours.add(neighbourId);

	}

	public void addInEdge(Edge edge) {
		inEdges.add(edge);
		incIn();
	}
	public void addOutEdge(Edge edge) {
		outEdges.add(edge);
		incOut();
	}

	public int getIn() {
		return in;
	}

	public void setIn(int in) {
		this.in = in;
	}

	private void incIn() {
		in++;
	}

	private void incOut() {
		out++;
	}

	public int getOut() {
		return out;
	}

	public void setOut(int out) {
		this.out = out;
	}

	public int getId() {
		return id;
	}

	public List<Integer> getNeighbours() {
		return neighbours;
	}

	@Override
	public String toString() {
		return "Node{" +
			"id=" + id +
			", in=" + in +
			", out=" + out +
			'}';
	}

	public List<Edge> getOutEdges() {
		return outEdges;
	}

	public List<Edge> getInEdges() {
		return inEdges;
	}
}
