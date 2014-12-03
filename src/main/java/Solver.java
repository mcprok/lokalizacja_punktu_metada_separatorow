package main.java;

import gogui.GeoList;
import gogui.Line;
import gogui.Point;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import static gogui.GoGui.snapshot;


public class Solver {
	private List<Node> vertices;
	private List<Edge> edges;
	private List<List<Edge>> separators;
	private GeoList<Point> points;
	private GeoList<Line> lines;


	public Solver() {

		this.vertices = new ArrayList<>();
		this.edges = new ArrayList<>();
		this.points = new GeoList<>();
		this.lines = new GeoList<>();
		this.separators = new ArrayList<>();
	}

	public void initializeGraph(String filename) throws IOException {

		Files.lines(Paths.get(filename)).forEach(
			line -> {
				List<String> splittedLine = Arrays.asList(line.split(" "));

				int id = Integer.valueOf(splittedLine.get(0));
				double x = Double.valueOf(splittedLine.get(1));
				double y = Double.valueOf(splittedLine.get(2));

				Node node = new Node(id, new Point(x, y));

				splittedLine.stream().skip(3).forEach(neighbour -> {
					node.addNeighbour(Integer.valueOf(neighbour));
				});
				points.add(new Point(x, y));
				vertices.add(node);
			}
		);

		Collections.sort(vertices, Comparator.comparing(Node::getId));

		createEdges();
		calculateWeightsForEdges();

		findSeparators();
		distinctSeparatorEdges(0, separators.size());

		colorEdgesUsingSeparatorColor();
	}

	private void createEdges() {

		vertices.stream().forEach(
			vertex -> {
				vertex.getNeighbours().stream().forEach(
					neighbour -> {

						Node tmp = getNodeById(neighbour);
						Edge newEdge = new Edge(vertex, tmp, vertex.getId(), neighbour);

						vertex.addOutEdge(newEdge);
						tmp.addInEdge(newEdge);

						edges.add(newEdge);
						lines.add(new Line(new Point(vertex.x, vertex.y), new Point(tmp.x, tmp.y)));
					}
				);
			}
		);

		snapshot();
	}

	private Node getNodeById(int id) {

		return vertices.stream().filter(v -> v.getId() == id).findFirst().get();
	}

	private void calculateWeightsForEdges() {

		for (int i = 0; i < vertices.size(); i++) {

			Node v = vertices.get(i);
			v.setIn(v.getInEdges().stream().mapToInt(Edge::getWeight).sum());
			Edge firstOut = v.getOutEdges().size() != 0 ? v.getOutEdges().get(0) : null;
			if (firstOut != null) {

				if (v.getIn() > v.getOut()) {
					firstOut.setWeight(v.getIn() - v.getOut() + firstOut.getWeight());
				}
			}
		}

		for (int i = vertices.size() - 1; i >= 0; i--) {

			Node v = vertices.get(i);
			v.setOut(v.getOutEdges().stream().mapToInt(Edge::getWeight).sum());
			Edge firstIn = v.getInEdges().size() != 0 ? v.getInEdges().get(0) : null;

			if (firstIn != null) {

				if (v.getOut() > v.getIn()) {
					firstIn.setWeight(v.getOut() - v.getIn() + firstIn.getWeight());
				}
			}
		}
	}

	private void findSeparators() {

		colorEdgesUsing(Colors.BLACK);

		for (int i = 0; i < vertices.get(0).getOutEdges().size(); i++) {
			Edge edge = vertices.get(0).getOutEdges().get(i);
			while (edge.getWeight() != 0) {
				List<Edge> separator = goToTheEnd(edge.getStart());
				separator.forEach(e -> {
					Line line = e.getLine();
					line.setColor(Colors.AQUA.getColor());
					lines.add(line);

				});
				snapshot();

				colorEdgesUsing(Colors.BLACK);

				separators.add(separator);
			}

		}
	}

	private List<Edge> goToTheEnd(int nodeId) {

		List<Edge> result = new ArrayList<>();
		Node node = getNodeById(nodeId);

		List<Edge> outEdges = node.getOutEdges();

		if (outEdges.size() != 0) {

			Edge firstOutEdge = getFirstAvailableEdge(outEdges);

			if (firstOutEdge == null) {
				return result;
			}

			if (firstOutEdge.getWeight() > 0) {
				firstOutEdge.setWeight(firstOutEdge.getWeight() - 1);
				result.add(firstOutEdge);
				result.addAll(goToTheEnd(firstOutEdge.getEnd()));
			}
		}

		return result;
	}

	private Edge getFirstAvailableEdge(List<Edge> edges) {

		for (Edge edge : edges) {
			if (edge.getWeight() != 0) {
				return edge;
			}
		}
		return null;
	}


	private void distinctSeparatorEdges(int start, int end) {

		if (start >= end) {
			return;
		}

		int center = (start + end) / 2;

		List<Edge> rootEdges = separators.get(center);

		for (int i = start; i < end; i++) {

			if (i != center) {
				separators.get(i).removeAll(rootEdges);
			}
		}

		distinctSeparatorEdges(start, center);
		distinctSeparatorEdges(center + 1, end);
	}

	private void colorEdgesUsingSeparatorColor() {
		lines.clear();
		Stream.iterate(0, n -> n + 1).limit(separators.size())
			.forEach(number -> {
				separators.get(number).forEach(e -> {
					e.setSeparatorId(number);
					Line line = e.getLine();
					line.setColor(Colors.getColorByIndex(number % Colors.values().length));
					lines.add(line);
				});
			});
		snapshot();
	}


	private static double det(Point x, Point y, Point z) {
		return (x.x - z.x) * (y.y - z.y) - (x.y - z.y) * (y.x - z.x);
	}

	public List<Edge> findPointLocation(Point point) {
		colorEdgesUsing(Colors.BLACK);
		points.add(point);

		Edge left = edges.get(0);
		Edge right = edges.get(0);

		int i = 0;
		int j = separators.size();

		boolean lastDecisionLeft = false;

		while (i < j) {
			int s = (i + j) / 2;

			List<Edge> edges = separators.get(s);

			colorEdgesUsing(edges, Colors.FUSCHSIA);


			Edge foundEdge = findProperEdge(edges, point);

			if (foundEdge == null) {
				if (lastDecisionLeft) {
					i = s + 1;
				} else {
					j = s;
				}
				colorEdgesUsing(Colors.BLACK);
				continue;
			}


			if (det(points.get(foundEdge.getStart()), points.get(foundEdge.getEnd()), point) < 0) {

				i = s + 1;
				left.getLine().setColor(Colors.BLACK.getColor());
				left = foundEdge;
				lastDecisionLeft = true;
			} else {

				j = s;
				right.getLine().setColor(Colors.BLACK.getColor());
				right = foundEdge;
				lastDecisionLeft = false;
			}

			foundEdge.getLine().setColor(Colors.LIME.getColor());
			snapshot();
			colorEdgesUsing(Colors.BLACK);
		}

		Line leftEdge = left.getLine();
		leftEdge.activate();

		Line rightEdge = right.getLine();
		rightEdge.activate();

		snapshot();

		return Arrays.asList(left, right);
	}

	private void colorEdgesUsing(Colors color) {
		edges.forEach(edge -> {
			Line line = edge.getLine();
			line.setColor(color.getColor());
		});
		snapshot();
	}

	private void colorEdgesUsing(List<Edge> edges, Colors color) {
		edges.forEach(edge -> {
			Line line = edge.getLine();
			line.setColor(color.getColor());
		});
		snapshot();
	}

	private Edge findProperEdge(List<Edge> edges, Point point) {
		int left = 0;
		int right = edges.size();

		while (left < right) {
			int s = (left + right) / 2;

			Edge edge = edges.get(s);
			edge.getLine().setColor(Colors.BLUE.getColor());
			snapshot();
			Line edgeLine = edge.getLine();
			if (edgeLine.getPoint1().y <= point.y && edgeLine.getPoint2().y >= point.y) {
				return edge;
			}

			if (!edge.isAbove(point)) {
				left = s + 1;
			} else {
				right = s;
			}

			edge.getLine().setColor(Colors.FUSCHSIA.getColor());
			snapshot();
		}

		return null;
	}

}
