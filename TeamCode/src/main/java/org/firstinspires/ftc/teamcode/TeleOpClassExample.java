package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.ArcadeDrive;
import org.firstinspires.ftc.teamcode.mechanisms.Launcher;

@TeleOp
public class TeleOpClassExample extends OpMode {
    ArcadeDrive drive = new ArcadeDrive();
    Launcher launcher = new Launcher();

    @Override
    public void init() {
        drive.init(hardwareMap);
        launcher.init(hardwareMap);
    }

    @Override
    public void loop() {
        drive.drive(-gamepad1.left_stick_y, -gamepad1.right_stick_x);

        if (gamepad1.y) {
            launcher.startLauncher();
        }
        else if (gamepad1.b) {
            launcher.stopLauncher();
        }

        //launcher.setTargetVelocity(gamepad1.right_trigger);
       // launcher.setTargetVelocity(-gamepad1.left_trigger);
        /*
        if (gamepad1.right_trigger > 0.1) {
            launcher.setTargetVelocity(gamepad1.right_trigger);
        }
        else if (gamepad1.left_trigger > 0.1) {
            launcher.setTargetVelocity(-gamepad1.left_trigger);
        }
         */

        launcher.updateState();

        telemetry.addData("Left Y", gamepad1.left_stick_y);
        telemetry.addData("Right X",gamepad1.right_stick_x);
        telemetry.addData("Left Power", drive.getPowerLeft());
        telemetry.addData("Right Power",drive.getPowerRight());
        telemetry.addData("State", launcher.getState());
        telemetry.addData("Launcher Velocity", launcher.getVelocity());
    }
}