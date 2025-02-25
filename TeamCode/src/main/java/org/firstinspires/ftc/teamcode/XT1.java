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
import org.firstinspires.ftc.teamcode.subsystems.ServoSubsystem16760;
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
        leftFrontDrive = hardwareMap.get(DcMotor.class, "frontLeft");
        leftBackDrive = hardwareMap.get(DcMotor.class, "backLeft");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "frontRight");
        rightBackDrive = hardwareMap.get(DcMotor.class, "backRight");
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);
        leftFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);



        SlidePairSubsystem Elevation = new SlidePairSubsystem(hardwareMap,
                "ElevatorA", "ElevatorB",
                3284, 3339,
                DcMotor.Direction.REVERSE, DcMotor.Direction.FORWARD,
                20);
        SlidePairSubsystem Extension = new SlidePairSubsystem(hardwareMap,
                "SlideA", "SlideB",
                4000, 4000,
                DcMotor.Direction.FORWARD, DcMotor.Direction.REVERSE,
                5);
        ServoSubsystem16760 Servo = new ServoSubsystem16760(hardwareMap,telemetry);
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
        double position = 0;
        Wrist.scaleRange(0, 1);
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            double max;

            // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
            double axial = gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
            double lateral = -gamepad1.left_stick_x;
            double yaw = gamepad1.right_stick_y;
            float up = gamepad2.left_stick_y;
            float out = gamepad2.right_stick_y;
            boolean claw_toggle = gamepad2.dpad_up;
            boolean toSub = gamepad2.y;
            boolean toGround = gamepad2.a;
            boolean release_slightly_claw = gamepad2.left_bumper;
            boolean slow_the_flip_down = gamepad1.right_bumper;
            float intakeIn = gamepad2.left_trigger;
            float intakeOut = gamepad2.right_trigger;
            boolean sampleReady = gamepad2.right_bumper;
            boolean wallReady = gamepad2.x;
            boolean specimenReady = gamepad2.left_bumper;
            boolean preset_specimen = gamepad2.right_bumper;
            boolean onoroff_Specimen = false;
            boolean reset_encoders = gamepad2.x;
            boolean SUB = gamepad2.dpad_left;
            boolean OZ = gamepad2.dpad_right;
            double dumb = 13.9;
            int wallReadyCounter = 1;
            int clawToggleCounter = 1;
            int sampleReadyCounter = 1;
            int toSubCounter = 1;
            double ElevAPos = Elevation.getAPosition();
            double ElevBPos = Elevation.getBPosition();
            double combinedPos = ElevAPos + ElevBPos;
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
            if (toGround) {
                Servo.toGround(true);
            } else {
                Servo.toGround(false);
            }
            if (slow_the_flip_down) {
                max *= 5;
            }
            if (combinedPos > 800) {
                max *= combinedPos / 800;
            }

            if (up > 0.05) {
                Elevation.setPower(-up);
            } else if (up < -0.05) {
                Elevation.setPower(-up);
            } else {
                Elevation.setPower(0);
            }
            if (out > 0.05) {
                Extension.setPower(-out);
            } else if (out < -0.05) {
                Extension.setPower(-out);
            } else {
                Extension.setPower(0);
            }
                if (max > 1.0) {
                    leftFrontPower /= max;
                    rightFrontPower /= max;
                    leftBackPower /= max;
                    rightBackPower /= max;
                }

                if (wallReady) {
                    wallReadyCounter += 1;
                }
                if (sampleReady) {
                    sampleReadyCounter += 1;
                }
                if (claw_toggle) {
                    clawToggleCounter += 1;
                }
                if (sampleReady) {
                    Servo.movePendulum(0.05);
                    Servo.setWrist(true);
                    Servo.closeClaw(false);
                    Servo.moveIntake(0.13);
                    sleep(500);
                    Servo.closeClaw(true);
                    sleep(100);
                } else if (wallReadyCounter%2 == 0) {
                    Servo.movePendulum(0.5);
                    sleep(600);
                    Servo.setWrist(false);
                    sleep(300);
                    Servo.movePendulum(0);
                    sleep(100000);
                } else if (specimenReady) {
                    Servo.movePendulum(0.9);
                    sleep(300);
                    Servo.setWrist(true);
                    Servo.closeClaw(true);
                    sleep(300);
                    Servo.closeClaw(false);
                } else {
                    Servo.movePendulum(0);
                    Servo.setWrist(true);
                    Servo.closeClaw(false);
                }
                if (claw_toggle) {
                    clawToggleCounter += 1;
                }
                if (claw_toggle) {
                    Servo.closeClaw(true);
                }
                if (toSub) {
                    toSubCounter += 1;
                }
                if (toSubCounter % 2 == 0) {
                    position = 0.2;
                }


                position = Math.min(Math.max(position, 0), 1);


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
//            if (toSub) {
//                Elevation.slideTo(0.3);
//            }

                // Send calculated power to wheels
                leftFrontDrive.setPower(leftFrontPower);
                rightFrontDrive.setPower(rightFrontPower);
                leftBackDrive.setPower(leftBackPower);
                rightBackDrive.setPower(rightBackPower);

                if (intakeIn > 0.05) {
                    Servo.runIntake(intakeIn,false);
                } else {
                    Servo.runIntake(intakeIn,false);
                }
                if (intakeOut > 0.05) {
                    Servo.runIntake(intakeIn,true);
                } else {
                    Servo.runIntake(intakeIn,true);
                }
//            if (toSub && !firstTime) {
//
//                firstTime = Elevation.slideTo(0.3);
//            } else {
//                firstTime = true;
//            }

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