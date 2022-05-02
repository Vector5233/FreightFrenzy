package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class TurnWithIMU extends LinearOpMode {
    Menu menu = new Menu(this);
    DcMotor FL, BL, FR, BR;

    final boolean CCW = true;
    final boolean CW = false;

    BNO055IMU imu;
    BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
    Orientation angles;


    public void runOpMode() {
        FL = hardwareMap.dcMotor.get("frontLeft");
        BL = hardwareMap.dcMotor.get("backLeft");
        FR = hardwareMap.dcMotor.get("frontRight");
        BR = hardwareMap.dcMotor.get("backRight");

        // set up imu
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".  The internal IMU is on port 0.
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
        new MenuItem();
        menu.add(new MenuItem(0.0, "Angle to turn", 180.0, 0.0, 1));
        menu.add(new MenuItem(0.0,"power",1.0,0.0,0.1));

        while (opModeIsActive() && !isStarted()) {
            menu.update();
            menu.display();
        }
        double dtheta = menu.get(0);
        double power = menu.get(1);

        reportAll();
        while (opModeIsActive()) {
            if (gamepad1.a) {
                turnWithIMU(power, dtheta, CCW);
            }
            else if (gamepad1.b) {
                turnWithIMU(power, dtheta, CW);
            }
            reportAll();
        }

    }

    public void turnWithIMU(double power, double dtheta, boolean direction) {
        double offset = 0.0;
        double lastAngle = getHeading();
        double currentAngle = lastAngle;
        double calcAngle = (currentAngle < 0)? currentAngle + offset : currentAngle;
        double targetAngle = calcAngle + dtheta;

        if (direction == CCW) {
            while (opModeIsActive() && calcAngle < targetAngle) {
                setTurnPower(power);
                currentAngle = getHeading();
                if ((lastAngle <0 ) && (currentAngle > 0)) {
                    offset += 360;
                }
                lastAngle = currentAngle;
                calcAngle = currentAngle + offset;
            }
            setTurnPower(0);
        }
        else {
            while (opModeIsActive() && calcAngle > targetAngle) {
                setTurnPower(-power);
                currentAngle = getHeading();
                if ((lastAngle > 0 ) && (currentAngle < 0)) {
                    offset -= 360;
                }
                lastAngle = currentAngle;
                calcAngle = currentAngle + offset;
            }
            setTurnPower(0);
        }
    }

    public double adjustAngle(double angle) {
        return (angle < 0)? angle+360 : angle;
    }

    public double getHeading() {
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX,
                AngleUnit.DEGREES);
        return angles.firstAngle;
    }

    public void reportAll() {
        telemetry.addData("Heading: ", getHeading());
        telemetry.update();
    }

    public void setTurnPower(double power) {
        FL.setPower(power);
        BL.setPower(power);
        FR.setPower(-power);
        BR.setPower(-power);
    }
}
