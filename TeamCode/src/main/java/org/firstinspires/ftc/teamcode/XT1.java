package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.ftc.LazyImu;
import com.qualcomm.hardware.bosch.BHI260IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.subsystems.SlidePairSubsystem;

/*
 * This file contains an example of a Linear "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When a selection is made from the menu, the corresponding OpMode is executed.
 *
 * This particular OpMode illustrates driving a 4-motor Omni-Directional (or Holonomic) robot.
 * This code will work with either a Mecanum-Drive or an X-Drive train.
 * Both of these drives are illustrated at https://gm0.org/en/latest/docs/robot-design/drivetrains/holonomic.html
 * Note that a Mecanum drive must display an X roller-pattern when viewed from above.
 *
 * Also note that it is critical to set the correct rotation direction for each motor.  See details below.
 *
 * Holonomic drives provide the ability for the robot to move in three axes (directions) simultaneously.
 * Each motion axis is controlled by one Joystick axis.
 *
 * 1) Axial:    Driving forward and backward               Left-joystick Forward/Backward
 * 2) Lateral:  Strafing right and left                     Left-joystick Right and Left
 * 3) Yaw:      Rotating Clockwise and counter clockwise    Right-joystick Right and Left
 *
 * This code is written assuming that the right-side motors need to be reversed for the robot to drive forward.
 * When you first test your robot, if it moves backward when you push the left stick forward, then you must flip
 * the direction of all 4 motors (see code below).
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 */

@TeleOp(name="XT1-", group="Linear OpMode")
//@Disabled
public class XT1 extends LinearOpMode {

    // Declare OpMode members for each of the 4 motors.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor rightBackDrive = null;
    private DcMotor ElevatorA = null;
    private DcMotor ElevatorB = null;
    private DcMotor SlideA = null;
    private DcMotor SlideB = null;
    private Servo IntakeA = null;
    private Servo IntakeB = null;
    private Servo IntakeElevationA = null;
    private Servo IntakeElevationB = null;
    private Servo PendulumA = null;
    private Servo PendulumB = null;
    private Servo Wrist = null;
    private Servo Claw = null;
    public LazyImu lazyImu;
    public IMU imu;
    private PinpointDrive drive;
    private ArmSubsystem arm;


//    //private Servo LeftFinger = null;
//    private Servo RightFinger = null;

    //declare subsystems:

    public void runOpMode() {

        // Initialize the hardware variables. Note that the strings used here must correspond
        // to the names assigned during the robot configuration step on the DS or RC devices.
        arm = new ArmSubsystem(hardwareMap,telemetry);
//        IntakeA = hardwareMap.get(Servo.class,"IntakeA");
//        IntakeB = hardwareMap.get(Servo.class,"IntakeB");
//        PendulumA = hardwareMap.get(Servo.class,"PendulumA");
//        PendulumB = hardwareMap.get(Servo.class,"PendulumB");
//        IntakeElevationA = hardwareMap.get(Servo.class,"IntakeElevationA");
//        IntakeElevationB = hardwareMap.get(Servo.class,"IntakeElevationB");
//        leftFrontDrive  = hardwareMap.get(DcMotor.class, "frontLeft");
//        leftBackDrive  = hardwareMap.get(DcMotor.class, "backLeft");
//        rightFrontDrive = hardwareMap.get(DcMotor.class, "frontRight");
//        rightBackDrive = hardwareMap.get(DcMotor.class, "backRight");
//        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
//        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
//        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
//        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);
////        RightFinger.setDirection(Servo.Direction.FORWARD);
//        leftFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        leftBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        rightFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        rightBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        IntakeA.setDirection(Servo.Direction.REVERSE);
//        PendulumA.setDirection(Servo.Direction.REVERSE);
//        IntakeElevationB.setDirection(Servo.Direction.REVERSE);


        SlidePairSubsystem Elevation = new SlidePairSubsystem(hardwareMap,
                "ElevatorA", "ElevatorB",
                4000, 4000,
                DcMotor.Direction.FORWARD, DcMotor.Direction.REVERSE,
                5);
        SlidePairSubsystem Extension = new SlidePairSubsystem(hardwareMap,
                "ElevatorA", "ElevatorB",
                4000, 4000,
                DcMotor.Direction.FORWARD, DcMotor.Direction.REVERSE,
                5);
        //rr
//        arm = new ArmSubsystem(hardwareMap,telemetry);
//        //LeftFinger = hardwareMap.get(Servo.class, "LeftFinger");
//        RightFinger= hardwareMap.get(Servo.class, "RightFinger");
        // create subsystems

//        ArmSubsystem arm = new ArmSubsystem(hardwareMap,telemetry);
//        wristSubsystem = new WristSubsystem(hardwareMap, telemetry);
        // ########################################################################################
        // !!!            IMPORTANT Drive Information. Test your motor directions.            !!!!!
        // ########################################################################################
        // Most robots need the motors on one side to be reversed to drive forward.
        // The motor reversals shown here are for a "direct drive" robot (the wheels turn the same direction as the motor shaft)
        // If your robot has additional gear reductions or uses a right-angled drive, it's important to ensure
        // that your motors are turning in the correct direction.  So, start out with the reversals here, BUT
        // when you first test your robot, push the left joystick forward and observe the direction the wheels turn.
        // Reverse the direction (flip FORWARD <-> REVERSE ) of any wheel that runs backward
        // Keep testing until ALL the wheels move the robot forward when you push the left joystick forward.

//        ElevatorA.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        ElevatorB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
////        RightFinger.scaleRange(0.4,0.7);
//        ElevatorA.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        ElevatorB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//
//        ElevatorA.setDirection(DcMotor.Direction.REVERSE);
//        ElevatorB.setDirection(DcMotor.Direction.FORWARD);
////        RightFinger.setDirection(Servo.Direction.FORWARD);
//        ElevatorA.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        ElevatorB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        // Wait for the game to start (driver presses START)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        boolean oldWristButton = false;
        float oldClawButton = 0;

        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            double max;

            // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
            double axial = gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
            double lateral = -gamepad1.left_stick_x;
            double yaw = gamepad1.right_stick_y;
            float up = gamepad2.right_stick_y;
            float out = gamepad2.left_stick_y;
            float claw_in = gamepad2.right_trigger;
            float claw_out = gamepad2.left_trigger;
            float claw_toggle = gamepad2.right_trigger;
            boolean toSub = gamepad2.b;
            boolean toGround = gamepad2.a;
            boolean release_slightly_claw = gamepad2.left_bumper;
            boolean slow_the_flip_down = gamepad1.right_bumper;
            float intakeIn = gamepad2.right_trigger;
            float intakeOut = gamepad2.left_trigger;
            boolean sampleReady = gamepad2.right_bumper;
            boolean wallReady = gamepad2.x;
            boolean specimenReady = gamepad2.left_bumper;
            boolean preset_specimen = gamepad2.right_bumper;
            boolean onoroff_Specimen = false;
            boolean reset_encoders = gamepad2.x;
            boolean SUB = gamepad2.dpad_left;
            boolean OZ = gamepad2.dpad_right;
            double dumb = 13.9;
//            double ElevAPos = ElevatorA.getCurrentPosition();
//            double ElevBPos = ElevatorB.getCurrentPosition();
//            double combinedPos = ElevAPos + ElevBPos;
            boolean firstTime = false;
//            double RobotTipAngle = imu.getRobotYawPitchRollAngles().getPitch();


            // Combine the joystick requests for each axis-motion to determine each wheel's power.
            // Set up a variable for each drive wheel to save the power level for telemetry.
            double leftFrontPower = axial + lateral + yaw;
            double rightFrontPower = axial - lateral - yaw;
            double leftBackPower = axial - lateral + yaw;
            double rightBackPower = axial + lateral - yaw;

            // Normalize the values so no wheel power exceeds 100%
            // This ensures that the robot maintains the desired motion.
            max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
            max = Math.max(max, Math.abs(leftBackPower));
            max = Math.max(max, Math.abs(rightBackPower));

            if (slow_the_flip_down) {
                max *= 5;
            }
//            if (combinedPos>800) {
//                max *= combinedPos/800;
//            }

            if (max > 1.0) {
                leftFrontPower /= max;
                rightFrontPower /= max;
                leftBackPower /= max;
                rightBackPower /= max;
            }
//            if (sampleReady) {
//                PendulumA.setPosition(180/300);
//                PendulumB.setPosition(180/300);
//                Wrist.setPosition(0);
//            } else {
//                PendulumA.setPosition(90/300);
//                PendulumB.setPosition(90/300);
//            }
//            if (wallReady) {
//                PendulumA.setPosition(90/300);
//                PendulumB.setPosition(90/300);
//                sleep(300);
//                Wrist.setPosition(180/300);
//                sleep(300);
//                PendulumA.setPosition(0);
//                PendulumB.setPosition(0);
//            } else {
//                PendulumA.setPosition(90/300);
//                PendulumB.setPosition(90/300);
//                Wrist.setPosition(0);
//            }
//            if (specimenReady) {
//                PendulumA.setPosition(180/300);
//                PendulumB.setPosition(180/300);
//                sleep(300);
//                Wrist.setPosition(0);
//            } else {
//                PendulumA.setPosition(90/300);
//                PendulumB.setPosition(90/300);
//                Wrist.setPosition(0);
//            }
//            if (toGround) {
//                IntakeElevationA.setPosition(45/300);
//                IntakeElevationB.setPosition(45/300);
//            } else {
//                IntakeElevationA.setPosition(0);
//                IntakeElevationB.setPosition(0);
//            }
            // This is test code:
            //
            // Uncomment the following code to test your motor directions.
            // Each button should make the corresponding motor run FORWARD.
            //   1) First get all the motors to take to correct positions on the robot
            //      by adjusting your Robot Configuration if necessary.
            //   2) Then make sure they run in the correct direction by modifying the
            //      the setDirection() calls above.
            // Once the correct motors move in the correct direction re-comment this code.


            /*
            leftFrontPower  = gamepad1.x ? 1.0 : 0.0;  // X gamepad
            leftBackPower   = gamepad1.a ? 1.0 : 0.0;  // A gamepad
            rightFrontPower = gamepad1.y ? 1.0 : 0.0;  // Y gamepad
            rightBackPower  = gamepad1.b ? 1.0 : 0.0;  // B gamepad
            */
            if (toSub) {
                Elevation.slideTo(0.3);
            }

            // Send calculated power to wheels
//            leftFrontDrive.setPower(leftFrontPower);
//            rightFrontDrive.setPower(rightFrontPower);
//            leftBackDrive.setPower(leftBackPower);
//            rightBackDrive.setPower(rightBackPower);
//            if (up > 0.05) {
//                ElevatorA.setPower(-1);
//                ElevatorB.setPower(-1);
//            } else if (up < -0.05) {
//                ElevatorA.setPower(1);
//                ElevatorB.setPower(1);
//            } else {
//                ElevatorA.setPower(0);
//                ElevatorB.setPower(0);
//            }
//            if (intakeIn > 0.05) {
//                IntakeA.setPosition(1);
//                IntakeB.setPosition(1);
//            } else {
//                ElevatorA.setPower(0.5);
//                ElevatorB.setPower(0.5);
//            }
//            if (intakeOut > 0.05) {
//                IntakeA.setPosition(0);
//                IntakeB.setPosition(0);
//            } else {
//                ElevatorA.setPower(0.5);
//                ElevatorB.setPower(0.5);
//            }
            if (toSub && !firstTime) {

                firstTime = Elevation.slideTo(0.3);
            } else {
                firstTime = true;
            }

            // Wrist Subsystem calls:
//            if (SUB) {
//                SUBMERSIBLE = new Pose2D(DistanceUnit.INCH,-29 ,dumb += 2,AngleUnit.DEGREES,0);
//                pinpoint.driveTo(SUBMERSIBLE,0.3,0);
//            } else {
//                leftFrontDrive.setPower(0);
//                rightFrontDrive.setPower(0);
//                leftBackDrive.setPower(0);
//                rightBackDrive.setPower(0);
//            }
//            if (OZ) {
//                pinpoint.driveTo(OBSERVATION,0.3,0);
//            } else {
//                leftFrontDrive.setPower(0);
//                rightFrontDrive.setPower(0);
//                leftBackDrive.setPower(0);
//                rightBackDrive.setPower(0);
//            }
            oldClawButton = claw_toggle;

            // Show the elapsed game time and wheel power.
//            telemetry.addData("ElevatorA",ElevatorA.getCurrentPosition());
//            telemetry.addData("ElevatorB",ElevatorB.getCurrentPosition());
//            telemetry.addData("CombinedPos",combinedPos);
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front left/Right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
            telemetry.addData("Back  left/Right", "%4.2f, %4.2f", leftBackPower, rightBackPower);
            telemetry.update();
        }
//l
    }

}