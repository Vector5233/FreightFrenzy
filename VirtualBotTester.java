package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "VirtualBotTester", group = "Red")
public class VirtualBotTester extends LinearOpMode {
    virtualBotObject robot = new virtualBotObject(this);

    public void runOpMode() {
        robot.init();
        waitForStart();
    }
}
