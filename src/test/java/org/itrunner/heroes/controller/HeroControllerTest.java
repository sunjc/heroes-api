package org.itrunner.heroes.controller;

import org.itrunner.heroes.base.WithMockKeycloakUser;
import org.itrunner.heroes.domain.Hero;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.itrunner.heroes.util.JsonUtil.asJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HeroControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockKeycloakUser
    public void crudSuccess() throws Exception {
        Hero hero = new Hero();
        hero.setName("Jack");

        // add hero
        mvc.perform(post("/api/heroes").content(asJson(hero)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().json("{'id':11, 'name':'Jack', 'createBy':'admin'}"));

        // update hero
        hero.setId(11l);
        hero.setName("Jacky");
        mvc.perform(put("/api/heroes").content(asJson(hero)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().json("{'name':'Jacky'}"));

        // find heroes by name
        mvc.perform(get("/api/heroes/?name=m").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // get hero by id
        mvc.perform(get("/api/heroes/11").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().json("{'name':'Jacky'}"));

        // delete hero successfully
        mvc.perform(delete("/api/heroes/11").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // delete hero
        mvc.perform(delete("/api/heroes/9999")).andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockKeycloakUser
    void addHeroValidationFailed() throws Exception {
        Hero hero = new Hero();
        mvc.perform(post("/api/heroes").content(asJson(hero)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }
}
