package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ServoPivotSubsystem {

    public static enum PartType{
        VERTICAL_EXTENSION_ARM,
        HORIZONTAL_EXTENSION_JOINT
    }

    public static final double VERTICAL_EXTENSION_ARM_ROTATION = 0;
    public static final double HORIZONTAL_EXTENSION_JOIN_ROTATION = 0;

    public static final double RESTING_PRESET = 0;
    public static final double RAISED_PRESET = 0.5;

    private Servo leftServo;
    private Servo rightServo;

    public ServoPivotSubsystem(HardwareMap hardwareMap, String leftServoName, String rightServoName, PartType type){
        this.leftServo = hardwareMap.get(Servo.class, leftServoName);
        this.rightServo = hardwareMap.get(Servo.class, rightServoName);
        leftServo.setDirection(Servo.Direction.FORWARD);
        rightServo.setDirection(Servo.Direction.REVERSE);

        double dist = (type == PartType.VERTICAL_EXTENSION_ARM)?VERTICAL_EXTENSION_ARM_ROTATION:HORIZONTAL_EXTENSION_JOIN_ROTATION;
        leftServo.scaleRange(0, dist);
        rightServo.scaleRange(0, dist);
        //armServo2.scaleRange(1-distance, 1);
    }

    public void armToCustom(double position){
        leftServo.setPosition(position);
        rightServo.setPosition(position);
        //rightServo.setPosition(1-position);
    }

    public void armToRest(){
        this.armToCustom(RESTING_PRESET);
    }

    public void armToRaised(){
        this.armToCustom(RAISED_PRESET);
    }
}
