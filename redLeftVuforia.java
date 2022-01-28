package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
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
    VisionObject vision = new VisionObject();
    //VuforiaLocalizer vuforia = null;
    //private OpenCvInternalCamera phoneCam;
    private DuckDetector detector = new DuckDetector();
    final double BOB = 0;
    int duckLevel = 3;
    final double DRIVEPOWER = .4;
    int FORWARDTICKS = 80;
    int STRAFETICKS = 10;
    long SLEEPYTIME = 3000;
    final double TURNPOWER =.2;
    final double INITGRABBERSERVOPOSITION = 1;
    /*private static String key = "AS5UxdP/////AAABmZv/KolYbkR8t/E1p/1N2dZifB38Q6w246S+wdKgUHvMduk79gG/5YxVVCYH/vKImXzh4IDRLARYXOOZOr66s/yrfEl56XMShywG/YnHi2xef8sBx0hG6GQFVmYCtf6BzVsiOR8llrFrn03ZrgysAFZZIFnwKyYGH31rqrhlIYU0W0uRCoeenefItA5c/7hlRRXgl+cPIIFc1LG3T19Y7j1K201S0rZAIL+B5fmso8WXT4BmRIirVXhaqGhFVyQlwSX3Z45iNgNvDW+rVF71KRaMwqq8A6ap3rYllr3MAB4w1avggu687SV9Z540feYIJ8HCHuU2M41vLWj7F/qBvaQ2V7u6ImkWBdiuvAVKn6fB";
    private VuforiaTrackables targets = null;
    private WebcamName webcamName = null;
    private boolean targetVisible = false;
    private OpenGLMatrix lastLocation = null;
*/
    //robot must start with black line on duck spinner brace parallel to the metal part of the carousel with three fingers in between the duck spinner and the carousel

    public void runOpMode() {
        initRobot();
        vision.initVuforia();
        vision.identifyDuck();
        waitForStart();
        duckSpinnerDrive();
        driveToMeasureSpot();
        robot.rotateToSweetSpot();
        /*phoneCam.stopStreaming();
        phoneCam.closeCameraDevice();
*/
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


/*
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
    }*/

    public void initRobot(){
        robot.init();
        robot.initGrabberServo(INITGRABBERSERVOPOSITION);
        robot.initCameraServo(BOB);
    }
/*
    public void initVuforia() {
        //OpenGLMatrix targetPose = null;
        webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        int[] viewportContainerIds = OpenCvCameraFactory.getInstance().splitLayoutForMultipleViewports(cameraMonitorViewId, 2, OpenCvCameraFactory.ViewportSplitMethod.VERTICALLY);
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = key;
        parameters.cameraName = webcamName;
        parameters.useExtendedTracking = false;
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
        targets = this.vuforia.loadTrackablesFromAsset("FreightFrenzy");


        targetVisible = false;
        identifyTarget(0, "Blue Storage");
        identifyTarget(1, "Blue Alliance Wall");
        identifyTarget(2, "Red Storage");
        identifyTarget(3, "Red Alliance Wall");
        targets.activate();
    }

    void identifyTarget(int targetIndex, String targetName) {
        VuforiaTrackable aTarget = targets.get(targetIndex);
        aTarget.setName(targetName);
    }*/
}