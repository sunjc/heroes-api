package org.itrunner.heroes.service;

import org.itrunner.heroes.domain.Hero;
import org.itrunner.heroes.dto.HeroDto;
import org.itrunner.heroes.exception.HeroNotFoundException;
import org.itrunner.heroes.repository.HeroRepository;
import org.itrunner.heroes.util.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.itrunner.heroes.mapper.HeroMapper.MAPPER;

@Service
@Transactional
public class HeroService {
    private final HeroRepository repository;
    private final Messages messages;

    @Autowired
    public HeroService(HeroRepository repository, Messages messages) {
        this.repository = repository;
        this.messages = messages;
    }

    public HeroDto getHeroById(Long id) {
        Hero hero = repository.findById(id).orElseThrow(() -> new HeroNotFoundException(messages.getMessage("hero.notFound", new Object[]{id})));
        return MAPPER.toHeroDto(hero);
    }

    public Page<HeroDto> getAllHeroes(Pageable pageable) {
        Page<Hero> heroes = repository.findAll(pageable);
        return MAPPER.toHeroDtoPage(heroes);
    }

    public List<HeroDto> findHeroesByName(String name) {
        List<Hero> heroes = repository.findByName(name);
        return MAPPER.toHeroDtos(heroes);
    }

    public HeroDto saveHero(HeroDto heroDto) {
        Hero hero = MAPPER.toHero(heroDto);
        hero = repository.save(hero);
        return MAPPER.toHeroDto(hero);
    }

    public void deleteHero(Long id) {
        repository.deleteById(id);
    }
}
