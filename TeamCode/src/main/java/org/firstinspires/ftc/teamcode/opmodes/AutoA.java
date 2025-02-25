package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Pose2dDual;
import com.acmerobotics.roadrunner.PoseMap;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.PinpointDrive;
import org.firstinspires.ftc.teamcode.subsystems.ServoSubsystem16760RR;

@Autonomous(name = "Specimen Auto")
public class AutoA extends LinearOpMode {
    public static final double GRAB = -56;
    public static final double CLIP = -35;
    public static final Pose2d START_POSE = new Pose2d(-63,-6, 0);
    public static Action clip(){
        return new SleepAction(1);
    }
    public static Action grab(){
        return clip();
    }
    @Override
    public void runOpMode(){
        // Steps:
        //      1. Initialize Subsystems required:
        //          a. arm base servo
        //          b. arm wrist servo
        //          c. vertical claw
        //          d. roadrunner
        //      2. Create path
        //      3. Run Actions Blocking

        Pose2d beginPose = new Pose2d(0, 0, 0);

        PinpointDrive drive = new PinpointDrive(hardwareMap, START_POSE);
        ServoSubsystem16760RR servo = new ServoSubsystem16760RR(hardwareMap,telemetry);

        waitForStart();

        Actions.runBlocking(new SequentialAction(
                drive.actionBuilder(START_POSE)
                        .lineToX(CLIP)
                        .build(),
                clip(),
                servo.closeClaw(true),
                drive.actionBuilder(new Pose2d(CLIP, -6, 0))
                        .setReversed(true)
                        .splineToConstantHeading(new Vector2d(-30, -36), 0)
                        .lineToX(-13)
                        .setTangent(Math.PI/2)
                        .lineToY(-45)
                        .setTangent(0)
                        .lineToX(GRAB)
                        .build(),
                grab(),
                drive.actionBuilder(new Pose2d(GRAB, -45, 0))
                        .splineToConstantHeading(new Vector2d(CLIP, -3), 0)
                        .build(),
                clip(),
                drive.actionBuilder(new Pose2d(CLIP, -3,0))
                        .setReversed(true)
                        .splineToConstantHeading(new Vector2d(-13, -45),0)
                        .setTangent(Math.PI/2)
                        .lineToY(-55)
                        .setTangent(0)
                        .lineToX(GRAB)
                        .build(),
                grab(),
                drive.actionBuilder(new Pose2d(GRAB, -55,0))
                        .splineToConstantHeading(new Vector2d(CLIP, 0),0)
                        .build(),
                clip(),
                drive.actionBuilder(new Pose2d(CLIP, 0,0))
                        .setReversed(true)
                        .splineToConstantHeading(new Vector2d(-40, -55),0)
                        .setTangent(Math.PI/2)
                        .lineToY(-63)
                        .setTangent(0)
                        .lineToX(GRAB)
                        .build(),
                grab(),
                drive.actionBuilder(new Pose2d(GRAB, -63,0))
                        .splineToConstantHeading(new Vector2d(CLIP,-6),0)
                        .build(),
                clip(),
                drive.actionBuilder(new Pose2d(CLIP, -6,0))
                        .setReversed(true)
                        .splineToConstantHeading(new Vector2d(-60, -55),Math.PI/2)
                        .build()
        ));
    }
}