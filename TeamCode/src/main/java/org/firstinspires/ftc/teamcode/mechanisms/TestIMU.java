package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class TestIMU {
    private IMU imu;
    private DcMotor motor;
    public void init(HardwareMap hwMap){
        imu = hwMap.get(IMU.class, "imu");
        motor = hwMap.get(DcMotor.class, "launcher");
        RevHubOrientationOnRobot RevOrientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD
        );
        imu.initialize(new IMU.Parameters(RevOrientation));
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public double getHeading(AngleUnit unit){
        return imu.getRobotYawPitchRollAngles().getYaw(unit);
    }
    public void setMotor(double power){
        motor.setPower(power);
    }
}
