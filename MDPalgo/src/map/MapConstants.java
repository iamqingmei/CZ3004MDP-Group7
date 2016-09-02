package map;

import java.awt.Color;


public class MapConstants {
	public static final int GOAL_ROW = 13;
	public static final int GOAL_COL = 18;
	public static final int MAP_ROW = 15;
	public static final int MAP_COL = 20;

		// Colors for rendering the map
	public static final Color C_BORDER = Color.BLACK;
	public static final Color C_BORDER_WARNING = new Color(255, 102, 153, 200);
	
	public static final Color C_GRID_LINE = Color.ORANGE;
	public static final int GRID_LINE_WEIGHT = 2;
	
	public static final Color C_START = Color.BLUE;
	public static final Color C_GOAL = Color.GREEN;
	
	public static final Color C_UNEXPLORED = Color.LIGHT_GRAY;
	public static final Color C_FREE = Color.WHITE;
	public static final Color C_OBSTACLE = Color.DARK_GRAY;

	// Grid size - for rendering only
	public static final int GRID_SIZE = 40;
}
