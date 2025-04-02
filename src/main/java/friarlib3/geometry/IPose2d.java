package friarlib3.geometry;

public interface IPose2d<S> extends IRotation2d<S>, ITranslation2d<S>
{
   Pose2d getPose();

    S transformBy(Pose2d transform);

    S mirror();

    /**
     * Mirrors pose about the vertical line defined by x = xValue.
     */
    public S mirrorAboutX(double xValue);

    /**
     * Mirrors pose about the horizontal line defined by y = yValue.
     */
    public S mirrorAboutY(double yValue);
}
