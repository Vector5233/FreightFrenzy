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
        robot.initGrabberServo();
        robot.turnOnDuckSpinner();
        robot.driveForward(.4, 80);
        robot.autoStrafe(.2,-30);
        sleep(3000);
        robot.turnOffDuckSpinner();
        robot.driveForward(.4, -100);
        robot.autoTurn(.2, 620);
        sleep(100);
        robot.driveForward(.3, 375);
        robot.deliverBlock(2);
        robot.autoTurn(.2,650);
        robot.driveForward(.5,-800);
    }
}