package org.firstinspires.ftc.teamcode.objj;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;
import org.opencv.core.Size;
import java.util.ArrayList;
import java.util.List;

// YellowObjectDetectionPipeline can only be accessed within the outer class, so it's private
class YellowObjectDetectionPipeline extends OpenCvPipeline {
    private Mat hsvImage;
    private Mat mask;
    private Mat maskedImage;
    private double xInches;
    private double yInches;
    private double area;
    private volatile boolean yellowObjectDetected;
    private Rect boundingRect;

    // Calibration factors (to be determined based on your camera setup)
    private static final double PIXELS_PER_INCH_X = 10.0;
    private static final double PIXELS_PER_INCH_Y = 8.0;

    public YellowObjectDetectionPipeline() {
        hsvImage = new Mat();
        mask = new Mat();
        maskedImage = new Mat();
        boundingRect = new Rect();
    }

    @Override
    public Mat processFrame(Mat input) {
        // Clone the input frame to keep the original intact
        Mat frame = input.clone();

        // Apply Gaussian blur to the frame
        int kernelSize = 5;
        Imgproc.GaussianBlur(frame, frame, new Size(kernelSize, kernelSize), 0);

        // Convert the frame to the HSV color space
        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGB2HSV);

        // Perform yellow color masking
        Scalar lowerYellow = new Scalar(20, 100, 100);
        Scalar upperYellow = new Scalar(45, 255, 255);
        Core.inRange(frame, lowerYellow, upperYellow, mask);

        // Apply the mask to the frame
        Core.bitwise_and(frame, frame, frame, mask);

        // Find contours of the yellow object
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // Process the contours and find the largest one
        double maxArea = 0;
        Rect maxRect = null;
        for (MatOfPoint contour : contours) {
            Rect rect = Imgproc.boundingRect(contour);
            double area = rect.width * rect.height;
            if (area > maxArea) {
                maxArea = area;
                maxRect = rect;
            }
        }

        if (maxRect != null) {
            // Draw bounding rectangle on the frame
            Imgproc.rectangle(frame, maxRect.tl(), maxRect.br(), new Scalar(0, 255, 0), 2);
            this.area = maxArea;

            // Calculate center of the rectangle
            int cX = maxRect.x + maxRect.width / 2;
            int cY = maxRect.y + maxRect.height / 2;

            // Get frame dimensions
            int frameWidth = frame.cols();
            int frameHeight = frame.rows();

            // Convert to coordinates where (0, 0) is the center of the frame
            double xPixels = cX - frameWidth / 2.0;
            double yPixels = frameHeight / 2.0 - cY;

            // Convert to inches using calibration factors
            this.xInches = xPixels / PIXELS_PER_INCH_X;
            this.yInches = yPixels / PIXELS_PER_INCH_Y;
        }

        // Release resources
        mask.release();
        hierarchy.release();

        return frame;
    }

    public boolean isYellowObjectDetected() {
        return yellowObjectDetected;
    }

    public double getArea() {
        return this.area;
    }

    public double getX() {
        return this.xInches;
    }

    public double getY() {
        return this.yInches;
    }
}