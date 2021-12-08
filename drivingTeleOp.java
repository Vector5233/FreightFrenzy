package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.vuforia.CameraDevice;


@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Gru", group = "Red")

public class drivingTeleOp extends OpMode {
    DcMotor frontLeft, frontRight, backLeft, backRight, leftDuckSpinner, rightDuckSpinner, freightLift, grabberMotor;

    Servo grabberServo, cameraServo, bucketServo;


                //ADD JAVADOC?

    final double APPROACHSPEED = .4;
    final double DUCKSPINNERPOWER = .5;
    final double FREIGHTLIFTPOWER = 1;
    final double SAFETYBUCKET = 1;
    final double BUCKETCOLLECT = .84;
    final double BUCKETDUMP = .75;
    final double THRESHOLD = .1;
    final double GRABBERSPEED = 1;
    final double GRABBERSERVO = 0;
    int[] ticks = {0, 4812, 6255, 8500};
    final int levelZero = 0;
    final int firstLevel = 1;
    final int secondLevel = 2;
    final int thirdLevel = 3;
    final int manualLevel = -1;
    int level = (manualLevel);
    final double CAMERASERVO = 0;
    final double MAXTICKS = 9000;
    final double SAFETICKS = 1200;
    final double LIFTGRABBER = .5;
    final double FIRSTLEVELGRABBER = 1;
    final double BOTTOM = 139;

    //Define Servos, Motors, set values

    //initializes all motors and servos, run modes, and sets direction
    public void init() {
        backLeft = hardwareMap.dcMotor.get("backLeft");
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        backRight = hardwareMap.dcMotor.get("backRight");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        freightLift = hardwareMap.dcMotor.get("freightLift");
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        freightLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        freightLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftDuckSpinner = hardwareMap.dcMotor.get("leftDuckSpinner");
        rightDuckSpinner = hardwareMap.dcMotor.get("rightDuckSpinner");
        grabberMotor = hardwareMap.dcMotor.get("grabberMotor");
        grabberServo = hardwareMap.servo.get("grabberServo");
        cameraServo = hardwareMap.servo.get("cameraServo");
        bucketServo = hardwareMap.servo.get("bucketServo");

        frontRight.setDirection(DcMotor.Direction.FORWARD);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        freightLift.setDirection(DcMotorSimple.Direction.REVERSE);

        initGrabberServo();
        initCameraServo();
        initBucketServo();

    }

    public void loop() {
        setDrive();
        resetMotors();
        setDuckSpinners();
        setFreightLiftLevel();
        setFreightLiftPower();
        setSlowApproach();
        setBucketGrabber();
        setBucketPosition();
        liftGrabber();
    }

    //called to trim power of driving joystick to ensure the robot does not move unintentionally
    public double trimPower(double Power) {
        if (Math.abs(Power) < THRESHOLD) {
            Power = 0;
        }
        return Power;

    }

    public void resetMotors() {
        if (gamepad1.a) {
            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        } else{
            backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }

    //Allows the grabber to be lifted, so robot can deliver blocks to first and second level
    public void liftGrabber(){
        if(gamepad1.b){
            grabberServo.setPosition(LIFTGRABBER);
        } else if (gamepad1.x){
            grabberServo.setPosition(GRABBERSERVO);
        } else if (gamepad1.a){
            grabberServo.setPosition(FIRSTLEVELGRABBER);
        }
    }

    //sets power to freight lift motor determined from level
    public void setFreightLiftPower() {
        double liftPower = trimPower(-gamepad2.right_stick_y);
        telemetry.addData("Lift Position: ", freightLift.getCurrentPosition());
        if (level == manualLevel) {
            if (freightLift.getCurrentPosition() >= MAXTICKS && gamepad2.right_stick_y < -THRESHOLD) {
                freightLift.setPower(0);
            } else if (freightLift.getCurrentPosition() <= BOTTOM && gamepad2.right_stick_y > THRESHOLD) {
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

    //takes button input to set level of lift and allows manual input
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

    //sets slow speed for drive motors if dpad is pressed (forwards and backwards)
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

    //sets speed and orientation of each duck spinner
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

    //sets Bucket position to collect and dump, then returns to the safety position
    public void setBucketPosition() {
        if(gamepad2.left_stick_y < -THRESHOLD) {
            bucketServo.setPosition(BUCKETCOLLECT);
        } else if ((freightLift.getCurrentPosition() >= SAFETICKS) && (gamepad2.left_stick_button)) {
            bucketServo.setPosition(BUCKETDUMP);
        } else {
            bucketServo.setPosition(SAFETYBUCKET);
        }
    }

    //sets grabber motor speed
    public void setBucketGrabber() {
        if (gamepad2.left_trigger > THRESHOLD) {
            grabberMotor.setPower(GRABBERSPEED);
        } else if (gamepad2.right_trigger > THRESHOLD) {
            grabberMotor.setPower(-GRABBERSPEED);
        } else {
            grabberMotor.setPower(0);
        }
    }

    //sets direction and power of all drive motors (frontLeft, frontRight, backLeft, backRight)
    public void setDrive(){
        double forward = -gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x;
        double turn = gamepad1.right_stick_x;
        telemetry.addData("Back Right Position: ", backRight.getCurrentPosition());
        telemetry.addData("Back Left Position: ", backLeft.getCurrentPosition());
        telemetry.addData("Front Right Position: ", frontRight.getCurrentPosition());
        telemetry.addData("Front Left Position: ", frontLeft.getCurrentPosition());
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

    //sets grabber servo position
    public void initGrabberServo() {
        grabberServo.setPosition(GRABBERSERVO);
    }

    //sets fixed camera position
    public void initCameraServo() {
        cameraServo.setPosition(CAMERASERVO);
    }

    //sets bucket servo to Safety position
    public void initBucketServo() {
        bucketServo.setPosition(SAFETYBUCKET);
    }
}

