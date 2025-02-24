package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.lang.Math;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                // the robot is about 7 inches to center axially, 6 inches to center laterally.
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        myBot.runAction(new SequentialAction(
                myBot.getDrive().actionBuilder(new Pose2d(6, -63, Math.PI/2))
                    .splineToConstantHeading(new Vector2d(0,-35), Math.PI/2)
                    .build(),
                new SleepAction(1),
                myBot.getDrive().actionBuilder(new Pose2d(0, -35, Math.PI/2))
                    .setTangent(0)
                    .lineToX(36)
                    .setTangent(Math.PI/2)
                    .splineToConstantHeading(new Vector2d(44,-13), 0)
                    .setTangent(Math.PI/2)
                    .lineToY(-60)
                    .build(),
                new SleepAction(1),
                myBot.getDrive().actionBuilder(new Pose2d(44, -60, Math.PI/2))
                    .splineToConstantHeading(new Vector2d(3, -35), Math.PI)
                    .build(),
                new SleepAction(1),
                myBot.getDrive().actionBuilder(new Pose2d(3, -35, Math.PI/2))
                    .setTangent(0)
                    .lineToX(36)
                    .setTangent(Math.PI/2)
                    .splineToConstantHeading(new Vector2d(55,-13), 0)
                    .setTangent(Math.PI/2)
                    .lineToY(-60)
                    .build(),
                new SleepAction(1),
                myBot.getDrive().actionBuilder(new Pose2d(55, -60, Math.PI/2))
                    .splineToConstantHeading(new Vector2d(-3,-35), Math.PI)
                    .build(),
                new SleepAction(1),
                myBot.getDrive().actionBuilder(new Pose2d(-3, -35, Math.PI/2))
                    .lineToY(-45)
                    .splineToConstantHeading(new Vector2d(55,-60), -Math.PI)
                    .build(),
                new SleepAction(1),
                myBot.getDrive().actionBuilder(new Pose2d(55, -60, Math.PI/2))
                    .splineToConstantHeading(new Vector2d(-6, -35), Math.PI)
                    .build(),
                new SleepAction(1),
                myBot.getDrive().actionBuilder(new Pose2d(-6, -35, Math.PI/2))
                    .lineToY(-45)
                    .splineToConstantHeading(new Vector2d(50,-55), -Math.PI)
                    .build()
        ));

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}