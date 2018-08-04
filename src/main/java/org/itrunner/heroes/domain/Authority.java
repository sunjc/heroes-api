package org.itrunner.heroes.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@Table(name = "AUTHORITY")
public class Authority {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUTHORITY_SEQ")
    @SequenceGenerator(name = "AUTHORITY_SEQ", sequenceName = "AUTHORITY_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "AUTHORITY_NAME", length = 50)
    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthorityName name;

    @ManyToMany(mappedBy = "authorities", fetch = FetchType.LAZY)
    private List<User> users;
}