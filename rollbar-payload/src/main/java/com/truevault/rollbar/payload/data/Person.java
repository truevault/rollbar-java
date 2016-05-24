package com.truevault.rollbar.payload.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.truevault.rollbar.utilities.ArgumentNullException;
import com.truevault.rollbar.utilities.InvalidLengthException;
import com.truevault.rollbar.utilities.Validate;
import javax.annotation.concurrent.Immutable;

/**
 * Represents the user affected by an error
 */
@Immutable
public class Person {
    private final String id;
    private final String username;
    private final String email;

    /**
     * @param id the affected user's id
     * @throws ArgumentNullException if {@code id} is null
     */
    public Person(String id) throws ArgumentNullException {
        this(id, null, null);
    }

    /**
     * @param id       the affected user's id
     * @param username the affected user's username
     * @param email    the affected user's email address
     * @throws InvalidLengthException if {@code username} or {@code email} are longer than 255 characters
     * @throws ArgumentNullException  if {@code id} is null
     */
    public Person(String id, String username, String email) throws InvalidLengthException, ArgumentNullException {
        Validate.isNotNullOrWhitespace(id, "id");
        this.id = id;
        if (username != null) {
            Validate.maxLength(username, 255, "username");
        }
        this.username = username;
        if (email != null) {
            Validate.maxLength(email, 255, "email");
        }
        this.email = email;
    }

    /**
     * @return the affected user's id
     */
    @JsonProperty("id")
    public String id() {
        return this.id;
    }

    /**
     * @return the affected user's username
     */
    @JsonProperty("username")
    public String username() {
        return this.username;
    }

    /**
     * @return the affected user's email
     */
    @JsonProperty("email")
    public String email() {
        return this.email;
    }
}
