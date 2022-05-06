package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

class TestingOpMode {

    private DcMotor frontLeft, frontRight, backLeft, backRight;
    private LinearOpMode parent;

    private Motion motion = new Motion();

    public TestingOpMode(LinearOpMode p) {
        parent = p;
    }

    public void init() {
        backLeft = parent.hardwareMap.get(DcMotorEx.class, "backLeft");
        backRight = parent.hardwareMap.dcMotor.get("backRight");
        frontLeft = parent.hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = parent.hardwareMap.dcMotor.get("frontRight");
    }

    public void motionTest(double drivePower, int driveTicks, boolean driveDirection){

        motion.driveStraight(drivePower, driveTicks, driveDirection);
        getAvgEncoders();
        checkAvgEncoders();
        checkIMU();
        diffOfLeftRight();
    }

    public void getAvgEncoders() {

    }

    public void checkAvgEncoders() {

    }

    public void checkIMU() {

    }

    public void diffOfLeftRight() {

    }
}
