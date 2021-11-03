package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;


@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Gru", group = "Red")

public class drivingTeleOp extends OpMode {
    DcMotor frontLeft, frontRight, backLeft, backRight, leftDuckSpinner, rightDuckSpinner, freightLift, freightGrabber;

    Servo freightDoor;

    final double approachSpeed = .2;

    //Define Servos, Motors, set values

    public void init() {
        backLeft = hardwareMap.dcMotor.get("backLeft");
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        backRight = hardwareMap.dcMotor.get("backRight");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        freightLift = hardwareMap.dcMotor.get("freightLift");
        freightLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftDuckSpinner = hardwareMap.dcMotor.get("leftDuckSpinner");
        rightDuckSpinner = hardwareMap.dcMotor.get("rightDuckSpinner");

        freightGrabber = hardwareMap.dcMotor.get("freightGrabber");

        frontRight.setDirection(DcMotor.Direction.FORWARD);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);

    }
    public void loop(){
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
        frontLeft.setPower(frontLeftPower);
        frontRight.setPower(frontRightPower);
        backLeft.setPower(backLeftPower);
        backRight.setPower(backRightPower);
        setFreightGrabber();
        setDuckSpinners();
        setFreightLift();
        setSlowApproach();




    }
    public double trimPower(double Power) {
        final double THRESHOLD = .1;
        if (Math.abs(Power) < THRESHOLD) {
            Power = 0;
        }
        return Power;

    }

    public void setFreightGrabber(){
        double freightPower = trimPower(-gamepad2.left_stick_y);
        freightGrabber.setPower(freightPower);
    }

    public void setFreightLift(){
        double liftPower = trimPower(gamepad2.right_stick_y);
        freightLift.setPower(liftPower);
        telemetry.addData("Lift Position: ", freightLift.getCurrentPosition());
    }
    public void setSlowApproach() {
        if (gamepad1.dpad_up) {
            frontLeft.setPower(approachSpeed);
            frontRight.setPower(approachSpeed);
            backLeft.setPower(approachSpeed);
            backRight.setPower(approachSpeed);
        }
    }

    final double DUCKSPINNERPOWER = .5;
    public void setDuckSpinners(){
        if (gamepad2.left_bumper) {
            leftDuckSpinner.setPower(-DUCKSPINNERPOWER);
        }
        else {
            leftDuckSpinner.setPower(0);
        }
        if (gamepad2.right_bumper) {
            rightDuckSpinner.setPower(DUCKSPINNERPOWER);
        }
        else {
            rightDuckSpinner.setPower(0);
        }
    }
}
