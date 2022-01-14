package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

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
import org.openftc.easyopencv.OpenCvCameraFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongUnaryOperator;

class virtualBotObject {

    DcMotor frontLeft, frontRight, backLeft, backRight, leftDuckSpinner, rightDuckSpinner, freightLift;
    Servo grabberServo, bucketServo, cameraServo;
    LinearOpMode parent;

    final double DUCKSPINNERPOWER = .5;
    final double LIFTPOWER = 1;
    final double SAFETYBUCKET = 1;
    final double BUCKETCOLLECT = .8;
    final double BUCKETDUMP = .7;
    final double GRABBERSERVO = 0;
    final double INITGRABBERSERVO = .6;
    final double REPLACECONSTANT = 0;
    //int[] ticksForLevels = {0, 2355, 3485, 4560};
    //testing ticksForLevels needed
    int[] ticksForLevels = {0, 4812, 6255, 8500};
    private static String key = "AS5UxdP/////AAABmZv/KolYbkR8t/E1p/1N2dZifB38Q6w246S+wdKgUHvMduk79gG/5YxVVCYH/vKImXzh4IDRLARYXOOZOr66s/yrfEl56XMShywG/YnHi2xef8sBx0hG6GQFVmYCtf6BzVsiOR8llrFrn03ZrgysAFZZIFnwKyYGH31rqrhlIYU0W0uRCoeenefItA5c/7hlRRXgl+cPIIFc1LG3T19Y7j1K201S0rZAIL+B5fmso8WXT4BmRIirVXhaqGhFVyQlwSX3Z45iNgNvDW+rVF71KRaMwqq8A6ap3rYllr3MAB4w1avggu687SV9Z540feYIJ8HCHuU2M41vLWj7F/qBvaQ2V7u6ImkWBdiuvAVKn6fB";
    private VuforiaLocalizer vuforia = null;
    private VuforiaTrackables targets = null;
    private WebcamName webcamName = null;
    private boolean targetVisible = false;
    private OpenGLMatrix lastLocation = null;


    public virtualBotObject(LinearOpMode p) {
        parent = p;
    }

    public void init() {
        backLeft = parent.hardwareMap.get(DcMotorEx.class, "backLeft");
        backRight = (DcMotorEx) parent.hardwareMap.dcMotor.get("backRight");
        frontLeft = parent.hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = (DcMotorEx) parent.hardwareMap.dcMotor.get("frontRight");
        leftDuckSpinner = (DcMotorEx) parent.hardwareMap.dcMotor.get("leftDuckSpinner");
        rightDuckSpinner = (DcMotorEx) parent.hardwareMap.dcMotor.get("rightDuckSpinner");
        freightLift = (DcMotorEx) parent.hardwareMap.dcMotor.get("freightLift");
        grabberServo = parent.hardwareMap.servo.get("grabberServo");
        cameraServo = parent.hardwareMap.servo.get("cameraServo");
        bucketServo = parent.hardwareMap.servo.get("bucketServo");
        bucketServo.setPosition(SAFETYBUCKET);


        frontRight.setDirection(DcMotor.Direction.FORWARD);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        freightLift.setDirection(DcMotorSimple.Direction.REVERSE);

        //Test of GitHub
        //test of mac connection to github
        // test of files 3
    }

    //Turns on the duck spinners
    public void turnOnDuckSpinner() {
        leftDuckSpinner.setPower(-DUCKSPINNERPOWER);
        rightDuckSpinner.setPower(DUCKSPINNERPOWER);
    }

    //Turns off the duck spinners
    public void turnOffDuckSpinner() {
        leftDuckSpinner.setPower(0);
        rightDuckSpinner.setPower(0);
    }

    public void initCameraServo(double CAMERAPOSITION) {
        cameraServo.setPosition(CAMERAPOSITION);
        parent.sleep(300);
    }

    //Moves lift to specific level specified by int
    public void turnOnLift(int level) {
        freightLift.setTargetPosition(ticksForLevels[level]);
        freightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        freightLift.setPower(LIFTPOWER);
        while (freightLift.isBusy() && parent.opModeIsActive()) {
            assert true;
        }
    }

    // Turns off the lift
    public void turnOffLift() {
        freightLift.setPower(0);
    }

    public void lowerLift() {
        freightLift.setTargetPosition(0);
        bucketServo.setPosition(SAFETYBUCKET);
        parent.sleep(300);
        freightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        freightLift.setPower(LIFTPOWER);
    }

    public void initGrabberServo(final double VALUE) {
        grabberServo.setPosition(VALUE);
    }

    //Makes the robot drive forward specified by int ticks
    public void driveForward(double power, int ticks) {
        setModeAll(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setTargetPosition(ticks);
        backRight.setTargetPosition(ticks);
        frontLeft.setTargetPosition(ticks);
        frontRight.setTargetPosition(ticks);
        setModeAll(DcMotor.RunMode.RUN_TO_POSITION);
        setPowerAll(power);

        while ((frontLeft.isBusy() || backRight.isBusy()) && parent.opModeIsActive()) {
            assert true;
        }
    }

    //Sets the modes to specified mode
    public void setModeAll(DcMotor.RunMode mode) {
        backLeft.setMode(mode);
        backRight.setMode(mode);
        frontLeft.setMode(mode);
        frontRight.setMode(mode);
    }

    //Sets power to specified power
    public void setPowerAll(double power) {
        backLeft.setPower(power);
        backRight.setPower(power);
        frontLeft.setPower(power);
        frontRight.setPower(power);
    }

    //Strafes to location specified by int ticks
    public void autoStrafe(double power, int ticks) {
        setModeAll(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setTargetPosition(ticks);
        frontLeft.setTargetPosition(-ticks);
        backRight.setTargetPosition(-ticks);
        frontRight.setTargetPosition(ticks);
        setModeAll(DcMotor.RunMode.RUN_TO_POSITION);
        setPowerAll(power);

        while ((frontLeft.isBusy() || backRight.isBusy()) && parent.opModeIsActive()) {
        }
    }

    //Turns to location specified by int ticks
    public void autoTurn(double power, int ticks) {
        setModeAll(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setTargetPosition(ticks);
        frontLeft.setTargetPosition(ticks);
        backRight.setTargetPosition(-ticks);
        frontRight.setTargetPosition(-ticks);
        setModeAll(DcMotor.RunMode.RUN_TO_POSITION);
        setPowerAll(power);

        while ((frontLeft.isBusy() || backRight.isBusy()) && parent.opModeIsActive()) {
            // assert True;
            reportAllDriveMotors();
            parent.telemetry.update();
        }
    }

    //Delivers the block after setting lift to specified location
    public void deliverBlock(int level) {
        turnOnLift(level);
        bucketServo.setPosition(BUCKETDUMP);
        parent.sleep(2000);
        lowerLift();

    }


    public void initVuforia() {
        //OpenGLMatrix targetPose = null;
        webcamName = parent.hardwareMap.get(WebcamName.class, "Webcam 1");
        int cameraMonitorViewId = parent.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", parent.hardwareMap.appContext.getPackageName());
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

    public void autoTurnDegrees(double power, double degrees) {
        final double ticksPerDegrees = 4.5;
        int ticks = (int) (degrees * ticksPerDegrees);
        autoTurn(power, ticks);
    }

    void identifyTarget(int targetIndex, String targetName) {
        VuforiaTrackable aTarget = targets.get(targetIndex);
        aTarget.setName(targetName);
    }
    //for (VuforiaTrackable trackable : allTrackables) {
    //  ((VuforiaTrackableDefaultListener) trackable.getListener()).setCameraLocationOnRobot(parameters.cameraName, cameraLocationOnRobot);

    void reportAllDriveMotors() {
        // caller must update
        parent.telemetry.addLine(String.format("Front Left: %d    Front Right: %d", frontLeft.getCurrentPosition(), frontRight.getCurrentPosition()));
        parent.telemetry.addLine(String.format("Back Left: %d     Back Right: %d", backLeft.getCurrentPosition(), backRight.getCurrentPosition()));
    }

    //needs testing
    public void rotateToSweetSpot() {
        final double currentAngleOne = findCurrentAngle();
        final double correctAngle = -.5;
        int rotateOne = (int) (currentAngleOne - correctAngle);
        parent.telemetry.addData("Rotate by ", rotateOne);
        parent.telemetry.update();
        autoTurnDegrees (1, rotateOne);
        parent.sleep(500);
        //second angle calculation
        final double currentAngleTwo = findCurrentAngle();
        int rotateTwo = (int) (currentAngleTwo - correctAngle);
        parent.telemetry.addData("Second rotate by ", rotateTwo);
        parent.telemetry.update();
        autoTurnDegrees (1, rotateTwo);
        parent.sleep(500);
        //third angle calculation
        final double currentAngleThree = findCurrentAngle();
        int rotateThree = (int) (currentAngleThree - correctAngle);
        parent.telemetry.addData("Third rotate by ", rotateThree);
        parent.telemetry.update();
        autoTurnDegrees (1, rotateThree);
        parent.sleep(500);
        //second angle calculation
        final double currentAngleFour = findCurrentAngle();
        int rotateFour = (int) (currentAngleFour - correctAngle);
        parent.telemetry.addData("Fourth rotate by ", rotateFour);
        parent.telemetry.update();
        autoTurnDegrees (1, rotateFour);
        parent.sleep(500);
        //third angle calculation
        final double currentAngleFive = findCurrentAngle();
        int rotateFive = (int) (currentAngleFive - correctAngle);
        parent.telemetry.addData("Fifth rotate by ", rotateFive);
        parent.telemetry.update();
        autoTurnDegrees (1, rotateFive);
        parent.sleep(500);
        parent.telemetry.addLine("Task Completed");
        parent.telemetry.update();
    }

    public double findCurrentAngle() {
        OpenGLMatrix pose = null;
        //caller must call init vuforia

        //parent.sleep(3000);

        parent.telemetry.addData("# of targets: ",targets.size());
        parent.telemetry.update();
        parent.sleep(2000);

        for (VuforiaTrackable target : targets) {
            if (((VuforiaTrackableDefaultListener) target.getListener()).isVisible()) {
                pose = ((VuforiaTrackableDefaultListener) target.getListener()).getVuforiaCameraFromTarget();
                parent.telemetry.addData("Target FOund: ", target.getName());
                targetVisible = true;
                parent.telemetry.update();
                    break;
            }
        }

        if (targetVisible == false) {
            parent.telemetry.addLine("No Target Found");
            parent.telemetry.update();
            return REPLACECONSTANT;
        }

        parent.sleep(1000);
        //VuforiaTrackable pictureTarget = targets.get(0);
        parent.telemetry.addLine("Step One Complete");
        parent.telemetry.update();
        //MatrixF pose = (MatrixF)((VuforiaTrackableDefaultListener)freightDuck.getListener()).getFtcCameraFromTarget();
        //OpenGLMatrix pose = ((VuforiaTrackableDefaultListener)pictureTarget.getListener()).getFtcCameraFromTarget();
        parent.telemetry.addLine("Step Two Complete");
        parent.telemetry.update();
        Orientation rot = Orientation.getOrientation(pose, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
        parent.telemetry.addLine("Step Three Complete");
        parent.telemetry.update();
        double rX = rot.firstAngle;
        double rY = rot.secondAngle;
        double rZ = rot.thirdAngle;
        parent.telemetry.addData("Rotation of X: ", rX);
        parent.telemetry.addData("Rotation of Y: ", rY);
        parent.telemetry.addData("Rotation of Z: ", rZ);
        parent.telemetry.update();
        parent.sleep(1000);
        return rot.thirdAngle;
    }
}