package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.firstinspires.ftc.robotcore.external.JavaUtil;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name="test", group="Linear Opmode")
public class test extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    ColorSensor colorSensor;
    private DcMotor oneDrive = null;
    private DcMotor twoDrive = null;
    private DcMotor threeDrive = null;
    private DcMotor fourDrive = null;

    @Override
    public void runOpMode() {
        colorSensor = hardwareMap.get(ColorSensor.class, "color_Sensor");

        oneDrive  = hardwareMap.get(DcMotor.class, "one_Drive");
        twoDrive = hardwareMap.get(DcMotor.class, "two_Drive");
        threeDrive  = hardwareMap.get(DcMotor.class, "three_Drive");
        fourDrive = hardwareMap.get(DcMotor.class, "four_Drive");


        waitForStart();
        runtime.reset();

        while(opModeIsActive()) {
            oneDrive.setPower(.5);
        }
    }
}
