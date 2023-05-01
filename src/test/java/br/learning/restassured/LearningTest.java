package br.learning.restassured;

import org.apache.http.HttpStatus;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class LearningTest {

    @Test
    public void testMatcherAssert() {
        MatcherAssert.assertThat("Hello", isA(String.class));
        MatcherAssert.assertThat(123, isA(Integer.class));
        Assertions.assertInstanceOf(String.class, "Hello");
    }

    @Test
    public void testValidandoBody() {

        given()
        .when()
                .get("https://restapi.wcaquino.me/ola")
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body(notNullValue())
                .body(is("Ola Mundo!"))
                .body(containsString("Mundo"));
    }

}
