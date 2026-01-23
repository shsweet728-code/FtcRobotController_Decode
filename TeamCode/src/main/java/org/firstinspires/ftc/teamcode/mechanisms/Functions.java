package org.firstinspires.ftc.teamcode.mechanisms;

public class Functions {

    double angle;
    int mode;
    public void setAngle(double Angle) {
        this.angle = Angle;
    }

    public double getAngle() {
        return this.angle;
    }

    public void changeAngle(double changeAngle) {
        this.angle += changeAngle;
        if (this.angle > 180) {
            this.angle -= 360;
        }
        else if (this.angle < -180) {
            this.angle += 360;
        }
    }

    public int changeMode(boolean change) {
        if (change) {
            mode++;
        }
        else{
            mode--;
        }
        if (mode>4){
            mode=1;
        }
        if(mode<1){
            mode=4;
        }
        return mode;
    }
    public int getMode(){
        return mode;
    }
}
