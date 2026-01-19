package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.mechanisms.ArcadeDrive;
import org.firstinspires.ftc.teamcode.mechanisms.Launcher;
import org.firstinspires.ftc.teamcode.mechanisms.RobotLed;
@Disabled
@TeleOp
public class TeleOpScrimmage extends OpMode {

    private boolean hasLaunched = false;

    private ElapsedTime runTime = new ElapsedTime();
    ArcadeDrive drive = new ArcadeDrive();
    Launcher launcher = new Launcher();

    RobotLed ledIndicator = new RobotLed();

    @Override
    public void init() {
        drive.init(hardwareMap);
        launcher.init(hardwareMap);
        ledIndicator.init(hardwareMap);
    }

    @Override
    public void loop() {
        drive.drive(-gamepad1.left_stick_y, gamepad1.right_stick_x, gamepad1.left_trigger);

        if (gamepad1.y) {
            launcher.startLauncher();
        }
        else if (gamepad1.b) {
            launcher.stopLauncher();
        }
        else if (gamepad1.x) {
            launcher.spinLauncher();
        }
        else if (gamepad1.a) {
            runTime.reset();
            while (runTime.seconds() < 0.5) {
                drive.drive(-1, 0, 1);
                if (runTime.seconds() > 0.35 && !hasLaunched) {
                    launcher.startLauncher();
                    hasLaunched = true;
                }
            }
            hasLaunched = false;
            runTime.reset();
            while (runTime.seconds() < 0.5) {
                drive.drive(1, 0, 1);
                launcher.updateState();
            }
        }

        ledIndicator.changeColor(gamepad1.left_stick_y);

        if (gamepad1.right_bumper){
            launcher.changeTargetVelocity(1);
        }
        else if (gamepad1.left_bumper){
            launcher.changeTargetVelocity(-1);
        }

        launcher.updateState();

        telemetry.addData("Left Y", gamepad1.left_stick_y);
        telemetry.addData("Right X",gamepad1.right_stick_x);
        telemetry.addData("Left Power", drive.getPowerLeft());
        telemetry.addData("Right Power",drive.getPowerRight());
        telemetry.addData("State", launcher.getState());
        telemetry.addData("Target Velocity", launcher.getTargetVelocity());
        telemetry.addData("Launcher Velocity", launcher.getVelocity());
    }
}