package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import java.util.ArrayList;

@TeleOp
public class motortest extends LinearOpMode {
    DcMotor motor;
    Servo servo;
    ColorSensor colorSensor;

    @Override
    public void runOpMode() throws InterruptedException {

        motor = hardwareMap.get(DcMotor.class, "flipMotor");

        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setPower(.1);
        motor.setTargetPosition(526);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setTargetPosition(0);
        sleep(1000);
    }
}
