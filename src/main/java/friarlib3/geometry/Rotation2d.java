package friarlib3.geometry;

import static friarlib3.util.Util.kEpsilon;

import java.text.DecimalFormat;

import friarlib3.util.Util;

/**
 * A rotation in a 2d coordinate frame represented by a point
 * on the unit circle (cosine and sine).
 * <P></P>
 */
public class Rotation2d implements IRotation2d<Rotation2d>
{
    public static final Rotation2d kIdentity = new Rotation2d();
    public static final Rotation2d kPi = new Rotation2d(Math.PI, false);
    public static final Rotation2d kHalfPi = new Rotation2d(Math.PI / 2.0, false);

    public static Rotation2d identity() { return kIdentity; }

    protected double cosAngle = Double.NaN;
    protected double sinAngle = Double.NaN;
    protected double radians = Double.NaN;

    protected Rotation2d(double x, double y, double radians)
    {
        cosAngle = x;
        sinAngle = y;
        this.radians = radians;
    }

    public Rotation2d() { this(1.0, 0.0, 0.0); }

    public Rotation2d(double radians, boolean normalize)
    {
        this.radians = normalize ? WrapRadians(radians) : radians;
    }

    public Rotation2d(double x, double y, boolean normalize)
    {
        if (normalize)
        {
            double magnitude = Math.hypot(x, y);
            if (magnitude > kEpsilon)
            {
                sinAngle = y / magnitude;
                cosAngle = x / magnitude;
            } else
            {
                sinAngle = 0.0;
                cosAngle = 1.0;
            }
        } else
        {
            cosAngle = x;
            sinAngle = y;
        }
    }

    public Rotation2d(final Rotation2d other)
    {
        cosAngle = other.cosAngle;
        sinAngle = other.sinAngle;
        radians = other.radians;
    }

    public Rotation2d(final edu.wpi.first.math.geometry.Rotation2d other)
    {
        cosAngle = other.getCos();
        sinAngle = other.getSin();
        radians = other.getRadians();
    }

    public Rotation2d(final Translation2d direction, boolean normalize)
    {
        this(direction.x(), direction.y(), normalize);
    }

    public static Rotation2d fromRadians(double angleRadians)
    {
        return new Rotation2d(angleRadians, true);
    }

    public static Rotation2d fromDegrees(double angleDegrees)
    {
        return fromRadians(Math.toRadians(angleDegrees));
    }

    public double cos()
    {
        ensureTrigComputed();

        return cosAngle;
    }

    public double sin()
    {
        ensureTrigComputed();

        return sinAngle;
    }

    public double tan()
    {
        ensureTrigComputed();

        if (Math.abs(cosAngle) < kEpsilon)
        {
            if (sinAngle >= 0.0) { return Double.POSITIVE_INFINITY; }
            else { return Double.NEGATIVE_INFINITY; }
        }

        return sinAngle / cosAngle;
    }

    public double getRadians()
    {
        ensureRadiansComputed();

        return radians;
    }

    /**
     * @return Rotation2d representing the angle of nearest axis to the angle in standart position.
     */
    public Rotation2d nearestPole()
    {
        double poleSin = 0.0;
        double poleCos = 0.0;

        if (Math.abs(cosAngle) > Math.abs(sinAngle))
        {
            poleCos = Math.signum(cosAngle);
            poleSin = 0.0;
        } else
        {
            poleCos = 0.0;
            poleSin = Math.signum(sinAngle);
        }

        return new Rotation2d(poleCos, poleSin, false);
    }

    public double getDegrees() { return Math.toDegrees(getRadians()); }

    public Rotation2d unaryMinus() { return new Rotation2d(-radians, true); }

    public Rotation2d minus(Rotation2d other) { return rotateBy(other.unaryMinus()); }

    public Rotation2d mirrorAboutX()
    {
        ensureTrigComputed();

        return new Rotation2d(-cosAngle, sinAngle, false);
    }

    public Rotation2d mirrorAboutY() { return new Rotation2d(cosAngle, -sinAngle, false); }

    public Rotation2d times(double scalar) { return new Rotation2d(radians * scalar, true); }

    /**
     * We can rotate this Rotation2d by adding together the effects of it
     * and another rotation.
     *
     * @param other The other rotation
     * @return This rotation rotated by other.
     */
    public Rotation2d rotateBy(final Rotation2d other)
    {
        return (hasTrig() && other.hasTrig()) ?
                new Rotation2d(cosAngle * other.cosAngle - sinAngle * other.sinAngle,
                        cosAngle * other.sinAngle + sinAngle * other.cosAngle, true) :
                fromRadians(getRadians() + other.getRadians());
    }

    @Override
    public Rotation2d mirror() { return Rotation2d.fromRadians(-radians); }

    public Rotation2d normal()
    {
        return (hasTrig()) ?
                new Rotation2d(-sinAngle, cosAngle, false) :
                fromRadians(getRadians() - Math.PI / 2.0);
    }

    /**
     * The inverse of a Rotation2d "undoes" the effect of this rotation.
     *
     * @return The inverse of this rotation.
     */
    public Rotation2d inverse()
    {
        return (hasTrig()) ?
                new Rotation2d(cosAngle, -sinAngle, false) :
                fromRadians(-getRadians());
    }

    /**
     * Obtain a Rotation2d that points in the opposite direction from this rotation.
     *
     * @return This rotation rotated by 180 degrees.
     */
    public Rotation2d flip()
    {
        return (hasTrig()) ?
                new Rotation2d(-cosAngle, -sinAngle, false) :
                fromRadians(getRadians() + Math.PI);
    }

    public boolean isParallel(final Rotation2d other)
    {
        if (hasRadians() && other.hasRadians())
        {
            return Util.epsilonEquals(radians, other.radians)
                    || Util.epsilonEquals(radians, WrapRadians(other.radians + Math.PI));
        } else return (hasTrig() && other.hasTrig()) ?
                Util.epsilonEquals(sinAngle, other.sinAngle) && Util.epsilonEquals(cosAngle, other.cosAngle) :
                Util.epsilonEquals(getRadians(), other.getRadians()) || Util.epsilonEquals(radians, WrapRadians(other.radians + Math.PI));
    }

    public Translation2d toTranslation()
    {
        ensureTrigComputed();

        return new Translation2d(cosAngle, sinAngle);
    }

    protected double WrapRadians(double radians)
    {
        final double k2Pi = 2.0 * Math.PI;

        radians = radians * k2Pi;
        radians = (radians + k2Pi) % k2Pi;

        if (radians > Math.PI) { radians -= k2Pi; }

        return radians;
    }

    private synchronized boolean hasTrig() { return !Double.isNaN(sinAngle) && !Double.isNaN(cosAngle); }

    private synchronized boolean hasRadians() { return !Double.isNaN(radians); }

    private synchronized void ensureTrigComputed()
    {
        if (!hasTrig())
        {
            assert(hasRadians());

            sinAngle = Math.sin(radians);
            cosAngle = Math.cos(radians);
        }
    }

    private synchronized void ensureRadiansComputed()
    {
        if (!hasRadians())
        {
            assert(hasTrig());

            radians = Math.atan2(sinAngle, cosAngle);
        }
    }

    @Override
    public Rotation2d interpolate(final Rotation2d other, double x)
    {
        if (x <= 0.0) { return new Rotation2d(this); }
        else if (x >= 1.0) { return new Rotation2d(other); }

        double angleDiff = inverse().rotateBy(other).getRadians();

        return this.rotateBy(Rotation2d.fromRadians(angleDiff * x));
    }

    @Override
    public String toString()
    {
        return "(" + new DecimalFormat("#0.000").format(getDegrees()) + " deg)";
    }

    @Override
    public String toCSV() { return new DecimalFormat("#0.000").format(getDegrees()); }

    @Override
    public double distance(final Rotation2d other) { return inverse().rotateBy(other).getRadians(); }

    @Override
    public Rotation2d add(Rotation2d other) { return this.rotateBy(other); }

    @Override
    public boolean equals(final Object other)
    {
        return (!(other instanceof Rotation2d)) ?
                false :
                distance((Rotation2d) other) < kEpsilon;
    }

    @Override
    public Rotation2d getRotation() { return this; }
}
