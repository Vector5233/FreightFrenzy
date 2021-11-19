package org.firstinspires.ftc.teamcode;

import android.hardware.Sensor;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.vuforia.CameraDevice;


@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Gru", group = "Red")

public class drivingTeleOp extends OpMode {
    DcMotor frontLeft, frontRight, backLeft, backRight, leftDuckSpinner, rightDuckSpinner, freightLift, grabberMotor;

    Servo grabberServo, cameraServo, bucketServo;

    DigitalChannel touchSensor;

    final double APPROACHSPEED = .3;
    final double DUCKSPINNERPOWER = .5;
    final double FREIGHTLIFTPOWER = 1;
    final double SAFETYBUCKET = 1;
    final double BUCKETCOLLECT = .83;
    final double BUCKETDUMP = .6;
    final double THRESHOLD = .1;
    final double GRABBERSPEED = 1;
    final double GRABBERSERVO = 0;
    int[] ticks = {0, 1211, 4964, 8075};
    final int levelZero = 0;
    final int firstLevel = 1;
    final int secondLevel = 2;
    final int thirdLevel = 3;
    final int manualLevel = -1;
    int level = (manualLevel);
    final double CAMERASERVO = 0;
    final double BUCKETSERVO = 1;
    final double MAXTICKS = 8200;
    final double HOLDINGPOWER = 0;
    final double SAFETICKS = 1528;

    //Define Servos, Motors, set values

    public void init() {
        backLeft = hardwareMap.dcMotor.get("backLeft");
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        backRight = hardwareMap.dcMotor.get("backRight");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        freightLift = hardwareMap.dcMotor.get("freightLift");
        freightLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        freightLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftDuckSpinner = hardwareMap.dcMotor.get("leftDuckSpinner");
        rightDuckSpinner = hardwareMap.dcMotor.get("rightDuckSpinner");
        grabberMotor = hardwareMap.dcMotor.get("grabberMotor");
        grabberServo = hardwareMap.servo.get("grabberServo");
        cameraServo = hardwareMap.servo.get("cameraServo");
        bucketServo = hardwareMap.servo.get("bucketServo");
        touchSensor = hardwareMap.get(DigitalChannel.class, "touchSensor");

        frontRight.setDirection(DcMotor.Direction.FORWARD);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        freightLift.setDirection(DcMotorSimple.Direction.REVERSE);
        touchSensor.setMode(DigitalChannel.Mode.INPUT);

        initGrabberServo();
        initCameraServo();
        initBucketServo();

    }

    public void loop() {
        setDrive();
        setDuckSpinners();
        setFreightLiftLevel();
        setFreightLiftPower();
        //setFreightLift();
        setSlowApproach();
        setBucketGrabber();
        setBucketPosition();
    }

    public double trimPower(double Power) {
        if (Math.abs(Power) < THRESHOLD) {
            Power = 0;
        }
        return Power;

    }

    public void setTurbo(){

    }

    public void setFreightLift() {

        //DETERMINE POWER OFF OF LIFT LEVELS
        if (freightLift.getCurrentPosition() >= MAXTICKS && gamepad2.right_stick_y < -THRESHOLD) {
            freightLift.setPower(HOLDINGPOWER);
        } else if (freightLift.getCurrentPosition() <= 0 && gamepad2.right_stick_y > THRESHOLD) {
            freightLift.setPower(0);
        } else {
            double liftPower = trimPower(-gamepad2.right_stick_y);
            freightLift.setPower(liftPower);
            telemetry.addData("Lift Position: ", freightLift.getCurrentPosition());
            telemetry.addData("Lift Power: ", freightLift.getPower());

        }
    }

    public void setFreightLiftPower() {
        double liftPower = trimPower(-gamepad2.right_stick_y);
        telemetry.addData("Lift Position: ", freightLift.getCurrentPosition());
        if (level == manualLevel) {
            if (freightLift.getCurrentPosition() >= MAXTICKS && gamepad2.right_stick_y < -THRESHOLD) {
                freightLift.setPower(0);
            } else if (freightLift.getCurrentPosition() <= 0 && gamepad2.right_stick_y > THRESHOLD) {
                freightLift.setPower(0);
            } else {
                freightLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                freightLift.setPower(liftPower);
            }
        } else {
            freightLift.setTargetPosition(ticks[level]);
            freightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            freightLift.setPower(FREIGHTLIFTPOWER);
        }
    }

    public int setFreightLiftLevel() {
        if (gamepad2.x) {
            level = levelZero;
        } else if (gamepad2.y) {
            level = firstLevel;
        } else if (gamepad2.b) {
            level = secondLevel;
        } else if (gamepad2.a) {
            level = thirdLevel;
        } else if (gamepad2.right_stick_y < -THRESHOLD || gamepad2.right_stick_y > THRESHOLD) {
            level = manualLevel;
        }
        return level;
    }

    public void setSlowApproach() {
        if (gamepad1.dpad_up) {
            frontLeft.setPower(APPROACHSPEED);
            frontRight.setPower(APPROACHSPEED);
            backLeft.setPower(APPROACHSPEED);
            backRight.setPower(APPROACHSPEED);
        } else if (gamepad1.dpad_down) {
            frontLeft.setPower(-APPROACHSPEED);
            frontRight.setPower(-APPROACHSPEED);
            backLeft.setPower(-APPROACHSPEED);
            backRight.setPower(-APPROACHSPEED);
        }
    }

    public void setDuckSpinners() {
        if (gamepad2.left_bumper) {
            leftDuckSpinner.setPower(-DUCKSPINNERPOWER);
        } else {
            leftDuckSpinner.setPower(0);
        } if (gamepad2.right_bumper) {
            rightDuckSpinner.setPower(DUCKSPINNERPOWER);
        } else {
            rightDuckSpinner.setPower(0);
        }
    }

    public void setBucketPosition() {
        if(gamepad2.left_stick_y < -THRESHOLD) {
            bucketServo.setPosition(BUCKETCOLLECT);
        } else if ((freightLift.getCurrentPosition() >= SAFETICKS) && (gamepad2.left_stick_button == true)) {
            bucketServo.setPosition(BUCKETDUMP);
        } else {
            bucketServo.setPosition(SAFETYBUCKET);
        }
    }

    public void setBucketGrabber() {
        if (gamepad2.left_trigger > THRESHOLD) {
            grabberMotor.setPower(GRABBERSPEED);
        } else if (gamepad2.right_trigger > THRESHOLD) {
            grabberMotor.setPower(-GRABBERSPEED);
        } else {
            grabberMotor.setPower(0);
        }
    }
    public void setDrive(){
        double forward = -gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x;
        double turn = gamepad1.right_stick_x;
        telemetry.addData("Drive Position: ", backLeft.getCurrentPosition());
        telemetry.update();
        double frontLeftPower = forward + strafe + turn;
        double frontRightPower = forward - strafe - turn;
        double backLeftPower = forward - strafe + turn;
        double backRightPower = forward + strafe - turn;
        frontLeftPower = trimPower(frontLeftPower);
        frontRightPower = trimPower(frontRightPower);
        backLeftPower = trimPower(backLeftPower);
        backRightPower = trimPower(backRightPower);
        frontLeft.setPower(frontLeftPower);
        frontRight.setPower(frontRightPower);
        backLeft.setPower(backLeftPower);
        backRight.setPower(backRightPower);
    }

    public void initGrabberServo() {
        grabberServo.setPosition(GRABBERSERVO);
    }

    public void initCameraServo() {
        cameraServo.setPosition(CAMERASERVO);
    }

    public void initBucketServo() {
        bucketServo.setPosition(BUCKETSERVO);
    }
}

