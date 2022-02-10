package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

public class VisionObject {
    LinearOpMode parent;
    VuforiaLocalizer vuforia = null;
    OpenCvCamera phoneCam;
    private DuckDetector detector = new DuckDetector();
    private int[] viewportContainerIds = null;
    private VuforiaLocalizer.Parameters parameters = null;
    private static String KEY = "AS5UxdP/////AAABmZv/KolYbkR8t/E1p/1N2dZifB38Q6w246S+wdKgUHvMduk79gG/5YxVVCYH/vKImXzh4IDRLARYXOOZOr66s/yrfEl56XMShywG/YnHi2xef8sBx0hG6GQFVmYCtf6BzVsiOR8llrFrn03ZrgysAFZZIFnwKyYGH31rqrhlIYU0W0uRCoeenefItA5c/7hlRRXgl+cPIIFc1LG3T19Y7j1K201S0rZAIL+B5fmso8WXT4BmRIirVXhaqGhFVyQlwSX3Z45iNgNvDW+rVF71KRaMwqq8A6ap3rYllr3MAB4w1avggu687SV9Z540feYIJ8HCHuU2M41vLWj7F/qBvaQ2V7u6ImkWBdiuvAVKn6fB";
    private VuforiaTrackables targets = null;
    private WebcamName webcamName = null;
    private boolean targetVisible = false;
    private OpenGLMatrix lastLocation = null;
    int level = 3;


    public VisionObject(LinearOpMode p) {
        parent = p;
    }

    public void initVuforia(){
        int cameraMonitorViewId = parent.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", parent.hardwareMap.appContext.getPackageName());
        viewportContainerIds = OpenCvCameraFactory.getInstance().splitLayoutForMultipleViewports(cameraMonitorViewId, 2, OpenCvCameraFactory.ViewportSplitMethod.VERTICALLY);
        parameters = new VuforiaLocalizer.Parameters(viewportContainerIds[0]);
        parameters.vuforiaLicenseKey = KEY;
        parameters.cameraDirection   = VuforiaLocalizer.CameraDirection.BACK;
        parent.telemetry.addLine("Set Cam Direction");
        parent.telemetry.update();
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
        parent.telemetry.addLine("Done");
        parent.telemetry.update();
    }

    public void createPassThrough() {
        phoneCam = OpenCvCameraFactory.getInstance().createVuforiaPassthrough(vuforia, parameters, viewportContainerIds[1]);
        phoneCam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            public void onOpened(){
                phoneCam.setViewportRenderer(OpenCvCamera.ViewportRenderer.GPU_ACCELERATED);
                phoneCam.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW);


                parent.telemetry.addLine("Before Pipeline");
                parent.telemetry.update();
                parent.sleep(1000);


                phoneCam.setPipeline(detector);

                parent.telemetry.addLine("After Pipeline");
                parent.telemetry.update();
                parent.sleep(1000);

                phoneCam.startStreaming(352, 288, OpenCvCameraRotation.SIDEWAYS_LEFT);
            }
            public void onError(int errorCode){
                parent.telemetry.addLine("No Cam Opened");
                parent.telemetry.update();
            }
        });
    }

    public int duckLevel(){
        level = detector.duckLevel();
        return level;
    }

    }