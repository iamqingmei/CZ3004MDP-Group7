package map;

import map.MapConstants;
import robot.Robot;
import robot.RobotConstants.DIRECTION;
import robot.RobotConstants.MOVE;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.JPanel;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import communication.CommMgr;


public class Map extends JPanel {
	private Block[][] blocks;
	private Robot theRobot;
	// public int delay = 50;

	// For rendering the map efficiently
	private MapGrid[][] _mapGrids = null;

	public Map(Robot bot){
		theRobot = bot;
		blocks = new Block [MapConstants.MAP_ROW][MapConstants.MAP_COL];  
		for (int r=0; r<MapConstants.MAP_ROW; r++){
			for (int c=0; c<MapConstants.MAP_COL; c++){
				blocks[r][c] = new Block(r,c); // for side walls, set virtual walls
				if (r==0 || c ==0 || r == MapConstants.MAP_ROW-1 || c == MapConstants.MAP_COL-1){
					blocks[r][c].setVirtualWall(true);
				}
			}
		}
	}

	public void setAllUnexplored(){
		// except for goalZone and StartZone, set as explored
		for (int r=0; r<MapConstants.MAP_ROW; r++){
			for (int c=0; c<MapConstants.MAP_COL; c++){
				if ((r<3 && c<3) || (r>16 && c>11)){ //startZone and goalZone
					blocks[r][c].setIsExplored(true);
				}
				else{
					blocks[r][c].setIsExplored(false);
				}
			}
		}
	}

	public boolean isObstacle(int r, int c){
		return this.blocks[r][c].getIsObstacle();
	}

	public boolean isStartZone(int r, int c){
		if (r <= 2 && r >= 0){
			if (c <= 2 && c >= 0){
				return true;
			}
		}
		return false;
	}

	public boolean isGoalZone(int r, int c){
		if (r <= MapConstants.GOAL_ROW+1 && r >= MapConstants.GOAL_ROW-1){
			if (c <= MapConstants.GOAL_COL + 1 && c >= MapConstants.GOAL_COL - 1){
				return true;
			}
		}
		return false;
	}
	
	public Block getGoalZone(){
		return blocks[MapConstants.GOAL_ROW][MapConstants.GOAL_COL];
	}

	public boolean blockInRange(int r, int c){
		if (r<0 || c<0){
			return false;
		}
		if (r>=MapConstants.MAP_ROW || c>=MapConstants.MAP_COL){
			return false;
		}
		return true;
	}

	public Block getBlock(int r, int c){
		return this.blocks[r][c];
	}

	public void setObstacle(int r, int c){
		this.blocks[r][c].setObstacle();
		if (r >= 1){
			this.blocks[r-1][c].setVirtualWall(true);
			if (c < MapConstants.MAP_COL - 1){
				this.blocks[r-1][c+1].setVirtualWall(true);
			}
			if (c >= 1){
				this.blocks[r-1][c-1].setVirtualWall(true);
			}
		}
		if (c >= 1){
			this.blocks[r][c-1].setVirtualWall(true);
			if (r < MapConstants.MAP_ROW - 1){
				this.blocks[r+1][c-1].setVirtualWall(true);
			}	
		}
		if (r < MapConstants.MAP_ROW - 1){
			this.blocks[r+1][c].setVirtualWall(true);
			if (c < MapConstants.MAP_COL - 1){
				this.blocks[r+1][c+1].setVirtualWall(true);
			}
		}
		if (c < MapConstants.MAP_COL - 1){
			this.blocks[r][c+1].setVirtualWall(true);
		}	
	}

	public void paintComponent(Graphics g) {

		// Calculate the map grids for rendering
		_mapGrids = new MapGrid[MapConstants.MAP_ROW][MapConstants.MAP_COL];
		for (int mapRow = 0; mapRow < MapConstants.MAP_ROW; mapRow++) {
			for (int mapCol = 0; mapCol < MapConstants.MAP_COL; mapCol++) {
				_mapGrids[mapRow][mapCol] = new MapGrid(mapCol
						* MapConstants.GRID_SIZE, mapRow
						* MapConstants.GRID_SIZE, MapConstants.GRID_SIZE);
			}
		}

        // Paint the grids
        for (int mapRow = 0; mapRow < MapConstants.MAP_ROW; mapRow++)
		{
			for (int mapCol = 0; mapCol < MapConstants.MAP_COL; mapCol++)
			{
				
				Color gridColor = null;
				
				// Determine what color to fill grid
				
				if(isStartZone(mapRow, mapCol))
					gridColor = MapConstants.C_START;
				else if(isGoalZone(mapRow, mapCol))
					gridColor = MapConstants.C_GOAL;
				else
				{	
					//if unexplored
					if (!blocks[mapRow][mapCol].getIsExplored()){
						gridColor = MapConstants.C_UNEXPLORED;
					}
					else if(blocks[mapRow][mapCol].getIsObstacle())
						gridColor = MapConstants.C_OBSTACLE;
					else
						gridColor = MapConstants.C_FREE;
				}
				
				g.setColor(gridColor);
				g.fillRect(_mapGrids[mapRow][mapCol].gridX,
						_mapGrids[mapRow][mapCol].gridY,
						_mapGrids[mapRow][mapCol].gridSize,
						_mapGrids[mapRow][mapCol].gridSize);
				
			}
		} // End outer for loop	

		//paint robot
		g.setColor(MapConstants.C_ROBOT);
		int r = theRobot.getRobotPosRow();
		int c = theRobot.getRobotPosCol();
		g.fillOval((c-1) * 40 + 22,758 - (r * 40 + 18),76,76);


		//paint direction
		g.setColor(MapConstants.C_ROBOT_DIR);
		DIRECTION d = theRobot.getRobotCurDir();
		switch (d) {
			case NORTH : 
				g.fillOval(c * 40 + 12,758 -r * 40 - 22,18,18);
				break;
			case WEST :
				g.fillOval(c * 40 - 22,758 - r * 40 + 8 ,18,18);
				break;
			case SOUTH :
				g.fillOval(c * 40 + 12,758 - r * 40 + 42,18,18);
				break;
			case EAST :
				g.fillOval(c * 40 + 42,758 - r * 40 + 8,18,18);
				break;
		}



	} // End paintComponent

	private class MapGrid {
		public int borderX;
		public int borderY;
		public int borderSize;
		
		public int gridX;
		public int gridY;
		public int gridSize;
		
		public MapGrid(int borderX, int borderY, int borderSize) {
			this.borderX = borderX;
			this.borderY = borderY;
			this.borderSize = borderSize;
			
			this.gridX = borderX + MapConstants.GRID_LINE_WEIGHT;
			this.gridY = 758 - (borderY - MapConstants.GRID_LINE_WEIGHT);
			this.gridSize = borderSize - (MapConstants.GRID_LINE_WEIGHT * 2);
		}
	}

	public void mapDescriptor(){
		// String DescriptorFormat = "11"; //pad the first 2 bits
		// String DescriptorFinal = "";
		// for(int r=0;r<MapConstants.MAP_ROW;r++){//output map descriptor for explored area
		// 	for(int c=0;c<MapConstants.MAP_COL;c++){
		// 		if(getBlock(r,c).getIsExplored() == true){					
		// 			//System.out.println(i++);
		// 			DescriptorFormat = DescriptorFormat + "1"; //grid explored
		// 		}
		// 		else
		// 			DescriptorFormat = DescriptorFormat + "0"; //grid not explored
		// 		if(DescriptorFormat.length()==16){
		// 			int DescriptorFormatBin = Integer.parseInt(DescriptorFormat,2);
		// 			String DescriptorFormatHex = Integer.toString(DescriptorFormatBin,16);
		// 			while(DescriptorFormatHex.length()!= DescriptorFormat.length()/4) // pad 0s in front according to string length
		// 				DescriptorFormatHex = "0"+ DescriptorFormatHex;
		// 			// System.out.println(DescriptorFormatHex);
		// 			DescriptorFinal += DescriptorFormatHex;
		// 			DescriptorFormat = "";//resets string after each column
		// 		}
		// 	}
		// }
		// DescriptorFormat = DescriptorFormat + "11";//pad the last 2 bits
		// int DescriptorFormatBin = Integer.parseInt(DescriptorFormat,2);
		// String DescriptorFormatHex = Integer.toString(DescriptorFormatBin,16);
		// // System.out.println(DescriptorFormatHex);
		// DescriptorFinal += DescriptorFormatHex; //last 4 hexa digits
		// // System.out.println(DescriptorFinal);
		int DescriptorFormatBin;
		String DescriptorFormatHex;
		String DescriptorFormat = "";
		String DescriptorFinal2 ="Grid: ";
		// System.out.println("printing obstacles");
		for(int r=0;r<MapConstants.MAP_ROW;r++){//output map descriptor for obstacles in map
			for(int c=0;c<MapConstants.MAP_COL;c++){

				if(getBlock(r,c).getIsObstacle() == true)
					DescriptorFormat += "1";
				else
					DescriptorFormat += "0";
				if(DescriptorFormat.length()==16){
					// System.out.println(DescriptorFormat);
					DescriptorFormatBin = Integer.parseInt(DescriptorFormat,2);
					DescriptorFormatHex = Integer.toString(DescriptorFormatBin,16);
					while(DescriptorFormatHex.length()!= 4)
						DescriptorFormatHex = "0" + DescriptorFormatHex;//add back the zeros automatically removed by the computer
					// System.out.println(DescriptorFormatHex);
					DescriptorFinal2 += DescriptorFormatHex;
					DescriptorFormat = "";//resets string after each column
				}

			}	
		}
		// System.out.println(DescriptorFormat);
		while(DescriptorFormat.length()%4!=0) // pad it to make the total digits divisible by 4
			DescriptorFormat = "0" + DescriptorFormat;
		DescriptorFormatBin = Integer.parseInt(DescriptorFormat,2);
		DescriptorFormatHex = Integer.toString(DescriptorFormatBin,16);
		while(DescriptorFormatHex.length()!= DescriptorFormat.length()/4) // pad 0s in front according to string length
			DescriptorFormatHex = "0"+ DescriptorFormatHex;
		// System.out.println("last df:" + DescriptorFormatHex);
		DescriptorFinal2 += DescriptorFormatHex; //last 4 hexa digits
		// System.out.println(DescriptorFinal2);
		// CommMgr.getCommMgr().sendMsg(DescriptorFinal2, "PC2AN");

		//pass the robot position to android
		String robotString = " [";
		robotString += theRobot.getRobotPosCol();
		robotString += ", ";
		robotString += theRobot.getRobotPosRow();
		robotString += ", ";
		robotString += DIRECTION.printDir(theRobot.getRobotCurDir());
		robotString += "]";
		CommMgr.getCommMgr().sendMsg(DescriptorFinal2+robotString, "PC2AN");
	}

	public void finalMap(){
		String DescriptorFormat = "11"; //pad the first 2 bits
		String DescriptorFinal = "";
		for(int r=0;r<MapConstants.MAP_ROW;r++){//output map descriptor for explored area
			for(int c=0;c<MapConstants.MAP_COL;c++){
				if(getBlock(r,c).getIsExplored() == true){					
					//System.out.println(i++);
					DescriptorFormat = DescriptorFormat + "1"; //grid explored
				}
				else{
					DescriptorFormat = DescriptorFormat + "0"; //grid not explored
				}
				if(DescriptorFormat.length()==16){
					int DescriptorFormatBin = Integer.parseInt(DescriptorFormat,2);
					String DescriptorFormatHex = Integer.toString(DescriptorFormatBin,16);
					while(DescriptorFormatHex.length()!= DescriptorFormat.length()/4) // pad 0s in front according to string length
						DescriptorFormatHex = "0"+ DescriptorFormatHex;
					// System.out.println(DescriptorFormatHex);
					DescriptorFinal += DescriptorFormatHex;
					DescriptorFormat = "";//resets string after each column
				}
			}
		}
		DescriptorFormat = DescriptorFormat + "11";//pad the last 2 bits
		int DescriptorFormatBin = Integer.parseInt(DescriptorFormat,2);
		String DescriptorFormatHex = Integer.toString(DescriptorFormatBin,16);
		// System.out.println(DescriptorFormatHex);
		while(DescriptorFormatHex.length()!= DescriptorFormat.length()/4) // pad 0s in front according to string length
			DescriptorFormatHex = "0"+ DescriptorFormatHex;
		DescriptorFinal += DescriptorFormatHex; //last 4 hexa digits

		// System.out.println(DescriptorFinal);
		
		DescriptorFormat = "";
		String DescriptorFinal2 ="";
		// System.out.println("printing obstacles");
		for(int r=0;r<MapConstants.MAP_ROW;r++){//output map descriptor for obstacles in map
			for(int c=0;c<MapConstants.MAP_COL;c++){
				if(getBlock(r,c).getIsExplored() == true){
					if(getBlock(r,c).getIsObstacle() == true)
						DescriptorFormat += "1";
					else{
						DescriptorFormat += "0";
					}
					if(DescriptorFormat.length()==16){
						// System.out.println(DescriptorFormat);
						DescriptorFormatBin = Integer.parseInt(DescriptorFormat,2);
						DescriptorFormatHex = Integer.toString(DescriptorFormatBin,16);
						while(DescriptorFormatHex.length()!= 4)
							DescriptorFormatHex = "0" + DescriptorFormatHex;//add back the zeros automatically removed by the computer
						// System.out.println(DescriptorFormatHex);
						DescriptorFinal2 += DescriptorFormatHex;
						DescriptorFormat = "";//resets string after each column
					}
				}
			}	
		}
		// System.out.println(DescriptorFormat);
		while(DescriptorFormat.length()%4!=0) // pad it to make the total digits divisible by 4
			DescriptorFormat = "0" + DescriptorFormat;
		DescriptorFormatBin = Integer.parseInt(DescriptorFormat,2);
		DescriptorFormatHex = Integer.toString(DescriptorFormatBin,16);
		while(DescriptorFormatHex.length()!= DescriptorFormat.length()/4) // pad 0s in front according to string length
			DescriptorFormatHex = "0"+ DescriptorFormatHex;
		// System.out.println("last df:" + DescriptorFormatHex);
		DescriptorFinal2 += DescriptorFormatHex; //last 4 hexa digits
		// System.out.println(DescriptorFinal2);

		CommMgr.getCommMgr().sendMsg("String1:"+DescriptorFinal +" String2:" + DescriptorFinal2, "PC2AN");
	}				
		
}