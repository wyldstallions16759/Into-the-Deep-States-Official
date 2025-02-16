package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.SlidePairSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.WristSubsystem28147;

@TeleOp(name="Official Teleop 28147")
public class OfficialTeleop28147 extends LinearOpMode {
    final double TRIGGER_TOLERANCE = 0.4;
    final double HORIZONTAL_SLIDE_SPEED = 0.025; // Tune this to Operator's liking.
    final double VERTICAL_EXTENSION_SUBMERSIBLE = 0.2; // Tune this to submersible height
    final double VERTICAL_EXTENSION_SPEED = 0.025; // Tune this to Operator's liking. Only used during Manual Override mode.

    // Declare OpMode members for each of the 4 motors.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor rightBackDrive = null;

    private SlidePairSubsystem horizontalSlides;
    private SlidePairSubsystem verticalSlides;

    private WristSubsystem28147 horizontalWrist;
    private WristSubsystem28147 verticalWrist;

    @Override
    public void runOpMode() {

        // Initialize the hardware variables. Note that the strings used here must correspond
        // to the names assigned during the robot configuration step on the DS or RC devices.
        leftFrontDrive  = hardwareMap.get(DcMotor.class, "lf");
        leftBackDrive  = hardwareMap.get(DcMotor.class, "lb");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "rf");
        rightBackDrive = hardwareMap.get(DcMotor.class, "rb");

        /// reverse one side of motors
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

        // initialize subsystems
        /*horizontalSlides = new SlidePairSubsystem(hardwareMap,
                "lhslide", "rhslide",
                100, 90,
                DcMotor.Direction.FORWARD, DcMotor.Direction.REVERSE,
                5);

        verticalSlides = new SlidePairSubsystem(hardwareMap,
                "lvslide", "rvslide",
                100, 90, // CHANGE THESE VALUES!!!!
                DcMotor.Direction.FORWARD, DcMotor.Direction.REVERSE,
                15); // CHANGE TOLERANCE!!!


        horizontalWrist = new WristSubsystem28147(hardwareMap, telemetry,
                "hwrist", "hclaw");

        verticalWrist = new WristSubsystem28147(hardwareMap, telemetry,
                "vwrist", "vclaw");

         */

        // Two more subsystems required - The vertical elevator arm subsystem,
        // and the horiz end effector rotation system.


        // Wait for the game to start (driver presses START)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        // Counting variables/toggles:
        boolean manualOverride = false; // RATHER THAN USING PRESETS USE MANUAL OVERRIDE
        // VERY DANGEROUS IF ACCIDENTALLY TOGGLED. CONTROLLER WILL RUMBLE WHEN ENABLED/DISABLED.
        // DISABLED BY DEFAULT. IF SOMETHING BREAKS, OPERATOR CAN SWITCH INTO MANUAL OVERRIDE MODE.
        // IF WE ARE USING THE LOGITECH CONTROLLERS, RUMBLE WILL NOT WORK. IT WILL DISPLAY ON TELEMETRY
        // ON THE FIRST LINE. COACH WILL KNOW HOW TO SEE IT OR SOMETHING.

        boolean manualOverrideTogglePreviousState = false;

        boolean horizontalEndEffectorClawTogglePreviousState = false;
        boolean verticalEndEffectorClawTogglePreviousState = false;

        double horizontalExtensionPosition = 0;
        double verticalExtensionPosition = 0;

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // GET CONTROLS
            double axial   = -gamepad1.left_stick_y;
            double lateral =  gamepad1.left_stick_x;
            double yaw     =  gamepad1.right_stick_x;

            boolean slowDown = gamepad1.right_bumper;

            // Operator Controls
            boolean manualOverrideToggle = gamepad2.back; // figure out what button this is.

            boolean horizontalExtensionOut = gamepad2.dpad_up;
            boolean horizontalExtensionIn = gamepad2.dpad_down;

            double horizontalEndEffectorRotate = -gamepad2.right_stick_y;
            double horizontalEndEffectorWrist = gamepad2.right_stick_x;
            boolean horizontalEndEffectorClawToggle = gamepad2.right_trigger > TRIGGER_TOLERANCE;

            boolean verticalExtensionUp = gamepad2.y; // go to up preset
            boolean verticalExtensionDown = gamepad2.a; // go to down preset

            //double verticalEndEffectorArmRotate = -gamepad2.left_stick_y;

            boolean verticalArmRotateUp = gamepad2.dpad_left;
            boolean verticalArmRotateDown = gamepad2.dpad_right;

            double verticalEndEffectorWrist = gamepad2.left_stick_x;
            boolean verticalEndEffectorClawToggle = gamepad2.left_trigger > TRIGGER_TOLERANCE;


            // Drive Power:
            double leftFrontPower  = axial + lateral + yaw;
            double rightFrontPower = axial - lateral - yaw;
            double leftBackPower   = axial - lateral + yaw;
            double rightBackPower  = axial + lateral - yaw;

            double max;

            max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
            max = Math.max(max, Math.abs(leftBackPower));
            max = Math.max(max, Math.abs(rightBackPower));

            if (slowDown){
                max *= 5;
            }

            if (max > 1.0) {
                leftFrontPower  /= max;
                rightFrontPower /= max;
                leftBackPower   /= max;
                rightBackPower  /= max;
            }

            leftFrontDrive.setPower(leftFrontPower);
            rightFrontDrive.setPower(rightFrontPower);
            leftBackDrive.setPower(leftBackPower);
            rightBackDrive.setPower(rightBackPower);

            // END Drive Power

            // Start Operator Controls
            /*
            // Horizontal Slides
            if (horizontalExtensionIn){
                horizontalExtensionPosition -= HORIZONTAL_SLIDE_SPEED;
            }
            else if (horizontalExtensionOut){
                horizontalExtensionPosition += HORIZONTAL_SLIDE_SPEED;
            }

            horizontalExtensionPosition = Math.min(Math.max(0, horizontalExtensionPosition), 1);

            // Vertical Slides
            if (verticalExtensionUp) {
                if (manualOverride){
                    verticalExtensionPosition += VERTICAL_EXTENSION_SPEED;
                } else {
                    verticalExtensionPosition = VERTICAL_EXTENSION_SUBMERSIBLE;
                }
            } else if (verticalExtensionDown) {
                if (manualOverride) {
                    verticalExtensionPosition -= VERTICAL_EXTENSION_SPEED;
                } else {
                    verticalExtensionPosition = 0;
                }
            }

            // horizontal/vertical end effector - wrist/claw:
            /// WRIST WILL BE DONE ONLY IN THE "MOVE EVERYTHING" SECTION
            if (horizontalEndEffectorClawToggle && !horizontalEndEffectorClawTogglePreviousState){
                horizontalWrist.claw();
            }

            horizontalEndEffectorClawTogglePreviousState = horizontalEndEffectorClawToggle;

            if (verticalEndEffectorClawToggle && !verticalEndEffectorClawTogglePreviousState){
                verticalWrist.claw();
            }

            verticalEndEffectorClawTogglePreviousState = verticalEndEffectorClawToggle;

            // Move Everything:

            horizontalSlides.slideTo(horizontalExtensionPosition);
            verticalSlides.slideTo(verticalExtensionPosition);

            horizontalWrist.wrist(horizontalEndEffectorWrist);
            verticalWrist.wrist(verticalEndEffectorWrist);

             */

            /// ENABLE MANUAL OVERRIDE MODE:
            if (manualOverrideToggle && !manualOverrideTogglePreviousState){
                manualOverride = !manualOverride;
                gamepad2.rumble(1000);
            }
            manualOverrideTogglePreviousState = manualOverrideToggle;

            // Show the elapsed game time and wheel power.
            telemetry.addData("Manual Override Enabled",manualOverride);
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front left/Right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
            telemetry.addData("Back  left/Right", "%4.2f, %4.2f", leftBackPower, rightBackPower);
            telemetry.addLine("Horizontal Slides");
            //horizontalSlides.log(telemetry);
            telemetry.addLine("Vertical Slides");
            //verticalSlides.log(telemetry);
            telemetry.update();
        }
    }}
