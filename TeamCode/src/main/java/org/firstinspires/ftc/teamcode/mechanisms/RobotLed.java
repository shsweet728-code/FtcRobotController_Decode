package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
@Disabled
public class RobotLed {
    private Servo ledIndicator;

    public void init(HardwareMap hwMap){
        ledIndicator = hwMap.get(Servo.class, "LED");
    }

    public void changeColor(double color){
        color = -color;
        if(color ==0){
            color = 0;
        }
        else if(color >= 0.01){
            color = -.1676767677*color + .6676767677;
        }
        else if(color <= -.01){
            color = .1090909091*color + .3890909091;
        }
        else {
            color = 0;
        }

        ledIndicator.setPosition(color);
    }
}
