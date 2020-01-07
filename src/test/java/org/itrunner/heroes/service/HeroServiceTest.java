package org.itrunner.heroes.service;

import org.itrunner.heroes.domain.Hero;
import org.itrunner.heroes.dto.HeroDto;
import org.itrunner.heroes.repository.HeroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class HeroServiceTest {
    @MockBean
    private HeroRepository heroRepository;

    @Autowired
    private HeroService heroService;

    @BeforeEach
    void setup() {
        List<Hero> heroes = new ArrayList<>();
        heroes.add(new Hero(1L, "Rogue"));
        heroes.add(new Hero(2L, "Jason"));

        given(heroRepository.findById(1L)).willReturn(Optional.of(heroes.get(0)));
        given(heroRepository.findAll()).willReturn(heroes);
        given(heroRepository.findByName("o")).willReturn(heroes);
    }

    @Test
    void getHeroById() {
        HeroDto hero = heroService.getHeroById(1L);
        assertThat(hero.getName()).isEqualTo("Rogue");
    }

    @Test
    void getAllHeroes() {
        List<HeroDto> heroes = heroService.getAllHeroes();
        assertThat(heroes.size()).isEqualTo(2);
    }

    @Test
    void findHeroesByName() {
        List<HeroDto> heroes = heroService.findHeroesByName("o");
        assertThat(heroes.size()).isEqualTo(2);
    }
}
