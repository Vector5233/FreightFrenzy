package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name ="TestMotionObject", group="experiments")
public class TestMotionObject extends LinearOpMode  {
    Motion motion;
    boolean FORWARD = true;
    boolean BACKWARD = false;

    public void runOpMode() {
        motion = new Motion(this);
        waitForStart();
        motion.driveStraight(0.5, 200, FORWARD);
        sleep(2000);
        motion.driveStraight(0.5, 200, BACKWARD);
    }
}

