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
    final double BLUE_LEFT = .7;
    int duckLevel = 3;
    final double G_SERVO_POSITION = 1;
    final double POWER2 = .2;


    public void runOpMode() {
        robot.init();
        robot.initGrabberServo(G_SERVO_POSITION);
        robot.initCameraServo(BLUE_LEFT);
        duckIdentifier();
        waitForStart();
        driveToHub();
        robot.deliverBlock(duckLevel);

        parkInWarehouse();

    }

    public void driveToHub(){
        int TURN_TO_HUB = -290;
        int DRIVE_TO_HUB = 150;
        robot.autoStrafe(.3,-40);
        robot.autoTurn(POWER2, TURN_TO_HUB);
        robot.driveForward(POWER2, DRIVE_TO_HUB);
    }

    public void parkInWarehouse(){
        double TURN_POWER = .5;
        double DEGREES = 90;
        int  POWER = 1;
        int DRIVE_TICKS = 850;
        int DRIVE = 100;

        robot.driveForward(TURN_POWER, DRIVE);
        robot.autoTurnDegrees(TURN_POWER, -DEGREES);
        robot.driveForward(POWER, DRIVE_TICKS);
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
