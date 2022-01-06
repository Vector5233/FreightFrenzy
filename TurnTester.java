package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous (name = "Turner", group = "Experiments")

public class TurnTester extends LinearOpMode {
    virtualBotObject robot = new virtualBotObject(this);
    @Override
    public void runOpMode()  {
        robot.init();
        waitForStart();
        robot.autoTurnDegrees (.5, 90);
        robot.setPowerAll(0);
        sleep(5000);
        telemetry.addLine("first turn complete");
        telemetry.update();
        robot.autoTurnDegrees(.5,-90);
        robot.setPowerAll(0);
        sleep(5000);
        robot.autoTurnDegrees(.5,3);
        robot.setPowerAll(0);
        sleep(5000);
    }
}
