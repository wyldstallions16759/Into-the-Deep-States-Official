package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.ServoPivotSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.WristSubsystem28147;

@TeleOp(name="Official Teleop 28147", group = "AAOpmodes")//make it appear at top
public class OfficialTeleop28147 extends LinearOpMode {
    final double TRIGGER_TOLERANCE = 0.4;
    final double STICK_TOLERANCE = 0.25;
    final double HORIZONTAL_SLIDE_SPEED = 1; // Tune this to Operator's liking.
    final double VERTICAL_EXTENSION_SPEED = 1; // Tune this to Operator's liking.
    final double ROLL_SPEED = 0.01;
    final double CLIP_SPECIMEN = .15;

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
        int transferState = 0; // 0 = off, 1 = step 1, etc.
        int previousTransferState = 0;
        boolean transfer_rotateArmTogglePreviousState = false;

        boolean transferTogglePreviousState = false;

        boolean horizontalEndEffectorClawTogglePreviousState = false;
        boolean verticalEndEffectorClawTogglePreviousState = false;

        double horizontalArmPosition = 0.5;
        double horizontalWristPosition = 0;

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // GET CONTROLS
            double axial   = -gamepad1.left_stick_y;
            double lateral =  gamepad1.left_stick_x;
            double yaw     =  gamepad1.right_stick_x;

            boolean slowDown = gamepad1.right_bumper;
            boolean opSlowMode = gamepad1.left_bumper;

            ///  Standard Mode Operator Controls

            //horizontal extension (manual0
            boolean horizontalExtensionOut = gamepad2.dpad_up;
            boolean horizontalExtensionIn = gamepad2.dpad_down;

            //horiz endeffector
            double horizontalArmRotate = -gamepad2.right_stick_y; // make this one STICKY!

            double horizontalEndEffectorWrist = -gamepad2.left_stick_x;
            boolean horizontalEndEffectorClawToggle = gamepad2.right_trigger > TRIGGER_TOLERANCE;

            //vertical extension (manual)
            boolean verticalExtensionUp = gamepad2.y; // go to up
            boolean verticalExtensionDown = gamepad2.a; // go to down

            // vertical arm servos (work together, two presets in one)
            boolean verticalArmRotateUp = gamepad2.b;
            boolean verticalArmRotateDown = gamepad2.x;

            // override wristarm
            double manualArmWristPos = -gamepad2.left_stick_y;
            boolean manualOverrideArmWrist = gamepad2.left_bumper;

            // vertical wristclaw
            boolean verticalEndEffectorClawToggle = gamepad2.left_trigger > TRIGGER_TOLERANCE;

            /// Transfer Mode Operator Controls

            boolean transferToggle = gamepad2.back; // figure out what button this is.
            boolean cancelTransfer = gamepad2.start; // cancel the transfer when this is pressed. No need for a toggle, as it only cancels the transfer.

            boolean transfer_rotateArmToggle = gamepad2.b;

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


            /// ENABLE TRANSFER MODE:
            if (transferToggle && !transferTogglePreviousState){
                transferState++;
                gamepad2.rumble(1000);
            }
            transferTogglePreviousState = transferToggle;

            if (cancelTransfer){
                transferState = 0; // simply cancel the transfer. robot will continue to function as normal.
            }

            // what CAN you control during transfer mode? horizontal slides (normal), vertical slides (normal), bottom wrist - two buttons
            // you annot control the arm attached to vertical slides.

            double opSpeedModifier = 1;
            if (opSlowMode){
                opSpeedModifier = 0.5;
            }

            // Horizontal Slides
            if (horizontalExtensionIn){
//                rhSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
//                lhSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

                rhSlide.setPower(-HORIZONTAL_SLIDE_SPEED*opSpeedModifier);
                lhSlide.setPower(-HORIZONTAL_SLIDE_SPEED*opSpeedModifier);
            }
            else if (horizontalExtensionOut){
//                rhSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
//                lhSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

                rhSlide.setPower(HORIZONTAL_SLIDE_SPEED*opSpeedModifier);
                lhSlide.setPower(HORIZONTAL_SLIDE_SPEED*opSpeedModifier);
            }
            else{
                rhSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                lhSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

                rhSlide.setPower(0);
                lhSlide.setPower(0);
            }

            if (verticalExtensionDown){
//                rvSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
//                lvSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

                rvSlide.setPower(-VERTICAL_EXTENSION_SPEED);
                lvSlide.setPower(-VERTICAL_EXTENSION_SPEED);
            }
            else if (verticalExtensionUp){
//                rvSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
//                lvSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

                rvSlide.setPower(VERTICAL_EXTENSION_SPEED);
                lvSlide.setPower(VERTICAL_EXTENSION_SPEED);
            }
            else{
                rvSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                lvSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

                rvSlide.setPower(0);
                lvSlide.setPower(0);
            }

            if (transferState == 0) {
                // horizontal/vertical end effector - wrist/claw:
                /// WRIST WILL BE DONE ONLY IN THE "MOVE EVERYTHING" SECTION
                if (horizontalEndEffectorClawToggle && !horizontalEndEffectorClawTogglePreviousState) {
                    horizontalWrist.claw();
                }

                horizontalEndEffectorClawTogglePreviousState = horizontalEndEffectorClawToggle;

                if (verticalEndEffectorClawToggle && !verticalEndEffectorClawTogglePreviousState) {
                    verticalWrist.claw();
                }

                verticalEndEffectorClawTogglePreviousState = verticalEndEffectorClawToggle;

                // Move Everything:

                // horizontalSlides.slideTo(horizontalExtensionPosition);
                //verticalSlides.slideTo(verticalExtensionPosition);

                if (Math.abs(horizontalEndEffectorWrist) > STICK_TOLERANCE) {
                    horizontalWristPosition += horizontalEndEffectorWrist * ROLL_SPEED;
                }
                telemetry.addData("horizwristpos", horizontalWristPosition);
                horizontalWristPosition = Math.min(1, Math.max(horizontalWristPosition, -1)); // limit it to 0-1


                horizontalWrist.wrist(horizontalWristPosition);

                // arm and roll
                if (Math.abs(horizontalArmRotate) > STICK_TOLERANCE) {
                    telemetry.addLine("Trigger toggled!!!");
                    horizontalArmPosition += horizontalArmRotate * ROLL_SPEED;
                }
                telemetry.addData("horizarmpos", horizontalArmPosition);
                horizontalArmPosition = Math.min(1, Math.max(horizontalArmPosition, 0)); // limit it to 0-1

                bottomRoll.armToCustom(horizontalArmPosition);

                // arm on the vertical extension slides.
                if (verticalArmRotateUp) {
                    armBase.armToRaised();
                    armWrist.armToRaised();
                    verticalWrist.wrist(-1);
                } else if (verticalArmRotateDown) {
                    armBase.armToRest();
                    armWrist.armToRest();
                    verticalWrist.wrist(1);
                }

                if (manualOverrideArmWrist) {
                    armWrist.armToCustom((manualArmWristPos + 1) / 2);
                }
            }
            else{ // if transferstate != 0
                // we can still move both sets of slides. We must be able to move bottom wrist in a toggly fashion.
                // first, program the transfermode presets
                //if (transferState != previousTransferState){ // if state just changed
                    if (transferState == 1){ // state one - lower arm moves up, top arm moves down but not all the way.
                        armBase.armToCustom(0.6); // retune upon mechanical adjustments.
                        horizontalWrist.wrist(0);
                        verticalWrist.wrist(-.95);
                        armWrist.armToCustom(.85);
                        verticalWrist.claw(WristSubsystem28147.ClawState.OPEN);
                        bottomRoll.armToCustom(0.3);
                        horizontalArmPosition = 0.3;
                        horizontalWristPosition = 0;
                    }
                    else if (transferState == 2){
                        armBase.armToCustom(.7);
                        armWrist.armToCustom(.85 );
                    }
                    else if (transferState == 3){
                        verticalWrist.claw(WristSubsystem28147.ClawState.CLOSED);
                    }
                    else if (transferState == 4){
                        horizontalWrist.claw(WristSubsystem28147.ClawState.OPEN);
                        transferState = 0;
                    }
               // }
                previousTransferState = transferState;

                if (transferState == 1){
                    if (transfer_rotateArmToggle && !transfer_rotateArmTogglePreviousState){
                        if (horizontalWristPosition == -1){
                            horizontalWristPosition = 1;
                        }
                        else{
                            horizontalWristPosition = -1;
                        }

                        horizontalWrist.wrist(horizontalWristPosition);
                    }
                    transfer_rotateArmTogglePreviousState = transfer_rotateArmToggle;
                }
            }





            // Show the elapsed game time and wheel power.
            telemetry.addData("Transfer Mode Enabled",transferState);
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
