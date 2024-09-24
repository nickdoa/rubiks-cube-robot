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
        servo = hardwareMap.get(Servo.class, "one_Servo");
        motor = hardwareMap.get(DcMotor.class, "one_Drive");
        colorSensor = hardwareMap.get(ColorSensor.class, "color_Sensor");

        motor.setPower(1);
        sleep(1000);
    }
}
