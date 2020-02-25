package org.itrunner.heroes.service;

import org.itrunner.heroes.domain.Hero;
import org.itrunner.heroes.dto.HeroDto;
import org.itrunner.heroes.repository.HeroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

class HeroServiceTest {
    @Mock
    private HeroRepository heroRepository;

    @InjectMocks
    private HeroService heroService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);

        List<Hero> heroes = new ArrayList<>();
        heroes.add(new Hero(1L, "Rogue"));
        heroes.add(new Hero(2L, "Jason"));

        given(heroRepository.findById(1L)).willReturn(Optional.of(heroes.get(0)));
        given(heroRepository.findAll(PageRequest.of(0, 10))).willReturn(Page.empty());
        given(heroRepository.findByName("o")).willReturn(heroes);
    }

    @Test
    void getHeroById() {
        HeroDto hero = heroService.getHeroById(1L);
        assertThat(hero.getName()).isEqualTo("Rogue");
    }

    @Test
    void getAllHeroes() {
        Page<HeroDto> heroes = heroService.getAllHeroes(PageRequest.of(0, 10));
        assertThat(heroes.getTotalElements()).isEqualTo(0);
    }

    @Test
    void findHeroesByName() {
        List<HeroDto> heroes = heroService.findHeroesByName("o");
        assertThat(heroes.size()).isEqualTo(2);
    }
}
