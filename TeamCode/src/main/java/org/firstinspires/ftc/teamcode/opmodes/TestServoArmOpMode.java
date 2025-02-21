package org.firstinspires.ftc.teamcode.opmodes;

// unscrew one servo from the rotating thing in case they are misaligned. this should work with both servo rotating pairs on the robot.

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Test Arm Teleop")
public class TestServoArmOpMode extends LinearOpMode {
    private double distance = 0;

    //declare members
    private Servo armServo1;
    private Servo armServo2;

    @Override
    public void runOpMode() throws InterruptedException {
        armServo1 = hardwareMap.get(Servo.class, "laservo");
        armServo2 = hardwareMap.get(Servo.class, "raservo");

        //armServo1.setDirection(Servo.Direction.FORWARD);
        //armServo2.setDirection(Servo.Direction.REVERSE);

        while (opModeInInit()){
            if (gamepad1.dpad_up){
                distance+=0.01;
            }
            else if(gamepad1.dpad_down){
                distance -= 0.01;
            }
            telemetry.addData("D=",distance);
            telemetry.update();
            Thread.sleep(100);
        }

        waitForStart();

        armServo1.scaleRange(0, 1-distance);
        //armServo2.scaleRange(0, 1-distance);
        armServo2.scaleRange(distance, 1);

        while (opModeIsActive()){
            boolean up = gamepad1.dpad_up;
            boolean down = gamepad1.dpad_down;

            if (up){
                armServo1.setPosition(0);
                armServo2.setPosition(1);
            }
            else if (down){
                armServo1.setPosition(1);
                armServo2.setPosition(0);
            }
            else{
                double in = gamepad1.left_stick_y;
                double pulse = (in+1)/2;
                armServo1.setPosition(pulse);
                armServo2.setPosition(1-pulse);
            }
        }
    }
}
