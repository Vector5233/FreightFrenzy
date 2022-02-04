package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous (name = "RobotRotator", group = "Experiments")
public class RobotRotator extends LinearOpMode {
    virtualBotObject robot = new virtualBotObject(this);
    Servo cameraServo;


    @Override
    public void runOpMode() {
        robot.init();
        //robot.initVuforia();
        cameraServo = hardwareMap.servo.get("cameraServo");
        waitForStart();
        final double currentAngleOne = robot.findCurrentAngle();
        final double correctAngle = -.5;
        int rotateOne = (int) (currentAngleOne - correctAngle);
        telemetry.addData("Rotate by ", rotateOne);
        telemetry.update();
        robot.autoTurnDegrees (1, rotateOne);
        sleep(500);
        //second angle calculation
        final double currentAngleTwo = robot.findCurrentAngle();
        int rotateTwo = (int) (currentAngleTwo - correctAngle);
        telemetry.addData("Second rotate by ", rotateTwo);
        telemetry.update();
        robot.autoTurnDegrees (1, rotateTwo);
        sleep(500);
        //third angle calculation
        final double currentAngleThree = robot.findCurrentAngle();
        int rotateThree = (int) (currentAngleThree - correctAngle);
        telemetry.addData("Third rotate by ", rotateThree);
        telemetry.update();
        robot.autoTurnDegrees (1, rotateThree);
        sleep(500);
        //second angle calculation
        final double currentAngleFour = robot.findCurrentAngle();
        int rotateFour = (int) (currentAngleFour - correctAngle);
        telemetry.addData("Fourth rotate by ", rotateFour);
        telemetry.update();
        robot.autoTurnDegrees (1, rotateFour);
        sleep(500);
        //third angle calculation
        final double currentAngleFive = robot.findCurrentAngle();
        int rotateFive = (int) (currentAngleFive - correctAngle);
        telemetry.addData("Fifth rotate by ", rotateFive);
        telemetry.update();
        robot.autoTurnDegrees (1, rotateFive);
        sleep(500);
        telemetry.addLine("Task Completed");
        telemetry.update();
    }
}
