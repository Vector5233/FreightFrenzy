package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="OpenHouse", group="")
public class OpenHouse extends OpMode {
    DcMotor frontLeft, frontRight, backLeft, backRight, leftDuckSpinner, rightDuckSpinner, freightLift, grabberMotor;

    Servo grabberServo, cameraServo, bucketServo;


    //ADD JAVADOC?

    final double APPROACHSPEED = .4;
    final double DUCKSPINNERPOWER = .5;
    final double FREIGHTLIFTPOWER = 1;
    final double SAFETYBUCKET = 1;
    final double BUCKETCOLLECT = .8;
    final double BUCKETDUMP = .7;
    final double THRESHOLD = .1;
    final double GRABBERSPEED = 1;
    final double GRABBERSERVO = 0;
    int[] ticks = {0, 2355, 3485, 4560};
    final int levelZero = 0;
    final int firstLevel = 1;
    final int secondLevel = 2;
    final int thirdLevel = 3;
    final int manualLevel = -1;
    int level = (manualLevel);
    final double CAMERASERVO = 0;
    final double MAXTICKS = 4570;
    final double SAFETICKS = 1200;
    final double LIFTGRABBER = .5;
    final double FIRSTLEVELGRABBER = 1;

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

        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        freightLift.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    public void loop() {
            double forward = -gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x;
            double turn = gamepad1.right_stick_x;
            double frontLeftPower = forward + strafe + turn;
            double frontRightPower = forward - strafe - turn;
            double backLeftPower = forward - strafe + turn;
            double backRightPower = forward + strafe - turn;
            frontLeftPower = trimPower(frontLeftPower);
            frontRightPower = trimPower(frontRightPower);
            backLeftPower = trimPower(backLeftPower);
            backRightPower = trimPower(backRightPower);
            telemetry.addLine("Back Right Position: " + backRight.getCurrentPosition()+ " power: " + backRightPower);
            telemetry.addLine("Back Left Position: " + backLeft.getCurrentPosition() + " power: " +backLeftPower);
            telemetry.addLine("Front Right Position: " + frontRight.getCurrentPosition() + " power: "+frontRightPower);
            telemetry.addLine("Front Left Position: " + frontLeft.getCurrentPosition() + " power: " +frontLeftPower);
            telemetry.update();

            frontLeft.setPower(frontLeftPower);
            frontRight.setPower(frontRightPower);
            backLeft.setPower(backLeftPower);
            backRight.setPower(backRightPower);
        }

        public double trimPower( double power){
            final double MAXPOWER = 0.50;

            if (power > MAXPOWER) {
                power = MAXPOWER;
            } else if (power < -MAXPOWER) {
                power = -MAXPOWER;
            }

            return power;
        }
}
