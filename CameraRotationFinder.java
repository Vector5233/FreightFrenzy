package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "CameraRotationFinder", group = "Experiments")
@Disabled
public class CameraRotationFinder extends LinearOpMode {
    virtualBotObject robot = new virtualBotObject(this);
    Servo cameraServo;

    @Override
    public void runOpMode() {
        robot.init();
        cameraServo = hardwareMap.servo.get("cameraServo");
        //sweet spot position = set camera servo to .38
        cameraServo.setPosition(.38);
        waitForStart();
        telemetry.addData("Camera Position", cameraServo.getPosition());
        robot.findCurrentAngle();
    }

}