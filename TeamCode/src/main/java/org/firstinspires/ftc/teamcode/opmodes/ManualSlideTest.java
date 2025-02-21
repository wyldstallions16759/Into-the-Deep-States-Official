package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="Manual Slide Tuner")
public class ManualSlideTest extends LinearOpMode {
    @Override
    public void runOpMode(){
        DcMotor slideR = hardwareMap.get(DcMotor.class, "rvslide");
        DcMotor slideL = hardwareMap.get(DcMotor.class, "lvslide");

        // Reverse right slide
        slideR.setDirection(DcMotor.Direction.REVERSE);
        slideL.setDirection(DcMotor.Direction.FORWARD);
        slideR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // reset encoders
        slideR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        slideR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slideL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        waitForStart();

        while (opModeIsActive()){
            double input = -gamepad1.left_stick_y;

            if (Math.abs(input) > 0.2) {
                slideR.setPower(input);
                slideL.setPower(input);
            }
            else{
                slideR.setPower(0);
                slideL.setPower(0);
            }

            telemetry.addData("Slide R Position", slideR.getCurrentPosition());
            telemetry.addData("Slide L Position", slideL.getCurrentPosition());
            telemetry.update();
        }
    }
}
