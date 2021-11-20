package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Autonomous", group = "GROUP_NAME")
public class Autonomous extends LinearOpMode {
    virtualBotObject robot = new virtualBotObject(this);

//drive to carousel and deliver ducks
//MUST START WITH DUCK SPINNER WHEEL LINED UP WITH FIRST HOLE NEXT TO CAROUSEL, AT AN ANGLE
    public void runOpMode() {
        robot.init();
        waitForStart();
        robot.driveForward(.4, 70);
        robot.turnOnDuckSpinner();
        sleep(3000);
        robot.driveForward(.7, -70);
        robot.turnOffDuckSpinner();
        telemetry.addLine("done");
        telemetry.update();
        sleep(10000);
    }
}

