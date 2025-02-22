package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ServoPivotSubsystem {

    public static enum PartType{
        VERTICAL_EXTENSION_ARM,
        VERTICAL_EXTENSION_BASE,
        HORIZONTAL_EXTENSION_JOINT
    }

    public static final double VERTICAL_EXTENSION_ARM_ROTATION = 0;
    public static final double HORIZONTAL_EXTENSION_JOIN_ROTATION = 0;
    public static final double VERTICAL_EXTENSION_BASE_ROTATION = 0.44;

    public static final double RESTING_PRESET_HORIZ = 0;
    public static final double RESTING_PRESET_VERT_BASE = .1;
    public static final double RESTING_PRESET_VERT = 0.25;

    public static final double RAISED_PRESET_HORIZ = 1;
    public static final double RAISED_PRESET_VERT_BASE = .95;
    public static final double RAISED_PRESET_VERT = .3;


    private Servo leftServo;
    private Servo rightServo;
    private PartType type;

    public ServoPivotSubsystem(HardwareMap hardwareMap, String leftServoName, String rightServoName, PartType type){
        this.leftServo = hardwareMap.get(Servo.class, leftServoName);
        this.rightServo = hardwareMap.get(Servo.class, rightServoName);
        leftServo.setDirection(Servo.Direction.FORWARD);
        rightServo.setDirection(Servo.Direction.REVERSE);

        double dist = (type == PartType.VERTICAL_EXTENSION_ARM)?VERTICAL_EXTENSION_ARM_ROTATION:(type == PartType.HORIZONTAL_EXTENSION_JOINT)?HORIZONTAL_EXTENSION_JOIN_ROTATION:VERTICAL_EXTENSION_BASE_ROTATION;
        leftServo.scaleRange(0, 1-dist);
        rightServo.scaleRange(dist, 1);

        this.type = type;
    }

    public void armToCustom(double position){
        leftServo.setPosition(position);
        rightServo.setPosition(position);
        //rightServo.setPosition(1-position);
    }

    public void armToRest(){
        double rest = RESTING_PRESET_HORIZ;
        if (type == PartType.VERTICAL_EXTENSION_ARM){
            rest = RESTING_PRESET_VERT;
        }
        else if (type == PartType.VERTICAL_EXTENSION_BASE){
            rest = RESTING_PRESET_VERT_BASE;
        }
        this.armToCustom(rest);
    }

    public void armToRaised(){
        double raise = RAISED_PRESET_HORIZ;
        if (type == PartType.VERTICAL_EXTENSION_ARM){
            raise = RAISED_PRESET_VERT;
        }
        else if (type == PartType.VERTICAL_EXTENSION_BASE){
            raise = RAISED_PRESET_VERT_BASE;
        }
        this.armToCustom(raise);
    }
}
