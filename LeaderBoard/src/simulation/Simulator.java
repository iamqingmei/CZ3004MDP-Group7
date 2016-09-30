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

	private static Map realMap = null;

	public static void main(String[] args){
		
		bot = new Robot(1,1);

		realMap = new Map(bot);
		realMap.setAllUnexplored();

		displayEverythings();
		CommMgr.getCommMgr().setConnection(60000);
		System.out.println("If it is connected: " + CommMgr.getCommMgr().isConnected());
		
		// for (int i=0;i<2;i++){
		// CommMgr.getCommMgr().sendMsg("hello Andriod", "PC2AN ");
		// 	try{
		// 		TimeUnit.MILLISECONDS.sleep(5000);
		// 	}
		// 	catch(InterruptedException e)
		// 	{
		// 	     System.out.println("Miao!");
		// 	}
		// 	System.out.println("received: " + CommMgr.getCommMgr().recvMsg());
		// }
		// CommMgr.getCommMgr().sendMsg(output.toString(), "PC2PC ");
		// System.out.println("received: " + CommMgr.getCommMgr().recvMsg());
		// System.out.println("received: " + CommMgr.getCommMgr().recvMsg());
		// System.out.println("received: " + CommMgr.getCommMgr().recvMsg());
		// System.out.println("received: " + CommMgr.getCommMgr().recvMsg());
		


		
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
				System.out.println("waiting for Andriod command");
				String startE = CommMgr.getCommMgr().recvMsg();
		    	while(!startE.equals("explore")){
		    		startE = CommMgr.getCommMgr().recvMsg();
		    	}
		    	System.out.println("start exploration");
				// bot.setRobotPos(1,1);
				realMap.repaint();
				// CommMgr.getCommMgr().sendMsg("Start Exploration", "PC2PC ");
				ExplorationAlgo exploration = new ExplorationAlgo(realMap, bot);
				exploration.runExploration();

				realMap.mapDescriptor();
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
				CommMgr.getCommMgr().sendMsg(output.toString(), "PC2AR");
		        return 111;
		    }
		}

		// for multithreading
		//Only display the path, not sending anything
		// class DisplayFastestPath extends SwingWorker<Integer, String>{
		//     protected Integer doInBackground() throws Exception
		//     {
		//     	// System.out.println("waiting for Andriod command");
		//     	// String startF = CommMgr.getCommMgr().recvMsg();
		//     	// while(!startF.equals("race")){
		//     	// 	startF = CommMgr.getCommMgr().recvMsg();
		//     	// }
		//     	System.out.println("start FastestPath");
		//         bot.setRobotPos(1,1);
		// 		simShortestPathMap.repaint();
		// 		ShortestPathAlgo shortestPath = new ShortestPathAlgo(simShortestPathMap, bot);

		// 		shortestPath.runShortestPath(simShortestPathMap, 18, 13);
		//         return 111;
		//     }
		// }

		// //Only display the path, not sending anything
		// JButton btn_DisplayShortestPath = new JButton("Fastest Path");
		// btn_DisplayShortestPath.setFont(new Font("Arial", Font.BOLD, 13));
		// btn_DisplayShortestPath.setFocusPainted(false);
		// btn_DisplayShortestPath.addMouseListener(new MouseAdapter() {
		// 	public void mousePressed(MouseEvent e) {
		// 		CardLayout cl = ((CardLayout) _mainCards.getLayout());
		// 	    cl.show(_mainCards, "MAIN");
		// 	    new DisplayFastestPath().execute();
		// 	}
		// });
		// _mainButtons.add(btn_DisplayShortestPath);

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

		//RecvMsg button
		JButton btn_recvMsg = new JButton("Receive Msg");
		btn_recvMsg.setFont(new Font("Arial", Font.BOLD, 13));
		btn_recvMsg.setFocusPainted(false);
		btn_recvMsg.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				CardLayout cl = ((CardLayout) _mainCards.getLayout());
			    cl.show(_mainCards, "MAIN");
			    new RecvMsg().execute();
			}
		});
		_mainButtons.add(btn_recvMsg);

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
				JTextField headTF = new JTextField(5);
				JTextField msgTF = new JTextField(20);
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
	}

}
