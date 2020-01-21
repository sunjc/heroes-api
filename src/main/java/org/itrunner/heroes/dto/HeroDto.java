package org.itrunner.heroes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HeroDto {
    private Long id;
    @NotNull
    @Size(min = 3, max = 30)
    private String name;
}
