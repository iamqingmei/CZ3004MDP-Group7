package robot;

import robot.RobotConstants.DIRECTION;
import robot.SensorConstants;

public class Sensor {
	private int minRange;
	private int maxRange;

	private int sensorPosRow;
	private int sensorPosCol;
	private DIRECTION sensorDirection;

	public Sensor(){
		this.minRange = SensorConstants.SENSOR_MIN_RANGE;
		this.maxRange = SensorConstants.SENSOR_MAX_RANGE;
		this.sensorPosCol = SensorConstants.STARTING_COL;
		this.sensorPosRow = SensorConstants.STARTING_ROW;
		this.sensorDirection = SensorConstants.STARTING_DIR;
	}
}


// import java.io.Serializable;

// import map.Grid;
// import map.Map;
// import map.MapConstants;
// import robot.RobotConstants.DIRECTION;

// public class Sensor implements Serializable {
	
// 	/
// 	* Generated serialVersionUID
// 	 */
// 	private static final long serialVersionUID = 8072244809942485168L;
	
// 	// Range (In grids)
// 	private int _minRange;
// 	private int _maxRange;
	
// 	// Sensor's position on the map (In grids)
// 	private int _sensorPosRow;
// 	private int _sensorPosCol;
	
// 	// Sensor's current direction
// 	private DIRECTION _sensorDirection;
	
// 	public Sensor(int minRange, int maxRange, int sensorPosRow, int sensorPosCol, DIRECTION sensorDirection) {
// 		_minRange = minRange;
// 		_maxRange = maxRange;
		
// 		_sensorPosRow = sensorPosRow;
// 		_sensorPosCol = sensorPosCol;
		
// 		_sensorDirection = sensorDirection;
// 	}
	
// 	public int getMinRange() {
// 		return _minRange;
// 	}
	
// 	public int getMaxRange() {
// 		return _maxRange;
// 	}
	
// 	public void setMinRange(int newMinRange) {
// 		_minRange = newMinRange;
// 	}
	
// 	public void setMaxRange(int newMaxRange) {
// 		_maxRange = newMaxRange;
// 	}
	
// 	public int getSensorPosRow() {
// 		return _sensorPosRow;
// 	}

// 	public int getSensorPosCol() {
// 		return _sensorPosCol;
// 	}
	
// 	/**
// 	 * Update the sensor's position on the map (In grids)<p>
// 	 * 
// 	 * This function should be invoked whenever the robot moves, or
// 	 * when the robot turns.<br>Be fully aware of sensor's position relative to the
// 	 * robot's position at all times!
// 	 * 
// 	 * @param newSensorPosRow Sensor's new row on the map
// 	 * @param newSensorPosCol Sensor's new column on the map
// 	 */
// 	public void updateSensorPos(int newSensorPosRow, int newSensorPosCol) {
// 		_sensorPosRow = newSensorPosRow;
// 		_sensorPosCol = newSensorPosCol;
// 	}
	
// 	public DIRECTION getSensorDirection() {
// 		return _sensorDirection;
// 	}
	
// 	/**
// 	 * Update the sensor's direction relative to the robot<p>
// 	 * 
// 	 * This function should be invoked whenever the robot moves, or
// 	 * when the robot turns.<br>Be fully aware of sensor's direction relative to the
// 	 * robot's direction at all times!
// 	 * 
// 	 * @param newDirection Sensor's new direction
// 	 */
// 	public void updateSensorDirection(DIRECTION newDirection) {
// 		_sensorDirection = newDirection;
// 	}

// 	/**
// 	 * Most important function of the sensor class
// 	 * <p>
// 	 * Returns number of free grids in the sensor's current direction<br>
// 	 * 0: Obstacle right in front of sensor<br>
// 	 * 1: 1 free grid in the current direction, and so on, up to maximum range
// 	 * <p>
// 	 * NOTE: This is based on the sensor's minimum range & maximum range!<br>
// 	 * If there is an obstacle that is lesser than the minimum range,<br>
// 	 * it will NOT be detected.
// 	 * 
// 	 * @param realMap A read-only map passed in for sensing
// 	 * 
// 	 * @return Number of free grids in this direction
// 	 */
// 	public int sense(final Map map) {
		
// 		final Grid [][] mapGrids = map.getMapGrids();
		
// 		for (int currGrid = _minRange; currGrid <= _maxRange; currGrid++) {
// 			switch (_sensorDirection) {
			
// 			case NORTH:
// 				//System.out.println("Checking " + (_sensorPosRow - currGrid) + ", " + _sensorPosCol + ".. ");
// 				// Reached top limit of map without detecting any obstacle
// 				if((_sensorPosRow - currGrid) < 0)
// 					return currGrid;
// 				else if(mapGrids[_sensorPosRow - currGrid][_sensorPosCol].isObstacle())
// 					return currGrid - 1; // Return number of free grids for this direction
// 				break;

// 			case SOUTH:
// 				//System.out.println("Checking " + (_sensorPosRow + currGrid) + ", " + _sensorPosCol + ".. ");
// 				// Reached bottom limit of map without detecting any obstacle
// 				if((_sensorPosRow + currGrid) > (MapConstants.MAP_ROWS - 1))
// 					return currGrid;
// 				else if(mapGrids[_sensorPosRow + currGrid][_sensorPosCol].isObstacle())
// 					return currGrid - 1; // Return number of free grids for this direction
// 				break;
				
// 			case EAST:
// 				//System.out.println("Checking " + _sensorPosRow + ", " + (_sensorPosCol + currGrid) + ".. ");
// 				// Reached right limit of map without detecting any obstacle
// 				if((_sensorPosCol + currGrid) > (MapConstants.MAP_COLS - 1))
// 					return currGrid;
// 				else if(mapGrids[_sensorPosRow][_sensorPosCol + currGrid].isObstacle())
// 					return currGrid - 1; // Return number of free grids for this direction
// 				break;

// 			case WEST:
// 				//System.out.println("Checking " + _sensorPosRow + ", " + (_sensorPosCol - currGrid) + ".. ");
// 				// Reached left limit of map without detecting any obstacle
// 				if((_sensorPosCol - currGrid) < 0)
// 					return currGrid;
// 				else if(mapGrids[_sensorPosRow][_sensorPosCol - currGrid].isObstacle())
// 					return currGrid - 1; // Return number of free grids for this direction
// 				break;
				
// 			} // End switch
// 		} // End for loop
		
// 		// No obstacles detected within the sensor's maximum range
// 		// Allow the robot to mark those grids as free
// 		return _maxRange;
// 	}
	
// 	// Just for testing purposes
// 	public void printSensorInfo() {
// 		System.out.println("Sensor Position (row, col): " + _sensorPosRow
// 				+ ", " + _sensorPosCol);
// 		System.out.println("Sensor Range (min, max): " + _minRange + ", "
// 				+ _maxRange);
// 		System.out.println("Sensor Direction: " + _sensorDirection.toString());
// 	}
	
// 	@Override
// 	public String toString() {
// 		return String.format("Min: %2d, Max: %2d, Direction: %5s",
// 				_minRange, _maxRange, _sensorDirection.toString());
// 	}
	
// }
