package de.metro.robocode;

import static robocode.util.Utils.normalRelativeAngleDegrees;
import robocode.*;
import java.awt.Color;

public class Yep extends RateControlRobot {

    double deplasarea;
    int ready = 0;
    int first_time = 0;
    int count = 0; 
    boolean setup=true;
	
    double gunTurnAmt; 
    String trackName; 
    int minim=1;
	
    public void run() {

        init();

        while (true) {
            if (getOthers() < minim) {
				if(setup){
					trackName = null;
				    setAdjustGunForRobotTurn(true);
				    gunTurnAmt = 10;
					setup=false;			
				}
                turnGunRight(gunTurnAmt);
                
                count++;
                
                if (count > 2) {
                    gunTurnAmt = -10;
                }
               
                if (count > 5) {
                    gunTurnAmt = 10;
                }
                
                if (count > 11) {
                    trackName = null;
                }
            } else {
                ready = 1;

                ahead(calculate_and_move());

                ready = 0;

                turnRight(90);
            }
        }
    }

    public void init() {
        
		
        //stabileste culorile
        setBodyColor(Color.orange);
        setGunColor(Color.red);
        setRadarColor(Color.yellow);
        setBulletColor(Color.black);
        setScanColor(Color.green);

        deplasarea = Math.max(getBattleFieldWidth(), getBattleFieldHeight());
        turnLeft(getHeading() % 90);
        ahead(calculate_and_move());

        ready = 1;

        turnGunRight(90);
        turnRight(90);
    }

    double calculate_and_move() {
        int val = 20;
        if (getHeading() == 0) {
            return (600 - getY() - val);
        } else if (getHeading() == 90) {
            return (800 - getX() - val);
        } else if (getHeading() == 180) {
            return (getY() - val);
        } else if (getHeading() == 270) {
            return (getX() - val);
        }
        return 800;
    }

    public void onHitRobot(HitRobotEvent e) {
        if (getOthers() < minim) {
            if (trackName != null && !trackName.equals(e.getName())) {
                out.println("Tracking " + e.getName() + " due to collision");
            }
            trackName = e.getName();
            gunTurnAmt = normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getRadarHeading()));
            turnGunRight(gunTurnAmt);
            fire(3);
            back(50);
        } else if (e.getBearing() > -90 && e.getBearing() < 90) {
            back(100);
        } else {
            ahead(100);
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        if (getOthers() < minim) {
            if (trackName != null && !e.getName().equals(trackName)) {
                return;
            }
            if (trackName == null) {
                trackName = e.getName();
                out.println("Tracking " + trackName);
            }
            count = 0;
            if (e.getDistance() > 150) {
                gunTurnAmt = normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getRadarHeading()));

                turnGunRight(gunTurnAmt);
                turnRight(e.getBearing());
				
                ahead(e.getDistance() - 140);
                return;
            }
            gunTurnAmt = normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getRadarHeading()));
            turnGunRight(gunTurnAmt);
            fire(3);
            if (e.getDistance() < 100) {
                if (e.getBearing() > -90 && e.getBearing() <= 90) {
                    back(40);
                } else {
                    ahead(40);
                }
            }
            scan();
        } else {
            if (getEnergy() < 1) {

            } else if (getEnergy() < 10) {
                fire(0.1);
            } else if (getEnergy() < 60) {
                fire(1);
            } else if (getOthers() > 10) {
                fire(3);
            } else {
                fire(1);
            }

            if (ready == 1) {
                scan();
            }
        }
    }

}
