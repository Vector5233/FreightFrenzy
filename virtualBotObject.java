package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;
import android.util.Log;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import java.util.Locale;


class virtualBotObject {

    DcMotor frontLeft, frontRight, backLeft, backRight, leftDuckSpinner, rightDuckSpinner, freightLift, grabberMotor;
    Servo grabberServo, bucketServo, cameraServo;
    LinearOpMode parent;

    final double SPINNER = .5;
    final double LIFT = 1;
    final double SAFETY = 1;
    final double DUMP = .7;
    final double MOTOR = .5;
    int[] ticksForLevels = {0, 680, 1200, 1840};
    private int motorTolerance = 30;
    private double motorAdjustment = 0.30;

    private double LAMBDA = 0.05;

    public virtualBotObject(LinearOpMode p) {
        parent = p;
    }

    public void init() {
        backLeft = parent.hardwareMap.get(DcMotorEx.class, "backLeft");
        backRight = parent.hardwareMap.dcMotor.get("backRight");
        frontLeft = parent.hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = parent.hardwareMap.dcMotor.get("frontRight");
        leftDuckSpinner = parent.hardwareMap.dcMotor.get("leftDuckSpinner");
        rightDuckSpinner = parent.hardwareMap.dcMotor.get("rightDuckSpinner");
        freightLift = parent.hardwareMap.dcMotor.get("freightLift");
        grabberMotor = parent.hardwareMap.dcMotor.get("grabberMotor");
        grabberServo = parent.hardwareMap.servo.get("grabberServo");
        cameraServo = parent.hardwareMap.servo.get("cameraServo");
        bucketServo = parent.hardwareMap.servo.get("bucketServo");
        bucketServo.setPosition(SAFETY);


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
        leftDuckSpinner.setPower(-SPINNER);
        rightDuckSpinner.setPower(SPINNER);
    }

    public void turnOnGrabberMotor() {
        grabberMotor.setPower(MOTOR);
    }
    public void turnOffGrabberMotor() {
        grabberMotor.setPower(0);
    }

    //Turns off the duck spinners
    public void turnOffDuckSpinner() {
        leftDuckSpinner.setPower(0);
        rightDuckSpinner.setPower(0);
    }

    public void initCameraServo(double CAMERA_POSITION) {
        cameraServo.setPosition(CAMERA_POSITION);
        parent.sleep(300);
    }

    //Moves lift to specific level specified by int
    public void turnOnLift(int level) {
        freightLift.setTargetPosition(ticksForLevels[level]);
        freightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        freightLift.setPower(LIFT);
        while (freightLift.isBusy() && parent.opModeIsActive()) {
            assert true;
        }
    }

    public void lowerLift() {
        freightLift.setTargetPosition(0);
        bucketServo.setPosition(SAFETY);
        parent.sleep(300);
        freightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        freightLift.setPower(LIFT);
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
        bucketServo.setPosition(DUMP);
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