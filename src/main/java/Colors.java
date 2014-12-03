package main.java;

import java.util.HashMap;
import java.util.Map;


public enum Colors {
	RED("red"),
	OLIVE("olive"),
	BLACK("black"),
	LIME("lime"),
	AQUA("aqua"),
	BLUE("blue"),
	MAROON("maroon"),
	GREEN("green"),
	FUSCHSIA("fuchsia"),
	PURPLE("purple");

	private static Map<Integer, Colors> indexToColorMap;

	static {
		indexToColorMap = new HashMap<>();
		indexToColorMap.put(0, RED);
		indexToColorMap.put(1, OLIVE);
		indexToColorMap.put(2, BLACK);
		indexToColorMap.put(3, LIME);
		indexToColorMap.put(4, AQUA);
		indexToColorMap.put(5, BLUE);
		indexToColorMap.put(6, MAROON);
		indexToColorMap.put(7, GREEN);
		indexToColorMap.put(8, FUSCHSIA);
		indexToColorMap.put(9, PURPLE);
	}


	private String getColor;


	private Colors(String getColor) {
		this.getColor = getColor;
	}

	public String getColor() {
		return getColor;
	}

	public static String getColorByIndex(int index) {
		return indexToColorMap.get(index).getColor;
	}


}