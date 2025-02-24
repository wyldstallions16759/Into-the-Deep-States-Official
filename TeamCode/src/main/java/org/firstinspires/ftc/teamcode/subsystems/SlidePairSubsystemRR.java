package org.firstinspires.ftc.teamcode.subsystems;
import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class SlidePairSubsystemRR {
    SlidePairSubsystem system;

    public SlidePairSubsystemRR(HardwareMap hardwareMap, String identifierA, String identifierB, int maxExtA, int maxExtB, DcMotor.Direction dirA, DcMotor.Direction dirB, int tolerance){
        system = new SlidePairSubsystem(hardwareMap, identifierA, identifierB, maxExtA, maxExtB, dirA, dirB, tolerance);
    }

    public class SlideToTargetAction implements Action {
        private double target;
        public SlideToTargetAction(double target){this.target = target;}
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            return system.slideTo(target);
        }
    }

    public Action SlideToTargetAction(double target){return new SlideToTargetAction(target);}

    public class SlideToAction implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            return system.slideTo();
        }
    }
}
