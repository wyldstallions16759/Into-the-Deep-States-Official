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
                              int tolerance, double speed){
        this.slideA = new SingleSlideSystem(hardwareMap, identifierA, maxExtA, dirA, tolerance, speed);
        this.slideB = new SingleSlideSystem(hardwareMap, identifierB, maxExtB, dirB, tolerance, speed);
        this.targetFraction = 0;
    }

    public boolean slideTo(double target){
        this.targetFraction = target;
        return this.slideTo();
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
