package simulation;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import map.MapConstants;

import java.util.concurrent.TimeUnit;

import map.Map; 
import map.Block;
import robot.Robot;
import robot.RobotConstants;
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
	//testing
	
	// JFrame for the application
	private static JFrame _appFrame = null;
	
	// JPanel for laying out different views
	private static JPanel _mainCards = null;
	private static JPanel _buttonsCards = null;

	// The frame used for main menu buttons
	private static JPanel _mainButtons = null;

	private static Robot bot;

	private static Map realMap = null;

	public static void main(String[] args){
		
		bot = new Robot(1,1);

		realMap = new Map(bot);
		realMap.setAllUnexplored();

		//goal zone and start zone is confirmed free
		zoneConfirmFree(MapConstants.GOAL_ROW,MapConstants.GOAL_COL);
		zoneConfirmFree(RobotConstants.STARTING_ROW,RobotConstants.STARTING_COL);

		displayEverythings();
		CommMgr.getCommMgr().setConnection(60000);
		System.out.println("If it is connected: " + CommMgr.getCommMgr().isConnected());
		
	}

	

	private static void displayEverythings(){
		// Main frame for displaying everything
		_appFrame = new JFrame();
		_appFrame.setTitle("MDP Group 7 Leaderboard");
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
		_mainCards.add(realMap,"MAIN");
		
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
				// CommMgr.getCommMgr().sendMsg("explore", "PC2PC");
				System.out.println("waiting for Andriod command");
				String startE = CommMgr.getCommMgr().recvMsg();
		    	while(!startE.equals("explore")){
		    		startE = CommMgr.getCommMgr().recvMsg();
		    	}
		    	System.out.println("start exploration");
		    	long start = System.currentTimeMillis();
				
				ExplorationAlgo exploration = new ExplorationAlgo(realMap, bot);
				exploration.runExploration();
				realMap.repaint();

				long end = System.currentTimeMillis();
				long duration = (end - start)/1000;
				System.out.println("exploration spend: " + duration + "second");

				try{
					TimeUnit.MILLISECONDS.sleep(RobotConstants.MSG_DELAY);
				}
				catch(InterruptedException e)
				{
				     System.out.println("send msg sleeping error!!!!!!");
				} 
				realMap.MDFString();

				try{
					TimeUnit.MILLISECONDS.sleep(RobotConstants.MSG_DELAY);
				}
				catch(InterruptedException e)
				{
				     System.out.println("send msg sleeping error!!!!!!");
				} 
				CommMgr.getCommMgr().sendMsg("W", "PC2AR");

				System.out.println("start to calculate FastestPath");
				ShortestPathAlgo sp = new ShortestPathAlgo(realMap, bot);
				StringBuilder output = sp.runShortestPath(realMap, MapConstants.GOAL_ROW, MapConstants.GOAL_COL);

				// byte[] outputByteArray = String.valueOf(output).getBytes();
				// System.out.println(outputByteArray);
				
				System.out.println("Fastest Path is : " + output);
				String startRace = CommMgr.getCommMgr().recvMsg();
		    	while(!startRace.equals("race")){
		    		startRace = CommMgr.getCommMgr().recvMsg();
		    	}
		    	if (output.length() != 0){
		    		CommMgr.getCommMgr().sendMsg("X" + output.toString(), "PC2AR");
		    	}
				return 222;
			}
		}
		// Exploration button
		JButton btn_Exploration = new JButton("Start Leaderboard!");
		btn_Exploration.setFont(new Font("Arial", Font.BOLD, 13));
		btn_Exploration.setFocusPainted(false);
		btn_Exploration.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				CardLayout cl = ((CardLayout) _mainCards.getLayout());
			    cl.show(_mainCards, "MAIN");
				new Exploration().execute();
			}
		});
		_mainButtons.add(btn_Exploration);


		// for multithreading
		class FastestPath extends SwingWorker<Integer, String>{
		    protected Integer doInBackground() throws Exception
		    {
		    	System.out.println("start FastestPath");
		        bot.setRobotPos(1,1);
				realMap.repaint();
				ShortestPathAlgo shortestPath = new ShortestPathAlgo(realMap, bot);

				StringBuilder output = shortestPath.runShortestPath(realMap, 18, 13);
				// byte[] outputByteArray = String.valueOf(output).getBytes();
				// System.out.println(outputByteArray);
				System.out.println("Fastest Path is : " + output);
				CommMgr.getCommMgr().sendMsg("X" + output.toString(), "PC2AR");
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

		// for multithreading
		class RecvMsg extends SwingWorker<Integer, String>{
		    protected Integer doInBackground() throws Exception
		    {
		        CommMgr.getCommMgr().recvMsg();
		        return 333;
		    }
		}

		// add obstacle button
		JButton btn_obstacle = new JButton("Obstacle");
		btn_obstacle.setFont(new Font("Arial", Font.BOLD, 13));
		btn_obstacle.setFocusPainted(false);
		btn_obstacle.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				CardLayout cl = ((CardLayout) _mainCards.getLayout());
			    cl.show(_mainCards, "MAIN");

			    JDialog d3=new JDialog(_appFrame,"Add/Remove Obstacles!",true);
				d3.setSize(350,200);
				d3.setLayout(new FlowLayout());
				final JTextField rowTF = new JTextField(2);
				final JTextField columnTF = new JTextField(2);
				JButton addButton = new JButton("Add");
				JButton removeButton = new JButton("Remove");
				JButton printObButton = new JButton("Print Obstacles");
				JButton printVirtualWall = new JButton("Print Virtual Wall");

				
				addButton.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
						int r = Integer.parseInt(rowTF.getText());
						int c = Integer.parseInt(columnTF.getText());
						realMap.getBlock(r,c).setIsExplored(true);
						realMap.setObstacle(r,c,true);
						System.out.println("obstacle at " + r + "," + c + " is added");
						realMap.repaint();
						realMap.MDFString();
					}
				});

				removeButton.addMouseListener(new MouseAdapter(){
					public void mousePressed(MouseEvent e) {
						int r = Integer.parseInt(rowTF.getText());
						int c = Integer.parseInt(columnTF.getText());
						realMap.getBlock(r,c).setIsExplored(true);
						realMap.setObstacle(r,c,false);
						System.out.println("obstacle at " + r + "," + c + " is removed");
						realMap.repaint();
						realMap.MDFString();
					}
				});

				printObButton.addMouseListener(new MouseAdapter(){
					public void mousePressed(MouseEvent e){
						for (int i=0; i < MapConstants.MAP_ROW; i++){
							for (int j=0; j< MapConstants.MAP_COL; j++){
								if (realMap.getBlock(i,j).getIsObstacle()){
									System.out.println("obstacle at " + i + "," + j);
								}
							}
						}
					}
				});

				printVirtualWall.addMouseListener(new MouseAdapter(){
					public void mousePressed(MouseEvent e){
						for (int i=0; i < MapConstants.MAP_ROW; i++){
							for (int j=0; j< MapConstants.MAP_COL; j++){
								if (realMap.getBlock(i,j).getIsVirtualWall()){
									System.out.println("Virtual wall at " + i + "," + j);
								}
							}
						}
					}
				});

				Box box1 = Box.createVerticalBox();
		        box1.add(new JLabel("Enter row number: "));
		        box1.add(rowTF);
		        Box box2 = Box.createVerticalBox();
		        box2.add(new JLabel("Enter column numer: "));
		        box2.add(columnTF);

		        d3.add(box1);
		        d3.add(box2);
		        d3.add(addButton);
		        d3.add(removeButton);
		        d3.add(printObButton);
		        d3.add(printVirtualWall);
		        d3.setVisible(true);
			}
		});
		_mainButtons.add(btn_obstacle);

		//send msg button
		JButton btn_SendMsg = new JButton("Send Msg");
		btn_SendMsg.setFont(new Font("Arial", Font.BOLD, 13));
		btn_SendMsg.setFocusPainted(false);
		btn_SendMsg.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				CardLayout cl = ((CardLayout) _mainCards.getLayout());
			    cl.show(_mainCards, "MAIN");

			    JDialog d3=new JDialog(_appFrame,"Send Message",true);
				d3.setSize(400,200);
				d3.setLayout(new FlowLayout());
				final JTextField headTF = new JTextField(5);
				final JTextField msgTF = new JTextField(20);
				JButton saveButton = new JButton("Send");
				
				saveButton.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
						String head = headTF.getText();
						String msg = msgTF.getText();
						CommMgr.getCommMgr().sendMsg(msg, head);
					}
				});
				Box box1 = Box.createVerticalBox();
		        box1.add(new JLabel("Enter msg head: "));
		        box1.add(headTF);
		        Box box2 = Box.createVerticalBox();
		        box2.add(new JLabel("Enter msg body: "));
		        box2.add(msgTF);

		        d3.add(box1);
		        d3.add(box2);
		        d3.add(saveButton);
		        d3.setVisible(true);
			}
		});
		_mainButtons.add(btn_SendMsg);

		//RecvMsg button
		JButton btn_disconnect = new JButton("disconnect");
		btn_disconnect.setFont(new Font("Arial", Font.BOLD, 13));
		btn_disconnect.setFocusPainted(false);
		btn_disconnect.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				CommMgr.getCommMgr().sendMsg("closed","");
				CommMgr.getCommMgr().closeConnection();
			}
		});
		_mainButtons.add(btn_disconnect);

		//RecvMsg button
		JButton btn_mdf = new JButton("MDF String");
		btn_mdf.setFont(new Font("Arial", Font.BOLD, 13));
		btn_mdf.setFocusPainted(false);
		btn_mdf.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				realMap.MDFString();
			}
		});
		_mainButtons.add(btn_mdf);
	}

	private static void zoneConfirmFree(int r, int c){
		realMap.getBlock(r,c).setConfirmFree(true);
		realMap.getBlock(r+1,c).setConfirmFree(true);
		realMap.getBlock(r-1,c).setConfirmFree(true);
		realMap.getBlock(r,c+1).setConfirmFree(true);
		realMap.getBlock(r+1,c+1).setConfirmFree(true);
		realMap.getBlock(r-1,c+1).setConfirmFree(true);
		realMap.getBlock(r,c-1).setConfirmFree(true);
		realMap.getBlock(r+1,c-1).setConfirmFree(true);
		realMap.getBlock(r-1,c-1).setConfirmFree(true);
	}

}
