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

	// For rendering the map efficiently
	private MapGrid[][] _mapGrids = null;

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


		// int _mapWidth = 800;
		// int _mapHeight = 600;

		// System.out.println("RealMap Graphics g; Map width: " + 800 + ", Map height: " + 600);

		// Calculate the map grids for rendering
		_mapGrids = new MapGrid[MapConstants.MAP_ROW][MapConstants.MAP_COL];
		for (int mapRow = 0; mapRow < MapConstants.MAP_ROW; mapRow++) {
			for (int mapCol = 0; mapCol < MapConstants.MAP_COL; mapCol++) {
				_mapGrids[mapRow][mapCol] = new MapGrid(mapCol
						* MapConstants.GRID_SIZE, mapRow
						* MapConstants.GRID_SIZE, MapConstants.GRID_SIZE);
			}
		}

		// // Clear the Graphics
		// g.setColor(Color.DARK_GRAY);
		// g.fillRect(0, 0, 800, 600);

        // Paint the grids
        for (int mapRow = 0; mapRow < MapConstants.MAP_ROW; mapRow++)
		{
			for (int mapCol = 0; mapCol < MapConstants.MAP_COL; mapCol++)
			{
				g.setColor(MapConstants.C_GRID_LINE);
				g.fillRect(_mapGrids[mapRow][mapCol].borderX,
						_mapGrids[mapRow][mapCol].borderY,
						_mapGrids[mapRow][mapCol].borderSize,
						_mapGrids[mapRow][mapCol].borderSize);
				
				Color gridColor = null;
				
				// Determine what color to fill grid
				if(isStartZone(mapRow, mapCol))
					gridColor = MapConstants.C_START;
				else if(isGoalZone(mapRow, mapCol))
					gridColor = MapConstants.C_GOAL;
				else
				{
					if(blocks[mapRow][mapCol].getIsObstacle())
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
			this.gridY = borderY + MapConstants.GRID_LINE_WEIGHT;
			this.gridSize = borderSize - (MapConstants.GRID_LINE_WEIGHT * 2);
		}
	}
}