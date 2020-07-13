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

import static com.piisw.cinema.cinemaappbackend.controllers.Adresses.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import com.google.gson.GsonBuilder;
import com.piisw.cinema.cinemaappbackend.CinemaAppBackendApplication;
import com.piisw.cinema.cinemaappbackend.models.Movie;
import com.piisw.cinema.cinemaappbackend.repository.MoviesRepository;

@SpringBootTest(
    classes = CinemaAppBackendApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ManageMoviesControllerTest {

  @Autowired private TestRestTemplate testRestTemplate;

  @Autowired private MoviesRepository moviesRepository;

  private String accessToken;

  private Movie movie;

  @BeforeEach
  void beforeEach() {
    String rawJson =
        "{\n" + "\t\"email\": \"admin@testing.com\",\n" + "\t\"password\": \"123456\"\n" + "}";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> entity = new HttpEntity<>(rawJson, headers);
    ResponseEntity<Object> response =
        testRestTemplate.postForEntity(HOST + SIGN_IN, entity, Object.class);
    accessToken =
        ((HashMap<String, String>) Objects.requireNonNull(response.getBody())).get("accessToken");

    // remove all movies
    moviesRepository.deleteAll();
    // add movies
    movie = new Movie("test", 145, "path", "url", "desc");
    moviesRepository.save(movie);
    movie = new Movie("test2", 121, "path", "url", "desc");
    moviesRepository.save(movie);
  }

  @Test
  void getAllMovies() {
    // given
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    HttpEntity<String> entity = new HttpEntity<>(headers);
    String jsonMovies =
        new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
            .toJson(moviesRepository.findAll());
    // when
    ResponseEntity<String> response =
        testRestTemplate.exchange(
            HOST + CINEMA_MANAGEMENT + MOVIES, HttpMethod.GET, entity, String.class);
    // then
    assertThat(response.getBody()).isEqualTo(jsonMovies);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void getExistingMovie() {
    // given
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    HttpEntity<String> entity = new HttpEntity<>(headers);
    String jsonMovie =
        new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(movie);
    UUID movieId = movie.getId();
    // when
    ResponseEntity<String> response =
        testRestTemplate.exchange(
            HOST + CINEMA_MANAGEMENT + MOVIES + "/" + movieId,
            HttpMethod.GET,
            entity,
            String.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(jsonMovie);
  }

  @Test
  void getNotExistingMovie() {
    // given
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    HttpEntity<String> entity = new HttpEntity<>(headers);
    String movieId = UUID.randomUUID().toString();
    String responseBody = "{\"message\":\"Movie not found!\"}";
    // when
    ResponseEntity<String> response =
        testRestTemplate.exchange(
            HOST + CINEMA_MANAGEMENT + MOVIES + "/" + movieId,
            HttpMethod.GET,
            entity,
            String.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isEqualTo(responseBody);
  }

  @Test
  void addMovieSuccessful() {
    // given
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    headers.setContentType(MediaType.APPLICATION_JSON);
    String rawJson = "{\"title\": \"title\", \"duration\": 112}";
    HttpEntity<String> entity = new HttpEntity<>(rawJson, headers);
    // when
    ResponseEntity<String> response =
        testRestTemplate.postForEntity(HOST + CINEMA_MANAGEMENT + MOVIES, entity, String.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).contains("\"title\":\"title\"");
    assertThat(response.getBody()).contains("\"duration\":112");
  }

  @Test
  void updateExistingMovie() {
    // given
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    headers.setContentType(MediaType.APPLICATION_JSON);
    String rawJson = "{\"id\": \"test-id\", \"title\": \"title\", \"duration\": 112}";
    HttpEntity<String> entity = new HttpEntity<>(rawJson, headers);
    String responseBody = "{\"message\":\"Movie updated successfully!\"}";
    // when
    ResponseEntity<String> response =
        testRestTemplate.exchange(
            HOST + CINEMA_MANAGEMENT + MOVIES + "/" + movie.getId(),
            HttpMethod.PUT,
            entity,
            String.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(responseBody);
  }

  @Test
  void updateNotExistingMovie() {
    // given
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    headers.setContentType(MediaType.APPLICATION_JSON);
    String movieId = UUID.randomUUID().toString();
    String rawJson = "{\"id\": \"" + movieId + "\", \"title\": \"title\", \"duration\": 112}";
    HttpEntity<String> entity = new HttpEntity<>(rawJson, headers);
    String responseBody = "{\"message\":\"Movie not found!\"}";
    // when
    ResponseEntity<String> response =
        testRestTemplate.exchange(
            HOST + CINEMA_MANAGEMENT + MOVIES + "/" + movieId,
            HttpMethod.DELETE,
            entity,
            String.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isEqualTo(responseBody);
  }

  @Test
  void deleteExistingMovie() {
    // given
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    HttpEntity<String> entity = new HttpEntity<>(headers);
    String responseBody = "{\"message\":\"Movie deleted successfully!\"}";
    // when
    ResponseEntity<String> response =
        testRestTemplate.exchange(
            HOST + CINEMA_MANAGEMENT + MOVIES + "/" + movie.getId(),
            HttpMethod.DELETE,
            entity,
            String.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(responseBody);
  }

  @Test
  void deleteNotExistingMovie() {
    // given
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    HttpEntity<String> entity = new HttpEntity<>(headers);
    String movieId = UUID.randomUUID().toString();
    String responseBody = "{\"message\":\"Movie not found!\"}";
    // when
    ResponseEntity<String> response =
        testRestTemplate.exchange(
            HOST + CINEMA_MANAGEMENT + MOVIES + "/" + movieId,
            HttpMethod.DELETE,
            entity,
            String.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isEqualTo(responseBody);
  }
}
