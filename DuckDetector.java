package org.firstinspires.ftc.teamcode;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class DuckDetector extends OpenCvPipeline {
    final int LEFTROWSTART = 10;
    final int LEFTROWEND = 100;
    final int LEFTCOLSTART = 5;
    final int LEFTCOLEND = 100;
    final int CENTERROWSTART = 110;
    final int CENTERROWEND = 210;
    final int CENTERCOLSTART = 5;
    final int CENTERCOLEND = 100;
    final int RIGHTROWSTART = 210;
    final int RIGHTTROWEND = 310;
    final int RIGHTCOLSTART = 5;
    final int RIGHTCOLEND = 100;


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

    public int duckLevel() {
        if (getPosition().equals("ONE")) {
            int level = 1;
        } else if (getPosition().equals("TWO")) {
            int level = 2;
        } else if (getPosition().equals("THREE")) {
            int level = 3;
        }
    return duckLevel();
    }
}