package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.LimelightSubsystem;

import java.util.List;

@TeleOp(name = "LimelightTest")
public class LimelightSubsystemTest extends LinearOpMode {
    private Limelight3A limelight;
    static final double camHeightOffGround = 13;
    double blockHeightOffGround = 1.4;
    double heightOffGround = blockHeightOffGround - camHeightOffGround;
    static final double a1 = 0;

    public void runOpMode() {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        telemetry.setMsTransmissionInterval(11);
        limelight.pipelineSwitch(0);
        limelight.start();
        waitForStart();
        boolean lastchanger = false;

        while (opModeIsActive()) {
            boolean changer = gamepad2.y;
            double heightOffGround = blockHeightOffGround - camHeightOffGround;
            LLResult result = limelight.getLatestResult();
            if (result != null && result.isValid()) {
                List<LLResultTypes.ColorResult> colorResults = result.getColorResults();
                for (LLResultTypes.ColorResult cr : colorResults) {
                    telemetry.addData("Distance", heightOffGround / (Math.tan(Math.toRadians(cr.getTargetYDegrees()) - a1)));
                }

                telemetry.addData("Resultsize", colorResults.size());
            }
            if (changer && !lastchanger) {
                blockHeightOffGround += 0.1;
            }
            lastchanger = changer;
            telemetry.addData("Block Height Off Ground", blockHeightOffGround);
            telemetry.update();
        }
    }
}
