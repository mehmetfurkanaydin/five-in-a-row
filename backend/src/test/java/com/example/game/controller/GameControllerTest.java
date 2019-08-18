package com.example.game.controller;

import com.google.gson.JsonObject;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Assert;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import com.google.gson.Gson;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GameControllerTest {

    @LocalServerPort
    private int port;

    HttpHeaders headers = new HttpHeaders();

    TestRestTemplate restTemplate = new TestRestTemplate();

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    @Test
    public void Scenario_1_testNewGame() throws Exception {
        String user1 = "user1";
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                createURLWithPort("/api/game/newGame?username=" + user1 ), HttpMethod.GET, entity, String.class);

        String response = responseEntity.getBody();
        Assert.assertTrue(response.contains("Waiting for second player!"));
    }

    @Test
    public void Scenario_2_testNewGameForSecondUser() throws Exception {
        String user2 = "user2";
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                createURLWithPort("/api/game/newGame?username=" + user2 ), HttpMethod.GET, entity, String.class);

        String response = responseEntity.getBody();
        Gson gson = new Gson();
        JsonObject object = gson.fromJson(response, JsonObject.class);
        Assert.assertTrue(object.get("grid") != null);
        Assert.assertTrue(object.get("status").getAsString().equals("ready"));
        Assert.assertTrue(object.get("turn").getAsString().equals("user1"));
    }

    @Test
    public void Scenario_3_playerOneMakesMove() throws Exception {
        String user1 = "user1";
        JsonObject moveData = new JsonObject();
        moveData.addProperty("row", 5);
        moveData.addProperty("colm", 8);
        moveData.addProperty("username", user1);

        HttpEntity<String> entity = new HttpEntity<String>(moveData.toString(), headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                createURLWithPort("/api/game/makeMove"), HttpMethod.POST, entity, String.class);

        String response = responseEntity.getBody();
        Gson gson = new Gson();
        JsonObject object = gson.fromJson(response, JsonObject.class);
        Assert.assertTrue(object.get("status").getAsString().equals("ready"));
        Assert.assertTrue(object.get("turn").getAsString().equals("user2"));
    }

    @Test
    public void Scenario_4_playerTwoMakesMove() throws Exception {
        String user1 = "user2";
        JsonObject moveData = new JsonObject();
        moveData.addProperty("row", 4);
        moveData.addProperty("colm", 7);
        moveData.addProperty("username", user1);

        HttpEntity<String> entity = new HttpEntity<String>(moveData.toString(), headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                createURLWithPort("/api/game/makeMove"), HttpMethod.POST, entity, String.class);

        String response = responseEntity.getBody();
        Gson gson = new Gson();
        JsonObject object = gson.fromJson(response, JsonObject.class);
        Assert.assertTrue(object.get("status").getAsString().equals("ready"));
        Assert.assertTrue(object.get("turn").getAsString().equals("user1"));
    }

    @Test
    public void Scenario_5_playerOneWins() throws Exception {
        String currentUser;
        ResponseEntity<String> responseEntity = null;

        for(int i = 0; i < 8; i++) {
            if (i % 2 == 0) {
                currentUser = "1";
            } else {
                currentUser = "2";
            }
            String user = "user" + currentUser;
            JsonObject moveData = new JsonObject();
            if (user.equals("user1")) {
                moveData.addProperty("colm", 3);
            } else {

                moveData.addProperty("colm", 4);
            }
            moveData.addProperty("row", 1 + i / 2);
            moveData.addProperty("username", user);

            HttpEntity<String> entity = new HttpEntity<String>(moveData.toString(), headers);

            responseEntity = restTemplate.exchange(
                    createURLWithPort("/api/game/makeMove"), HttpMethod.POST, entity, String.class);

        }

        JsonObject moveData = new JsonObject();
        moveData.addProperty("row", 5);
        moveData.addProperty("colm", 3);
        moveData.addProperty("username", "user1");

        HttpEntity<String> entity = new HttpEntity<String>(moveData.toString(), headers);

        responseEntity = restTemplate.exchange(
                createURLWithPort("/api/game/makeMove"), HttpMethod.POST, entity, String.class);

        String response = responseEntity.getBody();
        Gson gson = new Gson();
        JsonObject object = gson.fromJson(response, JsonObject.class);
        Assert.assertTrue(object.get("winner").getAsString().equals("user1"));
    }
}
