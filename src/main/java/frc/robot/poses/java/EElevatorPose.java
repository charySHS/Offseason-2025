package frc.robot.poses.java;

public enum EElevatorPose {
    Stowed(0),
    IntakeReady(9),
    IntakeGrab(7.25),
    ClearArm(10.5),
    L4(19.5),
    L3(11.25),
    L2(4.75),
    L1(2),
    AlgaeLow(11.25),
    AlgaeHigh(17.5),
    ClimbReady(8.73),
    Climbing(12.17);

    public final double Rotations;

    EElevatorPose(double rotations) { Rotations = rotations; }
}
