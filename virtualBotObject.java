package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.linearOpMode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

class virtualBotObject {

    DcMotorEx backLeft, backRight, frontLeft, frontRight, leftDuckSpinner, rightDuckSpinner, freightLift;
    CRServo freightGrabber;

    public void init() {
        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        backRight = (DcMotorEx) hardwareMap.dcMotor.get("backRight");
        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = (DcMotorEx) hardwareMap.dcMotor.get("frontRight");
        leftDuckSpinner = (DcMotorEx) hardwareMap.dcMotor.get("leftDuckSpinner");
        rightDuckSpinner = (DcMotorEx) hardwareMap.dcMotor.get("rightDuckSpinner");
        freightLift = (DcMotorEx) hardwareMap.dcMotor.get("freightLift");

        freightGrabber = hardwareMap.crservo.get("freightGrabber");

        frontRight.setDirection(DcMotor.Direction.FORWARD);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
    }
}
