package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.linearOpMode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

class Motion2 {

    DcMotor FL, FR, BL, BR;

    public final Boolean FORWARD = true;
    public final Boolean BACKWARD = true;
    LinearOpMode parent;

    int FLFinalPosition = 0;
    int FRFinalPosition = 0;
    int BLFinalPosition = 0;
    int BRFinalPosition = 0;

    final int DECEL_TICKS = 75;
    final double DECEL_POWER = 0.15;

    public Motion2(LinearOpMode p) {
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
        BL.setPower(power);
        BR.setPower(power);
    }


    public void setTargetAll(int target) {
        FL.setTargetPosition(target);
        FR.setTargetPosition(target);
        BL.setTargetPosition(target);
        BR.setTargetPosition(target);
    }

    private double adjustPower(double power, int currentPos, int leadPos) {
        /*
         * return power for motor based on current position
         * relative to the lead motor position
         */

        final double LAMBDA = 0.01;

        power = power* (1+LAMBDA*(leadPos - currentPos));
        if (power < 0) {
            power = 0;
        }
        else if (power > 1) {
            power = 1;
        }
        return power;
    }
    public void driveStraight(double power, int ticks, boolean direction) {
        int oldFL, oldFR, oldBL, oldBR;
        int currentFL, currentFR, currentBL, currentBR;
        //double lambda;
        int remaining;

        setModeAll(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        parent.sleep(50);
        if (direction == FORWARD) {
            setTargetAll(ticks);
        }
        else {
            setTargetAll(-ticks);
        }

        setModeAll(DcMotor.RunMode.RUN_TO_POSITION);
        oldFL = FL.getCurrentPosition();
        oldFR = FR.getCurrentPosition();
        oldBL = BL.getCurrentPosition();
        oldBR = BR.getCurrentPosition();
        setPowerAll(power);
        while (FL.isBusy() && parent.opModeIsActive()) {
            currentFL = FL.getCurrentPosition();
            currentFR = FR.getCurrentPosition();
            currentBL = BL.getCurrentPosition();
            currentBR = BR.getCurrentPosition();

            FR.setPower(adjustPower(power, currentFR, currentFL));
            BL.setPower(adjustPower(power, currentBL, currentFL));
            BR.setPower(adjustPower(power, currentBR, currentFL));

            remaining = currentFL - ticks;
            if (remaining <= DECEL_TICKS) {
                setPowerAll(DECEL_POWER);
            }

            oldFL= FL.getCurrentPosition();
            oldFR = FR.getCurrentPosition();
            oldBL = BL.getCurrentPosition();
            oldBR = BR.getCurrentPosition();

        }
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

    public String strAllEncoders() {

        String s = String.format("...\nFL: %d   FR: %d \n BL: %d    BR: %d\n\n",
                FL.getCurrentPosition(),
                FR.getCurrentPosition(),
                BL.getCurrentPosition(),
                BR.getCurrentPosition());
        return s;
    }
}
