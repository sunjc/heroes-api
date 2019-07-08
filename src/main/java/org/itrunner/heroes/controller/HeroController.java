package org.itrunner.heroes.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.itrunner.heroes.domain.Hero;
import org.itrunner.heroes.service.HeroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "${api.base-path}", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = {"Hero Controller"})
@Slf4j
public class HeroController {
    private final HeroService service;

    @Autowired
    public HeroController(HeroService service) {
        this.service = service;
    }

    @ApiOperation("Get hero by id")
    @GetMapping("/heroes/{id}")
    public Hero getHeroById(@ApiParam(required = true, example = "1") @PathVariable("id") Long id) {
        return service.getHeroById(id);
    }

    @ApiOperation("Get all heroes")
    @GetMapping("/heroes")
    public List<Hero> getHeroes() {
        return service.getAllHeroes();
    }

    @ApiOperation("Search heroes by name")
    @GetMapping("/heroes/")
    public List<Hero> searchHeroes(@ApiParam(required = true) @RequestParam("name") String name) {
        return service.findHeroesByName(name);
    }

    @ApiOperation("Add new hero")
    @PostMapping("/heroes")
    public Hero addHero(@ApiParam(required = true) @Valid @RequestBody Hero hero) {
        return service.saveHero(hero);
    }

    @ApiOperation("Update hero info")
    @PutMapping("/heroes")
    public Hero updateHero(@ApiParam(required = true) @RequestBody Hero hero) {
        return service.saveHero(hero);
    }

    @ApiOperation("Delete hero by id")
    @DeleteMapping("/heroes/{id}")
    public void deleteHero(@ApiParam(required = true, example = "1") @PathVariable("id") Long id) {
        service.deleteHero(id);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, Object>> handleDataAccessException(DataAccessException exception) {
        log.error(exception.getMessage(), exception);
        Map<String, Object> body = new HashMap<>();
        body.put("message", exception.getMessage());
        return ResponseEntity.badRequest().body(body);
    }
}