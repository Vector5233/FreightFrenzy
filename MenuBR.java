package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@Autonomous(name="MenuBR", group="Experiments")
@Disabled
public class MenuBR extends LinearOpMode {
    virtualBotObject robot = new virtualBotObject(this);
    Menu menu = new Menu(this);
    private OpenCvInternalCamera phoneCam;
    private final DuckDetector detector = new DuckDetector("blue");
    final double BOB = .72;
    int TICKS = 200;
    int duckLevel = 3;
    final double DRIVE_POWER = .4;
    int FORWARD_TICKS = 75;
    long SLEEPY = 3000;
    final double TURN_POWER =.2;
    final double G_SERVO_POSITION = .5;
    public void runOpMode(){
        initRobot();
        identifyDuck();
        waitForStart();
        duckSpinnerDrive();
    }

    public void initRobot(){
        robot.init();
        robot.initCameraServo(BOB);
        menu.add(new MenuItem(600, "ticks forward",850,500,20));

        while (!isStarted()) {
            menu.update();
            menu.display();
        }
        TICKS = (int) menu.get(0);
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
    public void duckSpinnerDrive(){
        int STRAFE = -25;
        double STRAFE_POWER = .9;
        telemetry.addData("Duck Level:", duckLevel);
        telemetry.update();
        robot.initGrabberServo(G_SERVO_POSITION);
        robot.turnOnDuckSpinner();
        robot.driveForward(DRIVE_POWER, FORWARD_TICKS);
        robot.autoStrafe6(STRAFE_POWER, STRAFE);
        sleep(SLEEPY);
        robot.turnOffDuckSpinner();
    }


    public void driveToHub(){
        int STRAFE_POWER = 1;
        int STRAFE_TICKS = 150;
        long SLEEP = 100;
        int TURN_LEFT = -575;
        robot.autoStrafe6(STRAFE_POWER, STRAFE_TICKS);
        robot.autoTurn(TURN_POWER, TURN_LEFT);
        sleep(SLEEP);
        if (duckLevel == 2) {
            robot.driveForward(TURN_POWER, TICKS);
        } else {
            robot.driveForward(TURN_POWER, TICKS);
        }
        sleep(SLEEP);
        robot.setPowerAll(0);
    }

}
