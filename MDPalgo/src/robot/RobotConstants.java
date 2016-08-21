package robot;

public final class RobotConstants {
	public static final int MOVE_COST = 1;
	public static final int TURN_COST = 0;
	
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
