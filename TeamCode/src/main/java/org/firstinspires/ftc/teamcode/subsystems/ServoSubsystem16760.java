package org.firstinspires.ftc.teamcode.subsystems;


import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;



public class ServoSubsystem16760 {
//    public static enum ClawState{
//        OPEN,
//        CLOSED
//    }

    private Servo IntakeA = null;
    private Servo IntakeB = null;
    private Servo IntakeElevationA = null;
    private Servo IntakeElevationB = null;
    private Servo PendulumA = null;
    private Servo PendulumB = null;
    private Servo Wrist = null;
    private Servo Claw = null;

    private Telemetry telemetry;

    public ServoSubsystem16760(HardwareMap hardwareMap, Telemetry telemetry){
        this.telemetry = telemetry;

        IntakeA = hardwareMap.get(Servo.class, "IntakeA");
        IntakeB = hardwareMap.get(Servo.class, "IntakeB");
        PendulumA = hardwareMap.get(Servo.class, "PendulumA");
        PendulumB = hardwareMap.get(Servo.class, "PendulumB");
        IntakeElevationA = hardwareMap.get(Servo.class, "IntakeElevationA");
        IntakeElevationB = hardwareMap.get(Servo.class, "IntakeElevationB");
        Claw = hardwareMap.get(Servo.class, "Claw");
        Wrist = hardwareMap.get(Servo.class, "Wrist");
        IntakeA.setDirection(Servo.Direction.REVERSE);
        PendulumA.setDirection(Servo.Direction.REVERSE);
        IntakeElevationB.setDirection(Servo.Direction.REVERSE);
        Claw.setDirection(Servo.Direction.REVERSE);
        Wrist.scaleRange(0, 1);
    }

    public void toGround(boolean go){
        if (go) {
            IntakeElevationA.setPosition(0.25);
            IntakeElevationB.setPosition(0.25);
        } else {
            IntakeElevationA.setPosition(0);
            IntakeElevationB.setPosition(0);
        }
    }
    public void movePendulum(double pos) {

            PendulumA.setPosition(pos);
            PendulumB.setPosition(pos);
    }
    public void moveIntake(double pos) {
        IntakeElevationA.setPosition(pos);
        IntakeElevationB.setPosition(pos);
    }
    public void setWrist(boolean forward) {
        if (forward) {
            Wrist.setPosition(0);
        } else {
            Wrist.setPosition(0.657);
        }
    }
    public void closeClaw(boolean close) {
        if (close) {
            Claw.setPosition(0);
        } else {
            Claw.setPosition(0.2);
        }
    }
    public void runIntake(double speed,boolean out) {
        if (speed > 0.01 && !out) {
            IntakeA.setPosition(0.5 + speed/2);
            IntakeB.setPosition(0.5 + speed/2);
        } else {
            IntakeA.setPosition(0.5);
            IntakeB.setPosition(0.5);
        }
        if (speed > 0.01 && out) {
            IntakeA.setPosition(0);
            IntakeB.setPosition(0);
        } else {
            IntakeA.setPosition(0.5);
            IntakeB.setPosition(0.5);
        }
    }


}
