package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.linearOpMode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

class Motion {

    DcMotor FL, FR, BL, BR;

    public final Boolean FORWARD = true;
    public final Boolean BACKWARD = true;
    LinearOpMode parent;

    int FLFinalPosition = 0;
    int FRFinalPosition = 0;
    int BLFinalPosition = 0;
    int BRFinalPosition = 0;

    public Motion(LinearOpMode p) {
        parent = p;
        FL = parent.hardwareMap.dcMotor.get("frontLeft");
        FR = parent.hardwareMap.dcMotor.get("frontRight");
        BL = parent.hardwareMap.dcMotor.get("backLeft");
        BR = parent.hardwareMap.dcMotor.get("backRight");
        FL.setDirection(DcMotorSimple.Direction.REVERSE);
        BL.setDirection(DcMotorSimple.Direction.REVERSE);
        FL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


    }

    public void setModeAll(DcMotor.RunMode mode) {
        FL.setMode(mode);
        FR.setMode(mode);
        BL.setMode(mode);
        BR.setMode(mode);
    }

    public void setPowerAll(double power) {
        FL.setPower(power);
        FR.setPower(power);
        BR.setPower(power);
        BL.setPower(power);

    }


    public void setTargetAll(int target) {
        FL.setTargetPosition(target);
        FR.setTargetPosition(target);
        BL.setTargetPosition(target);
        BR.setTargetPosition(target);
    }

    public void driveStraight(double power, int ticks, boolean direction) {
        int currentFL, currentBL, currentFR, currentBR;
        double tInit=0, tBR=0, tBL=0, tFR=0, tFL=0;


        ElapsedTime timer = new ElapsedTime();
        final double LAMBDA = 0.03;
        final int SLOWDOWN_TICKS = 150;
        final double SLOWDOWN_POWER = 0.15;
        setModeAll(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        parent.sleep(10);
        if(direction == FORWARD) {
            setTargetAll(ticks);
        }else{
            setTargetAll(-ticks);
        }
        setModeAll(DcMotor.RunMode.RUN_TO_POSITION);
        setPowerAll(power);
        while (FL.isBusy() && parent.opModeIsActive()) {

            currentFL = FL.getCurrentPosition();
            currentBL = BL.getCurrentPosition();
            currentBR = BR.getCurrentPosition();
            currentFR = FR.getCurrentPosition();

            double BLpower = LAMBDA*(currentFL - currentBL);
            double BRpower = LAMBDA*(currentFL - currentBR);
            double FRpower = LAMBDA*(currentFL - currentFR);

            // PID control
            if(direction == FORWARD) {
                BL.setPower(power + BLpower);
                BR.setPower(power + BRpower);
                FR.setPower(power + FRpower);
            }else{
                BL.setPower(power - BLpower);
                BR.setPower(power - BRpower);
                FR.setPower(power - FRpower);
            }

             //

        }
        //parent.telemetry.addData("FL time", tFL-tFR);
        //parent.telemetry.addData("FR time", tFR-tBR);
        //parent.telemetry.addData("BR time", tBR-tBL);
        //parent.telemetry.addData("BL time", tBL-tInit);
        parent.telemetry.update();
        setPowerAll(0);
        recordFinalPosition();
        setModeAll(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


    }

    public void recordFinalPosition() {
        FLFinalPosition = FL.getCurrentPosition();
        FRFinalPosition = FR.getCurrentPosition();
        BLFinalPosition = BL.getCurrentPosition();
        BRFinalPosition = BR.getCurrentPosition();
    }

    public String strFinalPosition() {
        String s = "\nFL: " + FLFinalPosition + "  FR: " + FRFinalPosition
                + "\nBL: " + BLFinalPosition + "  BR: " + BRFinalPosition;
        return s;
    }

}
