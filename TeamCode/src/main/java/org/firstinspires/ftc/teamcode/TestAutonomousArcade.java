package org.firstinspires.ftc.teamcode;

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
@Autonomous(name="Robot: Auto Drive Blue Arcade by LS", group="Robot")
public class TestAutonomousArcade extends LinearOpMode {

    Launcher launcher = new Launcher();
    ArcadeDrive drive = new ArcadeDrive();

    /* Declare OpMode members. */
    private ElapsedTime runTime = new ElapsedTime();

    static  final long    PAUSE_TIME = 500;
    static final double     FORWARD_SPEED = 0.5;
    static final double     TURN_SPEED    = 0.25;

    @Override
    public void runOpMode() {

        launcher.init(hardwareMap);
        drive.init(hardwareMap);

        launcher.spinLauncher();

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();

        // Wait for the game to start (driver presses START)
        waitForStart();

        // Step through each leg of the path, ensuring that the OpMode has not been stopped along the way.
        // Step 1:  Drive forward for 3 seconds
        drive.drive(FORWARD_SPEED, 0, 0);
        runTime.reset();
        while (opModeIsActive() && (runTime.seconds() < 1.5)) {
            telemetry.addData("Path", "Leg 1: %4.1f S Elapsed", runTime.seconds());
            telemetry.update();
        }
        drive.drive(0, 0, 0);
        sleep(PAUSE_TIME);

        // Step 2:  Spin right for 1.3 seconds
        drive.drive(0, -TURN_SPEED, 0);
        runTime.reset();
        while (opModeIsActive() && (runTime.seconds() < 0.75)) {
            telemetry.addData("Path", "Leg 2: %4.1f S Elapsed", runTime.seconds());
            telemetry.update();
        }
        drive.drive(0, 0, 0);
        sleep(PAUSE_TIME);

        // Step 1:  Drive forward for 3 seconds
        drive.drive(FORWARD_SPEED, 0, 0);
        runTime.reset();
        while (opModeIsActive() && (runTime.seconds() < 2.9)) {
            telemetry.addData("Path", "Leg 1: %4.1f S Elapsed", runTime.seconds());
            telemetry.update();
        }
        drive.drive(0, 0, 0);
        sleep(PAUSE_TIME);

        drive.drive(0, 0, 0);
        sleep(PAUSE_TIME);
        for (int i = 0; i < 3; i++) {
            launcher.startLauncher();
            runTime.reset();
            while (opModeIsActive() && (runTime.seconds() < 3)) {
                launcher.updateState();
            }
        }
        launcher.stopLauncher();

        // Step 3:  Drive Backward for 1 Second
        drive.drive(-FORWARD_SPEED, 0, 0);
        runTime.reset();
        while (opModeIsActive() && (runTime.seconds() < 3.0)) {
            telemetry.addData("Path", "Leg 3: %4.1f S Elapsed", runTime.seconds());
            telemetry.update();
        }

        // Step 4:  Stop
        drive.drive(0, 0, 0);

        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(PAUSE_TIME);
    }
}