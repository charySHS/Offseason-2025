package friarlib3.geometry;

import java.text.DecimalFormat;

import friarlib3.util.Util;

/**
 * A Translation ina 3d coordinate frame. Translations are simply
 * shifts in (x, y) plane.
 */
public class Translation2d implements ITranslation2d<Translation2d>
{
    protected static final Translation2d kIdentity = new Translation2d();

    public static Translation2d identity() { return kIdentity; }

    protected double x;
    protected double y;

    public Translation2d()
    {
        x = 0;
        y = 0;
    }

    public Translation2d(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public Translation2d(final Translation2d other)
    {
        x = other.x;
        y = other.y;
    }

    public Translation2d(final Translation2d start, final Translation2d end)
    {
        x = end.x - start.x;
        y = end.y - start.y;
    }

    public Translation2d(final edu.wpi.first.math.geometry.Translation2d other)
    {
        x = other.getX();
        y = other.getY();
    }

    public static Translation2d fromPolar(Rotation2d direction, double magnitude)
    {
        return new Translation2d(direction.cos() * magnitude, direction.sin() * magnitude);
    }

    /**
     * The "norm" of a transform, being the Euclidean distance
     * in x and y.
     *
     * @return sqrt(X ^ 2 + y ^ 2)
     */
    public double norm() { return Math.hypot(x, y); }

    public double norm2() { return x * x + y * y; }

    public double x() { return x; }

    public double y() { return y; }

    /**
     * We can compose Translation2d's by adding together the x and y
     * shifts.
     *
     * @param other The other translation to add.
     * @return The combined effect of translating by this object
     * and the other.
     */
    public Translation2d translateBy(final Translation2d other)
    {
        return new Translation2d(x + other.x, y + other.y);
    }

    public Translation2d plus(Translation2d other)
    {
        return new Translation2d(x + other.x, y + other.y);
    }

    public Translation2d minus(Translation2d other)
    {
        return new Translation2d(x - other.x, y - other.y);
    }

    public Translation2d unaryMinus() { return new Translation2d(-x, -y); }

    public Translation2d times(double scalar) { return new Translation2d(x * scalar, y * scalar); }

    /**
     * We can also rotate Translation2d's.
     *
     * @param rotation The rotation to apply.
     *
     */
}
