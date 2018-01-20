package de.metro.robocode;

import robocode.*;
import java.awt.Color;

public class Yep extends RateControlRobot {

    double deplasarea;
    int ready = 0;
    int first_time = 0;

    public void run() {

        init();

        while (true) {

            ready = 1;

            ahead(calculate_and_move());

            ready = 0;

            turnRight(90);
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
        if (getHeading() == 0) {
            return (600 - getY()-1);
        } else if (getHeading() == 90) {
            return (600 - getX()-1);
        } else if (getHeading() == 180) {
            return (getY()-1);
        } else if (getHeading() == 270) {
            return (getX()-1);
        }
        return 800;
    }

    public void onHitRobot(HitRobotEvent e) {

        if (e.getBearing() > -90 && e.getBearing() < 90) {
            back(100);
        } else {
            ahead(100);
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        if (getOthers() > 10) {
            fire(3);
        } else {
            fire(1);
        }

        if (ready == 1) {
            scan();
        }
    }

}
