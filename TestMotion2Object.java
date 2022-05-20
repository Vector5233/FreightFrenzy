package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
@Disabled
@Autonomous(name ="TestMotion2Object", group="experiments")
public class TestMotion2Object extends LinearOpMode  {
    Motion2 motion;
    boolean FORWARD = true;
    boolean BACKWARD = false;
    double power = 0.0;
    int ticks = 0;
    Menu menu;
    final String tag = "driveStraight";

    public void runOpMode() {
        menu = new Menu(this);
        menu.add(new MenuItem(0,"power",1.0,0.0,0.1));
        menu.add(new MenuItem(0,"ticks",8000, 0, 100));
        while (!isStarted()) {
            menu.update();
            menu.display();
        }
        motion = new Motion2(this);
        power = menu.get(0);
        ticks = (int)(menu.get(1));
        Log.i(tag, "------\nSTART OF TEST: Motion2.driveStraight\n-------\n");
        Log.i(tag,String.format("power %f   ticks %d\n", power, ticks));

        waitForStart();
        Log.i(tag,"initial battery: "+ hardwareMap.voltageSensor.get("Control Hub").getVoltage());
        Log.i(tag,"initial encoders");
        motion.recordFinalPosition();
        Log.i(tag, motion.strFinalPosition());
        motion.driveStraight(power, ticks, FORWARD);
        //motion.driveStraight(0.5,200,FORWARD);

        sleep(2000);
        Log.i(tag,"first leg encoders\n");
        Log.i(tag, motion.strFinalPosition());
        motion.driveStraight(power, ticks, BACKWARD);
        Log.i(tag,"second leg encoders");
        sleep(2000);
        Log.i(tag, motion.strFinalPosition());
        Log.i(tag,"final battery: "+hardwareMap.voltageSensor.get("Control Hub").getVoltage());
        Log.i(tag,"------\nEND OF TEST\n------\n");
    }
}

