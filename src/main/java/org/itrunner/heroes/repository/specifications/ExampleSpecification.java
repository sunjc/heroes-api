package org.itrunner.heroes.repository.specifications;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.Assert;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder.getPredicate;

public class ExampleSpecification<T> implements Specification<T> {
    private static final long serialVersionUID = 1L;

    private final Example<T> example; //NOSONAR

    public ExampleSpecification(Example<T> example) {
        Assert.notNull(example, "Example must not be null!");
        this.example = example;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return getPredicate(root, criteriaBuilder, example);
    }
}