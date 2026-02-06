package org.firstinspires.ftc.teamcode.Mecanum_Bot;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.mechanisms.Launcher;
import org.firstinspires.ftc.teamcode.mechanisms.MecanumDrive;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import java.util.function.Supplier;
@Disabled
@Configurable
@TeleOp
public class MecanumPedroTeleOpMode
        extends OpMode {

    //Pedro Pathing Code
    private Follower follower;
    public static Pose startingPose;
    private boolean automatedDrive;
    private Supplier<PathChain> pathChain;
    private TelemetryManager telemetryM;
    private boolean slowMode = false;
    private double slowModeMultiplier = .5;

    //Toggle Code
    Gamepad currentGamepad1 = new Gamepad();
    Gamepad previousGamepad1 = new Gamepad();

    //Misc Variables
    double speedReduction;
    double forward, strafe, rotate;

    //private boolean hasLaunched = false;

    private ElapsedTime runTime = new ElapsedTime();

    //Define new instances
    Launcher launcher = new Launcher();
    MecanumDrive drive = new MecanumDrive();

    //Define toggles
    boolean yToggle= false;
    boolean bToggle= false;
    boolean fieldRobotToggle = false;
    boolean fieldMode = false;
    boolean launcherToggle= false;
    boolean driveTypeToggle = false;

    //Constants
    double SPEED_REDUCTION = .7;

    @Override
    public void init() {
        drive.init(hardwareMap);
        launcher.init(hardwareMap);

        //Pedropathing Inits
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startingPose == null ? new Pose() : startingPose);
        follower.update();
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();

        pathChain = () -> follower.pathBuilder()  //Lazy curve generation
                .addPath(new Path(new BezierLine(follower::getPose, new Pose(45,98))))
                .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(follower::getHeading,Math.toRadians(45), .8))
                .build();
    }
    @Override
    public void start() {
        //The parameter controls whether the Follower should use break mode on the motors (using it is recommended).
        //In order to use float mode, add .useBrakeModeInTeleOp(true); to your Drivetrain Constants in Constant.java (for Mecanum)
        //If you don't pass anything in, it uses the default (false)
        follower.startTeleopDrive();
    }

    @Override
    public void loop() {
        follower.update();
        telemetryM.update();



        previousGamepad1.copy(currentGamepad1);
        currentGamepad1.copy(gamepad1);

        speedReduction = (1-SPEED_REDUCTION * gamepad1.left_trigger);

        forward = -gamepad1.left_stick_y*speedReduction;
        strafe = -gamepad1.left_stick_x*speedReduction;
        rotate = -gamepad1.right_stick_x*speedReduction;

        if(gamepad1.back && !previousGamepad1.back){
            fieldRobotToggle = !fieldRobotToggle;
            if(fieldRobotToggle){
                fieldMode = true;
            }
            else{
                fieldMode = false;
            }
        }
        //Pedropathing TeleOp Code
        if (!automatedDrive) {
            //Make the last parameter false for field-centric
            //In case the drivers want to use a "slowMode" you can scale the vectors
            //This is the normal version to use in the TeleOp
            if (!slowMode) follower.setTeleOpDrive(
                    forward,
                    strafe,
                    rotate,

                    fieldMode // Robot Centric
            );

        }
        //Automated PathFollowing
        if (gamepad1.aWasPressed()) {
            follower.followPath(pathChain.get());
            automatedDrive = true;
        }
        //Stop automated following if the follower is done
        if (automatedDrive && (gamepad1.xWasPressed() || !follower.isBusy())) {
            follower.startTeleopDrive();
            automatedDrive = false;
        }

        telemetryM.debug("position", follower.getPose());
        telemetryM.debug("velocity", follower.getVelocity());
        telemetryM.debug("automatedDrive", automatedDrive);



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
            }
            else{
                launcher.setTargetVelocity(1250);
                launcher.spinLauncher();
                //set LED color to blue
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


    }
}
