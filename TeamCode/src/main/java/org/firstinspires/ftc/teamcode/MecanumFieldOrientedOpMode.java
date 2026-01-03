package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.mechanisms.MecanumDrive;

@TeleOp
public class MecanumFieldOrientedOpMode extends OpMode {

    MecanumDrive drive = new MecanumDrive();

    double forward, strafe, rotate;

    @Override
    public void init() {
        drive.init(hardwareMap);
    }


    @Override
    public void loop() {
        forward = -gamepad1.left_stick_y;
        strafe = gamepad1.left_stick_x;
        rotate = gamepad1.right_stick_x;

        drive.driveFieldRelative2(forward, strafe,rotate);

        telemetry.addData("X pos", drive.returnPosX());
        telemetry.addData("Y pos", drive.returnPosY());
        telemetry.addData("Heading", drive.returnHeading());

    }
}
