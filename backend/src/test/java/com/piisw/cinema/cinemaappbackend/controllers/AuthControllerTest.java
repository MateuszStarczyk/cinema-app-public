/*
 * MIT License
 *
 * Copyright (c) 2020 Politechnika Wrocławska - Projektowanie i implementacja systemów webowych
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.piisw.cinema.cinemaappbackend.controllers;

import static com.piisw.cinema.cinemaappbackend.controllers.Adresses.HOST;
import static com.piisw.cinema.cinemaappbackend.controllers.Adresses.SIGN_IN;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import com.piisw.cinema.cinemaappbackend.CinemaAppBackendApplication;

@SpringBootTest(
    classes = CinemaAppBackendApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AuthControllerTest {

  @Autowired private TestRestTemplate testRestTemplate;

  @Test
  void successfulSignUpTest() {
    String rawJson =
        "{\n"
            + "\t\"firstName\": \"Test\",\n"
            + "\t\"lastName\": \"Tester\",\n"
            + "\t\"email\": \"tt@test.com\",\n"
            + "\t\"password\": \"abc123\"\n"
            + "}";

    String expectedRawBody = "{message=User registered successfully!}";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> entity = new HttpEntity<>(rawJson, headers);
    ResponseEntity<Object> response =
        testRestTemplate.postForEntity(HOST + "/api/auth/signup", entity, Object.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(Objects.requireNonNull(response.getBody()).toString()).isEqualTo(expectedRawBody);
  }

  @Test
  void existingUserSignUpTest() {
    String rawJson =
        "{\n"
            + "\t\"firstName\": \"Testing\",\n"
            + "\t\"lastName\": \"Tester\",\n"
            + "\t\"email\": \"tt@testing.com\",\n"
            + "\t\"password\": \"123456\"\n"
            + "}";

    String expectedRawBody = "{message=Email is already in use!}";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> entity = new HttpEntity<>(rawJson, headers);
    ResponseEntity<Object> response =
        testRestTemplate.postForEntity(HOST + "/api/auth/signup", entity, Object.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(Objects.requireNonNull(response.getBody()).toString()).isEqualTo(expectedRawBody);
  }

  @Test
  void successfulSignInTest() {
    String rawJson =
        "{\n" + "\t\"email\": \"tt@testing.com\",\n" + "\t\"password\": \"123456\"\n" + "}";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> entity = new HttpEntity<>(rawJson, headers);
    ResponseEntity<Object> response =
        testRestTemplate.postForEntity(HOST + SIGN_IN, entity, Object.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(Objects.requireNonNull(response.getBody()).toString()).contains("accessToken");
    assertThat(response.getBody().toString()).contains("email=tt@testing.com");
    assertThat(response.getBody().toString()).contains("firstName=Testing");
    assertThat(response.getBody().toString()).contains("id=2");
    assertThat(response.getBody().toString()).contains("lastName=Tester");
    assertThat(response.getBody().toString()).contains("roles=[ROLE_USER]");
    assertThat(response.getBody().toString()).contains("tokenType=Bearer");
  }

  @Test
  void wrongCredentialsSignInTest() {
    String rawJson =
        "{\n" + "\t\"email\": \"tt@testing.com\",\n" + "\t\"password\": \"wrongpassword\"\n" + "}";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> entity = new HttpEntity<>(rawJson, headers);
    ResponseEntity<Object> response =
        testRestTemplate.postForEntity(HOST + SIGN_IN, entity, Object.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }
}
