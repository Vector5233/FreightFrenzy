package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous (name = "redRight", group = "GROUP_NAME")

public class redRight extends LinearOpMode {
    virtualBotObject robot = new virtualBotObject(this);
    private OpenCvInternalCamera phoneCam;
    private DuckDetector detector = new DuckDetector();
    final double REDRIGHT = .2;
    int duckLevel = 3;
    final double INITGRABBERSERVOPOSITION = 1;
    final double POWER2 = .2;

    public void runOpMode(){
        initRobot();
        waitForStart();
        robot.initGrabberServo(0);
        parkInWarehouse();

    }

    public void initRobot(){
        robot.init();
        robot.initGrabberServo(INITGRABBERSERVOPOSITION);
        robot.initCameraServo(REDRIGHT);
    }
    public void duckIdentifier(){
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        phoneCam.openCameraDevice();
        phoneCam.setPipeline(detector);
        phoneCam.startStreaming(352, 288, OpenCvCameraRotation.SIDEWAYS_LEFT);
        sleep(3000);
        duckLevel = detector.duckLevel();
    }
    public void turnToHub(){

    }

    public void driveToHub(){

    }

    public void parkInWarehouse(){
       double TURNPOWER = .5;
       double DEGREES = 90;
       int  POWER = 1;
       int DRIVETICKS = 700;
       int DRIVE = 20;

       robot.driveForward(TURNPOWER, DRIVE);
       robot.autoTurnDegrees(TURNPOWER, DEGREES);
       robot.driveForward(POWER, DRIVETICKS);
    }
}

