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
    public static final double GRAB = -60.5;
    public static final double ALMOST_GRAB = -40;
    public static final double CLIP = -41;
    public static final double PUSH = -55;
    public static final Pose2d START_POSE = new Pose2d(-63,-7.5, 0);


    public static final double SHIFT = 4;
    public static final double PUSH_1 = -45+SHIFT;
    public static final double PUSH_2 = -55+SHIFT;
    public static final double PUSH_3 = -65+SHIFT;

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
                // the robot is about 8.5 inches to center axially, 8.25 inches to center laterally.
                .setConstraints(50, 50, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        DriveShim drive = myBot.getDrive();

        SequentialAction action =  new SequentialAction(
                clipPos(),
                drive.actionBuilder(START_POSE)
                        .splineToConstantHeading(new Vector2d(CLIP,-2),0) // clip preset
                        .build(),
                new SleepAction(0.5), //wait for arm to raise
                clip(),
                grabPos(),
                drive.actionBuilder(new Pose2d(CLIP, -2, 0))
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

                        .splineToConstantHeading(new Vector2d(GRAB,PUSH_3+6),1) // go to grab first
                        .build(),
                grab(),
                clipPos(),
                drive.actionBuilder(new Pose2d(GRAB, PUSH_3+6, 0))
                        //.splineToConstantHeading(new Vector2d(CLIP, -12),0)
                        .strafeToConstantHeading(new Vector2d(CLIP, -10))
                        .build(),
                clip(),
                grabPos(),
                drive.actionBuilder(new Pose2d(CLIP, -10,0))
                        .setReversed(true)
//                        .splineToConstantHeading(new Vector2d(GRAB, -38),Math.PI)
                        .strafeToConstantHeading(new Vector2d(GRAB, -38))
//                        .lineToX(GRAB)
                        .build(),
                grab(),
                clipPos(),
                drive.actionBuilder(new Pose2d(GRAB, -38, 0))
//                        .splineToConstantHeading(new Vector2d(CLIP, -10),0)
                        .strafeToConstantHeading(new Vector2d(CLIP, -8))
                        .build(),
                clip(),
                grabPos(),
                drive.actionBuilder(new Pose2d(CLIP, -8,0))
                        .setReversed(true)
//                        .splineToConstantHeading(new Vector2d(GRAB, -38),Math.PI)
                        .strafeToConstantHeading(new Vector2d(GRAB,-38))
//                        .lineToX(GRAB)
                        .build(),
                grab(),
                clipPos(),
                drive.actionBuilder(new Pose2d(GRAB, -38, 0))
//                        .splineToConstantHeading(new Vector2d(CLIP, -8),0)
                        .strafeToConstantHeading(new Vector2d(CLIP, -6))
                        .build(),
                clip(),
                grabPos(),
                drive.actionBuilder(new Pose2d(CLIP, -6,0))
                        .setReversed(true)
//                        .splineToConstantHeading(new Vector2d(GRAB, -38),Math.PI)
//                        .lineToX(GRAB)
                        .strafeToConstantHeading(new Vector2d(GRAB, -38))
                        .build(),
                grab(),
                clipPos(),
                drive.actionBuilder(new Pose2d(GRAB, -38, 0))
//                        .splineToConstantHeading(new Vector2d(CLIP, -8),0)
                        .strafeToConstantHeading(new Vector2d(CLIP, -4))
                        .build(),
                clip(),
                grabPos(),
                drive.actionBuilder(new Pose2d(CLIP, -4,0))
                        .setReversed(true)
                        .splineToConstantHeading(new Vector2d(GRAB, -62),Math.PI+1)
                        .build()
        );

        myBot.runAction(action);
//        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(0, 0, 0))
//                .splineToConstantHeading(new Vector2d(12, 12), 0).build());
        Image img = null;
        try { img = ImageIO.read(new File("C:\\Users\\Team16759\\StudioProjects\\Into-the-Deep-States-Official\\MeepMeepTesting\\src\\main\\java\\com\\example\\meepmeeptesting\\field.png")); }
        catch(IOException e) {}

        meepMeep.setBackground(img)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}