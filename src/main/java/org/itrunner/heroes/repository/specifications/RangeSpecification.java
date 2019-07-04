package org.itrunner.heroes.repository.specifications;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Optional;

public class RangeSpecification<T, E extends Comparable<E>> implements Specification<T> {
    private FieldRange<E> fieldRange; //NOSONAR

    public RangeSpecification(FieldRange<E> fieldRange) {
        this.fieldRange = fieldRange;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Optional<E> lower = fieldRange.getRange().getLowerBound().getValue();
        Optional<E> upper = fieldRange.getRange().getUpperBound().getValue();
        Path<E> path = root.get(fieldRange.getField());

        if (lower.isPresent() && upper.isPresent()) {
            return builder.between(path, lower.get(), upper.get());
        }

        if (lower.isPresent()) {
            return builder.greaterThanOrEqualTo(path, lower.get());
        }

        if (upper.isPresent()) {
            return builder.lessThanOrEqualTo(path, upper.get());
        }

        return null;
    }
}