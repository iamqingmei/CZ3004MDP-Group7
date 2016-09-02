package map;

import map.MapConstants;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.JPanel;


@SuppressWarnings("serial")

public class Map extends JPanel {
	private Block[][] blocks;

	// For measuring size of the canvas
	// private boolean _bMeasured = false;

	public Map(){
		blocks = new Block [MapConstants.MAP_ROW][MapConstants.MAP_COL];  
		for (int r=0; r<MapConstants.MAP_ROW; r++){
			for (int c=0; c<MapConstants.MAP_COL; c++){
				blocks[r][c] = new Block(r,c);
				if (r==0 || c ==0 || r == MapConstants.MAP_ROW-1 || c == MapConstants.MAP_COL-1){
					blocks[r][c].setVirtualWall(true);
				}
			}
		}
		//goal point is not virtual wall!
		blocks[MapConstants.GOAL_ROW][MapConstants.GOAL_COL].setVirtualWall(false);
	}

	public boolean isObstacle(int r, int c){
		return this.blocks[r][c].getIsObstacle();
	}

	public boolean isStartZone(int r, int c){
		if (r==0 && c==0){
			return true;
		}
		else{
			return false;
		}
	}

	public boolean isGoalZone(int r, int c){
		if (r==MapConstants.GOAL_ROW && c == MapConstants.GOAL_COL){
			return true;
		}
		else{
			return false;
		}
	}
	
	public Block getGoalZone(){
		return blocks[MapConstants.GOAL_ROW][MapConstants.GOAL_COL];
	}

//	public Block[][] getMap(){
//		return this.blocks;
//	}
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
		

		// if (!_bMeasured) {

		// 	_mapWidth = MapConstants.MAP_COL;
		// 	_mapHeight = MapConstants.MAP_ROW;

		// 	System.out.println("RealMap Graphics g; Map width: " + _mapWidth
		// 			+ ", Map height: " + _mapHeight);

		// 	// Calculate the map grids for rendering
		// 	_mapGrids = new MapGrid[MapConstants.MAP_ROWS][MapConstants.MAP_COLS];
		// 	for (int mapRow = 0; mapRow < MapConstants.MAP_ROWS; mapRow++) {
		// 		for (int mapCol = 0; mapCol < MapConstants.MAP_COLS; mapCol++) {
		// 			_mapGrids[mapRow][mapCol] = new MapGrid(mapCol
		// 					* MapConstants.GRID_SIZE, mapRow
		// 					* MapConstants.GRID_SIZE, MapConstants.GRID_SIZE);
		// 		}
		// 	}

		// 	_bMeasured = true;
		// }
		
		// Clear the map
		g.setColor(Color.BLACK);
        g.fillRect(0, 0, _mapWidth, _mapHeight);
        
        Border border = BorderFactory.createLineBorder(
				MapConstants.C_GRID_LINE, MapConstants.GRID_LINE_WEIGHT);
        this.setBorder(border);
        
        // Paint the grids
        for (int mapRow = 0; mapRow < MapConstants.MAP_ROWS; mapRow++)
		{
			for (int mapCol = 0; mapCol < MapConstants.MAP_COLS; mapCol++)
			{
				g.setColor(MapConstants.C_GRID_LINE);
				g.fillRect(_mapGrids[mapRow][mapCol].borderX,
						_mapGrids[mapRow][mapCol].borderY,
						_mapGrids[mapRow][mapCol].borderSize,
						_mapGrids[mapRow][mapCol].borderSize);
				
				Color gridColor = null;
				
				// Determine what color to fill grid
				if(isBorderWalls(mapRow, mapCol))
					gridColor = MapConstants.C_BORDER;
				else if(isStartZone(mapRow, mapCol))
					gridColor = MapConstants.C_START;
				else if(isGoalZone(mapRow, mapCol))
					gridColor = MapConstants.C_GOAL;
				else
				{
					if(_grids[mapRow][mapCol].isObstacle())
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
	} // End paintComponent
}