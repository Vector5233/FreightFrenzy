package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous (name = "blueLeft", group = "GROUP_NAME")

public class blueLeft extends LinearOpMode {
    virtualBotObject robot = new virtualBotObject(this);
    private OpenCvInternalCamera phoneCam;
    private DuckDetector detector = new DuckDetector();
    final double BLUELEFT = .7;
    int duckLevel = 3;
    final double INITGRABBERSERVOPOSITION = 1;
    final double POWER2 = .2;


    public void runOpMode() {
        robot.init();
        robot.initGrabberServo(INITGRABBERSERVOPOSITION);
        robot.initCameraServo(BLUELEFT);
        waitForStart();
        parkInWarehouse();

    }

    public void driveToHub(){
        int TURNTOHUB = -290;
        int DRIVETOHUB = 150;
        robot.autoTurn(POWER2, TURNTOHUB);
        robot.driveForward(POWER2, DRIVETOHUB);
    }

    public void parkInWarehouse(){
        double TURNPOWER = .5;
        double DEGREES = 90;
        int  POWER = 1;
        int DRIVETICKS = 850;
        int DRIVE = 100;

        robot.driveForward(TURNPOWER, DRIVE);
        robot.autoTurnDegrees(TURNPOWER, -DEGREES);
        robot.driveForward(POWER, DRIVETICKS);
        robot.initGrabberServo(0);
        sleep(3000);
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
}
