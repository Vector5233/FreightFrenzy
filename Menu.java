package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.ArrayList;

public class Menu {

    LinearOpMode parent;
    ArrayList<MenuItem> items = new ArrayList<>();
    int menuActiveItem = 0;

    public Menu(LinearOpMode p) {
        parent = p;
    }

    public void add(MenuItem m) {
        items.add(m);
    }

    public void display() {
        int i;
        for (i=0; i<items.size(); i++) {
            if (i == menuActiveItem) {
                parent.telemetry.addLine(">"+items.get(i).getRepr());
            }
            else {
                parent.telemetry.addLine(items.get(i).getRepr());
            }
        }
        parent.telemetry.update();
    }

    public void update() {
        double joystickThreshold = 0.10;
        int delay = 150;
        if ((parent.gamepad1.right_stick_y > joystickThreshold)||parent.gamepad1.a) {
            items.get(menuActiveItem).down();
            parent.sleep(delay);
        }
        else if((parent.gamepad1.right_stick_y < -joystickThreshold)||parent.gamepad1.y) {
            items.get(menuActiveItem).up();
            parent.sleep(delay);
        }
        else if(parent.gamepad1.b) {
            menuActiveItem++;
            if (menuActiveItem >= items.size()) {
                menuActiveItem = 0;
            }
            parent.sleep(delay);
        }
        else if(parent.gamepad1.x) {
            menuActiveItem--;
            if (menuActiveItem < 0) {
                menuActiveItem = items.size() - 1;
            }
            parent.sleep(delay);
        }
    }

    public double get(int index) {
        return items.get(index).getValue();
    }
}
