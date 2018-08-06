package org.itrunner.heroes.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.itrunner.heroes.domain.Hero;
import org.itrunner.heroes.service.HeroService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "${api.base-path}", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = {"Hero Controller"})
public class HeroController {
    private static final Logger LOG = LoggerFactory.getLogger(HeroController.class);

    @Autowired
    private HeroService service;

    @ApiOperation(value = "Get hero by id")
    @GetMapping("/heroes/{id}")
    public Hero getHeroById(@ApiParam(required = true) @PathVariable("id") Long id) {
        return service.getHeroById(id);
    }

    @ApiOperation(value = "Get all heroes")
    @GetMapping("/heroes")
    public List<Hero> getHeroes() {
        return service.getAllHeroes();
    }

    @ApiOperation(value = "Search heroes by name")
    @GetMapping("/heroes/")
    public List<Hero> searchHeroes(@ApiParam(required = true) @RequestParam("name") String name) {
        return service.findHeroesByName(name);
    }

    @ApiOperation(value = "Add new hero")
    @PostMapping("/heroes")
    public Hero addHero(@ApiParam(required = true) @RequestBody Hero hero) {
        return service.saveHero(hero);
    }

    @ApiOperation(value = "Update hero info")
    @PutMapping("/heroes")
    public Hero updateHero(@ApiParam(required = true) @RequestBody Hero hero) {
        return service.saveHero(hero);
    }

    @ApiOperation(value = "Delete hero by id")
    @DeleteMapping("/heroes/{id}")
    public void deleteHero(@ApiParam(required = true) @PathVariable("id") Long id) {
        service.deleteHero(id);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, Object>> handleDataAccessException(DataAccessException exception) {
        LOG.error(exception.getMessage(), exception);
        Map<String, Object> body = new HashMap<>();
        body.put("message", exception.getMessage());
        return ResponseEntity.badRequest().body(body);
    }
}