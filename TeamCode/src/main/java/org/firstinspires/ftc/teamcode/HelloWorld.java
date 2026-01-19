package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.mechanisms.Functions;


@Disabled
@TeleOp
public class HelloWorld extends OpMode {

    private DcMotor motor = null;
    Functions Functions = new Functions();
    double mode;

    double modeModifier;
    boolean leftButtonPressed;
    boolean rightButtonPressed;

    @Override
    public void init() {
        motor = hardwareMap.get(DcMotor.class, "launcher");
        Functions.setAngle(0);
        mode = 1;
        leftButtonPressed = false;
        rightButtonPressed = false;
    }

    @Override
    public void loop() {

        double changeAngle = 0.1 * gamepad1.right_stick_y;

        if (gamepad1.right_stick_y > 0.1) {
            Functions.changeAngle(changeAngle);
        }
        else if (gamepad1.right_stick_y < -0.1) {
            Functions.changeAngle(changeAngle);
        }

        if (gamepad1.left_bumper&&!leftButtonPressed) {
            modeModifier= Functions.changeMode(true);
            motor.setPower(0.25 * modeModifier);
        }
        else if(gamepad1.right_bumper&&!rightButtonPressed){
            modeModifier= Functions.changeMode(false);
            motor.setPower(0.25 * modeModifier);
        }
        leftButtonPressed=gamepad1.left_bumper;
        rightButtonPressed=gamepad1.right_bumper;
        telemetry.addData("Angle", Functions.getAngle());
        telemetry.addData("Mode", Functions.getMode());
        telemetry.addData("Speed", motor.getPower());
    }
}