package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

class TestingOpMode extends LinearOpMode {

    private DcMotor frontLeft, frontRight, backLeft, backRight;
    private LinearOpMode parent;

    private Motion motion;
    int[] distances = {25, 50, 100, 200, 400, 800};
    private Menu menu;
    private double drivePower;

    public void runOpMode() {
    }

    public void initialize() {
        motion = new Motion(this);
        menu = new Menu(this);
        menu.add(new MenuItem(0.0, "power", 0.0, 1.0, 0.05));
        while (!isStarted()) {
            menu.update();
            menu.display();
        }

        drivePower = menu.get(0);

    }

    public void motionTest(double drivePower, int driveTicks, boolean driveDirection){

        for (int d : distances) {
            motion.driveStraight(drivePower, d, driveDirection);
            getAvgEncoders();
            checkIMU();
            diffOfLeftRight();
        }


    }

    public int getAvgEncoders() {
        return 0;
    }


    public void checkIMU() {
        ;
    }

    public void diffOfLeftRight() {

    }
}
