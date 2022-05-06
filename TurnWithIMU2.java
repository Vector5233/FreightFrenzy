package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@Autonomous(name="TurnWithIMU2", group="experiments")

public class TurnWithIMU2 extends LinearOpMode {
    Menu menu = new Menu(this);
    DcMotor FL, BL, FR, BR;

    final boolean CCW = true;
    final boolean CW = false;
    final double FULLCIRCLE = 360;

    BNO055IMU imu;
    BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
    Orientation angles;
    double lastAngle = 0;
    double integratedAngle = 0;
    double offset = 0;
    double currentAngle;


    public void runOpMode() {
        FL = hardwareMap.dcMotor.get("frontLeft");
        BL = hardwareMap.dcMotor.get("backLeft");
        FR = hardwareMap.dcMotor.get("frontRight");
        BR = hardwareMap.dcMotor.get("backRight");
        FR.setDirection(DcMotorSimple.Direction.REVERSE);
        BR.setDirection(DcMotorSimple.Direction.REVERSE);

        // set up imu
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".  The internal IMU is on port 0.
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
        new MenuItem();
        menu.add(new MenuItem(0.0, "Angle to turn", 180.0, 0.0, 1));
        menu.add(new MenuItem(0.0, "power", 1.0, 0.0, 0.1));

        while (!isStarted()) {
            menu.update();
            menu.display();
        }
        double dtheta = menu.get(0);
        double power = menu.get(1);

        waitForStart();
        reportAll();

        while (opModeIsActive()) {
            if (gamepad1.a) {
                telemetry.addData("Turning CounterClockwise by ", dtheta);
                telemetry.update();
                turnWithIMU(power, dtheta, CCW);

            } else if (gamepad1.b) {
                telemetry.addData("Turning Clockwise by ", dtheta);
                turnWithIMU(power, dtheta, CW);
            }
            reportAll();
        }

    }

    public void turnWithIMU(double power, double dtheta, boolean direction) {

        double targetAngle;

        if (direction == CCW) {
            targetAngle = integratedAngle + dtheta;
            while (opModeIsActive() && integratedAngle < targetAngle) {
                setTurnPower(power);
                getHeading();
            }
            setTurnPower(0);
        } else {
            targetAngle = integratedAngle - dtheta;
            while (opModeIsActive() && integratedAngle > targetAngle) {
                setTurnPower(-power);
                getHeading();
            }
            setTurnPower(0);
        }
    }

    public double adjustAngle(double angle) {
        return (angle < 0) ? angle + 360 : angle;
    }

    public double getHeading() {
        /*
         *
         * Return current heading and update integratedAngle and associated information.
         */
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX,
                AngleUnit.DEGREES);
        currentAngle = angles.firstAngle;
        if ((lastAngle > 90) && (currentAngle < -90)) {
            offset += FULLCIRCLE;
        } else if ((lastAngle < -90) && (currentAngle > 90)) {
            offset -= FULLCIRCLE;
        }
        integratedAngle = currentAngle + offset;
        lastAngle = currentAngle;
        return currentAngle;
    }

    public void reportAll() {
        telemetry.addData("IMU Heading: ", getHeading());
        telemetry.addData("Accumulated Heading: ", integratedAngle);
        telemetry.update();
    }

    public void setTurnPower(double power) {
        FL.setPower(power);
        BL.setPower(power);
        FR.setPower(-power);
        BR.setPower(-power);
    }
}

