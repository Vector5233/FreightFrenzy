package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@Autonomous(name="StrafeTester", group="experiments")
public class StrafeTester extends LinearOpMode {
    DcMotor frontLeft, frontRight, backLeft, backRight;

    double strafePower = 0.0;   // power from 0 to 1
    double strafeDistance = 12.0; // distance in inches.  Can be negative.  Capped at 144"
    double joystickThreshold = 0.10;
    virtualBotObject robot = new virtualBotObject(this);

    //Define Servos, Motors, set values

    //initializes all motors and servos, run modes, and sets direction
    public void initialize() {
        int menuActiveItem = 0;
        MenuItem[] menuItems = {new MenuItem(0.0, "power", 1.0, 0.0, 0.1 ),
                                new MenuItem(0.0, "distance", 144, -144, 0.5)};
        int i;
        final int delay = 150;

        robot.init();

        while (!isStarted()) {
            for (i=0; i<menuItems.length; i++) {
                if (i == menuActiveItem) {
                    telemetry.addLine(">"+menuItems[i].getRepr());
                }
                else {
                    telemetry.addLine(menuItems[i].getRepr());
                }
            }
            telemetry.update();

            if (gamepad1.right_stick_y > joystickThreshold) {
                menuItems[menuActiveItem].down();
                sleep(delay);
            }
            else if(gamepad1.right_stick_y < -joystickThreshold) {
                menuItems[menuActiveItem].up();
                sleep(delay);
            }
            else if(gamepad1.left_stick_y > joystickThreshold) {
                menuActiveItem++;
                if (menuActiveItem >= menuItems.length) {
                    menuActiveItem = 0;
                }
                sleep(delay);
            }
            else if(gamepad1.left_stick_y < -joystickThreshold) {
                menuActiveItem--;
                if (menuActiveItem < 0) {
                    menuActiveItem = menuItems.length - 1;
                }
                sleep(delay);
            }
        }
        strafePower = menuItems[0].getValue();
        strafeDistance = menuItems[1].getValue();

    }

    public void runOpMode() {
        initialize();
        telemetry.addLine("Strafing " + strafeDistance + " inches at " + strafePower + " power");
        telemetry.update();
        robot.autoStrafe(strafePower, inchesToTicks(strafeDistance));
    }

    public int inchesToTicks(double inches) {
        final double TICKS_PER_INCH = 2.0;
        return (int)(inches * TICKS_PER_INCH);
    }
}
