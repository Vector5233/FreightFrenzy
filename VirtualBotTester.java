package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "VirtualBotTester", group = "Red")
public class VirtualBotTester extends LinearOpMode {
    virtualBotObject robot = new virtualBotObject(this);

    public void runOpMode() {
        robot.init();
        waitForStart();
        robot.driveForward (.2, 700);
        sleep(5000);
        robot.driveForward(.6, -700);
    }
}

// robot.turnOnDuckSpinner();
//        sleep(5000);
//        robot.turnOffDuckSpinner();
//        telemetry.addLine("done");
//        telemetry.update();
//        sleep(5000);