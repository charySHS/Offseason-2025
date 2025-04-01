package frc.robot.poses.java;

import frc.robot.poses.kotlin.EPivotPose;

public enum ERobotPose
{
    Stowed(EElevatorPose.Stowed, EPivotPose.Stowed),
    IntakeReady(EElevatorPose.IntakeReady, EPivotPose.Intake),
    IntakeGrab(EElevatorPose.IntakeGrab, EPivotPose.Intake),

    L1(EElevatorPose.L1, EPivotPose.L1),
    L2(EElevatorPose.L2, EPivotPose.L2),
    L3(EElevatorPose.L3, EPivotPose.L3),
    L4(EElevatorPose.L4, EPivotPose.L4),

    AlgaeLow(EElevatorPose.AlgaeLow, EPivotPose.AlgaeLow),
    AlgaeHigh(EElevatorPose.AlgaeHigh, EPivotPose.AlgaeHigh),
    ClimbReady(EElevatorPose.ClimbReady, EPivotPose.ClimbReady);

    public final EElevatorPose eElevatorPose;
    public final EPivotPose ePivotPose;

    ERobotPose(EElevatorPose elevatorPose, EPivotPose pivotPose)
    {
        eElevatorPose = elevatorPose;
        ePivotPose = pivotPose;
    }
}
