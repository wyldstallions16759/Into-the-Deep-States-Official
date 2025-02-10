package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.WristSubsystem28147;

@TeleOp(name="WristTestTeleop")
public class WristSubsystemTest extends LinearOpMode {
    private WristSubsystem28147 wrist;

    private double wristPosition = 0.5;
    private double wristIncrement = 0.075;

    @Override
    public void runOpMode() throws InterruptedException {
        wrist = new WristSubsystem28147(hardwareMap, telemetry);

        boolean buttonXToggle = false;
        boolean buttonBToggle = false;
        boolean bumperToggle = false;

        waitForStart();

        while (opModeIsActive()){

            boolean b = gamepad1.b;
            boolean x = gamepad1.x;
            boolean bumper = gamepad1.right_bumper;

             /*if (!toggleState && gamepad1.a){
                 wrist.claw();
             }
             toggleState = gamepad1.a;*/

            if (b && !buttonBToggle){
                telemetry.addLine("B");
                wristPosition += wristIncrement;
            }
            else if (x && !buttonXToggle){
                telemetry.addLine("X");
                wristPosition -= wristIncrement;
            }
            else {
                telemetry.addLine("NOTA");
            }

            wristPosition = Math.min(Math.max(0, wristPosition), 1);

            wrist.wrist((wristPosition*2)-1);

            if (bumper && !bumperToggle){
                wrist.claw();
            }

             // update toggles
            buttonBToggle = b;
            buttonXToggle = x;
            bumperToggle = bumper;

            telemetry.update();
        }
    }
}
