package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name ="redLeft", group = "GROUP_NAME")

public class redLeft extends LinearOpMode {
    virtualBotObject robot = new virtualBotObject(this);
    DuckDetector duckDetector = new DuckDetector();
    //int duckLevel = duckDetector.duckLevel();


    //robot must start with black line on duck spinner brace parallel to
    public void runOpMode() {
        robot.init();
        waitForStart();
        robot.initGrabberServo();
        robot.turnOnDuckSpinner();
        robot.driveForward(.4, 80);
        robot.autoStrafe(.2,30);
        sleep(3000);
        robot.turnOffDuckSpinner();
        robot.driveForward(.4, -100);
        robot.autoTurn(.2, 600);
        sleep(100);
        robot.driveForward(.2, 600);
        sleep(100);
        robot.deliverBlock(3);
        robot.autoTurn(.2,-650);
        robot.driveForward(.5,-1400);
    }
}