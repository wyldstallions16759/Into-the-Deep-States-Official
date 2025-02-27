package org.firstinspires.ftc.teamcode.opmodes.auto;
// leave a preset for alliance member?
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
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

@Autonomous(name = "Auto_4spec")
public class FourSpecimen extends LinearOpMode {
    public static final double GRAB = -59.5;
    public static final double ALMOST_GRAB = -40;
    public static final double CLIP = -39;
    public static final double PUSH = -50;
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
                        .splineToConstantHeading(new Vector2d(-30, -35), 0)
                        .splineToConstantHeading(new Vector2d(-10, -42),Math.PI)
                        .splineToConstantHeading(new Vector2d(PUSH,-42),0)
                        .splineToConstantHeading(new Vector2d(-10, -55),3.2)
                        .splineToConstantHeading(new Vector2d(PUSH, -55),0)
                        .splineToConstantHeading(new Vector2d(-10, -62),3.2)
                        .splineToConstantHeading(new Vector2d(PUSH, -62),0)
                        .splineToConstantHeading(new Vector2d(ALMOST_GRAB,-38),0)
                        .lineToX(GRAB)
                        .build(),
                grab(),
                clipPos(),
                drive.actionBuilder(new Pose2d(GRAB, -38, 0))
                        .splineToConstantHeading(new Vector2d(CLIP, -12),0)
                        .build(),
                clip(),
                grabPos(),
                drive.actionBuilder(new Pose2d(CLIP, -12,0))
                        .setReversed(true)
                        .splineToConstantHeading(new Vector2d(ALMOST_GRAB, -38),0)
                        .lineToX(GRAB)
                        .build(),
                grab(),
                clipPos(),
                drive.actionBuilder(new Pose2d(GRAB, -38, 0))
                        .splineToConstantHeading(new Vector2d(CLIP, -10),0)
                        .build(),
                clip(),
                grabPos(),
                drive.actionBuilder(new Pose2d(CLIP, -10,0))
                        .setReversed(true)
                        .splineToConstantHeading(new Vector2d(ALMOST_GRAB, -38),0)
                        .lineToX(GRAB)
                        .build(),grab(),
                clipPos(),
                drive.actionBuilder(new Pose2d(GRAB, -38, 0))
                        .splineToConstantHeading(new Vector2d(CLIP, -6),0)
                        .build(),
                clip(),
                grabPos(),
                drive.actionBuilder(new Pose2d(CLIP, -4,0))
                        .setReversed(true)
                        .splineToConstantHeading(new Vector2d(GRAB, -38),0)
                        .build()
        );

        SequentialAction testArmAction = new SequentialAction(
                grab()
        );

        Actions.runBlocking(action);
    }
}