package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@Autonomous(name="StrafeTester5", group="experiments")
public class StrafeTester5 extends LinearOpMode {
    DcMotor frontLeft, frontRight, backLeft, backRight;

    double strafePower = 0.0;   // power from 0 to 1
    double strafeDistance = 12.0; // distance in inches.  Can be negative.  Capped at 144"
    double joystickThreshold = 0.10;
    virtualBotObject robot = new virtualBotObject(this);

    Menu initializationMenu = new Menu(this);
    //Define Servos, Motors, set values

    //initializes all motors and servos, run modes, and sets direction
    public void initialize() {
        robot.init();
        initializationMenu.add(new MenuItem(0.0, "power", 1.0, 0.0, 0.1 ));
        initializationMenu.add(new MenuItem(0.0, "distance", 144, -144, 0.5));
        while (!isStarted()) {
            initializationMenu.update();
            initializationMenu.display();
        }
        strafePower = initializationMenu.get(0);
        strafeDistance = initializationMenu.get(1);

    }

    public void runOpMode() {
        initialize();
        telemetry.addLine("Strafing " + strafeDistance + " inches at " + strafePower + " power");
        telemetry.update();
        robot.autoStrafe5(strafePower, inchesToTicks(strafeDistance));
        robot.reportAllDriveMotors();
        telemetry.update();
        sleep(10000);
    }

    public int inchesToTicks(double inches) {
        final double TICKS_PER_INCH = 30.0;
        return (int)(inches * TICKS_PER_INCH);
    }
}
