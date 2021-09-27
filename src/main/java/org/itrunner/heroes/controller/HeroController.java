package org.itrunner.heroes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.itrunner.heroes.dto.HeroDto;
import org.itrunner.heroes.service.HeroService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/heroes", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Hero Controller")
@Slf4j
public class HeroController {
    private final HeroService service;

    @Autowired
    public HeroController(HeroService service) {
        this.service = service;
    }

    @Operation(summary = "Get hero by id")
    @GetMapping("/{id}")
    public HeroDto getHeroById(@Parameter(required = true, example = "1") @PathVariable("id") Long id) {
        return service.getHeroById(id);
    }

    @Operation(summary = "Get all heroes")
    @GetMapping
    public Page<HeroDto> getHeroes(@SortDefault.SortDefaults({@SortDefault(sort = "name", direction = Sort.Direction.ASC)})
                                   @ParameterObject Pageable pageable) {
        return service.getAllHeroes(pageable);
    }

    @Operation(summary = "Search heroes by name")
    @GetMapping("/")
    public List<HeroDto> searchHeroes(@Parameter(required = true) @RequestParam("name") String name) {
        return service.findHeroesByName(name);
    }

    @Operation(summary = "Add new hero")
    @PostMapping
    public HeroDto addHero(@Parameter(required = true) @Valid @RequestBody HeroDto hero) {
        return service.saveHero(hero);
    }

    @Operation(summary = "Update hero")
    @PutMapping
    public HeroDto updateHero(@Parameter(required = true) @Valid @RequestBody HeroDto hero) {
        return service.saveHero(hero);
    }

    @Operation(summary = "Delete hero by id")
    @DeleteMapping("/{id}")
    public void deleteHero(@Parameter(required = true, example = "1") @PathVariable("id") Long id) {
        service.deleteHero(id);
    }

    /*@ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, Object>> handleDataAccessException(DataAccessException exception) {
        log.error(exception.getMessage(), exception);
        Map<String, Object> body = new HashMap<>();
        body.put("message", exception.getMessage());
        return ResponseEntity.badRequest().body(body);
    }*/
}