package org.firstinspires.ftc.teamcode.Mecanum_Bot;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.mechanisms.Launcher;
import org.firstinspires.ftc.teamcode.mechanisms.MecanumDrive;
import org.firstinspires.ftc.teamcode.mechanisms.RobotLed;

@TeleOp(name="Mecanum Teleop", group="Pedropathing")
public class MecanumTeleOpMode extends OpMode {

    Gamepad currentGamepad1 = new Gamepad();
    Gamepad previousGamepad1 = new Gamepad();

    double speedReduction;

    private boolean hasLaunched = false;

    private ElapsedTime runTime = new ElapsedTime();
    Launcher launcher = new Launcher();
    RobotLed ledIndicator = new RobotLed();

    //Define toggles
    boolean yToggle= false;
    boolean bToggle= false;
    boolean launcherToggle= false;
    boolean driveTypeToggle = false;

    //Constants
    double SPEED_REDUCTION = .7;
    double BLUE = .611;
    double RED = .28;
    MecanumDrive drive = new MecanumDrive();

    double forward, strafe, rotate;

    @Override
    public void init() {
        drive.init(hardwareMap);
        launcher.init(hardwareMap);
        ledIndicator.init(hardwareMap);
        ledIndicator.launcherSpeed(BLUE);
    }


    @Override
    public void loop() {

        previousGamepad1.copy(currentGamepad1);
        currentGamepad1.copy(gamepad1);

        speedReduction = (1-SPEED_REDUCTION * gamepad1.left_trigger);

        forward = -gamepad1.left_stick_y*speedReduction;
        strafe = gamepad1.left_stick_x*speedReduction;
        rotate = gamepad1.right_stick_x*speedReduction;

        //Toggle drive mode between Fieldcentric and Robotcentric
        if (currentGamepad1.back && !previousGamepad1.back){
            driveTypeToggle = !driveTypeToggle;
            drive.init(hardwareMap);
        }
        if(driveTypeToggle) {
            drive.driveFieldRelative2(forward, strafe, rotate);
        }
        else {
            drive.drive(forward,strafe,rotate);
        }

        telemetry.addData("X pos", drive.returnPosX());
        telemetry.addData("Y pos", drive.returnPosY());
        telemetry.addData("Heading", drive.returnHeading());

        //If 'R Trigger' pressed
        if (gamepad1.right_trigger_pressed) {
            launcher.startLauncher();
        }

        if(currentGamepad1.y && !previousGamepad1.y){
            yToggle = !yToggle;
            if(yToggle) {
                launcher.setTargetVelocity(1400);
                launcher.spinLauncher();
                //set LED color to red
                ledIndicator.launcherSpeed(RED);
            }
            else{
                launcher.setTargetVelocity(1250);
                launcher.spinLauncher();
                //set LED color to blue
                ledIndicator.launcherSpeed(BLUE);
            }
        }


        //If 'b' pressed, toggle switch
        if (currentGamepad1.b && !previousGamepad1.b) {
            bToggle = !bToggle;
            if(bToggle) {
                launcher.spinLauncher();
            }
            else{
                launcher.stopLauncher();
            }

        }

        //If 'x' pressed
        if (gamepad1.x) {

        }
        //If 'a' pressed
        if (gamepad1.a) {

        }


        //If bumpers pressed
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
        if(driveTypeToggle){
            telemetry.addData("Fieldcentric", driveTypeToggle);
        }
        else {
            telemetry.addData("Robotcentric", !driveTypeToggle);
        }

    }
}
