package robot;

import robot.RobotConstants.DIRECTION;
import map.Map;

public class Sensor {
	private int range;
	private int sensorPosRow;
	private int sensorPosCol;
	private DIRECTION sensorDirection;

	public Sensor(int r, int row, int col, DIRECTION dir){
		this.range = r;
		this.sensorPosCol = col;
		this.sensorPosRow = row;
		this.sensorDirection = dir;
	}

	public void setSensor(int row, int col, DIRECTION dir){
		this.sensorPosCol = col;
		this.sensorPosRow = row;
		this.sensorDirection = dir;
	}
	
	//return the distance of the nearest obstacle
	//set the blocks in range as isExplored
	public int sense(Map exMap, Map realMap){
		switch(sensorDirection){
			case NORTH:
				for (int i=0; i<=this.range; i++){
					if (this.sensorPosRow + i == 20){ //touching north wall
						return 1;
					}
					exMap.getBlock(this.sensorPosRow + i, this.sensorPosCol).setIsExplored(true);
					if (realMap.getBlock(this.sensorPosRow + i, this.sensorPosCol).getIsObstacle()){
						exMap.setObstacle(this.sensorPosRow + i, this.sensorPosCol);
						return i;
					}
				}
				return 0;
			case SOUTH:
				for (int i=0; i<=this.range; i++){
					if (this.sensorPosRow == 0){ //touching south wall
						return 1;
					}
					exMap.getBlock(this.sensorPosRow - i, this.sensorPosCol).setIsExplored(true);
					if (realMap.getBlock(this.sensorPosRow - i, this.sensorPosCol).getIsObstacle()){
						exMap.setObstacle(this.sensorPosRow - i, this.sensorPosCol);
						return i;
					}
				}
				return 0;
			case EAST:
				for (int i=0; i<=this.range; i++){
					if (this.sensorPosCol == 15){ //touching east wall
						return 1;
					}
					exMap.getBlock(this.sensorPosRow, this.sensorPosCol + i).setIsExplored(true);
					if (realMap.getBlock(this.sensorPosRow, this.sensorPosCol + i).getIsObstacle()){
						exMap.setObstacle(this.sensorPosRow, this.sensorPosCol + i);
						return i;
					}
				}
				return 0;
			case WEST:
				for (int i=0; i<=this.range; i++){
					if (this.sensorPosCol == 0){ // touching west wall
						System.out.println("touching west wall");
						return 1;
					}
					exMap.getBlock(this.sensorPosRow, this.sensorPosCol - i).setIsExplored(true);
					if (realMap.getBlock(this.sensorPosRow, this.sensorPosCol - i).getIsObstacle()){
						exMap.setObstacle(this.sensorPosRow, this.sensorPosCol - i);
						return i;
					}
				}
				return 0;
		}
		return 0;
	}
}
