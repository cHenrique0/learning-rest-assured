package br.learning.restassured;

import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.request;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.greaterThan;

public class UserJsonTest {
    @Test
    public void testValidandoJsonPrimeiroNivel() {
        given()
        .when()
                .get("https://restapi.wcaquino.me/users/1")
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body(notNullValue())
                .body("id", is(1))
                .body("name", containsString("Silva"))
                .body("age", greaterThan(18));
    }

    @Test
    public void testValidandoJsonPrimeiroNivel2() {
        Response reponse = request(Method.GET, "https://restapi.wcaquino.me/users/1");

        /* path */
        Assertions.assertEquals(Integer.valueOf(1), reponse.path("id"));

        /* jsonpath */
        JsonPath jsonPath = new JsonPath(reponse.asString());
        Assertions.assertEquals(1, jsonPath.getInt("id"));

        /* from */
        int id = JsonPath.from(reponse.asString()).getInt("id");
        Assertions.assertEquals(1, id);
    }

    @Test
    public void testValidandoJsonSegundoNivel() {
        given()
        .when()
                .get("https://restapi.wcaquino.me/users/2")
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body(notNullValue())
                .body("name", containsString("Joaquina"))
                .body("endereco.rua", is("Rua dos bobos"));
    }

    @Test
    public void testValidandoJsonLista() {
        given()
        .when()
                .get("https://restapi.wcaquino.me/users/3")
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body(notNullValue())
                .body("name", containsString("Ana"))
                .body("filhos", hasSize(2))
                .body("filhos[0].name", is("Zezinho"))
                .body("filhos[1].name", is("Luizinho"))
                .body("filhos.name", hasItem("Zezinho"))
                .body("filhos.name", hasItems("Zezinho", "Luizinho"));
    }
}
