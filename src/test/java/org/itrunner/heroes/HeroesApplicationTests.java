package org.itrunner.heroes;

import org.itrunner.heroes.domain.Hero;
import org.itrunner.heroes.exception.ErrorMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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
    void setup() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("client_id", "heroes");
        map.add("username", "admin");
        map.add("password", "admin");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, requestHeaders);

        Map<String, String> response = restTemplate.postForObject("http://localhost:8090/auth/realms/heroes/protocol/openid-connect/token", requestEntity, Map.class);
        String token = response.get("access_token");

        restTemplate.getRestTemplate().setInterceptors(
                Collections.singletonList((request, body, execution) -> {
                    HttpHeaders headers = request.getHeaders();
                    headers.setBearerAuth(token);
                    return execution.execute(request, body);
                }));
    }

    @Test
    void crudSuccess() {
        Hero hero = new Hero();
        hero.setName("Jack");

        // add hero
        hero = restTemplate.postForObject("/api/heroes", hero, Hero.class);
        assertThat(hero.getId()).isNotNull();

        // update hero
        hero.setName("Jacky");
        HttpEntity<Hero> requestEntity = new HttpEntity<>(hero);
        hero = restTemplate.exchange("/api/heroes", HttpMethod.PUT, requestEntity, Hero.class).getBody();
        assertThat(hero.getName()).isEqualTo("Jacky");

        // find heroes by name
        Map<String, String> urlVariables = new HashMap<>();
        urlVariables.put("name", "m");
        List<Hero> heroes = restTemplate.getForObject("/api/heroes/?name={name}", List.class, urlVariables);
        assertThat(heroes.size()).isEqualTo(5);

        // get hero by id
        hero = restTemplate.getForObject("/api/heroes/" + hero.getId(), Hero.class);
        assertThat(hero.getName()).isEqualTo("Jacky");

        // delete hero successfully
        ResponseEntity<String> response = restTemplate.exchange("/api/heroes/" + hero.getId(), HttpMethod.DELETE, null, String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        // delete hero
        response = restTemplate.exchange("/api/heroes/9999", HttpMethod.DELETE, null, String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    void addHeroValidationFailed() {
        Hero hero = new Hero();
        ResponseEntity<ErrorMessage> responseEntity = restTemplate.postForEntity("/api/heroes", hero, ErrorMessage.class);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
        assertThat(responseEntity.getBody().getError()).isEqualTo("MethodArgumentNotValidException");
    }
}