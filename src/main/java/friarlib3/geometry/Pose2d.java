package friarlib3.geometry;

import friarlib3.util.Util;

/**
 * Represents a 2d pose (rigid transform) containing translational and rotational elements.
 * <p>
 *     Inspired by 254
 * </p>
 */
public class Pose2d implements IPose2d<Pose2d>
{
    protected static final Pose2d kIdentity = new Pose2d();

    public static Pose2d identity() { return kIdentity; }

    private static final double kEps = 1e-9;

    protected final Translation2d translation;
    protected final Rotation2d rotation;

    public Pose2d()
    {
        translation = new Translation2d();
        rotation = new Rotation2d();
    }

    public Pose2d(double x, double y, final Rotation2d rotation)
    {
        translation = new Translation2d(x, y);
        this.rotation = rotation;
    }

    public Pose2d(final Translation2d translation, final Rotation2d rotation)
    {
        this.translation = translation;
        this.rotation = rotation;
    }

    public Pose2d(final Pose2d other)
    {
        translation = new Translation2d(other.translation);
        rotation = new Rotation2d(other.rotation);
    }

    public Pose2d(final Pose2dWithMotion other)
    {
        translation = new Translation2d(other.getTranslation());
        rotation = new Rotation2d(other.getRotation());
    }

    public Pose2d(final edu.wpi.first.math.geometry.Pose2d other)
    {
        translation = new Translation2d(other.getTranslation());
        rotation = new Rotation2d(other.getRotation());
    }

    public static Pose2d fromTranslation(final Translation2d translation) { return new Pose2d(translation, new Rotation2d()); }

    public static Pose2d fromRotation(final Rotation2d rotation) { return new Pose2d(new Translation2d(), rotation); }

    /**
     * Obtain a new Pose2d from a (constant curvature) velocity.
     */
    public static Pose2d exp(final Twist2d delta)
    {
        double sinTheta = Math.sin(delta.dtheta);
        double cosTheta = Math.cos(delta.dtheta);

        double s, c;

        if (Math.abs(delta.dtheta) < kEps)
        {
            s = 1.0 - 1.0 / 6.0 * delta.dtheta * delta.dtheta;
            c = 0.5 * delta.dtheta;
        } else
        {
            s = sinTheta / delta.dtheta;
            c = (1.0 - cosTheta) / delta.dtheta;
        }

        return new Pose2d(new Translation2d(delta.dx * s - delta.dy * c, delta.dx * c + delta.dy * s),
                new Rotation2d(cosTheta, sinTheta, false));
    }

    /**
     * Logical inverse of the above.
     */
    public static Twist2d log(final Pose2d transform)
    {
        final double dtheta = transform.getRotation().getRadians();
        final double halfDtheta = 0.5 * dtheta;
        final double cosMinusOne = transform.getRotation().cos() - 1.0;

        double halfThetaByTanOfHalfDtheta;

        if (Math.abs(cosMinusOne) < kEps)
        {
            halfThetaByTanOfHalfDtheta = 1.0 - 1.0 / 12.0 * dtheta * dtheta;
        } else
        {
            halfThetaByTanOfHalfDtheta = -(halfDtheta * transform.getRotation().sin()) / cosMinusOne;
        }

        final Translation2d translationPart = transform.getTranslation()
                .rotateBy(new Rotation2d(halfThetaByTanOfHalfDtheta, -halfDtheta, false));

        return new Twist2d(translationPart.x(), translationPart.y(), dtheta);
    }

    @Override
    public Translation2d getTranslation() { return translation; }

    @Override
    public Rotation2d getRotation() { return rotation; }

    @Override
    public Pose2d rotateBy(Rotation2d other) { return this.transformBy(new Pose2d(Translation2d.identity(), other)); }

    @Override
    public Pose2d add(Pose2d other) { return this.transformBy(other); }

    /**
     * Transforming this RigidTransform2d means first translating by other.translation
     * and rotating by other.rotation.
     *
     * @param other The other transform.
     * @return This transform * other
     */
    @Override
    public Pose2d TransformBy(final Pose2d other)
    {
        return new Pose2d(translation.translateBy(other.translation.rotateBy(rotation)),
                rotation.rotateBy(other.rotation));
    }

    public Pose2d transformBy(Transform2d other)
    {
        return new Pose2d(
                translation.plus(other.getTranslation().rotateBy(rotation)),
                rotation.rotateBy(other.getRotation()));
    }

    public Transform2d minus(Pose2d other)
    {
        final var pose = this.relativeTo(other);

        return new Transform2d(pose.getTranslation(), pose.getRotation());
    }

    public Pose2d relativeTo(Pose2d other)
    {
        var transform = new Transform2d(other, this);

        return new Pose2d(transform.getTranslation(), transform.getRotation());
    }

    /**
     * Inverse of this transform "undoes" effect of translating by transform.
     *
     * @return The opposite of this transform.
     */
    public Pose2d inverse()
    {
        Rotation2d rotationInverted = rotation.inverse();

        return new Pose2d(translation.inverse().rotateBy(rotationInverted), rotationInverted);
    }

    public Pose2d normal() { return new Pose2d(translation, rotation.normal()); }

    /**
     * Finds the point where the heading of this pose intersects
     * the heading of another.
     *
     * @return (+INF, +INF) if parallel.
     */
    public Translation2d intersection(final Pose2d other)
    {
        final Rotation2d otherRotation = other.getRotation();

        if (rotation.isParallel(otherRotation))
        {
            // Lines are parallel
            return new Translation2d(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        }

        if (Math.abs(rotation.cos()) < Math.abs(otherRotation.cos()))
        {
            return intersectionInternal(this, other);
        } else
        {
            return intersectionInternal(other, this);
        }
    }

    /**
     * Returns true if pose is (nearly) collinear with another.
     */
    public boolean isCollinear(final Pose2d other)
    {
        if (!getRotation().isParallel(other.getRotation()))
            return false;

        final Twist2d twist = log(inverse().transformBy(other));

        return (Util.epsilonEquals(twist.dy, 0.0) && Util.epsilonEquals(twist.dtheta, 0.0));
    }

    public boolean epsilonEquals(final Pose2d other, double epsilon)
    {
        return getTranslation().epsilonEquals(other.getTranslation(), epsilon)
                && getRotation().isParallel(other.getRotation());
    }

    private static Translation2d intersectionInternal(final Pose2d a, final Pose2d b)
    {
        final Rotation2d aR = a.getRotation();
        final Rotation2d bR = b.getRotation();
        final Translation2d aT = a.getTranslation();
        final Translation2d bT = b.getTranslation();

        final double tanB = bR.tan();
        final double t = ((aT.x() - bT.x()) * tanB + bT.y() - aT.y())
                / (aR.sin() - aR.cos() * tanB);

        if (Double.isNan(t)) { return new Translation2d(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY); }

        return aT.translateBy(aR.toTranslation().scale(t));
    }

    /**
     * Do twist interpolation of pose assuming constant curvature.
     */
    @Override
    public Pose2d interpolate(final Pose2d other, double x)
    {
        if (x <= 0) { return new Pose2d(this); }
        else if (x >= 1) { return new Pose2d(other); }

        final Twist2d twist = Pose2d.log(inverse().transformBy(other));

        return transformBy(Pose2d.exp(twist.scaled(x)));
    }

    @Override
    public String toString()
    {
        return "Translation: " + translation.toString() + ", Rotation: " + rotation.toString();
    }

    @Override
    public String toCSV()
    {
        return translation.toCSV() + ", " + rotation.toCSV();
    }

    @Override
    public double distance(final Pose2d other)
    {
        return Pose2d.log(inverse().transformBy(other)).norm();
    }

    @Override
    public boolean equals(final Object other){
        if (!(other instanceof Pose2d)) { return false; }

        return epsilonEquals((Pose2d)other, Util.kEpsilon);
    }

    @Override
    public Pose2d getPose() { return this; }

    @Override
    public Pose2d mirror()
    {
        return new Pose2d(new Translation2d(getTranslation().x(), -getTranslation().y()), getRotation().inverse());
    }

    @Override
    public Pose2d mirrorAboutX(double xValue)
    {
        return new Pose2d(getTranslation().mirrorAboutX(xValue), getRotation().mirrorAboutX());
    }

    @Override
    public Pose2d mirrorAboutY(double yValue)
    {
        return new Pose2d(getTranslation().mirrorAboutY(yValue), getRotation().mirrorAboutY());
    }
}
