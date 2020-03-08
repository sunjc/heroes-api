package org.itrunner.heroes.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "HERO", uniqueConstraints = {@UniqueConstraint(name = "UK_HERO_NAME", columnNames = {"HERO_NAME"})})
public class Hero {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HERO_SEQ")
    @SequenceGenerator(name = "HERO_SEQ", sequenceName = "HERO_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "HERO_NAME", length = 30, nullable = false)
    private String name;

    @Column(name = "CREATED_BY", length = 50, updatable = false, nullable = false)
    @CreatedBy
    private String createdBy;

    @Column(name = "CREATED_DATE", updatable = false, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdDate;

    @Column(name = "LAST_MODIFIED_BY", length = 50)
    @LastModifiedBy
    private String lastModifiedBy;

    @Column(name = "LAST_MODIFIED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date lastModifiedDate;

    public Hero(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}