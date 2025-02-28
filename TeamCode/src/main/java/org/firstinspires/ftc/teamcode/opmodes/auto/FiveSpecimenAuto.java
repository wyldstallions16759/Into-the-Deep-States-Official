package org.firstinspires.ftc.teamcode.opmodes.auto;

// four specimen - uses both preloads, pushes in two

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.PinpointDrive;
import org.firstinspires.ftc.teamcode.subsystems.ServoPivotSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ServoPivotSubsystemRR;
import org.firstinspires.ftc.teamcode.subsystems.WristSubsystem28147;
import org.firstinspires.ftc.teamcode.subsystems.WristSubsystem28147RR;

@Autonomous(name = "5_Specimen_Auto", group = "_testing")
public class FiveSpecimenAuto extends LinearOpMode {
    public static final double GRAB = -59.5;
    public static final double ALMOST_GRAB = -40;
    public static final double CLIP = -39;
    public static final double PUSH = -50;
    public static final Pose2d START_POSE = new Pose2d(-63,-7.5, 0);


    public static final double SHIFT = 4;
    public static final double PUSH_1 = -45+SHIFT;
    public static final double PUSH_2 = -55+SHIFT;
    public static final double PUSH_3 = -65+SHIFT;



    private static WristSubsystem28147RR wrist;
    private static ServoPivotSubsystemRR armBase;
    private static ServoPivotSubsystemRR armWrist;

    public static Action clipPos(){
        return new SequentialAction(
            armBase.armToRestAction(),
            armWrist.armToRestAction(),
            wrist.wrist(1)
        );
    }
    public static Action grabPos(){
        return new SequentialAction(
            armBase.armToRaisedAction(),
            armWrist.armToRaisedAction(),
            wrist.wrist(-1),
            wrist.clawToAction(WristSubsystem28147.ClawState.OPEN));
    }

    public static Action clip(){
        return new ParallelAction(
                new SleepAction(0.25),
                armWrist.armToCustomAction(0),
                new SleepAction(0.5));
    }

    public static Action grab(){
        return new ParallelAction(
                wrist.clawToAction(WristSubsystem28147.ClawState.CLOSED), // close to grab
                new SleepAction(0.5));
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
        wrist = new WristSubsystem28147RR(hardwareMap, telemetry,
                "topclawwrist", "topclaw");
        armBase = new ServoPivotSubsystemRR(hardwareMap, "rarmbase","larmbase",ServoPivotSubsystem.PartType.VERTICAL_EXTENSION_BASE);
        armWrist = new ServoPivotSubsystemRR(hardwareMap, "laservo", "raservo", ServoPivotSubsystem.PartType.VERTICAL_EXTENSION_ARM);

        MecanumDrive.PARAMS.maxWheelVel = 60;
        MecanumDrive.PARAMS.maxProfileAccel = 45;
        MecanumDrive.PARAMS.minProfileAccel = -45;

        PinpointDrive drive = new PinpointDrive(hardwareMap, START_POSE);

        waitForStart();

        SequentialAction action =  new SequentialAction(
                clipPos(),
                drive.actionBuilder(START_POSE)
                        .splineToConstantHeading(new Vector2d(CLIP,-4),0) // clip preset
                        .build(),
                new SleepAction(0.75), //wait for arm to raise
                clip(),
                grabPos(),
                drive.actionBuilder(new Pose2d(CLIP, -4, 0))
                        .setReversed(true)
                        .splineToConstantHeading(new Vector2d(-30, -35), 0) // go push first one
                        .splineToConstantHeading(new Vector2d(-6, PUSH_1),Math.PI) // get behind first one
                        .splineToConstantHeading(new Vector2d(PUSH-4,PUSH_1),0) //push it

                        .splineToConstantHeading(new Vector2d((PUSH-5)/2,PUSH_1),0.1)// retreat behind second one
                        .splineToConstantHeading(new Vector2d(-6, PUSH_2),3.4) // get behind second one
                        .splineToConstantHeading(new Vector2d(PUSH-4, PUSH_2),0) // push second one

                        .splineToConstantHeading(new Vector2d((PUSH-5)/2,PUSH_2),0.1)// retreat behind third one
                        .splineToConstantHeading(new Vector2d(-6, PUSH_3),3.4) // get behind third one
                        .splineToConstantHeading(new Vector2d(PUSH-4, PUSH_3),0) // push third one

                        .splineToConstantHeading(new Vector2d(ALMOST_GRAB,-38),0) // go to grab first
                        .lineToX(GRAB)
                        .build(),
                grab(),
                clipPos(),
                drive.actionBuilder(new Pose2d(GRAB, -38, 0))
                        //.splineToConstantHeading(new Vector2d(CLIP, -12),0)
                        .strafeTo(new Vector2d(CLIP, -10))
                        .build(),
                clip(),
                grabPos(),
                drive.actionBuilder(new Pose2d(CLIP, -10,0))
                        .setReversed(true)
                        .splineToConstantHeading(new Vector2d(GRAB, -38),Math.PI)
//                        .lineToX(GRAB)
                        .build(),
                grab(),
                clipPos(),
                drive.actionBuilder(new Pose2d(GRAB, -38, 0))
//                        .splineToConstantHeading(new Vector2d(CLIP, -10),0)
                        .strafeTo(new Vector2d(CLIP, -8))
                        .build(),
                clip(),
                grabPos(),
                drive.actionBuilder(new Pose2d(CLIP, -8,0))
                        .setReversed(true)
                        .splineToConstantHeading(new Vector2d(GRAB, -38),Math.PI)
//                        .lineToX(GRAB)
                        .build(),
                grab(),
                clipPos(),
                drive.actionBuilder(new Pose2d(GRAB, -38, 0))
//                        .splineToConstantHeading(new Vector2d(CLIP, -8),0)
                        .strafeTo(new Vector2d(CLIP, -4))
                        .build(),
                clip(),
                grabPos(),
                drive.actionBuilder(new Pose2d(CLIP, -8,0))
                        .setReversed(true)
                        .splineToConstantHeading(new Vector2d(GRAB, -38),Math.PI)
//                        .lineToX(GRAB)
                        .build(),
                grab(),
                clipPos(),
                drive.actionBuilder(new Pose2d(GRAB, -38, 0))
//                        .splineToConstantHeading(new Vector2d(CLIP, -8),0)
                        .strafeTo(new Vector2d(CLIP, -4))
                        .build(),
                clip(),
                grabPos(),
                drive.actionBuilder(new Pose2d(CLIP, -4,0))
                        .setReversed(true)
                        .splineToConstantHeading(new Vector2d(GRAB, -62),Math.PI+1)
                        .build()
        );

        Actions.runBlocking(action);

        MecanumDrive.PARAMS.maxWheelVel = 50;
        MecanumDrive.PARAMS.maxProfileAccel = 30;
        MecanumDrive.PARAMS.minProfileAccel = -30;
    }
}
