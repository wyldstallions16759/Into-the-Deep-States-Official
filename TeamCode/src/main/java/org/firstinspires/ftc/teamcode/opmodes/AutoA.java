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
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.PinpointDrive;
import org.firstinspires.ftc.teamcode.subsystems.ServoSubsystem16760RR;
import org.firstinspires.ftc.teamcode.subsystems.SlidePairSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.SlidePairSubsystemRR;
import org.firstinspires.ftc.teamcode.subsystems.WristSubsystem28147;

@Autonomous(name = "Specimen Auto")
public class AutoA extends LinearOpMode {
    public static final double GRAB = -59.5;
    public static final double ALMOST_GRAB = -40;
    public static final double CLIP = -39;
    public static final double PUSH = -50;
    public static final double CLIP_HEIGHT = 0.5;
    public static final double GRAB_HEIGHT = 0.2;

    public static final Pose2d START_POSE = new Pose2d(-63,-7.5, 0);

    private static ServoSubsystem16760RR servo;
    private static SlidePairSubsystemRR Elevation;

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
        servo = new ServoSubsystem16760RR(hardwareMap, telemetry);
        Elevation = new SlidePairSubsystemRR(hardwareMap,
                "SlideA", "SlideB",
                4000, 4000,
                DcMotor.Direction.FORWARD, DcMotor.Direction.REVERSE,
                5);

        PinpointDrive drive = new PinpointDrive(hardwareMap, START_POSE);



        waitForStart();

        SequentialAction action = new SequentialAction(

                drive.actionBuilder(START_POSE)
                        .splineToConstantHeading(new Vector2d(CLIP,-4),0) // clip preset
                        .build(),
                new SleepAction(0.75), //wait for arm to raise
                drive.actionBuilder(new Pose2d(CLIP, -4, 0))
                        .setReversed(true)
                        .splineToConstantHeading(new Vector2d(-30, -35), 0) // go push first one
                        .splineToConstantHeading(new Vector2d(-6, -45),Math.PI) // get behind first one
                        .splineToConstantHeading(new Vector2d(PUSH-4,-45),0) //push it
                        .splineToConstantHeading(new Vector2d((PUSH-5)/2,-45),0.1)// retreat behind second one
                        .splineToConstantHeading(new Vector2d(-6, -53),3.4) // get behind second one
                        .splineToConstantHeading(new Vector2d(PUSH-4, -53),0) // push second one
//                        .splineToConstantHeading(new Vector2d((PUSH-5)/2,-53),0.1) // retreat for third push
//                        .splineToConstantHeading(new Vector2d(-6, -62),3.4) // get behind third
//                        .splineToConstantHeading(new Vector2d(PUSH-4, -62),0) // push third.
                        //.splineToConstantHeading(new Vector2d((PUSH-8)/2,-57),0)
                        .splineToConstantHeading(new Vector2d(ALMOST_GRAB,-38),0) // go to grab first
                        .lineToX(GRAB)
                        .build(),
                drive.actionBuilder(new Pose2d(GRAB, -38, 0))
                        .splineToConstantHeading(new Vector2d(CLIP, -12),0)
                        .build(),
                drive.actionBuilder(new Pose2d(CLIP, -12,0))
                        .setReversed(true)
                        .splineToConstantHeading(new Vector2d(GRAB, -38),Math.PI)
//                        .lineToX(GRAB)
                        .build(),
                drive.actionBuilder(new Pose2d(GRAB, -38, 0))
                        .splineToConstantHeading(new Vector2d(CLIP, -10),0)
                        .build(),
                drive.actionBuilder(new Pose2d(CLIP, -10,0))
                        .setReversed(true)
                        .splineToConstantHeading(new Vector2d(GRAB, -38),Math.PI)
//                        .lineToX(GRAB)
                        .build(),
                drive.actionBuilder(new Pose2d(GRAB, -38, 0))
                        .splineToConstantHeading(new Vector2d(CLIP, -8),0)
                        .build(),
//                grabPos(),
//                drive.actionBuilder(new Pose2d(CLIP, -8,0))
//                        .setReversed(true)
//                        .splineToConstantHeading(new Vector2d(GRAB, -38),Math.PI)
////                        .lineToX(GRAB)
//                        .build(),
//                grab(),
//                clipPos(),
//                drive.actionBuilder(new Pose2d(GRAB, -38, 0))
//                        .splineToConstantHeading(new Vector2d(CLIP, -6),0)
//                        .build(),
//                clip(),
//                grabPos(),
                drive.actionBuilder(new Pose2d(CLIP, -8,0))
                        .setReversed(true)
                        .splineToConstantHeading(new Vector2d(GRAB, -62),Math.PI+1)
                        .build()
        );
    }
}