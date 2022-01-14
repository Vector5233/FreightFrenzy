package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name ="redLeftVuforia", group = "GROUP_NAME")

public class redLeftVuforia extends LinearOpMode {
    virtualBotObject robot = new virtualBotObject(this);
    private OpenCvInternalCamera phoneCam;
    private DuckDetector detector = new DuckDetector();
    final double BOB = 0;
    int duckLevel = 3;
    final double DRIVEPOWER = .4;
    int FORWARDTICKS = 80;
    int STRAFETICKS = 10;
    long SLEEPYTIME = 3000;
    final double TURNPOWER =.2;
    final double INITGRABBERSERVOPOSITION = 1;

    //robot must start with black line on duck spinner brace parallel to the metal part of the carousel with three fingers in between the duck spinner and the carousel

    public void runOpMode() {
        initRobot();
        robot.initVuforia();
        identifyDuck();
        waitForStart();
        duckSpinnerDrive();
        driveToMeasureSpot();
        phoneCam.stopStreaming();
        phoneCam.closeCameraDevice();
        robot.rotateToSweetSpot();

        //robot.deliverBlock(duckLevel);
    }

    public void duckSpinnerDrive(){
        telemetry.addData("Duck Level:", duckLevel);
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
        final double VUFORIA = .5;
        robot.driveForward(DRIVEPOWER, DRIVETICKS);
        robot.autoTurn(TURNPOWER, TICKTURN);
        sleep(HUBSLEEP);
        robot.driveForward(TURNPOWER, TICKFORWARD);
        robot.initCameraServo(VUFORIA);
        sleep(HUBSLEEP);
    }



    public void identifyDuck(){
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        phoneCam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                phoneCam.setPipeline(detector);
                phoneCam.startStreaming(352, 288, OpenCvCameraRotation.SIDEWAYS_LEFT);
            }

            @Override
            public void onError(int i) {

            }
        });

        sleep(SLEEPYTIME);
        duckLevel = detector.duckLevel();

    }

    public void initRobot(){
        robot.init();
        robot.initGrabberServo(INITGRABBERSERVOPOSITION);
        robot.initCameraServo(BOB);
    }
}