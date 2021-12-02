package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
@Disabled
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "VirtualBotTester", group = "Red")
public class VirtualBotTester extends LinearOpMode {
    virtualBotObject robot = new virtualBotObject(this);

    public void runOpMode() {
        robot.init();
        waitForStart();
        robot.turnOnDuckSpinner();
        sleep(5000);
        robot.turnOffDuckSpinner();
        telemetry.addLine("done");
        telemetry.update();
        sleep(5000);
        robot.deliverBlock(3);
    }
}
