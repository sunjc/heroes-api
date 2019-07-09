package org.itrunner.heroes.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "HERO", uniqueConstraints = {@UniqueConstraint(name = "UK_HERO_NAME", columnNames = {"NAME"})})
public class Hero {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HERO_SEQ")
    @SequenceGenerator(name = "HERO_SEQ", sequenceName = "HERO_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "NAME", length = 30, nullable = false)
    @NotNull
    private String name;
}