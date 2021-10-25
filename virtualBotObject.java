package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

class virtualBotObject {

    DcMotorEx backLeft, backRight, frontLeft, frontRight, leftDuckSpinner, rightDuckSpinner, freightLift;
    CRServo freightGrabber;
    LinearOpMode parent;

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
}
