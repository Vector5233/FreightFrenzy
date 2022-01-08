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
        robot.initVuforia();
        cameraServo = hardwareMap.servo.get("cameraServo");
        waitForStart();
        final double currentAngle = robot.findCurrentAngle();
        final double correctAngle = -.5;
        int rotate = (int) (currentAngle - correctAngle);
        robot.autoTurnDegrees (.5, rotate);
        robot.findCurrentAngle();
    }
}
