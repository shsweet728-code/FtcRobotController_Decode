package org.firstinspires.ftc.teamcode.Tank_Bot;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.mechanisms.ArcadeDrive;
import org.firstinspires.ftc.teamcode.mechanisms.Launcher;

/*
 * This OpMode illustrates the concept of driving a path based on time.
 * The code is structured as a LinearOpMode
 *
 * The code assumes that you do NOT have encoders on the wheels,
 *   otherwise you would use: RobotAutoDriveByEncoder;
 *
 *   The desired path in this example is:
 *   - Drive forward for 3 seconds
 *   - Spin right for 1.3 seconds
 *   - Drive Backward for 1 Second
 *
 *  The code is written in a simple form with no optimizations.
 *  However, there are several ways that this type of sequence could be streamlined,
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 */
@Disabled
@Autonomous(name="Red Far", group="Red")
public class CHIPS_Auto_Red_Far extends LinearOpMode {

    Launcher launcher = new Launcher();
    ArcadeDrive drive = new ArcadeDrive();

    /* Declare OpMode members. */
    private ElapsedTime runTime = new ElapsedTime();

    static  final long    PAUSE_TIME = 50;
    static final double     FORWARD_SPEED = 0.5;
    static final double     TURN_SPEED    = 0.25;
    static final double     TARGET_VELOCITY = 1400;


    @Override
    public void runOpMode() {

        launcher.init(hardwareMap);
        drive.init(hardwareMap);

        launcher.spinLauncher();
        launcher.setTargetVelocity(TARGET_VELOCITY);


        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();

        // Wait for the game to start (driver presses START)
        waitForStart();

        // Step through each leg of the path, ensuring that the OpMode has not been stopped along the way.
        // Step 1:  Drive forward off back line
        drive.drive(FORWARD_SPEED, 0, 0);
        runTime.reset();
        while (opModeIsActive() && (runTime.seconds() < 2)) {
            telemetry.addData("Path", "Leg 1: %4.1f S Elapsed", runTime.seconds());
            telemetry.update();
        }
        drive.drive(0, 0, 0);
        sleep(PAUSE_TIME);

        // Step 2:  Turn right towards red tower
        drive.drive(0, TURN_SPEED, 0);
        runTime.reset();
        while (opModeIsActive() && (runTime.seconds() < 1.1)) {
            telemetry.addData("Path", "Leg 2: %4.1f S Elapsed", runTime.seconds());
            telemetry.update();
        }
        drive.drive(0, 0, 0);
        sleep(PAUSE_TIME);

        // Step 4:  Launch ball 3 times
        for (int i = 0; i < 3; i++) {
            launcher.startLauncher();
            runTime.reset();
            while (opModeIsActive() && (runTime.seconds() < 3)) {
                launcher.updateState();
            }
        }
        launcher.stopLauncher();

        // Step 5:  Stop
        drive.drive(0, 0, 0);

        telemetry.addData("Path", "Complete");
        telemetry.update();
    }
}