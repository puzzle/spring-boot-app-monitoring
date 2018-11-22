/*
 * Copyright 2016-2017 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ch.puzzle.monitoring;

import ch.puzzle.monitoring.service.Fruit;
import ch.puzzle.monitoring.service.FruitRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FruitControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private FruitRepository fruitRepository;

    @Before
    public void beforeTest() {
        fruitRepository.deleteAll();
    }

    @Test
    public void testGetAll() throws Exception {
        Fruit cherry = fruitRepository.save(new Fruit("Cherry"));
        Fruit apple = fruitRepository.save(new Fruit("Apple"));
        mvc.perform(get("/api/fruits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(cherry.getId(), apple.getId())))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(cherry.getName(), apple.getName())));
    }

    @Test
    public void testGetEmptyArray() throws Exception {
        mvc.perform(get("/api/fruits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testGetOne() throws Exception {
        Fruit cherry = fruitRepository.save(new Fruit("Cherry"));
        mvc.perform(get("/api/fruits/{id}", cherry.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(cherry.getId())))
                .andExpect(jsonPath("$.name", is(cherry.getName())));
    }

    @Test
    public void testGetNotExisting() throws Exception {
        mvc.perform(get("/api/fruits/{id}", 13))
                .andExpect(status().is(404));
    }

    @Test
    public void testPost() throws Exception {
        mvc.perform(post("/api/fruits")
                .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Cherry\"}"))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.id", not(isEmptyString())))
                .andExpect(jsonPath("$.name", is("Cherry")));
    }

    @Test
    public void testPostWithWrongPayload() throws Exception {
        mvc.perform(post("/api/fruits")
                .contentType(MediaType.APPLICATION_JSON).content("{\"id\":0}"))
                .andExpect(status().is(422));
    }

    @Test
    public void testPostWithEmptyPayload() throws Exception {
        mvc.perform(post("/api/fruits")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(415));
    }

    @Test
    public void testPut() throws Exception {
        Fruit cherry = fruitRepository.save(new Fruit("Cherry"));
        mvc.perform(put("/api/fruits/{id}", cherry.getId())
                .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Lemon\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(cherry.getId())))
                .andExpect(jsonPath("$.name", is("Lemon")));
    }

    @Test
    public void testPutNotExisting() throws Exception {
        mvc.perform(put("/api/fruits/{id}", 0)
                .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Lemon\"}"))
                .andExpect(status().is(404));
    }

    @Test
    public void testPutWithWrongPayload() throws Exception {
        Fruit cherry = fruitRepository.save(new Fruit("Cherry"));
        mvc.perform(put("/api/fruits/{id}", cherry.getId())
                .contentType(MediaType.APPLICATION_JSON).content("{\"id\":0}"))
                .andExpect(status().is(422));
    }

    @Test
    public void testPutWithEmptyPayload() throws Exception {
        Fruit cherry = fruitRepository.save(new Fruit("Cherry"));
        mvc.perform(put("/api/fruits/{id}", cherry.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(415));
    }

    @Test
    public void testDelete() throws Exception {
        Fruit cherry = fruitRepository.save(new Fruit("Cherry"));
        mvc.perform(delete("/api/fruits/{id}", cherry.getId()))
                .andExpect(status().is(204));
        assertFalse(fruitRepository.existsById(cherry.getId()));
    }

    @Test
    public void testDeleteNotExisting() throws Exception {
        mvc.perform(delete("/api/fruits/{id}", 0))
                .andExpect(status().is(404));
    }

}
