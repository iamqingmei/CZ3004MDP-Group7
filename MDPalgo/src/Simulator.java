
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import map.MapConstants;

import javax.swing.JDialog;

import java.util.concurrent.TimeUnit;
import java.util.*;

import map.Block;
import robot.Robot;
import robot.RobotConstants.MOVE;
import robot.RobotConstants.DIRECTION;

import map.Map; 
import robot.ShortestPathAlgo;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Simulator {
	// JFrame for the application
	private static JFrame _appFrame = null;
	
	// JPanel for laying out different views
	private static JPanel _mainCards = null;
	private static JPanel _buttonsCards = null;

	// The frame used for main menu buttons
	private static JPanel _mainButtons = null;

	private static Map theMap = null;

	private static Robot bot;

	private static boolean runFastestPath = false;

	

	public static void main(String[] args){
		//ReadMap();
		
		bot = new Robot(1,1);
		theMap = new Map(bot);
		theMap = ReadMap();
/*		theMap.setObstacle(5, 0);
		theMap.setObstacle(5, 1);
		theMap.setObstacle(5, 2);
		theMap.setObstacle(5,3);
		theMap.setObstacle(0,13);
		theMap.setObstacle(1,13);
		theMap.setObstacle(2,13);
		theMap.setObstacle(3,13);
		theMap.setObstacle(7,13);
		theMap.setObstacle(8,13);
		theMap.setObstacle(9,13);
		theMap.setObstacle(9,9);*/
		// theMap.setObstacle(10,16);
		// theMap.setObstacle(11,16);
		// theMap.setObstacle(12,16);
		// theMap.setObstacle(13,16);
		// theMap.setObstacle(14,16);


		displayEverythings();

		while(true){
			System.out.print("");
			if (runFastestPath){
				System.out.println("entered!");
				FastestPath();
			}
			continue;
		}
	}

	private static void displayEverythings(){
		// Main frame for displaying everything
		_appFrame = new JFrame();
		_appFrame.setTitle("MDP Group 7 Simulator");
		_appFrame.setSize(new Dimension(800, 870));
		_appFrame.setResizable(false);
		
		// Center the main frame in the middle of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		_appFrame.setLocation(dim.width/2 - _appFrame.getSize().width/2, dim.height/2 - _appFrame.getSize().height/2);
		
		// Create the CardLayouts for storing the different views
		_mainCards = new JPanel(new CardLayout());
		_buttonsCards = new JPanel(new CardLayout());
		
		// Initialize the main CardLayout
		initMainLayout();
		
		// Initialize the buttons CardLayout
		initButtonsLayout();
		
		// Add CardLayouts to content pane
		Container contentPane = _appFrame.getContentPane();
		contentPane.add(_mainCards, BorderLayout.CENTER);
		contentPane.add(_buttonsCards, BorderLayout.SOUTH);
		
		// Display the application
		_appFrame.setVisible(true);
		_appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private static void initMainLayout() {
		
		// Initialize the Map for simulation
		_mainCards.add(theMap, "MAIN");
		
		CardLayout cl = ((CardLayout) _mainCards.getLayout());
	    cl.show(_mainCards, "MAIN");
		
	}

	private static void initButtonsLayout() {
		
		// Initialize the buttons used in main menu
		_mainButtons = new JPanel();
		addMainMenuButtons();
		_buttonsCards.add(_mainButtons, "MAIN_BUTTONS");
		
		// Show the real map (main menu) buttons by default
		CardLayout cl = ((CardLayout) _buttonsCards.getLayout());
		cl.show(_buttonsCards, "MAIN_BUTTONS");
	}

	private static void addMainMenuButtons() {
		// Main menu buttons
		// Exploration button
		JButton btn_Exploration = new JButton("Exploration");
		btn_Exploration.setFont(new Font("Arial", Font.BOLD, 13));
		// btn_Exploration.setMargin(new Insets(5, 5, 5, 5));
		btn_Exploration.setFocusPainted(false);
		btn_Exploration.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				//Do something here
			}
		});
		_mainButtons.add(btn_Exploration);


		//Shortest Path button
		JButton btn_ShortestPath = new JButton("Fastest Path");
		btn_ShortestPath.setFont(new Font("Arial", Font.BOLD, 13));
		// btn_ShortestPath.setMargin(new Insets(5, 5, 5, 5));
		btn_ShortestPath.setFocusPainted(false);
		btn_ShortestPath.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				runFastestPath = true;
				System.out.println("miao true!");
			}
		});
		_mainButtons.add(btn_ShortestPath);

		// Load Map button
		JButton btn_LoadMap = new JButton("Load Map");
		btn_LoadMap.setFont(new Font("Arial", Font.BOLD, 13));
		// btn_LoadMap.setMargin(new Insets(5, 5, 5, 5));
		btn_LoadMap.setFocusPainted(false);
		btn_LoadMap.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				//Do something here
			}
		});
		_mainButtons.add(btn_LoadMap);

		// Set speed of robot (speed of X steps per second ) button
		JButton btn_Speed = new JButton("Robot Speed");
		btn_Speed.setFont(new Font("Arial", Font.BOLD, 13));
		// btn_Speed.setMargin(new Insets(5, 5, 5, 5));
		btn_Speed.setFocusPainted(false);
		btn_Speed.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				JDialog d1=new JDialog(_appFrame,"Change Robot Speed",true);
				d1.setSize(400,400);
				d1.setLayout(new FlowLayout());
				JTextField speedTF = new JTextField(5);
				JButton speedSaveButton = new JButton("Save");
				
				speedSaveButton.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
						bot.setRobotSpeed(1000/(Integer.parseInt(speedTF.getText())));
						// System.out.println("botSpeed: " + botSpeed);
					}
				});

		        d1.add(new JLabel("Enter Speed (X steps per second): "));
		        d1.add(speedTF);
		        d1.add(speedSaveButton);

		        d1.setVisible(true);
			}
		});
		_mainButtons.add(btn_Speed);

		// Time-limited Exploration button
		JButton btn_TimeExploration = new JButton("Time-limited");
		btn_TimeExploration.setFont(new Font("Arial", Font.BOLD, 13));
		// btn_TimeExploration.setMargin(new Insets(5, 5, 5, 5));
		btn_TimeExploration.setFocusPainted(false);
		btn_TimeExploration.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				JDialog d2=new JDialog(_appFrame,"Time Limit Exploration",true);
				d2.setSize(400,400);
				d2.setLayout(new FlowLayout());
				JTextField timeTF = new JTextField(5);
				JButton timeSaveButton = new JButton("Save");
				
				timeSaveButton.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
						// get time entered
						// do time limit exploration
					}
				});

		        d2.add(new JLabel("Enter time for exploration: "));
		        d2.add(timeTF);
		        d2.add(timeSaveButton);

		        d2.setVisible(true);
		        
			}
		});
		_mainButtons.add(btn_TimeExploration);

		// Coverage-limited Exploration button
		JButton btn_CoverageExploration = new JButton("Coverage-limited");
		btn_CoverageExploration.setFont(new Font("Arial", Font.BOLD, 13));
		// btn_CoverageExploration.setMargin(new Insets(5, 5, 5, 5));
		btn_CoverageExploration.setFocusPainted(false);
		btn_CoverageExploration.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				JDialog d3=new JDialog(_appFrame,"Coverage Limit Exploration",true);
				d3.setSize(400,400);
				d3.setLayout(new FlowLayout());
				JTextField coverageTF = new JTextField(5);
				JButton coverageSaveButton = new JButton("Save");
				
				coverageSaveButton.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
						// get coverage entered
						// do time limit exploration
					}
				});

		        d3.add(new JLabel("Enter coverage for exploration: "));
		        d3.add(coverageTF);
		        d3.add(coverageSaveButton);

		        d3.setVisible(true);
			}
		});
		_mainButtons.add(btn_CoverageExploration);
	}

	private static void FastestPath(){
		bot.setRobotPos(1,1);
		theMap.repaint();
		ShortestPathAlgo shortestPath = new ShortestPathAlgo(theMap, bot);
		shortestPath.runShortestPath(theMap, 18, 13);
		runFastestPath = false;
	}
	private static Map ReadMap(){//Map descriptor format
		Robot bot = new Robot(1,1);
		Map NewMap = new Map(bot);
		System.out.println("starting to read map");
		try{
			int decInt, asciiInt;
			FileReader fr = new FileReader("MAP1.txt");
			BufferedReader br = new BufferedReader(fr);
				
			//while ((asciiInt = fr.read()) != -1) {//read char by char in ASCII from textfile and convert to decimal
				//decInt = Character.getNumericValue(asciiInt);
				//System.out.println(decInt);

				
					
				for(int r=0;r<MapConstants.MAP_ROW;r++){
					for(int c=0;c<MapConstants.MAP_COL;c++){
						asciiInt = fr.read();
						if(asciiInt==-1)//check for EOF
							break;
						decInt = Character.getNumericValue(asciiInt);
						System.out.println(decInt);
						if(decInt == 1)
							NewMap.setObstacle(r, c);
						else if(decInt != 0)//when character in text file is not part of the map eg.spaces etc
							c--;//empty block has to be assigned 1 or 0 to indicate obstacle
					}
				}
				
			//}//end of while loop
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		 	
		System.out.println("end of read map method");
		return NewMap;
	}
}
