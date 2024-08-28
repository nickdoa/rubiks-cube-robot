package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.JavaUtil;


@TeleOp(name="ColorReaderTest", group="Linear Opmode")
public class ColorReaderTest extends LinearOpMode {
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    ColorSensor colorSensor;
    /*
    private DcMotor oneDrive = null;
    private DcMotor twoDrive = null;
    private DcMotor threeDrive = null;
    private DcMotor fourDrive = null;

    private Servo oneServo = null;
    private Servo twoServo = null;
    private Servo threeServo = null;
    private Servo fourServo = null;

    private int currentStep = 10;
    private int runningStep = -1;
    private double stepStartTime = 0;
    */

    private int hue = 0;
    private String currentColor = "";

    private String[][] cube = {
            {"R","R","R","R","R","R","R","R","R"},
            {"O","O","O","O","O","O","O","O","O"},
            {"W","W","W","W","W","W","W","W","W"},
            {"Y","Y","Y","Y","Y","Y","Y","Y","Y"},
            {"G","G","G","G","G","G","G","G","G"},
            {"B","B","B","B","B","B","B","B","B"},


    };

    private int cubePos = 0;
    private int cubeFace = 0;


    @Override
    public void runOpMode() {

        telemetry.addData("Status", "Initialized");
        telemetry.update();


        colorSensor = hardwareMap.get(ColorSensor.class, "color_Sensor");


        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            hue = (int)JavaUtil.rgbToHue(colorSensor.red(), colorSensor.green(), colorSensor.blue());
            currentColor = getColor();
            switch (currentColor)
            {
                case "red":
                {
                    if(cubePos == 0)
                    {
                        cubeFace = 0;
                    }
                    break;
                } // end step 10

                case "blue":
                {

                    break;
                }

                case "green":
                {

                    break;
                }

                case "yellow":
                {

                    break;
                }

                case "white":
                {

                    break;
                }

                case "orange":
                {
                    break;
                }

                default :
                {

                    break;
                }
            } // end switch
            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Current Color ", currentColor);
            telemetry.addData("color", hue);
            telemetry.update();
            //sleep(1000);
            //cubePos++;
        }
    }

    public String getColor() {
        if (hue >= 16 && hue <= 21) {
            return "red";
        }
        else if (hue >= 218 && hue <= 223) {
            return "blue";
        }
        else if (hue >= 135 && hue <= 140) {
            return "green";
        }
        else if (hue >= 102 && hue <= 108) {
            return "yellow";
        }
        else if (hue >= 178 && hue <= 183) {
            return "white";
        }
        else if (hue >= 36 && hue <= 41) {
            return "orange";
        }
        else {
            return "no color";
        }
    }

    private void setCubeFace(int face) {
        if(cubePos == 0)
        {
            cubeFace = face;
        }
    }

    /*
    private void motorPowers(float motor1,float motor2, float motor3, float motor4){
        oneDrive.setPower();
    }

     */
}
