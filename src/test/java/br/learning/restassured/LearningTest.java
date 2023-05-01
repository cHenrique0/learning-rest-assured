package br.learning.restassured;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LearningTest {

    @Test
    public void testMatcherAssert() {
        MatcherAssert.assertThat("Hello", Matchers.isA(String.class));
        MatcherAssert.assertThat(123, Matchers.isA(Integer.class));
        Assertions.assertInstanceOf(String.class, "Hello");
    }


}
