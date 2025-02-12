package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.subsystems.SlidePairSubsystem;

@TeleOp(name = "Slide Pair Subsystem Test")
public class SlidePairSubsystemTest extends LinearOpMode {
    @Override
    public void runOpMode(){
        SlidePairSubsystem slides = new SlidePairSubsystem(hardwareMap,
                "lSlide", "rSlide",
                109, 92,
                DcMotor.Direction.FORWARD, DcMotor.Direction.REVERSE,
                10);

        waitForStart();

        while (opModeIsActive()){
            double input = -gamepad1.left_stick_y;

            input += 1;
            input /= 2;

            slides.slideTo(input);

            slides.log(telemetry);
            telemetry.update();
        }
    }
}
