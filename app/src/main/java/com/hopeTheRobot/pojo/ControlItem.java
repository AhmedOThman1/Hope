package com.hopeTheRobot.pojo;

public class ControlItem {

    private boolean swabControl;
    private boolean movementControl;
    private boolean maskControl;
    private boolean thermalControl;

    public ControlItem() {
    }

    public boolean getSwabControl() {
        return swabControl;
    }

    public void setSwabControl(boolean swabControl) {
        this.swabControl = swabControl;
    }

    public boolean getMovementControl() {
        return movementControl;
    }

    public void setMovementControl(boolean movementControl) {
        this.movementControl = movementControl;
    }

    public boolean getMaskControl() {
        return maskControl;
    }

    public void setMaskControl(boolean maskControl) {
        this.maskControl = maskControl;
    }

    public boolean getThermalControl() {
        return thermalControl;
    }

    public void setThermalControl(boolean thermalControl) {
        this.thermalControl = thermalControl;
    }
}
