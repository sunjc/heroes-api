package org.itrunner.heroes.mapper;

import org.itrunner.heroes.domain.Hero;
import org.itrunner.heroes.dto.HeroDto;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper
public interface HeroMapper {
    HeroDto toHeroDto(Hero hero);

    Hero toHero(HeroDto heroDto);

    List<HeroDto> toHeroDtos(List<Hero> heroes);

    default Page<HeroDto> toHeroDtoPage(Page<Hero> heroPage) {
        return heroPage.map(this::toHeroDto);
    }
}
