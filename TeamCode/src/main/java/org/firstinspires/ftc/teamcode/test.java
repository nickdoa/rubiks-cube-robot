package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.JavaUtil;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.ArrayList;


@TeleOp(name = "test", group = "Linear Opmode")
public class test extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    ColorSensor colorSensor;
    private DcMotor baseMotor = null;  // rotates the cube base
    private DcMotor flipMotor = null;  // flips the cube
    private Servo servo = null;


    @Override
    public void runOpMode() {

        colorSensor = hardwareMap.get(ColorSensor.class, "color_Sensor");
        baseMotor = hardwareMap.get(DcMotor.class, "baseMotor");
        flipMotor = hardwareMap.get(DcMotor.class, "flipMotor");
        servo = hardwareMap.get(Servo.class, "colorServo");
        
        servo.setPosition(0);
        sleep(1000);
    }
}
