#include<stdio.h>

typedef struct Sensors {
   char sensorDir;
   int sensorCol;
   int sensorRow;
   int sensorRange;
} Sensor;

//----------global variables--------------------
//define map size
int row = 20;
int col = 15;
//define sensor range
int shortSensorRange = 1;
int longSensorRange = 4;
//declare map,robot,sensor
int map[row][col];
int robotRow = 1;
int robotCol = 1;
char robotDir = "N";
Sensor sensor1;
Sensor sensor2; 
Sensor sensor3; 
Sensor sensor4; 
Sensor sensor5;

int main()
{
    
    sensor1.sensorRange = shortSensorRange;
    sensor2.sensorRange = shortSensorRange;
    sensor3.sensorRange = shortSensorRange;
    sensor4.sensorRange = shortSensorRange;
    sensor5.sensorRange = longSensorRange;   

    // initialize the map, all empty
    for (int i=0;i<row;i++){
        for (int j=0;j<col;j++){
            map[i][j]=0;
        }
    }
    //--------------------algo functions -------------------------
    
    //--------------------robot functions-------------------------

    //moveRobot(char move) is to update the robot position
    //char move can be F,R,L,R
    void moveRobot(char move){

    }

    //setting the sensor position and direction by robot current postion
    void setSensors(){

    }

    //update the obstacles in the map by current sensor readings
    void sense(Sensor s, int readings){

    }

    //get 5 sensor readings
    //call sense() to update map
    void readSensor(){

    }

    //round cm to number of grid
    int roundToGrid(int cm){
        if (cm<0){
            return 0;
        }
        int base = cm/10;
        if (cm%10>=5){
            return ++base;
        }
        return base;
    }

}