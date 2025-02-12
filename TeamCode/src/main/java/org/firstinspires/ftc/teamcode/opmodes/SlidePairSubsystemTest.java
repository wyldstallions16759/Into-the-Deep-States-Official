package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.subsystems.SlidePairSubsystem;

@TeleOp(name = "Slide Pair Subsystem Test")
public class SlidePairSubsystemTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        SlidePairSubsystem slides = new SlidePairSubsystem(hardwareMap,
                "lSlide", "rSlide",
                100, 90,
                DcMotor.Direction.FORWARD, DcMotor.Direction.REVERSE,
                5);

        waitForStart();

        double position = 0;

        while (opModeIsActive()){
            double input = -gamepad1.left_stick_y;

            position += input/25;

            position = Math.min(Math.max(position, 0), 1);

            slides.slideTo(position);
            telemetry.addData("rawInput", input);

            slides.log(telemetry);
            telemetry.update();
        }

        Thread.sleep(10000);
    }
}
