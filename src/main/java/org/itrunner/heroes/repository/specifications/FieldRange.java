package org.itrunner.heroes.repository.specifications;

import org.springframework.data.domain.Range;
import org.springframework.data.domain.Range.Bound;

import static org.springframework.data.domain.Range.Bound.inclusive;

public class FieldRange<T extends Comparable<T>> {
    private String field;
    private Range<T> range;

    public FieldRange(String field, T lower, T upper) {
        this.field = field;
        this.range = of(lower, upper);
    }

    private Range<T> of(T lower, T upper) {
        Bound<T> lowerBound = Bound.unbounded();
        Bound<T> upperBound = Bound.unbounded();

        if (lower != null) {
            lowerBound = inclusive(lower);
        }

        if (upper != null) {
            upperBound = inclusive(upper);
        }

        return Range.of(lowerBound, upperBound);
    }

    public String getField() {
        return field;
    }

    public Range<T> getRange() {
        return range;
    }
}