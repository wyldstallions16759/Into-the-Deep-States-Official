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
        private boolean close;
        public Wrist(boolean position){
            this.close = position;
        }
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            system.setWrist(close);
            return false;
        }
    }
    public Action setWrist(boolean close){
        return new Wrist(close);
    }
    public class Pendulum implements Action{
        private double position;
        public Pendulum(double position){
            this.position = position;
        }
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            system.movePendulum(position);
            return false;
        }
    }
    public Action movePendulum(double position){
        return new Pendulum(position);
    }
    public class inIntake implements Action{
        private double speed;
        public inIntake(){
            this.speed = 1;
        }
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            system.runIntake(speed,false);
            return false;
        }
    }
    public Action inIntake(double speed){
        return new inIntake();
    }
    public Action moveIntake(double position){
        return new Pendulum(position);
    }
    public class moveIntake implements Action{
        private double position;
        public moveIntake(double position){
            this.position = position;
        }
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            system.movePendulum(position);
            return false;
        }
    }
    public Action Pendulum(double position){
        return new Pendulum(position);
    }
}