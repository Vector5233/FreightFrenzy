package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name ="NewRedLeftVuforia", group = "GROUP_NAME")

public class NewRedLeftVuforia extends LinearOpMode {
    virtualBotObject robot = new virtualBotObject(this);
    VisionObject vision = new VisionObject(robot.parent);
    private DuckDetector detector = new DuckDetector();
    final double INITGRABBERSERVOPOSITION = 1;
    final double BOB = 0;


    public void runOpMode() {
    initRobot();
    vision.initVuforia();
    telemetry.addLine("done!");
    telemetry.update();
    vision.createPassThrough();
    waitForStart();
    telemetry.addData("Level: ", vision.duckLevel()); //PROBLEM
    telemetry.update();
    sleep(2000);
    }

    public void initRobot(){
        robot.init();
        robot.initGrabberServo(INITGRABBERSERVOPOSITION);
        robot.initCameraServo(BOB);
    }
}
