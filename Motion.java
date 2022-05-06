package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.linearOpMode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

class Motion {

    DcMotor FL, FR, BL, BR;

    public final Boolean FORWARD = true;
    public final Boolean BACKWARD = true;

    public Motion() {
        FL = hardwareMap.dcMotor.get("frontLeft");
        FR = hardwareMap.dcMotor.get("frontRight");
        BL = hardwareMap.dcMotor.get("backLeft");
        BR = hardwareMap.dcMotor.get("backRight");
        FR.setDirection(DcMotorSimple.Direction.REVERSE);
        BR.setDirection(DcMotorSimple.Direction.REVERSE);
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

    public void driveStraight(double power, int ticks, boolean direction) {
        setModeAll(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setModeAll(DcMotor.RunMode.RUN_TO_POSITION);
        setTargetAll(ticks);
        setPowerAll(power);
        while (FL.isBusy() && linearOpMode.opModeIsActive()) {
        }
        setPowerAll(0);
        setModeAll(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


    }
}
