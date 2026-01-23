package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.mechanisms.TestIMU;
@Disabled
@TeleOp
public class IMUDegreeOpMode extends OpMode {
    TestIMU testIMU = new TestIMU();
    double heading;
    DcMotor motor;
    public void init(){
        testIMU.init(hardwareMap);

    }
    public void loop(){
         heading = testIMU.getHeading(AngleUnit.DEGREES);
         telemetry.addData("Heading",testIMU.getHeading(AngleUnit.DEGREES));
         if(heading <45  && heading > -45){
            testIMU.setMotor(0);
         }
         else if(heading > 45){
            testIMU.setMotor(.5);
         }
         else{
            testIMU.setMotor(-.5);
         }
    }
}
