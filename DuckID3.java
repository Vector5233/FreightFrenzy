package org.firstinspires.ftc.teamcode;

import static org.opencv.imgproc.Imgproc.putText;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

class DuckID3 extends OpenCvPipeline {

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

    public DuckID3(String side) {
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
            LEFTCOLSTART = 1;
            LEFTCOLEND = 100;
            CENTERCOLSTART = 105;
            CENTERCOLEND = 205;
            RIGHTCOLSTART = 205;
            RIGHTCOLEND = 305;
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
        final int SCALE = 10000;
        //channel IDs
        final int Y = 0;
        final int Cr = 1;
        final int Cb = 2;

        if (workingMatrix.empty()) {
            return input;
        }
        //sets the color scheme needed to see yellow
        Imgproc.cvtColor(workingMatrix, workingMatrix, Imgproc.COLOR_RGB2YCrCb);
        //Imgproc.GaussianBlur(workingMatrix, workingMatrix, new Size(9, 9), 0, 0);

        //calculateBox();

        //sets the squares on the drivers hub
        Mat matRight = workingMatrix.submat(RIGHTROWSTART, RIGHTROWEND, RIGHTCOLSTART, RIGHTCOLEND);
        Mat matCenter = workingMatrix.submat(CENTERROWSTART, CENTERROWEND, CENTERCOLSTART, CENTERCOLEND);
        Mat matLeft = workingMatrix.submat(LEFTROWSTART, LEFTROWEND, LEFTCOLSTART, LEFTCOLEND);

        //adds up the concentration of channels in each;
        int[] leftTotals = {(int)(Core.sumElems(matLeft).val[Y]/SCALE),
                (int)(Core.sumElems(matLeft).val[Cr]/SCALE),
                (int)(Core.sumElems(matLeft).val[Cb]/SCALE)};

        int[] centerTotals = {(int)(Core.sumElems(matCenter).val[Y]/SCALE),
                (int)(Core.sumElems(matCenter).val[Cr]/SCALE),
                (int)(Core.sumElems(matCenter).val[Cb]/SCALE)};

        int[] rightTotals = {(int)(Core.sumElems(matRight).val[Y]/SCALE),
                (int)(Core.sumElems(matRight).val[Cr]/SCALE),
                (int)(Core.sumElems(matRight).val[Cb]/SCALE)};

        leftTotal = leftTotals[Cb];
        centerTotal = centerTotals[Cb];
        rightTotal = rightTotals[Cb];

        if ((leftTotal >= centerTotal) && (leftTotal >= rightTotal)) {
            position = "ONE";
        }
        else if (centerTotal >= rightTotal) {
            position = "TWO";
        }
        else position = "THREE";

        putText(workingMatrix, "Y: " + leftTotals[Y] + "  " + centerTotals[Y] + "  " + rightTotals[Y],
                new Point(1, 190), 0, .40, new Scalar(255,255,255), 1);
        putText(workingMatrix, "Cr: " + leftTotals[Cr] + "  " + centerTotals[Cr] + "  " + rightTotals[Cr],
                new Point(1, 220), 0, .40, new Scalar(255,255,255), 1);
        putText(workingMatrix, "Cb: " + leftTotals[Cb] + "  " + centerTotals[Cb] + "  " + rightTotals[Cb],
                new Point(1, 250), 0, .40, new Scalar(255,255,255), 1);


        //checks which square has the most concentration of yellow
        //corresponds to each level
        if (position.equals("TWO")){
            Imgproc.rectangle(workingMatrix, new Point(LEFTCOLSTART, LEFTROWSTART), new Point(LEFTCOLEND, LEFTROWEND), new Scalar(247, 181, 0));
            Imgproc.rectangle(workingMatrix, new Point(CENTERCOLSTART, CENTERROWSTART), new Point(CENTERCOLEND, CENTERROWEND), new Scalar(247, 50, 0));
            Imgproc.rectangle(workingMatrix, new Point(RIGHTCOLSTART, RIGHTROWSTART), new Point(RIGHTCOLEND, RIGHTROWEND), new Scalar(247, 181, 0));
        } else if (position.equals("ONE")) {
            Imgproc.rectangle(workingMatrix, new Point(LEFTCOLSTART, LEFTROWSTART), new Point(LEFTCOLEND, LEFTROWEND), new Scalar(247, 50, 0));
            Imgproc.rectangle(workingMatrix, new Point(CENTERCOLSTART, CENTERROWSTART), new Point(CENTERCOLEND, CENTERROWEND), new Scalar(247, 181, 0));
            Imgproc.rectangle(workingMatrix, new Point(RIGHTCOLSTART, RIGHTROWSTART), new Point(RIGHTCOLEND, RIGHTROWEND), new Scalar(247, 181, 0));
        } else {
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
