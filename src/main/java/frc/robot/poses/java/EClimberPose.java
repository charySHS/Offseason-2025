package frc.robot.poses.java;

public enum EClimberPose
{
    // Starting configuration just stowed out of the way
    Stowed(0),
    // Out for alignment before grabbing
    Align(0.17),
    // Rotated in for a completed climb
    Climb(-0.22),
    ForwardLimit(0.25),
    ReverseLimit(-0.25);

    public final double Rotations;

    EClimberPose(double rotations) { Rotations = rotations; }
}
