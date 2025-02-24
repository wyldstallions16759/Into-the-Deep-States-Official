package org.firstinspires.ftc.teamcode.subsystems;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;


import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

public class LimelightSubsystem {
    Limelight3A limelight;
    Telemetry telemetry;
    LimelightConstants limelightConstants;

    public LimelightSubsystem(HardwareMap hardwareMap, Telemetry telemetry) {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelightConstants = new LimelightConstants();
        this.telemetry = telemetry;
        telemetry.setMsTransmissionInterval(11);
        limelight.pipelineSwitch(0);
        limelight.start();
    }

    //Get distance from limelight to the block
    public List<Double> distanceFrom() {
        //If you are using the method, check for -1 distance which means it didn't find anything and -361 angle
        double distance = -1;
        double angle = -361;
        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()) {
            List<LLResultTypes.ColorResult> colorResults = result.getColorResults();
            for (LLResultTypes.ColorResult cr : colorResults) {
                distance = limelightConstants.LIMELIGHT_HEIGHT - limelightConstants.BLOCK_HEIGHT / (Math.tan(limelightConstants.DEFAULT_TY - cr.getTargetYDegrees()));
                angle = cr.getTargetXDegrees() - limelightConstants.TX_OFFSET;
            }
        }
        List<Double> items = new ArrayList<Double>();
        items.add(0, distance);
        items.add(1, angle);
        return items;
    }

    public void switchPipeline(int pipeline){
        limelight.pipelineSwitch(pipeline);
    }
}