package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.pedropathing.util.Timer;

@TeleOp
public class PedroPathingTest extends OpMode {
    private Follower follower;
    private Timer pathTimer, opModeTimer;

    public enum PathState {
        //Start Position_End Position

        DRIVE_STARTPOS_SHOOT_POS,
        SHOOT_PRELOAD
    }

    PathState pathState;

    private final Pose startPose = new Pose(21.113673805601323, 124.78418451400331, Math.toRadians(144));
    private final Pose shootPose = new Pose(63.59143327841845, 41.21416803953872, Math.toRadians(144));

    private PathChain driveStartSquare;

    public void buildPath() {
        driveStartSquare = follower.pathBuilder()
                .addPath(new BezierLine(startPose, shootPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), shootPose.getHeading())
                .build();
    }

    public void statePathUpdate() {
        switch(pathState) {
            case DRIVE_STARTPOS_SHOOT_POS:
                follower.followPath(driveStartSquare, true);
                pathState = PathState.SHOOT_PRELOAD;
                break;
            case SHOOT_PRELOAD:
                if (follower.isBusy()) {
                    telemetry.addLine("Done Path 1");
                }
        }
    }

    @Override
    public void init() {

    }

    @Override
    public void loop() {

    }
}