package com.hopeTheRobot.pojo;

public class MotorsPositionsItem {
    private int baseMotorPosition;
    private int shoulderMotorPosition;
    private int elbowMotorPosition;
    private int wristRollMotorPosition;
    private int wristPitchMotorPosition;
    private int gripperMotorPosition;

    public MotorsPositionsItem() {
    }

    public MotorsPositionsItem(int baseMotorPosition, int shoulderMotorPosition, int elbowMotorPosition, int wristRollMotorPosition, int wristPitchMotorPosition, int gripperMotorPosition) {
        this.baseMotorPosition = baseMotorPosition;
        this.shoulderMotorPosition = shoulderMotorPosition;
        this.elbowMotorPosition = elbowMotorPosition;
        this.wristRollMotorPosition = wristRollMotorPosition;
        this.wristPitchMotorPosition = wristPitchMotorPosition;
        this.gripperMotorPosition = gripperMotorPosition;
    }

    public int getBaseMotorPosition() {
        return baseMotorPosition;
    }

    public void setBaseMotorPosition(int baseMotorPosition) {
        this.baseMotorPosition = baseMotorPosition;
    }

    public int getShoulderMotorPosition() {
        return shoulderMotorPosition;
    }

    public void setShoulderMotorPosition(int shoulderMotorPosition) {
        this.shoulderMotorPosition = shoulderMotorPosition;
    }

    public int getElbowMotorPosition() {
        return elbowMotorPosition;
    }

    public void setElbowMotorPosition(int elbowMotorPosition) {
        this.elbowMotorPosition = elbowMotorPosition;
    }

    public int getWristRollMotorPosition() {
        return wristRollMotorPosition;
    }

    public void setWristRollMotorPosition(int wristRollMotorPosition) {
        this.wristRollMotorPosition = wristRollMotorPosition;
    }

    public int getWristPitchMotorPosition() {
        return wristPitchMotorPosition;
    }

    public void setWristPitchMotorPosition(int wristPitchMotorPosition) {
        this.wristPitchMotorPosition = wristPitchMotorPosition;
    }

    public int getGripperMotorPosition() {
        return gripperMotorPosition;
    }

    public void setGripperMotorPosition(int gripperMotorPosition) {
        this.gripperMotorPosition = gripperMotorPosition;
    }
}
