package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "VirtualBotTester", group = "Red")
public class VirtualBotTester extends LinearOpMode {
    virtualBotObject robot = new virtualBotObject(this);

public void runOpMode() {
    robot.init();
    waitForStart();
    liftHeightTesting();
}
//method to test duck spinner
    /*
    public void duckSpinnerTesting() {
        robot.turnOnDuckSpinner();
        sleep(5000);
        robot.turnOffDuckSpinner();
        telemetry.addLine("done");
        telemetry.update();
        sleep(5000);
    }
    */

//method to test height of lift
// assuming approximate numbers for lift bc the grabber has not been added yet
    public void liftHeightTesting() {
        robot.turnOnLift(0);
        sleep(3000);
        telemetry.addLine("Level 0");
        telemetry.update();
        robot.turnOnLift(2);
        sleep(3000);
        telemetry.addLine("Level 2");
        telemetry.update();
        robot.turnOnLift(1);
        sleep(3000);
        telemetry.addLine("Level 1");
        telemetry.update();
        robot.turnOnLift(3);
        sleep(3000);
        telemetry.addLine("Level 3");
        telemetry.update();
        robot.turnOnLift(0);
        telemetry.addLine("Level 0");
        telemetry.addLine("done");
        telemetry.update();
        robot.turnOffLift();

    }
}