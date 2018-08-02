package org.itrunner.heroes.service;

import org.itrunner.heroes.domain.Hero;
import org.itrunner.heroes.repository.HeroRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class HeroServiceTest {
    @Mock
    private HeroRepository heroRepository;

    @InjectMocks
    private HeroService heroService;

    private List<Hero> heroes;

    @Before
    public void setup() {
        heroes = new ArrayList<>();
        heroes.add(new Hero(1L, "Rogue"));
        heroes.add(new Hero(2L, "Jason"));

        given(heroRepository.findById(1L)).willReturn(Optional.of(heroes.get(0)));
        given(heroRepository.findAll()).willReturn(heroes);
        given(heroRepository.findByName("o")).willReturn(heroes);
    }

    @Test
    public void getHeroById() {
        Hero hero = heroService.getHeroById(1L);
        assertThat(hero.getName()).isEqualTo("Rogue");
    }

    @Test
    public void getAllHeroes() {
        List<Hero> heroes = heroService.getAllHeroes();
        assertThat(heroes.size()).isEqualTo(2);
    }

    @Test
    public void findHeroesByName() {
        List<Hero> heroes = heroService.findHeroesByName("o");
        assertThat(heroes.size()).isEqualTo(2);
    }
}
