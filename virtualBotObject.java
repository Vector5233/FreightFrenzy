package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import java.util.function.LongUnaryOperator;

class virtualBotObject {

    DcMotorEx backLeft, backRight, frontLeft, frontRight, leftDuckSpinner, rightDuckSpinner, freightLift;
    CRServo freightGrabber;
    LinearOpMode parent;

    final double DUCKSPINNERPOWER = .5;
    final double LIFTPOWER = .7;
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

        freightGrabber = parent.hardwareMap.crservo.get("freightGrabber");

        frontRight.setDirection(DcMotor.Direction.FORWARD);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);

        //Test of GitHub
        //test of mac connection to github
        // test of files 3
    }
    public void turnOnDuckSpinner (){
        leftDuckSpinner.setPower(-DUCKSPINNERPOWER);
        rightDuckSpinner.setPower(DUCKSPINNERPOWER);
    }

    public void turnOffDuckSpinner (){
        leftDuckSpinner.setPower(0);
        rightDuckSpinner.setPower(0);
    }

    public void turnOnLift(int level) {
        int[] ticks = {0, 10, 20, 30};
        freightLift.setTargetPosition(ticks[level]);
        freightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        freightLift.setPower(LIFTPOWER);
        while (freightLift.isBusy() && parent.opModeIsActive()) {
            assert true;
        }
    }
    public void turnOffLift(){
        freightLift.setPower(0);
    }
}
