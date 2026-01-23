package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Launcher {
    //Define CONSTANTS
    private final double FEED_TIME_SECONDS = .5;
    private final double FULL_SPEED = 1.0;
    private double launcherTargetVelocity = 1125;
    private final double STOP_SPEED = 0.0;
    private double launcherMinVelocity = launcherTargetVelocity - 25;

    // Declare variables
    private DcMotorEx launcher;
    private CRServo leftFeeder, rightFeeder;
    private LaunchState launchState;
    ElapsedTime feederTimer = new ElapsedTime();
    private enum LaunchState {
        IDLE,
        SPIN_UP,
        LAUNCH,
        LAUNCHING,
    }


    public void init(HardwareMap hwMap) {
        launcher = hwMap.get(DcMotorEx.class, "launcher");
        leftFeeder = hwMap.get(CRServo.class, "left_feeder");
        rightFeeder = hwMap.get(CRServo.class, "right_feeder");

        launcher.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        launcher.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        launcher.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(
                300, 0, 0, 10
        ));
        leftFeeder.setDirection(CRServo.Direction.REVERSE);

        launchState = LaunchState.IDLE;
        stopFeeder();
        stopLauncher();
    }

    public void stopFeeder() {
        leftFeeder.setPower(STOP_SPEED);
        rightFeeder.setPower(STOP_SPEED);
    }

    public void updateState() {
        switch (launchState) {
            case IDLE:
                break;
            case SPIN_UP:
                launcher.setVelocity(launcherTargetVelocity);
                if (launcher.getVelocity() >= launcherMinVelocity) {
                    launchState = LaunchState.LAUNCH;
                }
                break;
            case LAUNCH:
                leftFeeder.setPower(FULL_SPEED);
                rightFeeder.setPower(FULL_SPEED);
                feederTimer.reset();
                launchState = LaunchState.LAUNCHING;
                break;
            case LAUNCHING:
                if (feederTimer.seconds() > FEED_TIME_SECONDS) {
                    stopFeeder();
                    launchState = LaunchState.IDLE;
                }
                break;
        }
    }

    public void startLauncher() {
        if (launchState == LaunchState.IDLE) {
            launchState = LaunchState.SPIN_UP;
        }
    }

    public void spinLauncher() {
        launcher.setVelocity(launcherTargetVelocity);
    }

    public void stopLauncher() {
        stopFeeder();
        launcher.setVelocity(STOP_SPEED);
        launchState = LaunchState.IDLE;
    }

    public double getVelocity() {
        return launcher.getVelocity();
    }

    public double getTargetVelocity() {
        return launcherTargetVelocity;
    }

    public String getState() {
        return launchState.toString();
    }

    public void changeTargetVelocity(double velocity) {
        launcherTargetVelocity += velocity;
        launcherMinVelocity += velocity;
        if (getVelocity() > 500) {
            launcher.setVelocity(launcherTargetVelocity);
        }
    }

    public void runLauncher() {
        launcher.setVelocity(launcherTargetVelocity);
    }
}