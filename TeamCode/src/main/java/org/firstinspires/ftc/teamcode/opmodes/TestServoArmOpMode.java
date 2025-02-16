package org.firstinspires.ftc.teamcode.opmodes;

// test arm using a single servo. using two servos without testing is dangerous in case they are not calibrated.

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Test Arm Teleop")
public class TestServoArmOpMode extends LinearOpMode {
    //declare members
    private Servo armServo1;
    private Servo armServo2;

    @Override
    public void runOpMode(){
        armServo1 = hardwareMap.get(Servo.class, "leftArmServo");

        waitForStart();

        while (opModeIsActive()){
            boolean up = gamepad1.dpad_up;
            boolean down = gamepad1.dpad_down;

            if (up){
                armServo1.setPosition(0);
                //armServo2.setPosition(1);
            }
            else if (down){
                armServo1.setPosition(1);
                //armServo2.setPosition(0);
            }
        }
    }
}
