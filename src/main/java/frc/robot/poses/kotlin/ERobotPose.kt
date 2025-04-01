package frc.robot.poses.kotlin

/**
 * ERobotPose enum represents the current robot state and takes both Pivot and Elevator poses
 *
 * @property elevatorPose Pose for elevator.
 * @property pivotPose Pose for pivot.
 */

enum class ERobotPose (@JvmField var elevatorPose: EElevatorPose, @JvmField var pivotPose: EPivotPose)
{
    /** Represents the Stowed position of the robot */
    Stowed(EElevatorPose.Stowed, EPivotPose.Stowed),

    /** Represents the Intake Ready position of the robot */
    IntakeReady(EElevatorPose.IntakeReady, EPivotPose.Intake),

    /** Represents the Intaking position of the robot */
    Intake(EElevatorPose.Intake, EPivotPose.Intake),

    /** Represents the L4 position of the robot */
    L4(EElevatorPose.L4, EPivotPose.L4),

    /** Represents the L3 position of the robot */
    L3(EElevatorPose.L3, EPivotPose.L3),

    /** Represents the L2 position of the robot */
    L2(EElevatorPose.L2, EPivotPose.L2),

    /** Represents the L1 position of the robot */
    L1(EElevatorPose.L1, EPivotPose.L1),

    /** Represents the Lower Algae position of the robot */
    AlgaeLow(EElevatorPose.AlgaeLow, EPivotPose.AlgaeLow),

    /** Represents the Higher Algae position of the robot */
    AlgaeHigh(EElevatorPose.AlgaeHigh, EPivotPose.AlgaeHigh),

    /** Represents the Ready to Climb position of the robot */
    ClimbReady(EElevatorPose.ClimbReady, EPivotPose.ClimbReady),

    /** Represents the Climbing position of the robot */
    Climbing(EElevatorPose.Climbing, EPivotPose.ClimbReady);
}