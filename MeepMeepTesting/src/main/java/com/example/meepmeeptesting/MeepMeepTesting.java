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
    public static final double GRAB = -56;
    public static final double CLIP = -35;
    public static final Pose2d START_POSE = new Pose2d(-63,-6, 0);
    public static Action clip(){
        return new SleepAction(1);
    }
    public static Action grab(){
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


        myBot.runAction(new SequentialAction(
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


        ));
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