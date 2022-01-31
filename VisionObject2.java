package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

public class VisionObject2 extends LinearOpMode {

    VuforiaLocalizer vuforia = null;
    OpenCvCamera phoneCam;
    private DuckDetector detector = new DuckDetector();
    private static String key = "AS5UxdP/////AAABmZv/KolYbkR8t/E1p/1N2dZifB38Q6w246S+wdKgUHvMduk79gG/5YxVVCYH/vKImXzh4IDRLARYXOOZOr66s/yrfEl56XMShywG/YnHi2xef8sBx0hG6GQFVmYCtf6BzVsiOR8llrFrn03ZrgysAFZZIFnwKyYGH31rqrhlIYU0W0uRCoeenefItA5c/7hlRRXgl+cPIIFc1LG3T19Y7j1K201S0rZAIL+B5fmso8WXT4BmRIirVXhaqGhFVyQlwSX3Z45iNgNvDW+rVF71KRaMwqq8A6ap3rYllr3MAB4w1avggu687SV9Z540feYIJ8HCHuU2M41vLWj7F/qBvaQ2V7u6ImkWBdiuvAVKn6fB";
    private VuforiaTrackables targets = null;
    private WebcamName webcamName = null;
    private boolean targetVisible = false;
    private OpenGLMatrix lastLocation = null;
    int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
    int[] viewportContainerIds = OpenCvCameraFactory.getInstance().splitLayoutForMultipleViewports(cameraMonitorViewId, 2, OpenCvCameraFactory.ViewportSplitMethod.VERTICALLY);
    VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
    int duckLevel = 3;
    long SLEEPYTIME = 3000;

    public void runOpMode(){
        initVuforia();
        initBoth();
    }
    public void identifyDuck(){
        /*public void onOpened() {
                phoneCam.setPipeline(detector);
                phoneCam.startStreaming(352, 288, OpenCvCameraRotation.SIDEWAYS_LEFT);
            }

            @Override
            public void onError(int i) {

            }*/

        sleep(SLEEPYTIME);
        duckLevel = detector.duckLevel();
        }

    public void initVuforia() {
        //OpenGLMatrix targetPose = null;
        webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");
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

    public void initBoth(){
       initVuforia();
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().createVuforiaPassthrough(vuforia, parameters, viewportContainerIds[1]);


        }

    void identifyTarget(int targetIndex, String targetName) {
        VuforiaTrackable aTarget = targets.get(targetIndex);
        aTarget.setName(targetName);
    }



}
