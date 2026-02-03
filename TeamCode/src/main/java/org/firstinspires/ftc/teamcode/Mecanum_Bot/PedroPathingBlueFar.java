package org.firstinspires.ftc.teamcode.Mecanum_Bot;


import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.mechanisms.Launcher;
import org.firstinspires.ftc.teamcode.mechanisms.MecanumDrive;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous
public class PedroPathingBlueFar extends OpMode {
    private Follower follower;
    private Timer pathTimer, opModeTimer;
    Launcher launcher = new Launcher();

    MecanumDrive drive = new MecanumDrive();
    private ElapsedTime runTime = new ElapsedTime();
    public enum PathState {
        //Start Position_End Position

        DRIVE_STARTPOS_SHOOT_POS,
        SHOOT_PRELOAD
    }

    PathState pathState;

    private final Pose startPose = new Pose(21.113673805601323, 124.78418451400331, Math.toRadians(144));
    private final Pose shootPose = new Pose(56, 8, Math.toRadians(90));
    private final Pose FinishPose = new Pose(43.176276771004936, 104.38220757825371, Math.toRadians(90));

    private PathChain driveStartShoot;

    public void buildPath() {
        driveStartShoot = follower.pathBuilder()
                .addPath(new BezierLine(startPose, shootPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), shootPose.getHeading())
                .build();
    }

    public void statePathUpdate() {
        switch(pathState) {
            case DRIVE_STARTPOS_SHOOT_POS:
                follower.followPath(driveStartShoot, true);
                setPathState(PathState.SHOOT_PRELOAD);
                break;
            case SHOOT_PRELOAD:
                if (!follower.isBusy()) {
                    for (int i = 0; i < 3; i++) {
                        launcher.startLauncher();
                        runTime.reset();
                        while (/*opModeIsActive() &&*/ (runTime.seconds() < 3)) {
                            launcher.updateState();
                        }
                    }
                    telemetry.addLine("Done Path 1");

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
        drive.drive(0,0,0);
    }
}