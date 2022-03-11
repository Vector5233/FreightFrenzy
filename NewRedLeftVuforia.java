package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name ="NewRedLeftVuforia", group = "GROUP_NAME")

public class NewRedLeftVuforia extends LinearOpMode {
    virtualBotObject robot = new virtualBotObject(this);

    final double INITGRABBERSERVOPOSITION = 1;
    final double BOB = 0;
    final double DRIVEPOWER = .4;
    int FORWARDTICKS = 80;
    int STRAFETICKS = 10;
    long SLEEPYTIME = 3000;
    final double TURNPOWER =.2;
    final double VUFORIATURN = .5;
    int level = 3;


    public void runOpMode() {
    initRobot();
    robot.initCameraServo(BOB);
    robot.vision.initVuforia();
    robot.vision.createPassThrough();
    sleep(1000);
    waitForStart();
    level = robot.vision.getLevel();

    telemetry.addData("Deliver to:", level);
    telemetry.update();

    robot.vision.closePassthrough();
    duckSpinnerDrive();
    robot.vision.activateTarget();
    driveToMeasureSpot();
    robot.rotateToSweetSpot();
    driveToHub();

    robot.deliverBlock(level);

    robot.driveForward(.4, -40);
    robot.autoTurnDegrees(.4,60);
    robot.driveForward(.2,-150);
    telemetry.speak("beep beep");
    telemetry.update();
    }

    public void initRobot(){
        robot.init();
        robot.initGrabberServo(INITGRABBERSERVOPOSITION);
        robot.initCameraServo(BOB);
    }

    public void duckSpinnerDrive(){
        telemetry.addData("Duck Level:", level);
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
        robot.driveForward(DRIVESLOW, DRIVETICKS);
        robot.autoTurn(TURNPOWER, TICKTURN);
        sleep(HUBSLEEP);
        robot.driveForward(TURNPOWER, TICKFORWARD);
        robot.initCameraServo(VUFORIATURN);
        sleep(HUBSLEEP);
    }

    public void driveToHub(){
        double POWERDRIVE = 0;
        int TICKDRIVE = 0;
        robot.driveForward(.3, 70);
    }
}