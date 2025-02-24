package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class SlidePairSubsystem {
    private SingleSlideSystem slideA;
    private SingleSlideSystem slideB;

    private double targetFraction;

    public SlidePairSubsystem(HardwareMap hardwareMap, String identifierA, String identifierB,
                              int maxExtA, int maxExtB,
                              DcMotor.Direction dirA, DcMotor.Direction dirB,
                              int tolerance){
        this.slideA = new SingleSlideSystem(hardwareMap, identifierA, maxExtA, dirA, tolerance);
        this.slideB = new SingleSlideSystem(hardwareMap, identifierB, maxExtB, dirB, tolerance);

        this.targetFraction = 0;
    }
    public void setPower(double Power) {
        this.slideA.setPower(Power);
        this.slideB.setPower(Power);
    }
    public boolean slideTo(double target){
        this.targetFraction = target;
        return this.slideTo();
    }
    public double getAPosition() {
        return slideA.get1Position();

    }
    public double getBPosition() {
        return slideB.get1Position();

    }
    public boolean slideToNew(double target, double tolerance) {
        if (getAPosition() > target/3200 && Math.abs(getAPosition() - target/3200) > tolerance) {
            slideA.setPower(-1);
            slideB.setPower(-1);
            return false;
        } else if (getAPosition() < target/3200 && Math.abs(getAPosition() - target/3200) > tolerance) {
            slideA.setPower(1);
            slideB.setPower(1);
            return false;
        } else {
            slideA.setPower(0);
            slideB.setPower(0);
            return true;
        }
    }

    public boolean slideTo(){
        return this.slideA.runToFraction(this.targetFraction)
                && this.slideB.runToFraction(this.targetFraction);
    }

    public void log(Telemetry telemetry){
        telemetry.addLine("Slide A:");
        this.slideA.log(telemetry);

        telemetry.addLine("Slide B:");
        this.slideB.log(telemetry);
    }
}
