package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous (name = "blueLeft", group = "blue", preselectTeleOp = "Gru")

public class blueLeft extends LinearOpMode {
    virtualBotObject robot = new virtualBotObject(this);
    private OpenCvInternalCamera phoneCam;
    private duckID2 detector = new duckID2("blue");
    final double BLUE_LEFT = .69;
    int duckLevel = 3;
    final double G_SERVO_POSITION = 0;
    final double POWER2 = .2;

    //does not read level one properly, black bar may be interfering. Either check put a different value or
    // add an if statement to override

    public void runOpMode() {
        initRobot();
        duckIdentifier();
        waitForStart();
        strafeOut();
        driveToHub();
        robot.deliverBlock(duckLevel);
        parkInWarehouse();
    }
    public void initRobot(){
        robot.init();
        robot.initCameraServo(BLUE_LEFT);
    }

    public void strafeOut(){
        int STRAFE_POWER = 1;
        int STRAFE_TICKS = 120;
        long SLEEP = 1000;
        int TURN_DEGREES = -75;
        robot.initGrabberServo(G_SERVO_POSITION);
        robot.autoStrafe6(STRAFE_POWER, STRAFE_TICKS);
        sleep(SLEEP);
        robot.autoTurnDegrees(.2, TURN_DEGREES);
        sleep(SLEEP);
        robot.setPowerAll(0);
    }

    public void driveToHub(){
            double DRIVE_POWER =.2;
            int DRIVE_TICKS = 180;
            long SLEEP = 100;
            robot.driveForward(DRIVE_POWER, DRIVE_TICKS);
            robot.setPowerAll(0);
            sleep(SLEEP);

    }

    public void parkInWarehouse(){
        double DRIVE_POWER = .2;
        double PARK_POWER = .5;
        int TURN = -115;
        int DRIVE = 1200;
        robot.autoTurnDegrees(DRIVE_POWER, TURN);
        robot.driveForward(PARK_POWER, DRIVE);
        robot.setPowerAll(0);
        robot.autoStrafe6(DRIVE_POWER,50);
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
