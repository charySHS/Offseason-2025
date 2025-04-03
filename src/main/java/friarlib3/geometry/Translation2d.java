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
     * @return This translation rotated by rotation.
     */
    public Translation2d rotateBy(final Rotation2d rotation)
    {
        return new  Translation2d(x * rotation.cos() - y * rotation.sin(), x * rotation.sin() + y * rotation.cos());
    }

    public Rotation2d direction() { return new Rotation2d(x, y, true); }

    /**
     * The inverse simply means a Translation2d that "undoes" this object.
     *
     * @return Translation by -x and -y.
     */
    public Translation2d inverse() { return new Translation2d(-x, -y); }

    @Override
    public Translation2d interpolate(final Translation2d other, double x) {
        if (x <= 0) { return new Translation2d(this); }
        else if (x >= 1) { return new Translation2d(other); }

        return extrapolate(other, x);
    }

    public Translation2d extrapolate(final Translation2d other, double x)
    {
        return new Translation2d(x * (other.x - this.x) + this.x, x * (other.y - this.y) + this.y);
    }

    public Translation2d scale(double s) { return new Translation2d(this.x * s, this.y * s); }

    public Translation2d mirrorAboutX(double xValue) { return new Translation2d(xValue + (xValue - this.x), this.y); }

    public Translation2d mirrorAboutY(double yValue) { return new Translation2d(this.x, yValue + (yValue - this.y)); }

    public boolean epsilonEquals(final Translation2d other, double epsilon)
    {
        return Util.epsilonEquals(x(), other.x(), epsilon) && Util.epsilonEquals(y(), other.y(), epsilon);
    }

    @Override
    public String toString() {
        final DecimalFormat format = new DecimalFormat("#0.000");

        return "(" + format.format(this.x) + ", " + format.format(this.y) + ")";
    }

    @Override
    public String toCSV()
    {
        final DecimalFormat format = new DecimalFormat("#0.000");

        return format.format(this.x) + ", " + format.format(this.y);
    }

    public static double dot(final Translation2d a, final Translation2d b) { return a.x * b.x + a.y * b.y; }

    public static Rotation2d getAngle(final Translation2d a, final Translation2d b)
    {
        double cosAngle = dot(a, b) / (a.norm() * b.norm());

        return (Double.isNaN(cosAngle)) ?
                new Rotation2d() :
                Rotation2d.fromDegrees(Math.acos(Util.limit(cosAngle, 1.0)));
    }

    public static double cross(final Translation2d a, final Translation2d b) { return a.x * b.y - a.y * b.x; }

    @Override
    public double distance(final Translation2d other) { return inverse().translateBy(other).norm(); }

    @Override
    public Translation2d add(Translation2d other) { return this.translateBy(other); }

    @Override
    public boolean equals(final Object other)
    {
        return (!(other instanceof Translation2d)) ?
                false :
                distance((Translation2d) other) < Util.kEpsilon;
    }

    @Override
    public Translation2d getTranslation() { return this; }
}
