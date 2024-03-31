# Yellow Object Detection System for Robotics

## Overview
This project is an advanced object detection system designed for use in robotics competitions, specifically tailored for the FIRST Tech Challenge (FTC). It utilizes computer vision techniques to accurately detect and localize yellow objects within the camera's field of view. The system is built using OpenCV and EasyOpenCV libraries, providing a robust framework for real-time object detection and analysis.

## Features
- **Real-Time Object Detection:** Utilizes a custom OpenCV pipeline to detect yellow objects in real-time.
- **3D Object Localization:** Computes X, Y, and Z coordinates to determine the precise location of the object relative to the camera.
- **Size Measurement:** Calculates the width and height of the detected object, enabling size-based analysis and decision-making.
- **Advanced Image Processing:** Incorporates Gaussian blur and color space conversion for improved detection accuracy.
- **Calibration Support:** Includes calibration factors for converting pixel measurements to real-world dimensions, which can be adjusted based on the camera setup.

## Technical Details
- **Language:** Java
- **Libraries/Frameworks:** OpenCV, EasyOpenCV
- **Platform:** FIRST Tech Challenge (FTC) robotics software framework
