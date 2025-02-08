package org.firstinspires.ftc.teamcode.subsystems;

import android.transition.Slide;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class SlideSubsystem {
    public enum Slide{
        SLIDE_A,
        SLIDE_B
    }

    private DcMotor slideA;
    private DcMotor slideB;

    private int maxExtentA;
    private int maxExtentB;

    private double tolerance = 0.03;

    public double target; // where target is a double 0.0-1.0

    public SlideSubsystem(HardwareMap hardwareMap, String a, String b, int maxExtentA, int maxExtentB){
        this.maxExtentA = maxExtentA;
        this.maxExtentB = maxExtentB;

        this.slideA = hardwareMap.get(DcMotor.class, a);
        this.slideB = hardwareMap.get(DcMotor.class, b);

        //Motor A is the forward motor.

        this.slideA.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.slideA.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.slideA.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.slideA.setDirection(DcMotor.Direction.FORWARD);

        this.slideB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.slideB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.slideB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.slideA.setDirection(DcMotor.Direction.FORWARD);
    }

    public void setTarget(double target){
        this.target = target;
    }

    public double getPosition(Slide slide){
        if (slide == Slide.SLIDE_A){
            return (double)slideA.getCurrentPosition()/this.maxExtentA;
        }
        // else if (slide == Slide.SLIDE_B){
        return (double)slideB.getCurrentPosition()/this.maxExtentB;
        // }
    }

    public boolean runToTarget(){
        return this.runToTarget(this.target);
    }

    public boolean runToTarget(double target){
        this.target = target;

        double a = this.getPosition(Slide.SLIDE_A);
        double b = this.getPosition(Slide.SLIDE_B);

        // if a is less than b, slow down a
        // if b is less than a, slow down b

        // if going up, set power of A to b/a.
        // if going down, set power of A to a/b.

        if (Math.abs(a - target) < this.tolerance){
            if (a < target){
                // we need to go UP!
                double aPower = b/a;
                double bPower = 1;

                double max = Math.max(aPower, bPower);

                aPower /= max;
                bPower /= max;

                slideA.setPower(aPower);
                slideB.setPower(bPower);
            }
            else{
                double aPower = a/b;
                double bPower = 1;

                double max = Math.max(aPower, bPower);

                aPower /= max;
                bPower /= max;

                slideA.setPower(-aPower);
                slideB.setPower(-bPower);
            }
        }
        else {
            slideA.setPower(0);
            slideB.setPower(0);
            return false;
        }

        return true;
    }
}
