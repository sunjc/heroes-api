package org.itrunner.heroes.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Hero {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HERO_SEQ")
    @SequenceGenerator(name = "HERO_SEQ", sequenceName = "HERO_SEQ")
    private Long id;

    @Column(name = "NAME", unique = true, length = 30)
    @NotNull
    private String name;

    public Hero() {
    }

    public Hero(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}