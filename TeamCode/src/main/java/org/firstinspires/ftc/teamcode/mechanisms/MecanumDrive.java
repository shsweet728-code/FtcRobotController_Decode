package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class MecanumDrive {

    GoBildaPinpointDriver imuExt;
    private DcMotor frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor;

    private IMU imu;

    public void init(HardwareMap hwMap){
        frontLeftMotor = hwMap.get(DcMotor.class, "front_left_motor");
        frontRightMotor = hwMap.get(DcMotor.class, "front_right_motor");
        backLeftMotor = hwMap.get(DcMotor.class, "back_left_motor");
        backRightMotor = hwMap.get(DcMotor.class, "back_right_motor");

        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftMotor.setDirection((DcMotor.Direction.REVERSE));

        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        imuExt = hwMap.get(GoBildaPinpointDriver.class, "imu_ext");

        //Pinpoint setup
        imuExt.setOffsets(65,75, DistanceUnit.MM);
        imuExt.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        imuExt.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.REVERSED,GoBildaPinpointDriver.EncoderDirection.FORWARD);

        //Set Pinpoint starting position
        imuExt.resetPosAndIMU();
        Pose2D startingPosition = new Pose2D(DistanceUnit.MM,0,0,AngleUnit.RADIANS,0);
        imuExt.setPosition(startingPosition);

        //Setup internal IMU
        imu = hwMap.get(IMU.class, "imu");

        RevHubOrientationOnRobot revOrientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD);

        imu.initialize(new IMU.Parameters(revOrientation));

    }

    public void drive(double forward, double strafe, double rotate){
        double frontLeftPower = forward + strafe + rotate;
        double backLeftPower   = forward - strafe + rotate;
        double frontRightPower = forward - strafe - rotate;
        double backRightPower = forward + strafe - rotate;

        double maxPower = 1;
        double maxSpeed = 1;

        maxPower = Math.max(maxPower, Math.abs(frontLeftPower));
        maxPower = Math.max(maxPower, Math.abs(backLeftPower));
        maxPower = Math.max(maxPower, Math.abs(frontRightPower));
        maxPower = Math.max(maxPower, Math.abs(backRightPower));

        frontLeftMotor.setPower(maxSpeed * (frontLeftPower/maxPower));
        frontRightMotor.setPower(maxSpeed * (frontRightPower/maxPower));
        backLeftMotor.setPower(maxSpeed * (backLeftPower/maxPower));
        backRightMotor.setPower(maxSpeed * (backRightPower/maxPower));

    }

    public void driveFieldRelative (double forward, double strafe, double rotate){
        double theta = Math.atan2(forward, strafe);
        double r = Math.hypot(strafe, forward);

        theta = AngleUnit.normalizeRadians(theta - imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));

        double newForward = r * Math.sin(theta);
        double newStrafe = r * Math.cos(theta);

        this.drive(newForward, newStrafe, rotate);
    }

    public double returnHeading(){
        return imuExt.getHeading(AngleUnit.DEGREES);
    }

    public double returnPosX(){
        return imuExt.getPosX(DistanceUnit.MM);
    }

    public double returnPosY(){
        return imuExt.getPosY(DistanceUnit.MM);
    }


    public void driveFieldRelative2 (double forward, double strafe, double rotate){
        Pose2D pos = imuExt.getPosition();
        double heading = -pos.getHeading(AngleUnit.RADIANS);

        double cosAngle = Math.cos(heading);
        double sinAngle = Math.sin(heading);

        double newForward = forward * cosAngle + strafe * sinAngle;
        double newStrafe = -forward * sinAngle + strafe * cosAngle;

        rotate = rotate * 1.1;

        this.drive(newForward, newStrafe, rotate);

        imuExt.update();

    }



}
