package org.firstinspires.ftc.teamcode;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class DuckDetector extends OpenCvPipeline {

    final int LEFTCOLSTART = 10;
    final int LEFTCOLEND = 100;
    final int LEFTROWSTART = 90;
    final int LEFTROWEND = 185;
    final int CENTERCOLSTART = 110;
    final int CENTERCOLEND = 210;
    final int CENTERROWSTART = 90;
    final int CENTERROWEND = 185;
    final int RIGHTCOLSTART = 210;
    final int RIGHTCOLEND = 310;
    final int RIGHTROWSTART = 90;
    final int RIGHTROWEND = 185;




    private Mat workingMatrix = new Mat();

    private String position;

    public DuckDetector() {
        //getPosition();
        //duckLevel();
    }

        public double leftTotal;
        public double centerTotal;
        public double rightTotal;
        @Override
        public final Mat processFrame (Mat input){
            input.copyTo(workingMatrix);

            if (workingMatrix.empty()) {
                return input;
            }

            Imgproc.cvtColor(workingMatrix, workingMatrix, Imgproc.COLOR_RGB2YCrCb);

            Mat matRight = workingMatrix.submat(RIGHTROWSTART, RIGHTROWEND, RIGHTCOLSTART, RIGHTCOLEND);
            Mat matCenter = workingMatrix.submat(CENTERROWSTART, CENTERROWEND, CENTERCOLSTART, CENTERCOLEND);
            Mat matLeft = workingMatrix.submat(LEFTROWSTART, LEFTROWEND, LEFTCOLSTART, LEFTCOLEND);


            leftTotal = Core.sumElems(matLeft).val[0];
            centerTotal = Core.sumElems(matCenter).val[0];
            rightTotal = Core.sumElems(matRight).val[0];

             boolean moreCtrThanLft = centerTotal > leftTotal;
             boolean moreCtrThanRt = centerTotal > rightTotal;
             boolean moreLftThanCtr = leftTotal > centerTotal;
             boolean moreLftThanRt = leftTotal > rightTotal;
             boolean moreRtThanCtr = rightTotal > centerTotal;
             boolean moreRtThanLft = rightTotal > leftTotal;



            if (moreCtrThanLft && moreCtrThanRt){
                    //level is 2
                    position = "TWO";
                    Imgproc.rectangle(workingMatrix, new Point(LEFTCOLSTART, LEFTROWSTART), new Point(LEFTCOLEND, LEFTROWEND), new Scalar(247, 181, 0));
                    Imgproc.rectangle(workingMatrix, new Point(CENTERCOLSTART, CENTERROWSTART), new Point(CENTERCOLEND, CENTERROWEND), new Scalar(247, 50, 0));
                    Imgproc.rectangle(workingMatrix, new Point(RIGHTCOLSTART, RIGHTROWSTART), new Point(RIGHTCOLEND, RIGHTROWEND), new Scalar(247, 181, 0));
            } else if (moreLftThanCtr && moreLftThanRt) {
                    //level is 1
                    position = "ONE";
                    Imgproc.rectangle(workingMatrix, new Point(LEFTCOLSTART, LEFTROWSTART), new Point(LEFTCOLEND, LEFTROWEND), new Scalar(247, 50, 0));
                    Imgproc.rectangle(workingMatrix, new Point(CENTERCOLSTART, CENTERROWSTART), new Point(CENTERCOLEND, CENTERROWEND), new Scalar(247, 181, 0));
                    Imgproc.rectangle(workingMatrix, new Point(RIGHTCOLSTART, RIGHTROWSTART), new Point(RIGHTCOLEND, RIGHTROWEND), new Scalar(247, 181, 0));
            } else {
                    position = "THREE";
                    Imgproc.rectangle(workingMatrix, new Point(LEFTCOLSTART, LEFTROWSTART), new Point(LEFTCOLEND, LEFTROWEND), new Scalar(247, 181, 0));
                    Imgproc.rectangle(workingMatrix, new Point(CENTERCOLSTART, CENTERROWSTART), new Point(CENTERCOLEND, CENTERROWEND), new Scalar(247, 181, 0));
                    Imgproc.rectangle(workingMatrix, new Point(RIGHTCOLSTART, RIGHTROWSTART), new Point(RIGHTCOLEND, RIGHTROWEND), new Scalar(247, 50, 0));
            }
            return workingMatrix;
        }

        public String getPosition () {
            return position;
        }

        public String printCenter () {
            return "Center" + centerTotal;
        }

        public String printLeft () { return "Left" + leftTotal; }

         public String printRight () { return "Right" + rightTotal; }

        public int duckLevel() {
            int level;
            if (getPosition().equals("ONE")) {
                level = 1;
            } else if (getPosition().equals("TWO")) {
                level = 2;
            } else {
                level = 3;
            }
            return level;
        }
    }
