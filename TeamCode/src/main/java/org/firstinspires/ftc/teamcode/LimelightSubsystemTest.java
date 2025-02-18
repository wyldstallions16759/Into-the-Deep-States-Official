package org.firstinspires.ftc.teamcode;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.LimelightSubsystem;

import java.util.List;

@Autonomous(name="LimelightTest")
public class LimelightSubsystemTest extends LinearOpMode{
    private Limelight3A limelight;

    public void runOpMode(){
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        telemetry.setMsTransmissionInterval(11);
        limelight.pipelineSwitch(0);
        limelight.start();
        waitForStart();

        while (opModeIsActive()){

            LLResult result = limelight.getLatestResult();
            if (result != null && result.isValid()) {
                List<LLResultTypes.FiducialResult> fiducialResults = result.getFiducialResults();
                for (LLResultTypes.FiducialResult fr : fiducialResults) {
                    telemetry.addData("Distance", -10 / (Math.tan(Math.toRadians(fr.getTargetYDegrees()) - 10)));
                }

                telemetry.addData("Resultsize", fiducialResults.size());
            }
            telemetry.update();
        }
    }
}
