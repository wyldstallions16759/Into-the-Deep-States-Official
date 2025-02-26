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
import org.firstinspires.ftc.teamcode.subsystems.ServoPivotSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ServoPivotSubsystemRR;
import org.firstinspires.ftc.teamcode.subsystems.WristSubsystem28147;
import org.firstinspires.ftc.teamcode.subsystems.WristSubsystem28147RR;

@Autonomous(name = "Auto")
public class AutoOpMode extends LinearOpMode {
    public static final double GRAB = -59.5;
    public static final double CLIP = -39;
    public static final Pose2d START_POSE = new Pose2d(-63,-8, 0);

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


        PinpointDrive drive = new PinpointDrive(hardwareMap, START_POSE);



        waitForStart();

        SequentialAction action = new SequentialAction(
                clipPos(),
                drive.actionBuilder(START_POSE)
                        .lineToX(CLIP)
                        .build(),
                new SleepAction(0.65),
                clip(),
                grabPos(),
                drive.actionBuilder(new Pose2d(CLIP, -8, 0))
                        .setReversed(true)
                        .splineToConstantHeading(new Vector2d(-30, -37), 0)
                        .lineToX(-13)
                        .setTangent(Math.PI/2)
                        .lineToY(-45)
                        .setTangent(0)
                        .lineToX(GRAB)
                        .build(),
                grab(),
                clipPos(),
                drive.actionBuilder(new Pose2d(GRAB, -45, 0))
                        .splineToConstantHeading(new Vector2d(CLIP, -3), 0)
                        .build(),
                clip(),
                grabPos(),
                drive.actionBuilder(new Pose2d(CLIP, -3,0))
                        .setReversed(true)
                        .splineToConstantHeading(new Vector2d(-13, -46),0)
                        .setTangent(Math.PI/2)
                        .lineToY(-55)
                        .setTangent(0)
                        .lineToX(GRAB)
                        .build(),
                grab(),
                clipPos(),
                drive.actionBuilder(new Pose2d(GRAB, -55,0))
                        .splineToConstantHeading(new Vector2d(CLIP, 0),0)
                        .build(),
                clip(),
                grabPos(),
                drive.actionBuilder(new Pose2d(CLIP, 0,0))
                        .setReversed(true)
                        .splineToConstantHeading(new Vector2d(-40, -63),0)
                        .setTangent(Math.PI/2)
                        //.lineToY(-63)
                        .setTangent(0)
                        .lineToX(GRAB)
                        .build(),
                grab(),
                clipPos(),
                drive.actionBuilder(new Pose2d(GRAB, -63,0))
                        .splineToConstantHeading(new Vector2d(CLIP,-6),0)
                        .build(),
                clip(),
                grabPos(),
                drive.actionBuilder(new Pose2d(CLIP, -6,0))
                        .setReversed(true)
                        .splineToConstantHeading(new Vector2d(-65, -55),Math.PI/2)
                        .build()
        );

        SequentialAction testArmAction = new SequentialAction(
                grab()
        );

        Actions.runBlocking(action);
    }
}
