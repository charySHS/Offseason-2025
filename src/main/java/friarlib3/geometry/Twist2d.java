package friarlib3.geometry;

import java.text.DecimalFormat;
import java.util.Optional;

import friarlib3.util.Interpolable;
import friarlib3.util.Util;

/**
 * A movement along an arc at a constant curvature and velocity.
 * We can use ideas from differential calculus to create new
 * RigidTransform2d's from a Twist2d and vice versa.
 * <p>
 *     A Twist can be used to represent a difference between two poses, a velocity, an acceleration, etc.
 * </p>
 */
public class Twist2d implements Interpolable<Twist2d>, ICourse2d<Twist2d>
{
    protected static final Twist2d kIdentity = new Twist2d(0.0, 0.0, 0.0);

    public static Twist2d identity() { return kIdentity; }

    public final double dx;
    public final double dy;
    public final double dtheta; // Radians!

    public Twist2d(double dx, double dy, double dtheta)
    {
        this.dx = dx;
        this.dy = dy;
        this.dtheta = dtheta;
    }

    public Twist2d scaled(double scale) { return new Twist2d(dx * scale, dy * scale, dtheta * scale); }

    public Twist2d mirror() { return new Twist2d(dx, -dy, -dtheta); }

    public Twist2d mirrorAboutX() { return new Twist2d(-dx, dy, -dtheta); }

    public Twist2d mirrorAboutY() { return new Twist2d(dx, -dy, -dtheta); }

    public double norm()
    {
        // Common case of dy == 0
        return (dy == 0.0) ?
                Math.abs(dx) :
                Math.hypot(dx, dy);
    }

    public double norm2() { return dx * dx + dy * dy; }

    public boolean hasTranslation() { return norm2() > Util.kEpsilon; }

    /**
     * @return Vector in the local direction of motion
     */
    @Override
    public Optional<Rotation2d> getCourse()
    {
        return (hasTranslation()) ?
                Optional.of(new Rotation2d(dx, dy, true)) :
                Optional.empty();
    }

    public boolean epsilonEquals(final Twist2d other, double epsilon)
    {
        return Util.epsilonEquals(dx, other.dx, epsilon) &&
                Util.epsilonEquals(dy, other.dy, epsilon) &&
                Util.epsilonEquals(dtheta, other.dtheta, epsilon);
    }

    @Override
    public String toString()
    {
        final DecimalFormat format = new DecimalFormat("0.0000");

        return "(" + format.format(dx) + ", " + format.format(dy) + ", " + format.format(Math.toDegrees(dtheta)) + ")";
    }

    @Override
    public Twist2d interpolate ( Twist2d other, double x)
    {
        return new Twist2d(Util.interpolate(dx, other.dx, x),
                Util.interpolate(dy, other.dy, x),
                Util.interpolate(dtheta, other.dtheta, x));
    }
}
