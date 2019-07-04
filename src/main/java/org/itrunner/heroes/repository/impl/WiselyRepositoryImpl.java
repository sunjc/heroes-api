package org.itrunner.heroes.repository.impl;

import org.itrunner.heroes.repository.WiselyRepository;
import org.itrunner.heroes.repository.specifications.ExampleSpecification;
import org.itrunner.heroes.repository.specifications.FieldRange;
import org.itrunner.heroes.repository.specifications.RangeSpecification;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;

public class WiselyRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements WiselyRepository<T, ID> { //NOSONAR

    public WiselyRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    @Override
    public Page<T> findByExampleAndRange(Example<T> example, List<FieldRange<? extends Comparable>> fieldRanges, Pageable pageable) {
        return findAll(specifications(example, fieldRanges), pageable);
    }

    @Override
    public List<T> findByExampleAndRange(Example<T> example, List<FieldRange<? extends Comparable>> fieldRanges) {
        return findAll(specifications(example, fieldRanges));
    }

    private Specification<T> specifications(Example<T> example, List<FieldRange<? extends Comparable>> fieldRanges) {
        boolean allMatching = example.getMatcher().isAllMatching();
        Specification<T> byExample = new ExampleSpecification<>(example);
        List<Specification<T>> byRanges = getRangeSpecifications(fieldRanges);
        return conjunction(byExample, byRanges, allMatching);
    }

    private List<Specification<T>> getRangeSpecifications(List<FieldRange<? extends Comparable>> fieldRanges) {
        List<Specification<T>> rangeSpecifications = new ArrayList<>();
        for (FieldRange fieldRange : fieldRanges) {
            rangeSpecifications.add(new RangeSpecification<>(fieldRange));
        }
        return rangeSpecifications;
    }

    private Specification<T> conjunction(Specification<T> byExample, List<Specification<T>> byRanges, boolean allMatching) {
        Specification<T> specification = where(byExample);
        for (Specification<T> rangeSpecification : byRanges) {
            if (allMatching) {
                specification = specification.and(rangeSpecification);
            } else {
                specification = specification.or(rangeSpecification);
            }
        }
        return specification;
    }
}