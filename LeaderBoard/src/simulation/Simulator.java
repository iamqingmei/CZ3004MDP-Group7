package simulation;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import map.MapConstants;

import java.util.concurrent.TimeUnit;

import map.Map; 
import map.Block;
import robot.Robot;
import robot.RobotConstants.MOVE;
import robot.RobotConstants.DIRECTION;
import robot.ShortestPathAlgo;
import robot.ExplorationAlgo;
import communication.CommMgr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;


import java.util.*;

public class Simulator {
	// JFrame for the application
	private static JFrame _appFrame = null;
	
	// JPanel for laying out different views
	private static JPanel _mainCards = null;
	private static JPanel _buttonsCards = null;

	// The frame used for main menu buttons
	private static JPanel _mainButtons = null;

	private static Robot bot;

	private static Map simExMap = null;
	private static Map simShortestPathMap = null;

	//for time limited exploration
	private static int timeLimited=10;

	//for coverage limited exploration
	private static long coverageLimited = 0;

	

	public static void main(String[] args){
		
		bot = new Robot(1,1);
		simShortestPathMap = new Map(bot);

		simExMap = new Map(bot);
		simExMap.setAllUnexplored();

		displayEverythings();
		CommMgr.getCommMgr().setConnection(60000);
		System.out.println("If it is connected: " + CommMgr.getCommMgr().isConnected());
		
		// for (int i=0;i<2;i++){
		CommMgr.getCommMgr().sendMsg("hello Andriod", "PC2PC ");
		// 	try{
		// 		TimeUnit.MILLISECONDS.sleep(5000);
		// 	}
		// 	catch(InterruptedException e)
		// 	{
		// 	     System.out.println("Miao!");
		// 	}
		// 	System.out.println("received: " + CommMgr.getCommMgr().recvMsg());
		// }
		System.out.println("received: " + CommMgr.getCommMgr().recvMsg());
		System.out.println("received: " + CommMgr.getCommMgr().recvMsg());
		
		
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
		_mainCards.add(simShortestPathMap, "MAIN");
		_mainCards.add(simExMap,"EXPLO");
		
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

		// for multithreading
		class Exploration extends SwingWorker<Integer, String>{
			protected Integer doInBackground() throws Exception{
				// bot.setRobotPos(1,1);
				simExMap.repaint();
				// CommMgr.getCommMgr().sendMsg("Start Exploration", "PC2PC ");
				ExplorationAlgo exploration = new ExplorationAlgo(simExMap, simShortestPathMap, bot);
				exploration.runExploration();

				mapDescriptor();
				return 222;
			}
		}
		// Exploration button
		JButton btn_Exploration = new JButton("Exploration");
		btn_Exploration.setFont(new Font("Arial", Font.BOLD, 13));
		btn_Exploration.setFocusPainted(false);
		btn_Exploration.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				CardLayout cl = ((CardLayout) _mainCards.getLayout());
			    cl.show(_mainCards, "EXPLO");
				new Exploration().execute();
			}
		});
		_mainButtons.add(btn_Exploration);

		// for multithreading
		class FastestPath extends SwingWorker<Integer, String>{
		    protected Integer doInBackground() throws Exception
		    {
		        bot.setRobotPos(1,1);
				simShortestPathMap.repaint();
				ShortestPathAlgo shortestPath = new ShortestPathAlgo(simShortestPathMap, bot);

				StringBuilder output = shortestPath.runShortestPath(simShortestPathMap, 18, 13);
				// byte[] outputByteArray = String.valueOf(output).getBytes();
				// System.out.println(outputByteArray);
				System.out.println("Fastest Path is : " + output);
				CommMgr.getCommMgr().sendMsg(output.toString(), "PC2PC ");
		        return 111;
		    }
		}

		//Shortest Path button
		JButton btn_ShortestPath = new JButton("Fastest Path");
		btn_ShortestPath.setFont(new Font("Arial", Font.BOLD, 13));
		btn_ShortestPath.setFocusPainted(false);
		btn_ShortestPath.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				CardLayout cl = ((CardLayout) _mainCards.getLayout());
			    cl.show(_mainCards, "MAIN");
			    new FastestPath().execute();
			}
		});
		_mainButtons.add(btn_ShortestPath);
	}


	private static void mapDescriptor(){
		String DescriptorFormat = "11"; //pad the first 2 bits
		String DescriptorFinal = "";
		for(int r=0;r<MapConstants.MAP_ROW;r++){//output map descriptor for explored area
			for(int c=0;c<MapConstants.MAP_COL;c++){
				if(simExMap.getBlock(r,c).getIsExplored() == true){					
					//System.out.println(i++);
					DescriptorFormat = DescriptorFormat + "1"; //grid explored
				}
				else
					DescriptorFormat = DescriptorFormat + "0"; //grid not explored
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
		System.out.println(DescriptorFormatHex);
		DescriptorFinal += DescriptorFormatHex; //last 4 hexa digits
		System.out.println(DescriptorFinal);
		
		DescriptorFormat = "";
		String DescriptorFinal2 ="";
		System.out.println("printing obstacles");
		for(int r=0;r<MapConstants.MAP_ROW;r++){//output map descriptor for obstacles in map
			for(int c=0;c<MapConstants.MAP_COL;c++){
				if(simExMap.getBlock(r,c).getIsExplored() == true){
					if(simExMap.getBlock(r,c).getIsObstacle() == true)
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
		}
		System.out.println(DescriptorFormat);
		while(DescriptorFormat.length()%4!=0) // pad it to make the total digits divisible by 4
			DescriptorFormat += "1";
		DescriptorFormatBin = Integer.parseInt(DescriptorFormat,2);
		DescriptorFormatHex = Integer.toString(DescriptorFormatBin,16);
		while(DescriptorFormatHex.length()!= DescriptorFormat.length()/4) // pad 0s in front according to string length
			DescriptorFormatHex = "0"+ DescriptorFormatHex;
		System.out.println("last df:" + DescriptorFormatHex);
		DescriptorFinal2 += DescriptorFormatHex; //last 4 hexa digits
		System.out.println(DescriptorFinal2);
	}
}
