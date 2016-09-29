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
	public void sense(Map exMap, int distance){
		switch(sensorDirection){
			case NORTH:
				if (distance>this.range){ // no obstacle
					for (int i=1; i<=this.range+1; i++){
						if (this.sensorPosRow + i == 20){ //touching north wall
							return;
						}
						exMap.getBlock(this.sensorPosRow + i, this.sensorPosCol).setIsExplored(true);
					}
				}
				if (distance<=this.range){ //obstacle detected
					for (int i=1; i<=distance+1; i++){
						if (this.sensorPosRow + i == 20){ //touching north wall
							return;
						}
						exMap.getBlock(this.sensorPosRow + i, this.sensorPosCol).setIsExplored(true);
					}
					exMap.setObstacle(this.sensorPosRow + distance+1, this.sensorPosCol);
				}
				return;
			case SOUTH:
				if (distance>this.range){ // no obstacle
					for (int i=1; i<=this.range+1; i++){
						if (this.sensorPosRow - i == -1){ //touching south wall
							return;
						}
						exMap.getBlock(this.sensorPosRow - i, this.sensorPosCol).setIsExplored(true);
					}
				}
				if (distance<=this.range){ //obstacle detected
					for (int i=1; i<=distance+1; i++){
						if (this.sensorPosRow - i == -1){ //touching south wall
							return;
						}
						exMap.getBlock(this.sensorPosRow - i, this.sensorPosCol).setIsExplored(true);
					}
					exMap.setObstacle(this.sensorPosRow - distance-1, this.sensorPosCol);
				}
				return;
			case EAST:
				if (distance>this.range){ // no obstacle
					for (int i=1; i<=this.range+1; i++){
						if (this.sensorPosCol + i == 15){ //touching east wall
							return;
						}
						exMap.getBlock(this.sensorPosRow, this.sensorPosCol+i).setIsExplored(true);
					}
				}
				if (distance<=this.range){ //obstacle detected
					for (int i=1; i<=distance+1; i++){
						if (this.sensorPosCol + i == 15){ //touching east wall
							return;
						}
						exMap.getBlock(this.sensorPosRow, this.sensorPosCol + i).setIsExplored(true);
					}
					exMap.setObstacle(this.sensorPosRow, this.sensorPosCol +distance+1);
				}
				return;
			case WEST:
				if (distance>this.range){ // no obstacle
					for (int i=1; i<=this.range+1; i++){
						if (this.sensorPosCol - i == -1){ // touching west wall
							return;
						}
						exMap.getBlock(this.sensorPosRow, this.sensorPosCol-i).setIsExplored(true);
					}
				}
				if (distance<=this.range){ //obstacle detected
					for (int i=1; i<=distance+1; i++){
						if (this.sensorPosCol - i == -1){ // touching west wall
							return;
						}
						exMap.getBlock(this.sensorPosRow, this.sensorPosCol - i).setIsExplored(true);
					}
					exMap.setObstacle(this.sensorPosRow, this.sensorPosCol -distance-1);
				}
				return;
		}
		return;
	}
}
