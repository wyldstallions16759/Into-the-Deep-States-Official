package org.firstinspires.ftc.teamcode.opmodes;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.ServoPivotSubsystem;

@TeleOp(name="Servo Pivot Subsystem Test")
public class ServoPivotTestOpmode extends LinearOpMode {
    @Override
    public void runOpMode(){
        ServoPivotSubsystem system = new ServoPivotSubsystem(hardwareMap, "larmbase", "rarmbase", ServoPivotSubsystem.PartType.HORIZONTAL_EXTENSION_JOINT);

        waitForStart();

        while (opModeIsActive()){
            if (gamepad1.dpad_up){
                system.armToRaised();
            }
            else if (gamepad1.dpad_down){
                system.armToRest();
            }
            else{
                system.armToCustom((gamepad1.left_stick_y+1)/2);
                telemetry.addData("input",(gamepad1.left_stick_y+1)/2);
            }

            telemetry.update();
        }
    }
}
