package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "CameraRotationFinder", group = "Experiments")

public class CameraRotationFinder extends LinearOpMode {
    virtualBotObject robot = new virtualBotObject(this);

    @Override
    public void runOpMode() {
        robot.init();
        waitForStart();
        robot.findCurrentAngle();
    }

}