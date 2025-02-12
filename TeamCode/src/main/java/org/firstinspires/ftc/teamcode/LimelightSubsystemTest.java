package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.LimelightSubsystem;

@TeleOp(name="LimelightTest")
public class LimelightSubsystemTest extends LinearOpMode{
    private LimelightSubsystem limelight;

    public void runOpMode(){
        limelight = new LimelightSubsystem(hardwareMap, telemetry);
        waitForStart();

        while (opModeIsActive()){
            limelight.distanceFrom();
        }
    }
}