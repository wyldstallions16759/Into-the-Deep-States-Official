package org.firstinspires.ftc.teamcode.opmodes;

import static org.firstinspires.ftc.teamcode.tuning.ManualFeedbackTuner.DISTANCE;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.PinpointDrive;
import org.firstinspires.ftc.teamcode.subsystems.ServoSubsystem16760RR;
import org.firstinspires.ftc.teamcode.subsystems.SlidePairSubsystemRR;

@Autonomous(name = "Specimen AutoB")
public class XAutoB extends LinearOpMode {
    public static final double GRAB = -56;
    public static final double CLIP = 29;
    public static final Pose2d START_POSE = new Pose2d(0,0, 0);
    public static Action clip(){
        return new SleepAction(100);
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

        Pose2d beginPose = new Pose2d(0, 0, Math.PI);

        PinpointDrive drive = new PinpointDrive(hardwareMap, START_POSE);
        ServoSubsystem16760RR servo = new ServoSubsystem16760RR(hardwareMap,telemetry);
        SlidePairSubsystemRR Elevation = new SlidePairSubsystemRR(hardwareMap,
                "ElevatorA", "ElevatorB",
                3284, 3339,
                DcMotor.Direction.REVERSE, DcMotor.Direction.FORWARD,
                20);

        waitForStart();

        Actions.runBlocking(new SequentialAction(
                new ParallelAction(
                        drive.actionBuilder(new Pose2d(0, 0, 0))
                                .lineToX(28)
                                .build(),
                servo.closeClaw(false),
                servo.movePendulum(0),
                servo.setWrist(false),
                servo.moveIntake(0.1),
                Elevation.SlideToTargetAction(0.6)
                        ),
                Elevation.SlideToTargetAction(0.5),
                new ParallelAction(
                        Elevation.SlideToTargetAction(0.3),
                        drive.actionBuilder(new Pose2d(28, 0, 0))
                                .setReversed(false)
                                .splineToLinearHeading(new Pose2d(10,-20,0),0)
                                .lineToX(40)
                                .splineToLinearHeading(new Pose2d(50,-35,Math.PI),0)
                                .build(),
                        servo.closeClaw(true)
                ),
                drive.actionBuilder(new Pose2d(50, -35, Math.PI))
                        .lineToX(15,drive.defaultVelConstraint)
                        .lineToX(50,drive.defaultVelConstraint)
                        .setTangent(Math.PI/2)
                        .lineToY(-36)
                        .setTangent(0)
                        .lineToX(15,drive.defaultVelConstraint)
                        .lineToX(50,drive.defaultVelConstraint)
                        .build()
        ));

    }
}