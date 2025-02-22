package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ServoPivotSubsystemRR {
    private ServoPivotSubsystem system;

    public ServoPivotSubsystemRR(HardwareMap hardwareMap, String leftServoName, String rightServoName, ServoPivotSubsystem.PartType type) {
        system = new ServoPivotSubsystem(hardwareMap, leftServoName, rightServoName, type);
    }

    public class ArmToCustomAction implements Action{
        private double position;
        public ArmToCustomAction(double position){
            this.position = position;
        }

        @Override
        public boolean run(TelemetryPacket packet){
            system.armToCustom(position);
            return false;
        }
    }

    public Action armToCustomAction(double position){
        return new ArmToCustomAction(position);
    }

    public class ArmToRaisedAction implements Action{
        @Override
        public boolean run(TelemetryPacket packet){
            system.armToRaised();
            return false;
        }
    }

    public Action armToRaisedAction(){
        return new ArmToRaisedAction();
    }

    public class ArmToRestAction implements Action{
        @Override
        public boolean run(TelemetryPacket packet){
            system.armToRest();
            return false;
        }
    }
}
