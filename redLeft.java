package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name ="redLeft", group = "GROUP_NAME")

public class redLeft extends LinearOpMode {
    virtualBotObject robot = new virtualBotObject(this);
    DuckDetector duckDetector = new DuckDetector();
    //int duckLevel = duckDetector.duckLevel();


    //
    public void runOpMode() {
        robot.init();
        waitForStart();
        robot.turnOnDuckSpinner();
        robot.driveForward(.4, 80);
        robot.autoStrafe(.2,30);
        //robot.driveForward(.25,100);
        sleep(3000);
        robot.turnOffDuckSpinner();
        robot.driveForward(.4, -100);
        robot.autoTurn(.2, 600);
        sleep(300);
        robot.driveForward(.3, 375);
        robot.deliverBlock(2);
        robot.autoTurn(.3,-650);
        robot.driveForward(.5,-800);
    }
}