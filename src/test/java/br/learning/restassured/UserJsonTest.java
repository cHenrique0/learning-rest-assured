package br.learning.restassured;

import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

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

    @Test
    public void testErroUsuarioInexistente() {
        given()
        .when()
                .get("https://restapi.wcaquino.me/users/4")
        .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .log().body()
                .body(notNullValue())
                .body("error", is("Usuário inexistente"));
    }

    @Test
    public void testValidandoLista() {
        given()
        .when()
                .get("https://restapi.wcaquino.me/users")
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body(notNullValue())
                .body("$", hasSize(3)) // $ significa raiz do json, pode não colocar o $ que vai funcionar.
                .body("name", hasItems("João da Silva", "Maria Joaquina", "Ana Júlia"))
                .body("age[1]", is(25))
                .body("filhos.name", hasItem(Arrays.asList("Zezinho", "Luizinho")))
                .body("salary", contains(1234.5678f, 2500, null));
    }

    @Test
    public void testVerificacoesAvancadas() {
        given()
        .when()
                .get("https://restapi.wcaquino.me/users")
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body(notNullValue())
                /* 'it' faz referência a idade atual no loop */
                .body("age.findAll{it <= 25}.size()", is(2))
                .body("age.findAll{it <= 25 && it > 20}.size()", is(1))
                /* 'it' faz referência ao objeto atual no loop */
                .body("findAll{it.age <= 25 }[0].name", is("Maria Joaquina"))
                .body("findAll{it.age <= 25 }[-1].name", is("Ana Júlia"))
                .body("find{it.age <= 25 }.name", is("Maria Joaquina"))
                .body("findAll{it.name.contains('n')}.name", hasItems("Maria Joaquina", "Ana Júlia"))
                .body("findAll{it.name.length()}.name", hasItems("João da Silva", "Maria Joaquina"));
    }

    @Test
    public void testValidandoJsonpath() {
        List<String> names =
                given()
                .when()
                        .get("https://restapi.wcaquino.me/users")
                .then()
                        .extract()
                            .path("name.findAll{it.startsWith('Maria')}");

        Assertions.assertEquals(1, names.size());
        Assertions.assertTrue(names.get(0).equalsIgnoreCase("maria joaquina"));
        Assertions.assertEquals(names.get(0).toUpperCase(), "maria joaquina".toUpperCase());
    }
}
