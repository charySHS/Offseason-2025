package frc.robot.poses.kotlin

/**
 * EPivotPose enum represents different positions that the
 * pivot can be within the robot's system.
 *
 * @property rotation The position of the pivot in rotations.
 */

enum class EPivotPose (@JvmField var rotations: Double)
{
    /** Represents the Stowed position of the pivot */
    Stowed(0.25),

    /** Represents the Intaking position of the pivot */
    Intake(-0.282),

    /** Represents the Scoring position of the pivot */
    Score(-0.05),

    /** Represents the L4 position of the pivot */
    L4(0.15),

    /** Represents the L3 position of the pivot */
    L3(0.088),

    /** Represents the L2 position of the pivot */
    L2(0.078),

    /** Represents the L1 position of the pivot */
    L1(0.0),

    /** Represents the Lower Algae position of the pivot */
    AlgaeLow(0.15),

    /** Represents the Higher Algae position of the pivot */
    AlgaeHigh(0.15),

    /** Represents the Ready to Climb position of the pivot */
    ClimbReady(-0.195);
}