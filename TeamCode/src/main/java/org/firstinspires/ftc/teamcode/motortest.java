package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;

@TeleOp
public class motortest extends LinearOpMode {
    DcMotor motor;
    Servo servo;
    ColorSensor colorSensor;

    @Override
    public void runOpMode() throws InterruptedException {

        motor = hardwareMap.get(DcMotor.class, "flipMotor");

        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setTargetPosition(0);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        waitForStart();

        while (opModeIsActive()) {

            motor.setTargetPosition(50);
            motor.setPower(0.5);

            while (motor.isBusy() && opModeIsActive()) {
                telemetry.addData("Moving to 25", motor.getCurrentPosition());
                telemetry.update();
            }

            sleep(1000);

            motor.setTargetPosition(-5);
            motor.setPower(0.5);

            while (motor.isBusy() && opModeIsActive()) {
                telemetry.addData("Moving to -25", motor.getCurrentPosition());
                telemetry.update();
            }

            sleep(50);

            motor.setTargetPosition(-1);
            motor.setPower(2);

            // Wait for the small tick move to complete
            while (motor.isBusy() && opModeIsActive()) {
                telemetry.addData("Moving slight tick back", motor.getCurrentPosition());
                telemetry.update();
            }

            // Sleep before starting the next cycle
            sleep(500);
        }
    }
}
