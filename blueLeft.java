package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous (name = "blueLeft", group = "GROUP_NAME")

public class blueLeft extends LinearOpMode {
    virtualBotObject robot = new virtualBotObject(this);
    private OpenCvInternalCamera phoneCam;
    private DuckDetector detector = new DuckDetector("blue");
    final double BLUE_LEFT = .7;
    int duckLevel = 3;
    final double G_SERVO_POSITION = 1;
    final double POWER2 = .2;


    public void runOpMode() {
        initRobot();
        duckIdentifier();
        waitForStart();
        strafeOut();


        //robot.deliverBlock(duckLevel);



    }
    public void initRobot(){
        robot.init();
        robot.initCameraServo(BLUE_LEFT);
    }

    public void strafeOut(){
        int STRAFE_POWER = 1;
        int STRAFE_TICKS = 110;
        long SLEEP = 100;
        int TURN_DEGREES = 270;
        robot.initGrabberServo(G_SERVO_POSITION);
        robot.autoStrafe6(STRAFE_POWER, STRAFE_TICKS);
        sleep(SLEEP);
        robot.autoTurnDegrees(.2, TURN_DEGREES);
        sleep(SLEEP);
        robot.setPowerAll(0);
    }

    public void strafeTimeout(double power, double ticks, long time){

    }

    public void duckIdentifier(){
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        phoneCam.openCameraDevice();
        phoneCam.setPipeline(detector);
        phoneCam.startStreaming(352, 288, OpenCvCameraRotation.SIDEWAYS_LEFT);
        sleep(3000);
        duckLevel = detector.duckLevel();
    }
}
