package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Pose2dDual;
import com.acmerobotics.roadrunner.PoseMap;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.DriveShim;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.lang.Math;
import java.net.URL;

import javax.imageio.ImageIO;

public class MeepMeepTesting {
    public static final double GRAB = -59.5;
    public static final double ALMOST_GRAB = -40;
    public static final double CLIP = -39;
    public static final double PUSH = -50;
    public static final Pose2d START_POSE = new Pose2d(-63,-8, 0);
    public static Action clip(){
        return new SleepAction(0.25);
    }
    public static Action grab(){
        return clip();
    }
    public static Action grabPos(){
        return clip();
    }
    public static Action clipPos(){
        return clip();
    }
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(750);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                // the robot is about 7 inches to center axially, 6 inches to center laterally.
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        DriveShim drive = myBot.getDrive();

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
                drive.actionBuilder(new Pose2d(CLIP, -6,0))
                        .setReversed(true)
                        .splineToConstantHeading(new Vector2d(ALMOST_GRAB, -38),0)
                        .lineToX(GRAB)
                        .build(),grab(),
                clipPos(),
                drive.actionBuilder(new Pose2d(GRAB, -38, 0))
                        .splineToConstantHeading(new Vector2d(CLIP, -4),0)
                        .build(),
                clip(),
                grabPos(),
                drive.actionBuilder(new Pose2d(CLIP, -4,0))
                        .setReversed(true)
                        .splineToConstantHeading(new Vector2d(GRAB, -38),0)
                        .build()
        );

        myBot.runAction(action);
//        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(0, 0, 0))
//                .splineToConstantHeading(new Vector2d(12, 12), 0).build());
        Image img = null;
        try { img = ImageIO.read(new File("D:\\field.png")); }
        catch(IOException e) {}

        meepMeep.setBackground(img)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}