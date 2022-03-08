package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

public class VisionObject {
    virtualBotObject parent;
    VuforiaLocalizer vuforia = null;
    OpenCvCamera phoneCam;
    private DuckDetector detector = new DuckDetector();
    private int[] viewportContainerIds = null;
    private VuforiaLocalizer.Parameters parameters = null;
    private final String KEY = "AS5UxdP/////AAABmZv/KolYbkR8t/E1p/1N2dZifB38Q6w246S+wdKgUHvMduk79gG/5YxVVCYH/vKImXzh4IDRLARYXOOZOr66s/yrfEl56XMShywG/YnHi2xef8sBx0hG6GQFVmYCtf6BzVsiOR8llrFrn03ZrgysAFZZIFnwKyYGH31rqrhlIYU0W0uRCoeenefItA5c/7hlRRXgl+cPIIFc1LG3T19Y7j1K201S0rZAIL+B5fmso8WXT4BmRIirVXhaqGhFVyQlwSX3Z45iNgNvDW+rVF71KRaMwqq8A6ap3rYllr3MAB4w1avggu687SV9Z540feYIJ8HCHuU2M41vLWj7F/qBvaQ2V7u6ImkWBdiuvAVKn6fB";
    public VuforiaTrackables targets = null;
    private WebcamName webcamName = null;
    final double REPLACECONSTANT = 0;
    private boolean targetVisible = false;
    private OpenGLMatrix lastLocation = null;
    int level = 3;


    public VisionObject(virtualBotObject p) {
        parent = p;
    }

    public void initVuforia(){
        int cameraMonitorViewId = parent.parent.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", parent.parent.hardwareMap.appContext.getPackageName());
        viewportContainerIds = OpenCvCameraFactory.getInstance().splitLayoutForMultipleViewports(cameraMonitorViewId, 2, OpenCvCameraFactory.ViewportSplitMethod.VERTICALLY);
        parameters = new VuforiaLocalizer.Parameters(viewportContainerIds[0]);
        parameters.vuforiaLicenseKey = KEY;
        parameters.cameraDirection   = VuforiaLocalizer.CameraDirection.BACK;
        parent.parent.telemetry.addLine("Set Cam Direction");
        parent.parent.telemetry.update();
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
        targets = this.vuforia.loadTrackablesFromAsset("FreightFrenzy");


        identifyTarget(0, "Blue Storage");
        identifyTarget(1, "Blue Alliance Wall");
        identifyTarget(2, "Red Storage");
        identifyTarget(3, "Red Alliance Wall");
        targets.activate();
        parent.parent.telemetry.addLine("After Trackables activated");
        parent.parent.telemetry.update();
    }

    void identifyTarget(int targetIndex, String targetName) {
        VuforiaTrackable aTarget = targets.get(targetIndex);
        aTarget.setName(targetName);
    }

    public void createPassThrough() {
        phoneCam = OpenCvCameraFactory.getInstance().createVuforiaPassthrough(vuforia, parameters, viewportContainerIds[1]);
        phoneCam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            public void onOpened(){
                phoneCam.setViewportRenderer(OpenCvCamera.ViewportRenderer.GPU_ACCELERATED);
                phoneCam.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW);

                phoneCam.setPipeline(detector);

                parent.parent.sleep(600);

                phoneCam.startStreaming(640, 480, OpenCvCameraRotation.SIDEWAYS_LEFT); // WHERE DID 352,288 COME FROM?
                parent.parent.sleep(600);
            }
            public void onError(int errorCode){
                parent.parent.telemetry.addLine("No Cam Opened");
                parent.parent.telemetry.update();
            }
        });
    }


    public double findCurrentAngle() {
        OpenGLMatrix pose = null;
        //caller must call init vuforia

        //parent.sleep(3000);

        for (VuforiaTrackable target : targets) {

            parent.parent.telemetry.addLine("BEFORE 'IF' IN VBO-FCA");
            parent.parent.telemetry.update();


            if (((VuforiaTrackableDefaultListener) target.getListener()).isVisible()) {
                pose = ((VuforiaTrackableDefaultListener) target.getListener()).getVuforiaCameraFromTarget();
                parent.parent.telemetry.addData("Target Found: ", target.getName());
                targetVisible = true;
                parent.parent.telemetry.update();
                break;
            }
        }

        if (!targetVisible) {
            parent.parent.telemetry.addLine("No Target Found");
            parent.parent.telemetry.update();
            return REPLACECONSTANT;
        }

        parent.parent.sleep(1000);
        parent.parent.telemetry.addLine("Step One Complete");
        parent.parent.telemetry.update();
        parent.parent.telemetry.addLine("Step Two Complete");
        parent.parent.telemetry.update();
        Orientation rot = Orientation.getOrientation(pose, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
        parent.parent.telemetry.addLine("Step Three Complete");
        parent.parent.telemetry.update();
        double rX = rot.firstAngle;
        double rY = rot.secondAngle;
        double rZ = rot.thirdAngle;
        parent.parent.telemetry.addData("Rotation of X: ", rX);
        parent.parent.telemetry.addData("Rotation of Y: ", rY);
        parent.parent.telemetry.addData("Rotation of Z: ", rZ);
        parent.parent.telemetry.update();
        parent.parent.sleep(1000);
        return rot.thirdAngle;
    }

    public void closePassthrough(){
        phoneCam.closeCameraDeviceAsync(new OpenCvCamera.AsyncCameraCloseListener() {
            @Override
            public void onClose() {
               phoneCam.stopStreaming();
            }
        });

    }

    public int getLevel(){
        level = detector.duckLevel();
        return level;
    }

    }