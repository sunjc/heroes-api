package org.itrunner.heroes.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "shedlock")
@Data
public class Shedlock {
    @Id
    @Column(name = "name", length = 64)
    private String name;

    @Column(name = "lock_until")
    private LocalDateTime lockUntil;

    @Column(name = "locked_at")
    private LocalDateTime lockedAt;

    @Column(name = "locked_by")
    private String lockedBy;
}