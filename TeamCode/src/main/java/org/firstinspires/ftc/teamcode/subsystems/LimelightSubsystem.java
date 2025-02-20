package org.firstinspires.ftc.teamcode.subsystems;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;


import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.List;

public class LimelightSubsystem {
    private Limelight3A limelight;
    private Telemetry telemetry;
    static final double camHeightOffGround = 13;
    double blockHeightOffGround = 1.4;
    double heightOffGround = blockHeightOffGround-camHeightOffGround;
    static final double a1 = 0;
    List<List<Double>> corners;
    double distance;
    public LimelightSubsystem(HardwareMap hardwareMap, Telemetry telemetry){
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        this.telemetry = telemetry;
        telemetry.setMsTransmissionInterval(11);
        limelight.pipelineSwitch(0);
        limelight.start();
    }
    //Get distance from ____ to the block
    public double distanceFrom(){
        double heightOffGround = blockHeightOffGround-camHeightOffGround;
        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()) {
            List<LLResultTypes.ColorResult> colorResults = result.getColorResults();
            for (LLResultTypes.ColorResult cr : colorResults) {
                distance = heightOffGround / (Math.tan(Math.toRadians(cr.getTargetYDegrees()) - a1));
            }
            if (!colorResults.isEmpty()) {
                return distance;
            }
        }
        return 0;
    }
    // 0= tan^-1 y2-y1/x2-x1
    //1 is a corner
    //2 is a corner next to it

    public List<List<Double>> orientationOf(){
        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()) {
            List<LLResultTypes.ColorResult> colorResults = result.getColorResults();
            for (LLResultTypes.ColorResult cr : colorResults) {
                corners = cr.getTargetCorners();
            }
            if (!corners.isEmpty()) {
                return corners;
            }
        }
        return java.util.Collections.emptyList();
    }
}
