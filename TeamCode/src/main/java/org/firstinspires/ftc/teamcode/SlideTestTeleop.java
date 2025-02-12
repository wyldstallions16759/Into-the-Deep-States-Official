package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="SlideTestTeleop")
public class SlideTestTeleop extends LinearOpMode {
    @Override
    public void runOpMode(){
        // LEFT SLIDE MAX EXTENSION ENCODER POSITION: 109
        // RIGHT SLIDE MAX EXTENSION ENCODER POSITION: -92
        DcMotor lSlide = hardwareMap.get(DcMotor.class, "leftSlide");
        DcMotor rSlide = hardwareMap.get(DcMotor.class, "rightSlide");

        lSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rSlide.setDirection(DcMotor.Direction.FORWARD);


        rSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rSlide.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {
            double stick = -gamepad1.left_stick_y;

            if (Math.abs(stick) > 0.3){
                lSlide.setPower(stick);
                rSlide.setPower(stick);
            }
            else{
                lSlide.setPower(0);
                rSlide.setPower(0);
            }

            telemetry.addData("left",lSlide.getCurrentPosition());
            telemetry.addData("right",rSlide.getCurrentPosition());
            telemetry.update();
        }
    }
}
