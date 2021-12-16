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
        duckIdentifier();
        waitForStart();

        telemetry.addData("Duck Level:", duckLevel);
        telemetry.update();
        robot.initGrabberServo(0);
        driveToHub();
        robot.deliverBlock(duckLevel);
        parkInWarehouse();
    }

    public void driveToHub(){
        int TURNTOHUB = -290;
        int DRIVETOHUB = 150;
        robot.autoTurn(POWER2, TURNTOHUB);
        robot.driveForward(POWER2, DRIVETOHUB);
    }

    public void parkInWarehouse(){
        long SLEEPYTIME = 6000;
        int TURNTOW = 280;
        int DRIVETOW = 600;
        robot.autoTurn(POWER2,TURNTOW);
        robot.driveForward(POWER2, DRIVETOW);
        sleep(SLEEPYTIME);
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
