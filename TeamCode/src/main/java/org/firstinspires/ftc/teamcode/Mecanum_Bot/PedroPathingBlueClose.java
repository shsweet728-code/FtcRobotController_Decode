package org.firstinspires.ftc.teamcode.Mecanum_Bot;


import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.mechanisms.Launcher;
import org.firstinspires.ftc.teamcode.mechanisms.MecanumDrive;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
@Autonomous(name="Blue Close", group="Autonomous")
public class PedroPathingBlueClose extends OpMode {
    private Follower follower;
    private Timer pathTimer, opModeTimer;
    Launcher launcher = new Launcher();

    MecanumDrive drive = new MecanumDrive();
    private ElapsedTime runTime = new ElapsedTime();
    public enum PathState {
        //Start Position_End Position

        DRIVE_STARTPOS_SHOOT_POS,
        SHOOT_PRELOAD,
        TURN_FOR_OPMODE
    }

    PathState pathState;

    private final Pose startPose = new Pose(21.113673805601323, 124.78418451400331, Math.toRadians(144));
    private final Pose shootPose = new Pose(43.176276771004936, 104.38220757825371, Math.toRadians(141));
    private final Pose finishPose = new Pose(18, 67, Math.toRadians(180));


    private PathChain driveStartShoot,turnAfterShot;

    public void buildPath() {
        driveStartShoot = follower.pathBuilder()
                .addPath(new BezierLine(startPose, shootPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), shootPose.getHeading())
                .build();
        turnAfterShot = follower.pathBuilder()
                .addPath(new BezierLine(shootPose, finishPose))
                .setLinearHeadingInterpolation(shootPose.getHeading(), finishPose.getHeading())
                .build();
    }

    public void statePathUpdate() {
        switch(pathState) {
            case DRIVE_STARTPOS_SHOOT_POS:
                follower.followPath(driveStartShoot, true);
                setPathState(PathState.SHOOT_PRELOAD);
                telemetry.addLine("Done Path 1");
                break;
            case SHOOT_PRELOAD:
                if (!follower.isBusy()) {
                    for (int i = 0; i < 3; i++) {
                        launcher.startLauncher();
                        runTime.reset();
                        while (/*opModeIsActive() &&*/ (runTime.seconds() < 2)) {
                            launcher.updateState();
                        }
                    }
                    telemetry.addLine("Done Shooting");
                    follower.followPath(turnAfterShot, true);
                    setPathState(PathState.TURN_FOR_OPMODE);


                }
                break;
            case TURN_FOR_OPMODE:
                if (!follower.isBusy()) {
                    telemetry.addLine("End Pose ");
                }
                break;
            default:
                telemetry.addLine("No State Commanded!");
                break;
        }
    }

    public void setPathState(PathState newState){
        pathState= newState;
        pathTimer.resetTimer();
    }

    @Override
    public void init() {
        pathState = PathState.DRIVE_STARTPOS_SHOOT_POS;
        pathTimer = new Timer();
        opModeTimer = new Timer();
        launcher.init(hardwareMap);
        drive.init(hardwareMap);
        follower = Constants.createFollower(hardwareMap);
        buildPath();

        follower.setPose(startPose);

    }

    public void start(){
        opModeTimer.resetTimer();
        setPathState(pathState);
    }

    @Override
    public void loop() {
        follower.update();
        statePathUpdate();

        telemetry.addData("path state", pathState.toString());
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.addData("path time", pathTimer.getElapsedTimeSeconds());

    }
}