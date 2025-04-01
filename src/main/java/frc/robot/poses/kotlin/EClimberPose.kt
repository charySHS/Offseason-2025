package frc.robot.poses.kotlin

/**
 * EClimberPose enum represents different positions that the
 * climber can be within the robot's system.
 *
 * @property rotation The position of the climber in rotations.
 */

enum class EClimberPose (@JvmField var rotations: Double)
{
    /** Represents the Stowed position of the climber */
    Stowed(0.0),

    /** Represents the Alignment position of the climber */
    Aligning(0.17),

    /** Represents the Climbing position of the climber */
    Climbing(-0.205),

    /** Software limits for rotation */
    ForwardLimit(0.25),

    ReverseLimit(-0.25);
}