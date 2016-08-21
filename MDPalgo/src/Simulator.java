import robot.Robot;

import map.Map; 
public class Simulator {

	public static void main(String[] args) {
		Robot bot = new Robot(0,0);
		Map theMap = new Map();
		bot.shortestPathAlgo(theMap);
	}

}
