package de.metro.robocode;

import robocode.*;
import java.awt.Color;

public class Yep extends RateControlRobot {

    double deplasarea;
    int ready=0;
    int first_time=0;
            
    public void run() {
        
        setBodyColor(Color.orange);
        setGunColor(Color.red);
        setRadarColor(Color.yellow);
        setBulletColor(Color.black);
        setScanColor(Color.green);

  
        deplasarea = Math.max(getBattleFieldWidth(), getBattleFieldHeight());

        turnLeft(getHeading() % 90);
        ahead(deplasarea);
        
        ready = 1;
        
        turnGunRight(90);
        turnRight(90);

        while (true) {
            // Look before we turn when ahead() completes.
            ready = 1;
            // Move up the wall
            ahead(deplasarea);
            // Don't look now
            ready = 0;
            // Turn to the next wall
            turnRight(90);
        }
    }

    /**
     * onHitRobot: Move away a bit.
     */
    public void onHitRobot(HitRobotEvent e) {
        // If he's in front of us, set back up a bit.
        if (e.getBearing() > -90 && e.getBearing() < 90) {
            back(100);
        } // else he's in back of us, so set ahead a bit.
        else {
            ahead(100);
        }
    }

    /**
     * onScannedRobot: Fire!
     */
    public void onScannedRobot(ScannedRobotEvent e) {
        fire(3);
        // Note that scan is called automatically when the robot is moving.
        // By calling it manually here, we make sure we generate another scan event if there's a robot on the next
        // wall, so that we do not start moving up it until it's gone.
        if (ready==1) {
            scan();
        }
    }

}
