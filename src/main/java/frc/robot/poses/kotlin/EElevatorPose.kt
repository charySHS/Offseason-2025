package frc.robot.poses.kotlin

/**
 * EElevatorPose enum represents different levels that the
 * elevator can be within the robot's system.
 *
 * @property rotation The position of the elevator in rotations.
 */

enum class EElevatorPose(@JvmField var rotation: Double) {
    /** Represents Stowed position of the elevator */
    Stowed(0.0),

    /** Represents the Ready to Intake position of the elevator */
    IntakeReady(9.0),

    /** Represents the Intaking position of the elevator */
    Intake(7.25),

    /** Represents the Clear position of the elevator */
    Clear(10.5),

    /** Represents the L4 position of the elevator */
    L4(19.5),

    /** Represents the L3 position of the elevator */
    L3(11.25),

    /** Represents the L2 position of the elevator */
    L2(4.75),

    /** Represents the L1 position of the elevator */
    L1(2.0),

    /** Represents the Higher Algae position of the elevator */
    AlgaeHigh(17.5),

    /** Represents the Lower Algae position of the elevator */
    AlgaeLow(11.25),

    /** Represents the Ready to Climb position of the elevator */
    ClimbReady(8.73),

    /** Represents the Climbing position of the elevator */
    Climbing(12.17);

}
