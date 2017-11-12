package com.examples;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Robustness principle
 * TOLERANT READER : BE TOLERANT IN WHAT YOU READ
 * TOLERANT WRITER : BE CONSERVATIVE IN THAT YOU WRITE
 */
public class JsonTest {

    @Test
    public void test1() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue("{}", User.class);

        assertThat("TOLERANT-READER. The json might miss property that you need. " +
                        "Use custom default values for properties that you care about that might not be in the json." +
                        "How: define the default in the property definition of the POJO",
                user.getName(), is("NO-NAME"));
    }

    @Test
    public void test2() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        User user =
                mapper.readValue("{ \"name\" : \"Nicola\", \"surname\" : \"Apicella\", \"sex\" : \"male\"}",
                        User.class);

        assertThat("TOLERANT-READER. The json might contain properties that are not defined in the pojo. " +
                        "Ignore them!" +
                        "How: use @JsonIgnoreProperties(ignoreUnknown = true) annotation on the POJO",
                user.getName(), is("Nicola"));
    }

    @Test
    public void test3() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        User user = new User("Michael", null);
        String json = mapper.writeValueAsString(user);

        User deserializedUser = mapper.readValue(json, User.class);

        assertThat("TOLERANT-WRITER. Don't serialize null values. A tolerant reader prefers no value at all, " +
                        "because in that case can provide a default. See test1." +
                        "If you serialize null, there is no way for the reader to understand that actually the property" +
                        "is missing." +
                        "How: use @JsonInclude(JsonInclude.Include.NON_NULL) annotation on the POJO",
                deserializedUser.getSurname(), is("NO-SURNAME"));

        assertThat(deserializedUser.getName(), is("Michael"));
    }

}
