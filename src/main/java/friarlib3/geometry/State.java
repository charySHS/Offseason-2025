package friarlib3.geometry;

import friarlib3.util.CSVWritable;
import friarlib3.util.Interpolable;

public interface State<S> extends Interpolable<S>, CSVWritable
{
    double distance(final S other);

    S add(S other);

    boolean equals(final Object other);

    String toString();

    String toCSV();
}
