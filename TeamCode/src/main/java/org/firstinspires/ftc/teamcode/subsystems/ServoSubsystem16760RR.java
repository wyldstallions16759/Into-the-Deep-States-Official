package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ServoSubsystem16760RR {
    public ServoSubsystem16760 system;
    public ServoSubsystem16760RR(HardwareMap hardwareMap, Telemetry telemetry){
        system = new ServoSubsystem16760(hardwareMap, telemetry);
    }

    public class ClawToAction implements Action{
        private boolean close;
        public ClawToAction(boolean to){
            this.close = to;
        }
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            system.closeClaw(close);
            return false;
        }
    }
    public Action closeClaw(boolean close){
        return new ClawToAction(close);
    }

    public class Wrist implements Action{
        private double position;
        public Wrist(double position){
            this.position = position;
        }
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
//            system.wrist(position);
            return false;
        }
    }
    public Action Wrist(double position){
        return new Wrist(position);
    }
}