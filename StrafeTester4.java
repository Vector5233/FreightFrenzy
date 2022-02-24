package org.firstinspires.ftc.teamcode;

import android.icu.text.DateFormat;
import android.icu.util.Calendar;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@Autonomous(name="StrafeTester4", group="experiments")
public class StrafeTester4 extends LinearOpMode {
    DcMotor frontLeft, frontRight, backLeft, backRight;

    double strafePower = 0.0;   // power from 0 to 1
    double strafeDistance = 12.0; // distance in inches.  Can be negative.  Capped at 144"
    double joystickThreshold = 0.10;
    virtualBotObject robot = new virtualBotObject(this);

    Menu initializationMenu = new Menu(this);
    //Define Servos, Motors, set values

    boolean Logging = false;

    //initializes all motors and servos, run modes, and sets direction
    public void initialize() {
        robot.init();
        initializationMenu.add(new MenuItem(0.0, "Motor Tolerance", 100, 2, 1));
        initializationMenu.add(new MenuItem(0.0, "Motor Adjustment", 0.5, 0.1, 0.1));
        initializationMenu.add(new MenuItem(0.0, "Logging", 1.0, 0.0, 1.0));
        while (!isStarted()) {
            initializationMenu.update();
            initializationMenu.display();
        }
        robot.setMotorTolerance((int)initializationMenu.get(0));
        robot.setMotorAdjustment(initializationMenu.get(1));
        if (initializationMenu.get(2)>0.5) {
            Logging = true;
        }

    }

    public void runOpMode() {
        initialize();

        strafeDistance = 20.0;
        strafePower = 0.50;
        telemetry.addLine("Strafing " + strafeDistance + " inches at " + strafePower + " power");
        telemetry.update();
        if (Logging) {
            Log.i(">>>>>>>>>>>>>>>>>>>>>>>>>\nStart Run", java.text.DateFormat.getDateTimeInstance().format(java.util.Calendar.getInstance().getTime()));
            robot.loggingAutoStrafe4(strafePower, inchesToTicks(strafeDistance));
        }
        else {
            robot.autoStrafe4(strafePower, inchesToTicks(strafeDistance));
        }

    }

    public int inchesToTicks(double inches) {
        final double TICKS_PER_INCH = 22.222;
        return (int)(inches * TICKS_PER_INCH);
    }

}
