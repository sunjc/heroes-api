package org.itrunner.heroes.service;

import org.itrunner.heroes.domain.Hero;
import org.itrunner.heroes.exception.HeroNotFoundException;
import org.itrunner.heroes.repository.HeroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class HeroService {
    @Autowired
    private HeroRepository repository;

    public Hero getHeroById(Long id) {
        return repository.findById(id).orElseThrow(() -> new HeroNotFoundException(id));
    }

    public List<Hero> getAllHeroes() {
        return repository.findAll();
    }

    public List<Hero> findHeroesByName(String name) {
        return repository.findByName(name);
    }

    public Hero saveHero(Hero hero) {
        return repository.save(hero);
    }

    public void deleteHero(Long id) {
        repository.deleteById(id);
    }
}
