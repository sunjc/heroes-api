package org.itrunner.heroes.service;

import org.itrunner.heroes.domain.Hero;
import org.itrunner.heroes.dto.HeroDto;
import org.itrunner.heroes.exception.HeroNotFoundException;
import org.itrunner.heroes.mapper.HeroMapper;
import org.itrunner.heroes.repository.HeroRepository;
import org.itrunner.heroes.util.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class HeroService {
    private final HeroRepository repository;
    private final HeroMapper heroMapper;
    private final Messages messages;

    @Autowired
    public HeroService(HeroRepository repository, HeroMapper heroMapper, Messages messages) {
        this.repository = repository;
        this.heroMapper = heroMapper;
        this.messages = messages;
    }

    public HeroDto getHeroById(Long id) {
        Hero hero = repository.findById(id).orElseThrow(() -> new HeroNotFoundException(messages.getMessage("hero.notFound", new Object[]{id})));
        return heroMapper.toHeroDto(hero);
    }

    public Page<HeroDto> getAllHeroes(Pageable pageable) {
        Page<Hero> heroes = repository.findAll(pageable);
        return heroMapper.toHeroDtoPage(heroes);
    }

    public List<HeroDto> findHeroesByName(String name) {
        List<Hero> heroes = repository.findByName(name);
        return heroMapper.toHeroDtos(heroes);
    }

    @Transactional
    public HeroDto saveHero(HeroDto heroDto) {
        Hero hero = heroMapper.toHero(heroDto);
        hero = repository.save(hero);
        return heroMapper.toHeroDto(hero);
    }

    @Transactional
    public void deleteHero(Long id) {
        repository.deleteById(id);
    }
}
