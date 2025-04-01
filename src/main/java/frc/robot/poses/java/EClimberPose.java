package frc.robot.poses.java;

public enum EClimberPose {
    Stowed(0.25),
    Intake(-0.282),
    Score(-0.05),
    L4(0.15),
    L3(0.088),
    L2(0.078),
    L1(0),
    AlgaeLow(0.15),
    AlgaeHigh(0.15),
    ClimbReady(-0.195);

    public final double Rotations;

    EClimberPose(double rotations) { Rotations = rotations; }
}
