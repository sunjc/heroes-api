package org.itrunner.heroes.repository;

import org.itrunner.heroes.repository.specifications.FieldRange;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface WiselyRepository<T, ID> extends JpaRepository<T, ID> { //NOSONAR
    Page<T> findByExampleAndRange(Example<T> example, List<FieldRange<? extends Comparable>> fieldRanges, Pageable pageable);

    List<T> findByExampleAndRange(Example<T> example, List<FieldRange<? extends Comparable>> fieldRanges);
}