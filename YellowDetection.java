package org.firstinspires.ftc.teamcode.objj;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@TeleOp(name = "YellowDetectionfaifai")
public class YellowDetection extends LinearOpMode {
    private OpenCvCamera webcam;
    private YellowObjectDetectionPipeline pipeline;

    // Calibration factors (to be determined based on your camera setup)
    private static final double PIXELS_PER_INCH_X = 10.0;
    private static final double PIXELS_PER_INCH_Y = 8.0;

    @Override
    public void runOpMode() {
        waitForStart();
        // OpenCV camera
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);

        pipeline = new YellowObjectDetectionPipeline();
        webcam.setPipeline(pipeline);

        // Open the camera device
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                // Start streaming from the camera
                webcam.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
                // Handle any errors that occur during camera opening
                telemetry.addData("Camera Error", "Error code: " + errorCode);
                telemetry.update();
            }
        });

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        while (opModeIsActive()) {
            // Access the yellow object detection result here
            boolean yellowObjectDetected = pipeline.isYellowObjectDetected();
            double xInches = pipeline.getX() / PIXELS_PER_INCH_X;
            double yInches = pipeline.getY() / PIXELS_PER_INCH_Y;
            telemetry.addData("Area of the Yellow Object: ", pipeline.getArea());
            telemetry.addData("X, Y of the Yellow Object in inches: ", xInches + ", " + yInches);
            //telemetry.addData("Yellow Object Detected: ", yellowObjectDetected);
            telemetry.update();
            idle();
        }
    }
}