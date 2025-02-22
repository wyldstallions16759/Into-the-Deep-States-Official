package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class WristSubsystem28147RR {
    public WristSubsystem28147 system;
    public WristSubsystem28147RR(HardwareMap hardwareMap, Telemetry telemetry, String wrist, String claw){
        system = new WristSubsystem28147(hardwareMap, telemetry, wrist, claw);
    }
    public WristSubsystem28147RR(HardwareMap hardwareMap, Telemetry telemetry, String wrist, String claw, boolean maxrange) {
        system = new WristSubsystem28147(hardwareMap, telemetry, wrist, claw, maxrange);
    }
    public class ClawAction implements Action{
        @Override
        public boolean run(TelemetryPacket telemetryPacket){
            system.claw();
            return false;
        }
    }
    public Action ClawAction(){
        return new ClawAction();
    }

    public class ClawToAction implements Action{
        private WristSubsystem28147.ClawState to;
        public ClawToAction(WristSubsystem28147.ClawState to){
            this.to = to;
        }
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            system.claw(to);
            return false;
        }
    }
    public Action ClawToAction(WristSubsystem28147.ClawState to){
        return new ClawToAction(to);
    }

    public class Wrist implements Action{
        private double position;
        public Wrist(double position){
            this.position = position;
        }
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            system.wrist(position);
            return false;
        }
    }
    public Action Wrist(double position){
        return new Wrist(position);
    }
}
