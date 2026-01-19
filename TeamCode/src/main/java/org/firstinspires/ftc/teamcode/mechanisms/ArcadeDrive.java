package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ArcadeDrive {
    private DcMotor leftDrive, rightDrive;
    private double leftPower, rightPower;

    private final double SPIN_DAMPING = 1.5;//higher means slower turn
    private final double THROTTLE_DAMPING = 1.5;

    public void init(HardwareMap hwMap) {
        leftDrive = hwMap.get(DcMotor.class, "left_drive");
        rightDrive = hwMap.get(DcMotor.class, "right_drive");

        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftDrive.setDirection(DcMotor.Direction.REVERSE);

        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void drive(double throttle, double spin, double turbo) {
        spin /= SPIN_DAMPING - (turbo/3);
        throttle /= THROTTLE_DAMPING - (turbo/2);
        leftPower = (throttle + spin);
        rightPower = (throttle - spin);
        double largest = Math.max(Math.abs(throttle), Math.abs(spin));
        if (largest > 1.0) {
            throttle /= largest;
            spin /= largest;
        }

        leftDrive.setPower(leftPower);
        rightDrive.setPower(rightPower);
    }

    public double getPowerLeft(){
        return leftPower;
    }

    public double getPowerRight(){
        return rightPower;
    }
}