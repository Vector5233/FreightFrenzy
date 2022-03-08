package org.firstinspires.ftc.teamcode;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class DuckDetector extends OpenCvPipeline {

    int LEFTCOLSTART = 50;
    int LEFTCOLEND = 250;
    int ROWSTART = 200;
    int ROWEND = 500;
    int CENTERCOLSTART = 350;
    int CENTERCOLEND = 560;
    int RIGHTCOLSTART = 550;
    int RIGHTCOLEND = 750;


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

            //calculateBox();

            //sets the squares on the drivers hub
            Mat matRight = workingMatrix.submat(ROWSTART, ROWEND, RIGHTCOLSTART, RIGHTCOLEND);
            Mat matCenter = workingMatrix.submat(ROWSTART, ROWEND, CENTERCOLSTART, CENTERCOLEND);
            Mat matLeft = workingMatrix.submat(ROWSTART, ROWEND, LEFTCOLSTART, LEFTCOLEND);

            //adds up the concentration of yellow in each square
            leftTotal = Core.sumElems(matLeft).val[0];
            centerTotal = Core.sumElems(matCenter).val[0];
            rightTotal = Core.sumElems(matRight).val[0];


             boolean moreCtrThanLft = centerTotal > leftTotal;
             boolean moreCtrThanRt = centerTotal > rightTotal;
             boolean moreLftThanCtr = leftTotal > centerTotal;
             boolean moreLftThanRt = leftTotal > rightTotal;


            //checks which square has the most concentration of yellow
            //corresponds to each level
            if (moreCtrThanLft && moreCtrThanRt){
                    //level is 2
                    position = "TWO";
                    Imgproc.rectangle(workingMatrix, new Point(LEFTCOLSTART, ROWSTART), new Point(LEFTCOLEND, ROWEND), new Scalar(247, 181, 0));
                    Imgproc.rectangle(workingMatrix, new Point(CENTERCOLSTART, ROWSTART), new Point(CENTERCOLEND, ROWEND), new Scalar(247, 50, 0));
                    Imgproc.rectangle(workingMatrix, new Point(RIGHTCOLSTART, ROWSTART), new Point(RIGHTCOLEND, ROWEND), new Scalar(247, 181, 0));
            } else if (moreLftThanCtr && moreLftThanRt) {
                    //level is 1
                    position = "ONE";
                    Imgproc.rectangle(workingMatrix, new Point(LEFTCOLSTART, ROWSTART), new Point(LEFTCOLEND, ROWEND), new Scalar(247, 50, 0));
                    Imgproc.rectangle(workingMatrix, new Point(CENTERCOLSTART, ROWSTART), new Point(CENTERCOLEND, ROWEND), new Scalar(247, 181, 0));
                    Imgproc.rectangle(workingMatrix, new Point(RIGHTCOLSTART, ROWSTART), new Point(RIGHTCOLEND, ROWEND), new Scalar(247, 181, 0));
            } else {
                    position = "THREE";
                    Imgproc.rectangle(workingMatrix, new Point(LEFTCOLSTART, ROWSTART), new Point(LEFTCOLEND, ROWEND), new Scalar(247, 181, 0));
                    Imgproc.rectangle(workingMatrix, new Point(CENTERCOLSTART, ROWSTART), new Point(CENTERCOLEND, ROWEND), new Scalar(247, 181, 0));
                    Imgproc.rectangle(workingMatrix, new Point(RIGHTCOLSTART, ROWSTART), new Point(RIGHTCOLEND, ROWEND), new Scalar(247, 50, 0));
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
