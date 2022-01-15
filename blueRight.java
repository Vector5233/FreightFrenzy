package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name ="blueRight", group = "GROUP_NAME")

public class blueRight extends LinearOpMode {
    virtualBotObject robot = new virtualBotObject(this);
    private OpenCvInternalCamera phoneCam;
    private DuckDetector detector = new DuckDetector();
    final double BLUERIGHT = .6;
    int duckLevel = 3;
    final double INITGRABBERSERVOPOSITION = 1;
    final double POWER2 = .2;


    //robot must start with black line on duck spinner brace parallel to the metal part of the carousel with three fingers in between the duck spinner and the carousel
    public void runOpMode() {
        initBlueRight();
        identifyDuck();
        waitForStart();
        driveToDuckSpinner();
        driveToShippingHub();
        robot.deliverBlock(duckLevel);
        parkInBox();
    }

    public void identifyDuck() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        phoneCam.openCameraDevice();
        phoneCam.setPipeline(detector);
        phoneCam.startStreaming(352, 288, OpenCvCameraRotation.SIDEWAYS_LEFT);
        sleep(3000);
        duckLevel = detector.duckLevel();
    }

    public void driveToDuckSpinner(){
        final double POWER3 = .3;
        int DRIVETICKS = 90;
        int STRAFETICKS = -25;
        long SLEEP = 2000;
        telemetry.addData("Duck Level:", duckLevel);
        telemetry.update();
        robot.turnOnDuckSpinner();
        robot.initGrabberServo(0);
        robot.driveForward(POWER3, DRIVETICKS);
        robot.autoStrafe(POWER3, STRAFETICKS);
        sleep(SLEEP);
        robot.turnOffDuckSpinner();
    }

    public void driveToShippingHub(){
        long SLEEP = 100;
        int TURNTICKS = 600;
        int DRIVETICKS = 475;
        int STRAFETICKS = 150;

        robot.autoStrafe(POWER2, STRAFETICKS);
        robot.autoTurn(POWER2, -TURNTICKS);
        sleep(SLEEP);
        robot.driveForward(POWER2, DRIVETICKS);
        sleep(SLEEP);
    }

    public void parkInBox(){
        long ENDSLEEP = 6000;
        int RIGHTTURN = -175;
        int DRIVEFORWARD = -650;
        robot.autoTurn(POWER2, RIGHTTURN);
        robot.driveForward(POWER2, DRIVEFORWARD);
        robot.initGrabberServo(INITGRABBERSERVOPOSITION);
        sleep(ENDSLEEP);
    }

    public void initBlueRight (){
        robot.init();
        robot.initGrabberServo(INITGRABBERSERVOPOSITION);
        robot.initCameraServo(BLUERIGHT);
    }
}