package org.firstinspires.ftc.teamcode;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class DuckDetector extends OpenCvPipeline {
    final int LEFTROWSTART = 200;
    final int LEFTROWEND = 250;
    final int LEFTCOLSTART = 10;
    final int LEFTCOLEND = 50;
    final int CENTERROWSTART = 200;
    final int CENTERROWEND = 250;
    final int CENTERCOLSTART = 100;
    final int CENTERCOLEND = 50;
    final int RIGHTROWSTART = 150;
    final int RIGHTTROWEND = 250;
    final int RIGHTCOLSTART = 200;
    final int RIGHTCOLEND = 250;


    private Mat workingMatrix = new Mat();

    private String position;

    public DuckDetector() {

    }

    @Override
    public final Mat processFrame(Mat input) {
        input.copyTo(workingMatrix);

        if (workingMatrix.empty()) {
            return input;
        }

        Imgproc.cvtColor(workingMatrix, workingMatrix, Imgproc.COLOR_RGB2YCrCb);

        Mat matRight = workingMatrix.submat(RIGHTROWSTART, RIGHTTROWEND, RIGHTCOLSTART, RIGHTCOLEND);
        Mat matCenter = workingMatrix.submat(CENTERROWSTART, CENTERROWEND, CENTERCOLSTART, CENTERCOLEND);
        Mat matLeft = workingMatrix.submat(LEFTROWSTART, LEFTROWEND, LEFTCOLSTART, LEFTCOLEND);


        double leftTotal = Core.sumElems(matLeft).val[0];
        double centerTotal = Core.sumElems(matCenter).val[0];
        double rightTotal = Core.sumElems(matRight).val[0];

        if (leftTotal > centerTotal) {
            if (leftTotal > rightTotal) {
                //level is 1
                position = "ONE";
                Imgproc.rectangle(workingMatrix, new Point(LEFTCOLSTART, LEFTROWSTART), new Point(LEFTCOLEND, LEFTROWEND), new Scalar(247, 181, 0));
            } else {
                if (centerTotal > leftTotal) {
                    if (centerTotal > rightTotal) {
                        //level is 2
                        position = "TWO";
                        Imgproc.rectangle(workingMatrix, new Point(CENTERCOLSTART, CENTERROWSTART), new Point(CENTERCOLEND, CENTERROWEND), new Scalar(247, 181, 0));
                    } else {
                        //level is 3
                        position = "THREE";
                        Imgproc.rectangle(workingMatrix, new Point(RIGHTCOLSTART, RIGHTROWSTART), new Point(RIGHTCOLEND, RIGHTTROWEND), new Scalar(247, 181, 0));
                    }
                }


            }
        }
        return workingMatrix;
    }
    public String getPosition() {
        return position;
    }
}