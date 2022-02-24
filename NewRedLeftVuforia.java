package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name ="NewRedLeftVuforia", group = "GROUP_NAME")

public class NewRedLeftVuforia extends LinearOpMode {
    virtualBotObject robot = new virtualBotObject(this);
    VisionObject vision = new VisionObject(robot.parent);
    DuckDetector detector = new DuckDetector();
    final double INITGRABBERSERVOPOSITION = 1;
    final double BOB = 0;
    final double DRIVEPOWER = .4;
    int FORWARDTICKS = 80;
    int STRAFETICKS = 10;
    long SLEEPYTIME = 3000;
    final double TURNPOWER =.2;


    public void runOpMode() {
    initRobot();
    vision.initVuforia();
    vision.createPassThrough();

    //necessary to avoid null pointer error
    waitForStart();
    /*duckSpinnerDrive();
    driveToMeasureSpot();

    robot.deliverBlock(vision.duckLevel());*/
    }

    public void initRobot(){
        robot.init();
        robot.initGrabberServo(INITGRABBERSERVOPOSITION);
        robot.initCameraServo(BOB);
    }

    public void duckSpinnerDrive(){
        telemetry.addData("Duck Level:", vision.duckLevel());
        telemetry.update();
        robot.initGrabberServo(0);
        robot.turnOnDuckSpinner();
        robot.driveForward(DRIVEPOWER, FORWARDTICKS);
        robot.autoStrafe(TURNPOWER,STRAFETICKS);
        sleep(SLEEPYTIME);
        robot.turnOffDuckSpinner();
    }

    public void driveToMeasureSpot(){
        int DRIVETICKS = -70;
        long HUBSLEEP = 100;
        int TICKTURN = 600;
        int TICKFORWARD = 450;
        double DRIVESLOW = .2;
        final double VUFORIA = .5;
        robot.driveForward(DRIVESLOW, DRIVETICKS);
        robot.autoTurn(TURNPOWER, TICKTURN);
        sleep(HUBSLEEP);
        robot.driveForward(TURNPOWER, TICKFORWARD);
        robot.initCameraServo(VUFORIA);
        sleep(HUBSLEEP);
    }
}