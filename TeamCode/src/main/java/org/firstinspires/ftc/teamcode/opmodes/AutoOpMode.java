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
    public static final double GRAB = -56;
    public static final double CLIP = -35;
    public static final Pose2d START_POSE = new Pose2d(-63,-6, 0);

    private static WristSubsystem28147RR wrist;
    private static ServoPivotSubsystemRR armBase;
    private static ServoPivotSubsystemRR armWrist;

    public static Action clip(){
        return new SequentialAction(
            armBase.armToRaisedAction(),
            armWrist.armToRaisedAction(),
            wrist.wrist(-1)
        );
    }
    public static Action grab(){
        return new SequentialAction(
            armBase.armToRestAction(),
            armWrist.armToRestAction(),
            wrist.wrist(1)
        );
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


        Pose2d beginPose = new Pose2d(0, 0, 0);

        PinpointDrive drive = new PinpointDrive(hardwareMap, START_POSE);

        waitForStart();

        SequentialAction action = new SequentialAction(
                drive.actionBuilder(START_POSE)
                        .lineToX(CLIP)
                        .build(),
                clip(),
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
        );

        SequentialAction testArmAction = new SequentialAction(
                grab()
        );

        Actions.runBlocking(testArmAction);
    }
}
