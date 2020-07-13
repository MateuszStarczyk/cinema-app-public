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

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import com.google.gson.Gson;
import com.piisw.cinema.cinemaappbackend.CinemaAppBackendApplication;
import com.piisw.cinema.cinemaappbackend.models.Movie;
import com.piisw.cinema.cinemaappbackend.models.Room;
import com.piisw.cinema.cinemaappbackend.models.Screening;
import com.piisw.cinema.cinemaappbackend.payload.response.ScreeningResponse;
import com.piisw.cinema.cinemaappbackend.repository.MoviesRepository;
import com.piisw.cinema.cinemaappbackend.repository.RoomsRepository;
import com.piisw.cinema.cinemaappbackend.repository.ScreeningsRepository;

@SpringBootTest(
    classes = CinemaAppBackendApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ManageScreeningsControllerTest {

  @Autowired private TestRestTemplate testRestTemplate;

  @Autowired private ScreeningsRepository screeningsRepository;

  @Autowired private MoviesRepository moviesRepository;

  @Autowired private RoomsRepository roomsRepository;

  private String accessToken;

  private Screening screening1, screening2;

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

    // remove all screenings
    screeningsRepository.deleteAll();
    // remove all movies
    moviesRepository.deleteAll();
    // add movies
    movie = new Movie("test", 145, "path", "url", "desc");
    moviesRepository.save(movie);
    // add rooms
    Room room = new Room(50);
    roomsRepository.save(room);
    screening1 =
        new Screening(
            Date.from(Instant.parse("2020-05-13T20:20:00.000Z")),
            Date.from(Instant.parse("2020-05-13T22:45:00.000Z")),
            room,
            movie);
    screeningsRepository.save(screening1);
    screening2 =
        new Screening(
            Date.from(Instant.parse("2020-05-14T20:20:00.000Z")),
            Date.from(Instant.parse("2020-05-14T22:20:00.000Z")),
            room,
            movie);
    screeningsRepository.save(screening2);
  }

  @Test
  void getAllScreenings() {
    // given
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    HttpEntity<String> entity = new HttpEntity<>(headers);
    List<ScreeningResponse> screeningResponses = new ArrayList<>();
    List<Screening> screenings = screeningsRepository.findAll();
    screenings.forEach(
        s ->
            screeningResponses.add(
                new ScreeningResponse(
                    s.getId(),
                    s.getStartDate().toString(),
                    s.getRoom().getId(),
                    s.getMovie().getId())));

    String jsonScreenings = new Gson().toJson(screeningResponses);
    // when
    ResponseEntity<String> response =
        testRestTemplate.exchange(
            HOST + CINEMA_MANAGEMENT + SCREENINGS, HttpMethod.GET, entity, String.class);
    // then
    assertThat(response.getBody()).isEqualTo(jsonScreenings);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void getAllScreeningsForMovie() {
    // given
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    HttpEntity<String> entity = new HttpEntity<>(headers);
    List<ScreeningResponse> screeningResponses = new ArrayList<>();
    List<Screening> screenings = screeningsRepository.findAllByMovieId(movie.getId());
    screenings.forEach(
        s ->
            screeningResponses.add(
                new ScreeningResponse(
                    s.getId(),
                    s.getStartDate().toString(),
                    s.getRoom().getId(),
                    s.getMovie().getId())));

    String jsonScreenings = new Gson().toJson(screeningResponses);
    // when
    ResponseEntity<String> response =
        testRestTemplate.exchange(
            HOST + CINEMA_MANAGEMENT + MOVIES + "/" + movie.getId() + SCREENINGS,
            HttpMethod.GET,
            entity,
            String.class);
    // then
    assertThat(response.getBody()).isEqualTo(jsonScreenings);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void getAllScreeningsForMovieNotExists() {
    // given
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    HttpEntity<String> entity = new HttpEntity<>(headers);
    String responseJSON = "{\"message\":\"Movie not found!\"}";
    // when
    ResponseEntity<String> response =
        testRestTemplate.exchange(
            HOST + CINEMA_MANAGEMENT + MOVIES + "/" + UUID.randomUUID() + SCREENINGS,
            HttpMethod.GET,
            entity,
            String.class);
    // then
    assertThat(response.getBody()).isEqualTo(responseJSON);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void getExistingScreening() {
    // given
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    HttpEntity<String> entity = new HttpEntity<>(headers);
    ScreeningResponse screeningResponse =
        new ScreeningResponse(
            screening1.getId(),
            screening1.getStartDate().toInstant().truncatedTo(ChronoUnit.SECONDS).toString(),
            screening1.getRoom().getId(),
            screening1.getMovie().getId());
    String jsonScreening = new Gson().toJson(screeningResponse);
    Integer screeningId = screening1.getId();
    // when
    ResponseEntity<String> response =
        testRestTemplate.exchange(
            HOST + CINEMA_MANAGEMENT + SCREENINGS + "/" + screeningId,
            HttpMethod.GET,
            entity,
            String.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(jsonScreening);
  }

  @Test
  void getNotExistingScreening() {
    // given
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    HttpEntity<String> entity = new HttpEntity<>(headers);
    int screeningId = -1;
    String responseBody = "{\"message\":\"Screening not found!\"}";
    // when
    ResponseEntity<String> response =
        testRestTemplate.exchange(
            HOST + CINEMA_MANAGEMENT + SCREENINGS + "/" + screeningId,
            HttpMethod.GET,
            entity,
            String.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isEqualTo(responseBody);
  }

  @Test
  void addScreeningSuccessful() {
    // given
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    headers.setContentType(MediaType.APPLICATION_JSON);
    String rawJson =
        "{\"startDate\":\"2020-05-13T17:54:00.000Z\",\"roomId\":"
            + screening1.getRoom().getId()
            + ",\"movieId\": \""
            + movie.getId()
            + "\"}";
    HttpEntity<String> entity = new HttpEntity<>(rawJson, headers);
    String responseBody = "{\"message\":\"Screening saved successfully!\"}";
    // when
    ResponseEntity<String> response =
        testRestTemplate.postForEntity(HOST + CINEMA_MANAGEMENT + SCREENINGS, entity, String.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(responseBody);
  }

  @Test
  void addScreeningUnsuccessful() {
    // given
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    headers.setContentType(MediaType.APPLICATION_JSON);
    String rawJson =
        "{\"startDate\":\"2020-05-13T21:20:00.000Z\",\"roomId\":"
            + screening1.getRoom().getId()
            + ",\"movieId\": \""
            + movie.getId()
            + "\"}";
    HttpEntity<String> entity = new HttpEntity<>(rawJson, headers);
    String responseBody =
        "{\"message\":\"Another screening is being broadcast during these dates!\"}";
    // when
    ResponseEntity<String> response =
        testRestTemplate.postForEntity(HOST + CINEMA_MANAGEMENT + SCREENINGS, entity, String.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    assertThat(response.getBody()).isEqualTo(responseBody);
  }

  @Test
  void deleteExistingScreening() {
    // given
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    HttpEntity<String> entity = new HttpEntity<>(headers);
    Integer screeningId = screening1.getId();
    String responseBody = "{\"message\":\"Screening deleted successfully!\"}";
    // when
    ResponseEntity<String> response =
        testRestTemplate.exchange(
            HOST + CINEMA_MANAGEMENT + SCREENINGS + "/" + screeningId,
            HttpMethod.DELETE,
            entity,
            String.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(responseBody);
  }

  @Test
  void deleteNotExistingScreening() {
    // given
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    HttpEntity<String> entity = new HttpEntity<>(headers);
    int screeningId = -1;
    String responseBody = "{\"message\":\"Screening not found!\"}";
    // when
    ResponseEntity<String> response =
        testRestTemplate.exchange(
            HOST + CINEMA_MANAGEMENT + SCREENINGS + "/" + screeningId,
            HttpMethod.DELETE,
            entity,
            String.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isEqualTo(responseBody);
  }
}
