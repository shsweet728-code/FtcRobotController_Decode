package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LED;

public class RobotLed {
    private CRServo ledIndicator;

    public void init(HardwareMap hwMap){
        ledIndicator = hwMap.get(CRServo.class, "LED");
    }

    public void changeColor(double color){
        ledIndicator.setPower(color);
    }
}
