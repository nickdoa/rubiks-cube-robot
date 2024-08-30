package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="ServoTest", group="Linear Opmode")
public class ServoTest extends LinearOpMode {
    private Servo servo;

    @Override
    public void runOpMode() throws InterruptedException {
        servo = hardwareMap.get(Servo.class, "one_Servo");
        servo.setPosition(1);

        waitForStart();
        while(opModeIsActive()){
            if(servo.getPosition() == 1){
                servo.setPosition(-1);
            }
            else{
                servo.setPosition(1);
            }
            sleep(500);
            telemetry.addData("Servo Pos", servo.getPosition());
        }
    }
}
