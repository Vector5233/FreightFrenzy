package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name ="blueRight", group = "GROUP_NAME")

public class blueRight extends LinearOpMode {
    virtualBotObject robot = new virtualBotObject(this);
    DuckDetector duckDetector = new DuckDetector();
    //int duckLevel = duckDetector.duckLevel();

    public void runOpMode() {
        robot.init();
        waitForStart();
        robot.driveForward(.4, 85);
        robot.turnOnDuckSpinner();
        sleep(3000);
        robot.turnOffDuckSpinner();
        robot.driveForward(.4, -100);
        robot.autoTurn(.2, -600);
        sleep(300);
        robot.driveForward(.3, 450);
        robot.deliverBlock(2);
        robot.autoTurn(.3, 650);
        robot.driveForward(.4, -800);

    }
}