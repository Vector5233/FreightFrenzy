package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous (name = "redRight", group = "GROUP_NAME")

class redRight extends LinearOpMode {
    virtualBotObject robot = new virtualBotObject(this);
    private OpenCvInternalCamera phoneCam;
    private DuckDetector detector = new DuckDetector();
    final double REDRIGHT = .2;
    int duckLevel = 3;
    final double INITGRABBERSERVOPOSITION = 1;
    final double POWER2 = .2;

    public void runOpMode(){
    robot.init();
    robot.initGrabberServo(INITGRABBERSERVOPOSITION);
    robot.initCameraServo(REDRIGHT);
    turnToHub();

    }

    public void turnToHub(){

    }
}

