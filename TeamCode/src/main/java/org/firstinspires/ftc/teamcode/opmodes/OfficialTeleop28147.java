package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.ServoPivotSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.SlidePairSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.WristSubsystem28147;

@TeleOp(name="Official Teleop 28147", group = "AAOpmodes")//make it appear at top
public class OfficialTeleop28147 extends LinearOpMode {
    final double TRIGGER_TOLERANCE = 0.4;
    final double HORIZONTAL_SLIDE_SPEED = 1; // Tune this to Operator's liking.
    final double VERTICAL_EXTENSION_SPEED = 1; // Tune this to Operator's liking.
    final double ROLL_SPEED = 0.01;

    // Declare OpMode members for each of the 4 motors.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor rightBackDrive = null;

    private DcMotor lhSlide;
    private DcMotor rhSlide;
    private DcMotor lvSlide;
    private DcMotor rvSlide;

    private WristSubsystem28147 horizontalWrist;
    private WristSubsystem28147 verticalWrist;

    private ServoPivotSubsystem bottomRoll;
    private ServoPivotSubsystem armBase;
    private ServoPivotSubsystem armWrist;

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

        /// set to brake mode
        leftFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        // initialize subsystems and motors

        lhSlide = hardwareMap.get(DcMotor.class, "lhslide");
        rhSlide = hardwareMap.get(DcMotor.class, "rhslide");

        rhSlide.setDirection(DcMotor.Direction.REVERSE);
        lhSlide.setDirection(DcMotor.Direction.FORWARD);
        rhSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lhSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        lvSlide = hardwareMap.get(DcMotor.class, "lvslide");
        rvSlide = hardwareMap.get(DcMotor.class, "rvslide");

        rvSlide.setDirection(DcMotor.Direction.FORWARD);
        lvSlide.setDirection(DcMotor.Direction.REVERSE);

        rvSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lvSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        horizontalWrist = new WristSubsystem28147(hardwareMap, telemetry,
                "bottomclawwrist", "bottomclaw");

        verticalWrist = new WristSubsystem28147(hardwareMap, telemetry,
                "topclawwrist", "topclaw");

        bottomRoll = new ServoPivotSubsystem(hardwareMap, "lrollbottom", "rrollbottom", ServoPivotSubsystem.PartType.HORIZONTAL_EXTENSION_JOINT);
        armBase = new ServoPivotSubsystem(hardwareMap, "rarmbase","larmbase",ServoPivotSubsystem.PartType.VERTICAL_EXTENSION_BASE);
        armWrist = new ServoPivotSubsystem(hardwareMap, "laservo", "raservo", ServoPivotSubsystem.PartType.VERTICAL_EXTENSION_ARM);

        // Two more subsystems required - The vertical elevator arm subsystem,
        // and the horiz end effector rotation system. "BottomRoll" is the horiz rotation.


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
        // ON THE FIRST LINE. COACH WILL KNOW HOW TO SEE IT OR SOMETHING. PROBABLY WON'T DO ANYTHING

        boolean manualOverrideTogglePreviousState = false;

        boolean horizontalEndEffectorClawTogglePreviousState = false;
        boolean verticalEndEffectorClawTogglePreviousState = false;

        double horizontalArmPosition = 0.5;

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // GET CONTROLS
            double axial   = -gamepad1.left_stick_y;
            double lateral =  gamepad1.left_stick_x;
            double yaw     =  gamepad1.right_stick_x;

            boolean slowDown = gamepad1.right_bumper;

            // Operator Controls
            boolean manualOverrideToggle = gamepad2.back; // figure out what button this is. probably wont do anything.

            //horizontal extension (manual0
            boolean horizontalExtensionOut = gamepad2.dpad_up;
            boolean horizontalExtensionIn = gamepad2.dpad_down;

            //horiz endeffector
            double horizontalArmRotate = -gamepad2.right_stick_y; // make this one STICKY!


            double horizontalEndEffectorWrist = gamepad2.right_stick_x;
            boolean horizontalEndEffectorClawToggle = gamepad2.right_trigger > TRIGGER_TOLERANCE;

            //vertical extension (manual)
            boolean verticalExtensionUp = gamepad2.y; // go to up
            boolean verticalExtensionDown = gamepad2.a; // go to down

            // vertical arm servos (work together, two presets in one)
            boolean verticalArmRotateUp = gamepad2.x;
            boolean verticalArmRotateDown = gamepad2.b;

            // override wristarm
            double manualArmWristPos = -gamepad2.left_stick_y;
            boolean manualOverrideArmWrist = gamepad2.left_bumper;

            // vertical wristclaw
            double verticalEndEffectorWrist = gamepad2.left_stick_x;
            boolean verticalEndEffectorClawToggle = gamepad2.left_trigger > TRIGGER_TOLERANCE;

            telemetry.addData("vertical in button", gamepad2.left_trigger);

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
                max *= 3.3;
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

            // Horizontal Slides
            if (horizontalExtensionIn){
                rhSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                lhSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

                rhSlide.setPower(-HORIZONTAL_SLIDE_SPEED);
                lhSlide.setPower(-HORIZONTAL_SLIDE_SPEED);
            }
            else if (horizontalExtensionOut){
                rhSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                lhSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

                rhSlide.setPower(HORIZONTAL_SLIDE_SPEED);
                lhSlide.setPower(HORIZONTAL_SLIDE_SPEED);
            }
            else{
                rhSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                lhSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

                rhSlide.setPower(0);
                lhSlide.setPower(0);
            }

            if (verticalExtensionDown){
                rvSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                lvSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

                rvSlide.setPower(-VERTICAL_EXTENSION_SPEED);
                lvSlide.setPower(-VERTICAL_EXTENSION_SPEED);
            }
            else if (verticalExtensionUp){
                rvSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                lvSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

                rvSlide.setPower(VERTICAL_EXTENSION_SPEED);
                lvSlide.setPower(VERTICAL_EXTENSION_SPEED);
            }
            else{
                rvSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                lvSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

                rvSlide.setPower(0);
                lvSlide.setPower(0);
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

           // horizontalSlides.slideTo(horizontalExtensionPosition);
            //verticalSlides.slideTo(verticalExtensionPosition);

            horizontalWrist.wrist(horizontalEndEffectorWrist);

            // arm and roll
            if (Math.abs(horizontalArmRotate) > TRIGGER_TOLERANCE) {
                telemetry.addLine("Trigger toggled!!!");
                horizontalArmPosition += horizontalArmRotate*ROLL_SPEED;
            }
            telemetry.addData("horizarmpos",horizontalArmPosition);
            horizontalArmPosition = Math.min(1,Math.max(horizontalArmPosition, 0)); // limit it to 0-1

            bottomRoll.armToCustom(horizontalArmPosition);

            // arm on the vertical extension slides.
            if (verticalArmRotateUp){
                armBase.armToRaised();
                armWrist.armToRaised();
                verticalWrist.wrist(1);
            }
            else if (verticalArmRotateDown){
                armBase.armToRest();
                armWrist.armToRest();
                verticalWrist.wrist(-1);
            }

            if (manualOverrideArmWrist){
                armWrist.armToCustom((manualArmWristPos+1)/2);
            }



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
