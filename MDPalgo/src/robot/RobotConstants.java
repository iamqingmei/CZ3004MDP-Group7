package robot;

import java.awt.Color;

public final class RobotConstants {
	public static final double MOVE_COST = 1;
	public static final double TURN_COST = 20;
	public static final int GOAL_ROW = 13;
	public static final int GOAL_COL = 18;
	public static final DIRECTION STARTING_DIR = DIRECTION.NORTH;
	public static final int STARTING_ROW = 1;
	public static final int STARTING_COL = 1;
	
	public static final double INFINITE_COST = 9999; 
	
	public static enum DIRECTION {
		NORTH, EAST, SOUTH, WEST;

		public static DIRECTION getNext(DIRECTION currDirection) {
			return values()[(currDirection.ordinal() + 1) % values().length];
		}
		
		public static DIRECTION getPrevious(DIRECTION currDirection) {
			return values()[(currDirection.ordinal() + values().length - 1)
					% values().length];
		}

		public static DIRECTION fromString(String direction) {
			return valueOf(direction.toUpperCase());
		}
	};
	
	// Colors for rendering the map
	public static final Color C_BORDER = Color.BLACK;
	
	public static final Color C_LINE = Color.ORANGE;
	public static final int LINE_WEIGHT = 2;
	
	public static final Color C_START = Color.BLUE;
	public static final Color C_GOAL = Color.GREEN;
	
	public static final Color C_UNEXPLORED = Color.LIGHT_GRAY;
	public static final Color C_FREE = Color.WHITE;
	public static final Color C_OBSTACLE = Color.DARK_GRAY;

	// For rendering the robot in the robot editor
	public static final Color C_ROBOT_OUTLINE_EDITOR = new Color(0, 0, 0, 160);
	public static final Color C_ROBOT_EDITOR = new Color(0, 205, 255, 100);
	public static final Color C_ROBOT_FRONT_EDITOR = new Color(0, 46, 155, 220);
	
	// For rendering the robot in the robot map
	public static final Color C_ROBOT_OUTLINE = new Color(0, 0, 0, 220);
	public static final Color C_ROBOT = new Color(0, 205, 255, 160);
	public static final Color C_ROBOT_FRONT = new Color(0, 46, 155, 220);
	
	// For rendering the robot path in the robot map
	public static final Color C_EXPLORE_PATH = Color.RED;
	public static final Color C_SHORTEST_PATH = new Color(153, 0, 153, 255);
	public static final int PATH_THICKNESS = 4;
	
	public static final Color C_SENSOR = Color.DARK_GRAY;
	public static final Color C_SENSOR_BEAM_OUTER = new Color(220, 0, 0, 160);
	public static final Color C_SENSOR_BEAM_INNER = new Color(255, 0, 0, 190);
	
	// Robot Default Configuration
	public static final int DEFAULT_START_ROW = 1; // Changed from ROBOT_SIZE to 1
	public static final int DEFAULT_START_COL = 1;
	public static final DIRECTION DEFAULT_START_DIR = DIRECTION.NORTH;
	
	// Robot Exploration Configuration
	public static final int DEFAULT_STEPS_PER_SECOND = 10;
	public static final int DEFAULT_COVERAGE_LIMIT = 100;
	public static final int DEFAULT_TIME_LIMIT = 360;
	
	
	// Prevent instantiation
	private RobotConstants() {}	
}
