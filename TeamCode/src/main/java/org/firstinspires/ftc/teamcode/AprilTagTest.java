package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.mechanisms.AprilTagWebcam;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@Disabled
@Autonomous
public class AprilTagTest extends OpMode {
    AprilTagWebcam aprilTagWebcam = new AprilTagWebcam();

    public void init(){
        aprilTagWebcam.init(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        //update the vision portal
        aprilTagWebcam.update();
        AprilTagDetection id21 = aprilTagWebcam.getTagBySpecificId(21);
        aprilTagWebcam.displayDetectionTelemetry(id21);
    }
}
