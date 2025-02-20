import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.LimelightSubsystem;

import java.util.ArrayList;
import java.util.List;

@TeleOp(name="NewLimelightTest")
public class NewLimelightSubsystemTest extends LinearOpMode {
    LimelightSubsystem limelightSubsystem;
    List<List<Double>> corners;
    public void runOpMode(){
        limelightSubsystem = new LimelightSubsystem(hardwareMap, telemetry);
        waitForStart();
        while (opModeIsActive()){
            telemetry.addData("Distance", limelightSubsystem.distanceFrom());

            corners = new ArrayList<List<Double>>(limelightSubsystem.orientationOf());
            if (!corners.isEmpty()) {
                telemetry.addData("corners", limelightSubsystem.orientationOf());
//                telemetry.addData("Corner 1", corners.get(0));
//                telemetry.addData("Corner 2", corners.get(1));
//                telemetry.addData("Corner 3", corners.get(2));
//                telemetry.addData("Corner 4", corners.get(3));
                telemetry.addData("Found Corners", true);
            } else {
                telemetry.addData("Found Corners", false);
            }
            telemetry.update();
        }
    }
}
