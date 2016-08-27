import robot.Robot;
//import robot.RobotConstants.DIRECTION;

import map.Map; 
import robot.ShortestPathAlgo;
public class Simulator {

	public static void main(String[] args) {
		Robot bot = new Robot(1,1);
		Map theMap = new Map();
		ShortestPathAlgo shortestPath = new ShortestPathAlgo(theMap, bot);
		theMap.setObstacle(8, 14);
		theMap.setObstacle(9, 14);
		theMap.setObstacle(10, 14);
		theMap.setObstacle(6,0);
		theMap.setObstacle(7,0);
		theMap.setObstacle(9,9);
		shortestPath.runShortestPath(theMap);
		// shortestPath.printGscores();
	}

}
