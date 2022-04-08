package org.firstinspires.ftc.teamcode;

import static org.opencv.imgproc.Imgproc.putText;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class duckID2 extends OpenCvPipeline {
    int LEFTROWSTART;
    int LEFTROWEND;
    int CENTERROWSTART;
    int CENTERROWEND;
    int RIGHTROWSTART;
    int RIGHTROWEND;
    int LEFTCOLSTART;
    int LEFTCOLEND;
    int CENTERCOLSTART;
    int CENTERCOLEND;
    int RIGHTCOLSTART;
    int RIGHTCOLEND;


    public duckID2(String side){
        if (side == "red") {
            LEFTROWSTART = 100;
            LEFTROWEND = 200;
            CENTERROWSTART = 75;
            CENTERROWEND = 175;
            RIGHTROWSTART = 70;
            RIGHTROWEND = 170;
            LEFTCOLSTART = 1;
            LEFTCOLEND = 100;
            CENTERCOLSTART = 105;
            CENTERCOLEND = 205;
            RIGHTCOLSTART = 205;
            RIGHTCOLEND = 305;
        } else if (side == "blue") {
            LEFTROWSTART = 25;
            LEFTROWEND = 125;
            CENTERROWSTART = 25;
            CENTERROWEND = 125;
            RIGHTROWSTART = 70;
            RIGHTROWEND = 170;
            LEFTCOLSTART = 25;//1;
            LEFTCOLEND = 75;//100;
            CENTERCOLSTART = 130;//105;
            CENTERCOLEND = 180;//205;
            RIGHTCOLSTART = 230;//205;
            RIGHTCOLEND = 280;//305;
        } else {
            ;
        }
    }


    private Mat workingMatrix = new Mat();

    private String position = "nothing yet";


    public double leftTotal;
    public double centerTotal;
    public double rightTotal;
    @Override
    public final Mat processFrame (Mat input){
        input.copyTo(workingMatrix);

        if (workingMatrix.empty()) {
            return input;
        }
        //sets the color scheme needed to see yellow
        Imgproc.cvtColor(workingMatrix, workingMatrix, Imgproc.COLOR_RGB2YCrCb);
        Imgproc.GaussianBlur(workingMatrix, workingMatrix, new Size(9, 9), 0, 0);

        //calculateBox();

        //sets the squares on the drivers hub
        Mat matRight = workingMatrix.submat(RIGHTROWSTART, RIGHTROWEND, RIGHTCOLSTART, RIGHTCOLEND);
        Mat matCenter = workingMatrix.submat(CENTERROWSTART, CENTERROWEND, CENTERCOLSTART, CENTERCOLEND);
        Mat matLeft = workingMatrix.submat(LEFTROWSTART, LEFTROWEND, LEFTCOLSTART, LEFTCOLEND);

        //adds up the concentration of yellow in each square
        leftTotal = Core.sumElems(matLeft).val[2];
        centerTotal = Core.sumElems(matCenter).val[2];
        rightTotal = Core.sumElems(matRight).val[2];

        boolean LessCtrThanLft = centerTotal < leftTotal;
        boolean lessCtrThanRt = centerTotal < rightTotal;
        boolean lessLftThanCtr = leftTotal < centerTotal;
        boolean lessLftThanRt = leftTotal < rightTotal;

        putText(workingMatrix, "Left Total: " + leftTotal + ", Right Total: " + centerTotal + ", Right Total: " + rightTotal, new Point(1, 250), 0, .25, new Scalar(255,255,255), 1);

        //checks which square has the most concentration of blue
        //corresponds to each level
        if (LessCtrThanLft && lessCtrThanRt){
            //level is 2
            position = "TWO";
            Imgproc.rectangle(workingMatrix, new Point(LEFTCOLSTART, LEFTROWSTART), new Point(LEFTCOLEND, LEFTROWEND), new Scalar(247, 181, 0));
            Imgproc.rectangle(workingMatrix, new Point(CENTERCOLSTART, CENTERROWSTART), new Point(CENTERCOLEND, CENTERROWEND), new Scalar(247, 50, 0));
            Imgproc.rectangle(workingMatrix, new Point(RIGHTCOLSTART, RIGHTROWSTART), new Point(RIGHTCOLEND, RIGHTROWEND), new Scalar(247, 181, 0));
        } else if (lessLftThanCtr && lessLftThanRt) {
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

        matRight.release();
        matCenter.release();
        matLeft.release();
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

    //corresponds position to each level
    public int duckLevel() {
        int level;
        if (getPosition() == null){


            level = 2;
        }
        else if (getPosition().equals("ONE")) {
            level = 1;
        } else if (getPosition().equals("TWO")) {
            level = 2;
        } else {
            level = 3;
        }
        return level;
    }
}
