package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

import static java.lang.Math.abs;

import android.util.Log;

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
import java.util.Locale;
import java.util.function.LongUnaryOperator;

class virtualBotObject {

    DcMotor frontLeft, frontRight, backLeft, backRight, leftDuckSpinner, rightDuckSpinner, freightLift;
    Servo grabberServo, bucketServo, cameraServo;
    LinearOpMode parent;

    final double DUCKSPINNERPOWER = .5;
    final double LIFTPOWER = 1;
    final double SAFETYBUCKET = 1;
    final double BUCKETDUMP = .7;
    int[] ticksForLevels = {0, 680, 1190, 1840};
    private boolean targetVisible = false;
    private int motorTolerance = 30;
    private double motorAdjustment = 0.30;

    private double LAMBDA = 0.05;

    public virtualBotObject(LinearOpMode p) {
        parent = p;
    }
    VisionObject vision = new VisionObject(parent);

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

        while ((frontLeft.isBusy()) && parent.opModeIsActive()) {
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
    //needs testing

    public void rotateToSweetSpot() {
        final double currentAngleOne = vision.findCurrentAngle();
        final double correctAngle = -.5;
        int rotateOne = (int) (currentAngleOne - correctAngle);
        parent.telemetry.addData("Rotate by ", rotateOne);
        parent.telemetry.update();
        autoTurnDegrees (1, rotateOne);
        parent.sleep(500);
        //second angle calculation
        final double currentAngleTwo = vision.findCurrentAngle();
        int rotateTwo = (int) (currentAngleTwo - correctAngle);
        parent.telemetry.addData("Second rotate by ", rotateTwo);
        parent.telemetry.update();
        autoTurnDegrees (1, rotateTwo);
        parent.sleep(500);
        //third angle calculation
        final double currentAngleThree = vision.findCurrentAngle();
        int rotateThree = (int) (currentAngleThree - correctAngle);
        parent.telemetry.addData("Third rotate by ", rotateThree);
        parent.telemetry.update();
        autoTurnDegrees (1, rotateThree);
        parent.sleep(500);
        //second angle calculation
        final double currentAngleFour = vision.findCurrentAngle();
        int rotateFour = (int) (currentAngleFour - correctAngle);
        parent.telemetry.addData("Fourth rotate by ", rotateFour);
        parent.telemetry.update();
        autoTurnDegrees (1, rotateFour);
        parent.sleep(500);
        //third angle calculation
        final double currentAngleFive = vision.findCurrentAngle();
        int rotateFive = (int) (currentAngleFive - correctAngle);
        parent.telemetry.addData("Fifth rotate by ", rotateFive);
        parent.telemetry.update();
        autoTurnDegrees (1, rotateFive);
        parent.sleep(500);
        parent.telemetry.addLine("Task Completed");
        parent.telemetry.update();
    }

    //Strafes to location specified by int ticks
    //CHANGE SIGNS
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
        setModeAll(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void autoStrafe2(double power, int ticks) {
        setModeAll(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setTargetPosition(ticks);
        frontLeft.setTargetPosition(-ticks);
        backRight.setTargetPosition(-ticks);
        frontRight.setTargetPosition(ticks);
        setModeAll(DcMotor.RunMode.RUN_TO_POSITION);
        setPowerAll(power);
        frontRight.setPower(power*1.50); // total hack to compensate for frontRight dragging

        while (frontLeft.isBusy() && parent.opModeIsActive()) {

        }
        setModeAll(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void autoStrafe3(double power, int ticks) {
        DcMotorEx BL=(DcMotorEx) backLeft;
        DcMotorEx BR=(DcMotorEx) backRight;
        DcMotorEx FL=(DcMotorEx) frontLeft;
        DcMotorEx FR=(DcMotorEx) frontRight;

        setModeAll(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FL.setTargetPosition(-ticks);
        FL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        FL.setPower(power);

        while (FL.isBusy() && parent.opModeIsActive()) {
            FR.setVelocity(-FL.getVelocity());
            BR.setVelocity(FL.getVelocity());
            BL.setVelocity(-FL.getVelocity());
        }

        setModeAll(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void autoStrafe4(double power, int ticks) {
        setModeAll(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setTargetPosition(-ticks);
        backLeft.setTargetPosition(ticks);
        backRight.setTargetPosition(-ticks);
        frontRight.setTargetPosition(ticks);
        setModeAll(DcMotor.RunMode.RUN_TO_POSITION);
        setPowerAll(power);

        while(frontLeft.isBusy() && parent.opModeIsActive()) {
            // Adjust power if any of the motors is substantially ahead or behind the average
            frontLeft.setPower(powerAdjust4(frontLeft, power));
            frontRight.setPower(powerAdjust4(frontRight, power));
            backLeft.setPower(powerAdjust4(backLeft, power));
            backRight.setPower(powerAdjust4(backRight, power));
            reportAllDriveMotors();
            parent.telemetry.update();
        }
        setPowerAll(0);
    }

    public void loggingAutoStrafe4(double power, int ticks) {
        setModeAll(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setTargetPosition(-ticks);
        backLeft.setTargetPosition(ticks);
        backRight.setTargetPosition(-ticks);
        frontRight.setTargetPosition(ticks);
        setModeAll(DcMotor.RunMode.RUN_TO_POSITION);
        setPowerAll(power);

        while(frontLeft.isBusy() && parent.opModeIsActive()) {
            // Adjust power if any of the motors is substantially ahead or behind the average
            frontLeft.setPower(powerAdjust4(frontLeft, power));
            frontRight.setPower(powerAdjust4(frontRight, power));
            backLeft.setPower(powerAdjust4(backLeft, power));
            backRight.setPower(powerAdjust4(backRight, power));
            reportAllDriveMotors();
            logAllDriveMotors();

            parent.telemetry.update();
        }
        setPowerAll(0);
    }
    public void setMotorTolerance(int tol) {
        this.motorTolerance = tol;
    }

    public void setMotorAdjustment(double adj) {
        this.motorAdjustment = adj;
    }

    public double powerAdjust4(DcMotor motor, double basePower) {
        int averageDistance = (abs(frontLeft.getCurrentPosition()) + abs(frontRight.getCurrentPosition())
        + abs(backLeft.getCurrentPosition()) + abs(backRight.getCurrentPosition()))/4;

        if (abs(motor.getCurrentPosition()) - averageDistance > motorTolerance) {
            return basePower * (1 - motorAdjustment);
        }
        else if (abs(motor.getCurrentPosition()) - averageDistance < -motorTolerance) {
            return basePower * (1 + motorAdjustment);

        }
        else return basePower;
    }

    public void autoStrafe5(double power, int ticks) {
        setModeAll(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setTargetPosition(ticks);
        frontLeft.setTargetPosition(-ticks);
        backRight.setTargetPosition(-ticks);
        frontRight.setTargetPosition(ticks);
        setModeAll(DcMotor.RunMode.RUN_TO_POSITION);
        setPowerAll(power);
        frontRight.setPower(power*1.25); // total hack to compensate for frontRight dragging
        backLeft.setPower(power*0.75);
        while (parent.opModeIsActive() && frontRight.isBusy()) {
            parent.sleep(10);
            reportAllDriveMotors();
            parent.telemetry.update();
        }
        setPowerAll(0);
    }

    public void autoStrafe6(double power, int ticks) {
        setModeAll(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setTargetPosition(ticks);
        frontLeft.setTargetPosition(-ticks);
        backRight.setTargetPosition(-ticks);
        frontRight.setTargetPosition(ticks);
        setModeAll(DcMotor.RunMode.RUN_TO_POSITION);
        setPowerAll(power);

        while (frontLeft.isBusy() && parent.opModeIsActive()) {
            frontLeft.setPower(powerAdjust6(frontLeft, power));
            frontRight.setPower(powerAdjust6(frontRight, power));
            backLeft.setPower(powerAdjust6(backLeft, power));
            backRight.setPower(powerAdjust6(backRight, power));
            reportAllDriveMotors();

            parent.telemetry.update();
        }

        setModeAll(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void setLambda(double val) {
        LAMBDA = val;
    }

    public double powerAdjust6(DcMotor motor, double basePower) {
        int averageDistance = (abs(frontLeft.getCurrentPosition()) + abs(frontRight.getCurrentPosition())
                + abs(backLeft.getCurrentPosition()) + abs(backRight.getCurrentPosition()))/4;
        return basePower * (1-LAMBDA*(abs(motor.getCurrentPosition()) - averageDistance));

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

    public void autoTurnDegrees(double power, double degrees) {
        final double ticksPerDegrees = 4.5;
        int ticks = (int) (degrees * ticksPerDegrees);
        autoTurn(power, ticks);
    }

    void reportAllDriveMotors() {
        // caller must update
        parent.telemetry.addLine(String.format(Locale.US, "Front Left: %d    Front Right: %d", frontLeft.getCurrentPosition(), frontRight.getCurrentPosition()));
        parent.telemetry.addLine(String.format(Locale.US, "Back Left: %d     Back Right: %d", backLeft.getCurrentPosition(), backRight.getCurrentPosition()));
    }

    void logAllDriveMotors() {
        Log.i("encoder: ", String.format("Front Left: %d    Front Right: %d    Back Left: %d    Back Right %d\n",
                frontLeft.getCurrentPosition(),
                frontRight.getCurrentPosition(),
                backLeft.getCurrentPosition(),
                backRight.getCurrentPosition()));
        Log.i("power: ", String.format("Front Left: %.2f    Front Right: %.2f    Back Left: %.2f    Back Right %.2f\n",
                frontLeft.getPower(),
                frontRight.getPower(),
                backLeft.getPower(),
                backRight.getPower()));
    }
}