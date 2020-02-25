package org.itrunner.heroes;

import org.itrunner.heroes.controller.AuthenticationRequest;
import org.itrunner.heroes.controller.AuthenticationResponse;
import org.itrunner.heroes.dto.HeroDto;
import org.itrunner.heroes.exception.ErrorMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class HeroesApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setup() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("admin");
        authenticationRequest.setPassword("admin");
        String token = restTemplate.postForObject("/api/auth", authenticationRequest, AuthenticationResponse.class).getToken();

        restTemplate.getRestTemplate().setInterceptors(
                Collections.singletonList((request, body, execution) -> {
                    HttpHeaders headers = request.getHeaders();
                    headers.add("Authorization", "Bearer " + token);
                    headers.add("Content-Type", "application/json");
                    return execution.execute(request, body);
                }));
    }

    @Test
    public void loginFailure() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("admin");
        request.setPassword("111111");
        int statusCode = restTemplate.postForEntity("/api/auth", request, HttpEntity.class).getStatusCodeValue();
        assertThat(statusCode).isEqualTo(403);
    }

    @Test
    public void crudSuccess() {
        HeroDto hero = new HeroDto();
        hero.setName("Jack");

        // add hero
        hero = restTemplate.postForObject("/api/heroes", hero, HeroDto.class);
        assertThat(hero.getId()).isNotNull();

        // update hero
        hero.setName("Jacky");
        HttpEntity<HeroDto> requestEntity = new HttpEntity<>(hero);
        hero = restTemplate.exchange("/api/heroes", HttpMethod.PUT, requestEntity, HeroDto.class).getBody();
        assertThat(hero.getName()).isEqualTo("Jacky");

        // find heroes by name
        Map<String, String> urlVariables = new HashMap<>();
        urlVariables.put("name", "m");
        List<HeroDto> heroes = restTemplate.getForObject("/api/heroes/?name={name}", List.class, urlVariables);
        assertThat(heroes.size()).isEqualTo(5);

        // get hero by id
        hero = restTemplate.getForObject("/api/heroes/" + hero.getId(), HeroDto.class);
        assertThat(hero.getName()).isEqualTo("Jacky");

        // delete hero successfully
        ResponseEntity<String> response = restTemplate.exchange("/api/heroes/" + hero.getId(), HttpMethod.DELETE, null, String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        // delete hero
        response = restTemplate.exchange("/api/heroes/9999", HttpMethod.DELETE, null, String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void addHeroValidationFailed() {
        HeroDto hero = new HeroDto();
        ResponseEntity<ErrorMessage> responseEntity = restTemplate.postForEntity("/api/heroes", hero, ErrorMessage.class);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
        assertThat(responseEntity.getBody().getError()).isEqualTo("MethodArgumentNotValidException");
    }
}