package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name ="redLeft", group = "GROUP_NAME")

public class redLeft extends LinearOpMode {
    virtualBotObject robot = new virtualBotObject(this);
    DuckDetector duckDetector = new DuckDetector();
    //int duckLevel = duckDetector.duckLevel();
    final double INITGRABBERSERVOPOSITION = 1;

    //robot must start with black line on duck spinner brace parallel to the metal part of the carousel with three fingers in between the duck spinner and the carousel
    public void runOpMode() {
        robot.init();
        robot.initGrabberServo(INITGRABBERSERVOPOSITION);
        waitForStart();
        robot.initGrabberServo(0);
        robot.turnOnDuckSpinner();
        robot.driveForward(.4, 80);
        robot.autoStrafe(.2,30);
        sleep(3000);
        robot.turnOffDuckSpinner();
        robot.driveForward(.4, -100);
        robot.autoTurn(.2, 600);
        sleep(100);
        robot.driveForward(.2, 575);
        sleep(100);
        robot.deliverBlock(3);
        robot.autoTurn(.2,-650);
        robot.driveForward(.5,400);

    }
}