import robot.Robot;
//import robot.RobotConstants.DIRECTION;

import map.Map; 
public class Simulator {

	public static void main(String[] args) {
		Robot bot = new Robot(0,0);
		Map theMap = new Map();
		theMap.getBlock(8, 14).setObstacle();
		theMap.getBlock(9, 14).setObstacle();
		theMap.getBlock(10, 14).setObstacle();
		theMap.getBlock(6, 0).setObstacle();
		theMap.getBlock(7, 0).setObstacle();
		bot.shortestPathAlgo(theMap);
	}

}
