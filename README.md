# CZ3004MDP
##Simulator
Simulate the exploration and shortest path algo.

how to run it:<br />
1.clone the whole project to your computer<br />
2.open cmd or terminal<br />
3.set the current directory as /CZ3004MDP-Group7/Simulator<br />
4.execute: java -cp ./bin simulation.Simulator<br />

how to compile it:<br />
1. open cmd or terminal<br />
2. set the current directory as /CZ3004MDP-Group7/Simulator<br />
3. execute: javac -d bin src/map/MapConstants.java src/map/Map.java src/robot/RobotConstants.java src/map/Block.java src/robot/Robot.java src/robot/ShortestPathAlgo.java  src/robot/ExplorationAlgo.java src/robot/Sensor.java src/simulation/Simulator.java<br />

##Leaderboard
Added Communication manager to communicate with Rpi<br />
get the sensor data from robot<br />
update map and send the map layout to android<br />
send the robot position to android<br />
after calculation, send the next move to robot<br />

The IP address and port can be changed in CommMgr.java file.<br />

how to run it:<br />
1.clone the whole project to your computer<br />
2.open cmd or terminal<br />
3.set the current directory as /CZ3004MDP-Group7/LeaderBoard<br />
4.execute: java -cp ./bin simulation.Simulator<br />

how to compile it:<br />
1. open cmd or terminal<br />
2. set the current directory as /CZ3004MDP-Group7/LeaderBoard<br />
3. execute: javac -d bin src/map/MapConstants.java src/map/Map.java src/robot/RobotConstants.java src/map/Block.java src/robot/Robot.java src/robot/ShortestPathAlgo.java  src/robot/ExplorationAlgo.java src/robot/Sensor.java src/communication/CommMgr.java src/simulation/Simulator.java<br />


 