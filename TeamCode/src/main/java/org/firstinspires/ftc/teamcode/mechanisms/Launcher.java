package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.StarterBotTeleop;

public class Launcher {
    private final double FEED_TIME_SECONDS = 0.20;
    private final double FULL_SPEED = 1.0;
    private double LAUNCHER_TARGET_VELOCITY = 1125;
    private final double STOP_SPEED = 0.0;
    private double LAUNCHER_MIN_VELOCITY = 1075;
    private DcMotorEx launcher;
    private CRServo leftFeeder, rightFeeder;
    ElapsedTime feederTimer = new ElapsedTime();
    private enum LaunchState {
        IDLE,
        SPIN_UP,
        LAUNCH,
        LAUNCHING,
    }
    private LaunchState launchState;

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
                launcher.setVelocity(LAUNCHER_TARGET_VELOCITY);
                if (launcher.getVelocity() >= LAUNCHER_MIN_VELOCITY) {
                    launchState = LaunchState.LAUNCH;
                }
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

    public void stopLauncher() {
        stopFeeder();
        launcher.setVelocity(STOP_SPEED);
        launchState = LaunchState.IDLE;
    }

    public double getVelocity() {
        return launcher.getVelocity();
    }

    public String getState() {
        return launchState.toString();
    }

    public void setTargetVelocity(double velocity) {
        LAUNCHER_TARGET_VELOCITY += velocity;
        LAUNCHER_MIN_VELOCITY += velocity;
        if (launchState != LaunchState.IDLE) {
            launcher.setVelocity(LAUNCHER_TARGET_VELOCITY);
        }
    }
}