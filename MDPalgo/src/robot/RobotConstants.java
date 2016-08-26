package robot;

public final class RobotConstants {
	public static final double MOVE_COST = 1;
	public static final double TURN_COST = 20;
	public static final int GOAL_ROW = 18;
	public static final int GOAL_COL = 13;
	public static final DIRECTION STARTING_DIR = DIRECTION.NORTH;
	public static final int STARTING_ROW = 1;
	public static final int STARTING_COL = 1;
	
	public static final double INFINITE_COST = 999999; 
	
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
	
	// Prevent instantiation
	private RobotConstants() {}	
}
