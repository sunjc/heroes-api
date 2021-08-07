package org.itrunner.heroes.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "name"})
public class HeroDto {
    private Long id;
    @NotNull
    @Size(min = 3, max = 30)
    private String name;
    private String createdBy;
    private Date createdDate;
}
