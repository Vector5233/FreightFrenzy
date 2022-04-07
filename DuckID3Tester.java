package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@Autonomous(name="DuckID3Tester", group="Experiments")
@Disabled
public class DuckID3Tester extends LinearOpMode {

    Menu menu = new Menu(this);


    virtualBotObject robot = new virtualBotObject(this);
    private OpenCvInternalCamera phoneCam;
    private DuckID3 detector;
    int duckLevel = 3;
    public void runOpMode() {
        menu.add(new MenuItem(0, "side (0=Red, 1=Blue)",1,0,1));
        menu.add(new MenuItem(0, "done (0=No, 1=Yes)", 1, 0, 1));
        robot.init();

        while (!isStarted()) {
            menu.update();
            menu.display();
            if (menu.get(1) == 1) { break;}

        }
        if (menu.get(0) == 0) {
            detector = new DuckID3("red");
        }
        else {
            detector = new DuckID3("blue");
        }
        identifyDuck();
        telemetry.addData("level:", duckLevel);
        waitForStart();

    }

    public void identifyDuck(){
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        phoneCam.openCameraDevice();

        phoneCam.setPipeline(detector);
        phoneCam.startStreaming(352, 288, OpenCvCameraRotation.SIDEWAYS_LEFT);
        sleep(1000);
        duckLevel = detector.duckLevel();
    }
}
