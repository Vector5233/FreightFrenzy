package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name ="redLeft", group = "GROUP_NAME")
//properties file
public class redLeft extends LinearOpMode {
    virtualBotObject robot = new virtualBotObject(this);
    private OpenCvInternalCamera phoneCam;
    private final DuckDetector detector = new DuckDetector();
    Menu initMenu = new Menu(this);
    final double BOB = .09;
    int duckLevel = 3;
    final double DRIVE_POWER = .4;
    int FORWARD_TICKS = 80;
    int STRAFE_TICKS = 30;
    long SLEEPY = 3000;
    final double TURN_POWER =.2;
    final double G_SERVO_POSITION = 1;

    //robot must start with black line on duck spinner brace parallel to the metal part of the carousel with three fingers in between the duck spinner and the carousel

    public void runOpMode() {
        initRobot();
        identifyDuck();

        waitForStart();
        duckSpinnerDrive();
        driveToHub();
        robot.deliverBlock(duckLevel);
        parkInStorage();
    }

    public void duckSpinnerDrive(){
        telemetry.addData("Duck Level:", duckLevel);
        telemetry.update();
        robot.initGrabberServo(0);
        robot.turnOnDuckSpinner();
        robot.driveForward(DRIVE_POWER, FORWARD_TICKS);
        robot.autoStrafe(TURN_POWER, STRAFE_TICKS);
        sleep(SLEEPY);
        robot.turnOffDuckSpinner();
    }

    public void driveToHub(){
        int STRAFE_POWER = 1;
        int STRAFE_TICKS = -150;         //sign change
        long SLEEP = 100;
        int TURN_RIGHT = 625;
        int TICK_FORWARD = 480;
        int TICK_FORWARD_TWO = 412;
        robot.autoStrafe(STRAFE_POWER, STRAFE_TICKS);
        robot.autoTurn(TURN_POWER, TURN_RIGHT);
        sleep(SLEEP);
        if (duckLevel == 2) {
            robot.driveForward(TURN_POWER, TICK_FORWARD_TWO);
        } else {
            robot.driveForward(TURN_POWER, TICK_FORWARD);
        }
        sleep(SLEEP);
        robot.setPowerAll(0);
    }

    public void parkInStorage(){
        int TURN_TICKS = 175;
        int STRAFE2 = 90;
        int BACKWARDS = -700;
        int STRAFE = 70;
        double PARKING_POWER = .2;

        robot.autoTurn(TURN_POWER,TURN_TICKS);

        if (duckLevel == 2) {
            robot.autoStrafe(TURN_POWER, STRAFE2);
        } else {
            robot.autoStrafe(TURN_POWER, STRAFE);
        }
        sleep(SLEEPY);
        robot.driveForward(PARKING_POWER,BACKWARDS);
        sleep(SLEEPY);

    }

    public void identifyDuck(){
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        phoneCam.openCameraDevice();

        phoneCam.setPipeline(detector);
        phoneCam.startStreaming(352, 288, OpenCvCameraRotation.SIDEWAYS_LEFT);
        sleep(SLEEPY);
        duckLevel = detector.duckLevel();
    }

    public void initRobot(){
        robot.init();
        robot.initGrabberServo(G_SERVO_POSITION);
        robot.initCameraServo(BOB);


        /*initMenu.add(new MenuItem(0.0, "camera servo", 1.0, 0.0, 0.01 ));
        while (!isStarted()) {
            initMenu.update();
            initMenu.display();
        }
        robot.initCameraServo(initMenu.get(0));*/
    }

}