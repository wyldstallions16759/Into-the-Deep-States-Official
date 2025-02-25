package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.subsystems.LimelightSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.SlidePairSubsystemRR;

import java.util.List;

@TeleOp (name = "GrabBlock")
public class GrabBlock extends LinearOpMode {
    PinpointDrive drive;
    SlidePairSubsystemRR slides;
    LimelightSubsystem ll;
    Pose2d START_POSE = new Pose2d(0,0,0);
    @Override
    public void runOpMode(){
        drive = new PinpointDrive(hardwareMap, START_POSE);
        //slides = new SlidePairSubsystemRR(hardwareMap,)
        waitForStart();
        while (opModeIsActive()){
            boolean grabBlock = gamepad2.a;
            List<Double> llreturns = ll.distanceFrom();
            if (grabBlock && !llreturns.isEmpty()){
                Actions.runBlocking(
                        new ParallelAction(
                                drive.actionBuilder(START_POSE)
                                        .turnTo(llreturns.get(1))
                                        .build()
                        )
                );
            }
        }
    }
}