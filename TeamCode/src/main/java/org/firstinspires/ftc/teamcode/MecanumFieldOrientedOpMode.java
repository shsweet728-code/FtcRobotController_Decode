package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.mechanisms.Launcher;
import org.firstinspires.ftc.teamcode.mechanisms.MecanumDrive;

@TeleOp
public class MecanumFieldOrientedOpMode extends OpMode {


    private boolean hasLaunched = false;

    private ElapsedTime runTime = new ElapsedTime();

    Launcher launcher = new Launcher();



    MecanumDrive drive = new MecanumDrive();

    double forward, strafe, rotate;

    @Override
    public void init() {
        drive.init(hardwareMap);
        launcher.init(hardwareMap);
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

        if (gamepad1.y) {
            launcher.startLauncher();
        }
        else if (gamepad1.b) {
            launcher.stopLauncher();
        }
        else if (gamepad1.x) {
            launcher.spinLauncher();
        }



        if (gamepad1.right_bumper){
            launcher.changeTargetVelocity(1);
        }
        else if (gamepad1.left_bumper){
            launcher.changeTargetVelocity(-1);
        }

        launcher.updateState();

        telemetry.addData("Left Y", gamepad1.left_stick_y);
        telemetry.addData("Right X",gamepad1.right_stick_x);
        telemetry.addData("State", launcher.getState());
        telemetry.addData("Target Velocity", launcher.getTargetVelocity());
        telemetry.addData("Launcher Velocity", launcher.getVelocity());

    }
}
