package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class SingleSlideSystem {
    private DcMotor slideMotor;
    private DcMotor.Direction direction;
    private int maxExtension;
    private int tolerance;

    private int targetPosition;


    public SingleSlideSystem(HardwareMap hardwareMap, String identifier, int maxExtend, DcMotor.Direction direction, int tolerance){
        this.slideMotor = hardwareMap.get(DcMotor.class, identifier);
        this.maxExtension = maxExtend;
        this.direction = direction;

        this.tolerance = tolerance;

        this.slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.slideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.slideMotor.setDirection(this.direction);

        this.targetPosition = 0;

    }

    public int getRawPosition(){
        return this.slideMotor.getCurrentPosition();
    }

    public double getPosition(){
        int ticks = this.getRawPosition();
        return (double)ticks/this.maxExtension;
    }

    public boolean runToFraction(double percent){
        this.targetPosition = (int)(percent*this.maxExtension);
        return this.run();
    }

    public boolean runToTicks(int ticks){
        this.targetPosition = ticks;
        return this.run();
    }
    public void setPower(double power) {
        slideMotor.setPower(power);
    }
    public double get1Position() {
        return slideMotor.getCurrentPosition();
    }
    public boolean run(){
        double speed = 1;

        if (Math.abs(this.targetPosition - this.getRawPosition()) < this.tolerance){
            this.slideMotor.setPower(0);
            return false;
        }

        speed = Math.min(1,(double)Math.abs(this.targetPosition-this.getRawPosition())/(this.tolerance*2));
        speed *= .6;

        // basically, if position is greater than our positoin, move positive (>1 == 1 in the motor's mind).
        if (this.targetPosition > this.getRawPosition()){
            this.slideMotor.setPower(speed);
        }
        else{
            this.slideMotor.setPower(-speed);
        }
        return true;
    }

    public void log(Telemetry telemetry){
        telemetry.addData("Slide Encoder Position", this.getRawPosition());
        telemetry.addData("Slide Fractional Position", this.getPosition());
        telemetry.addData("Slide Target Position",this.targetPosition);
    }
}
