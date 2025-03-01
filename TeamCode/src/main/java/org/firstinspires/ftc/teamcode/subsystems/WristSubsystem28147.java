package org.firstinspires.ftc.teamcode.subsystems;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;


public class WristSubsystem28147{
    public static enum ClawState{
        OPEN,
        CLOSED
    }

    private Servo wristServo;
    private Servo clawServo;

    private ClawState clawState = ClawState.OPEN;

    private Telemetry telemetry;

    public WristSubsystem28147(HardwareMap hardwareMap, Telemetry telemetry, String wrist, String claw){
        this.telemetry = telemetry;

        wristServo = hardwareMap.get(Servo.class, wrist);
        clawServo = hardwareMap.get(Servo.class, claw);

        wristServo.scaleRange(0.225, 0.925);
        clawServo.scaleRange(0.24, 0.5); //0.5 opened sufficiently

        this.claw(ClawState.CLOSED);
        //this.wrist(0);
    }

    public void claw(){
        if (this.clawState == ClawState.CLOSED){
            this.claw(ClawState.OPEN);
        }
        else{
            this.claw(ClawState.CLOSED);
        }
    }

    public void claw(ClawState to){
        this.clawState = to;
        if (this.clawState == ClawState.OPEN){
            this.clawServo.setPosition(1);
        }
        else{
            this.clawServo.setPosition(0);
        }
    }

    public void wrist(double position){
        // where position is in range [-1,1]
        this.wristServo.setPosition((position+1)/2);
        this.telemetry.addData("wrist position", wristServo.getPosition());
    }
}
