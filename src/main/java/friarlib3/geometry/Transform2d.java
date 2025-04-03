package friarlib3.geometry;

import java.util.Objects;

/**
 * Represents a transformation for a Pose2d.
 */
public class Transform2d
{
    private final Translation2d translation;
    private final Rotation2d rotation;

    /**
     * Constructs the transform the maps initial pose to the final pose.
     *
     * @param initial The initial pose for the transformation.
     * @param last The final pose for the transformation.
     */
    public Transform2d(Pose2d initial, Pose2d last)
    {
        /**
         * We are rotating the difference between the translations
         * using a clockwise rotation matrix. This transforms the global
         * delta into a local delta (relative to initial pose).
         */
        translation =
                last.getTranslation()
                        .minus(initial.getTranslation())
                        .rotateBy(initial.getRotation().unaryMinus());

        rotation = last.getRotation().minus(initial.getRotation());
    }

    /**
     * Constructs a transform with the given translation and rotation components.
     *
     * @param translation Translational component of the transform.
     * @param rotation Rotational component of the transform.
     */
    public Transform2d(Translation2d translation, Rotation2d rotation)
    {
        this.translation = translation;
        this.rotation = rotation;
    }

    /**
     * Constructs the identity transform -- maps an initial pose to itself.
     */
    public Transform2d()
    {
        translation = new Translation2d();
        rotation = new Rotation2d();
    }

    /**
     * Scales the transform by a scalar.
     *
     * @param scalar The scalar.
     * @return The scaled Transform 2d.
     */
    public Transform2d times(double scalar)
    {
        return new Transform2d(translation.times(scalar), rotation.times(scalar));
    }

    /**
     * Composes two transformations.
     *
     * @param other The transform to compose with this one.
     * @return The composition of the two transforms.
     */
    public Transform2d plus(Transform2d other)
    {
        return new Transform2d(new Pose2d(), new Pose2d().transformBy(this).transformBy(other));
    }
}
