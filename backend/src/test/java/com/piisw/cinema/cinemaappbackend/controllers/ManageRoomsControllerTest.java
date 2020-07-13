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

import java.util.HashMap;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import com.piisw.cinema.cinemaappbackend.CinemaAppBackendApplication;
import com.piisw.cinema.cinemaappbackend.models.Room;
import com.piisw.cinema.cinemaappbackend.repository.RoomsRepository;

@SpringBootTest(
    classes = CinemaAppBackendApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ManageRoomsControllerTest {

  @Autowired private TestRestTemplate testRestTemplate;

  @Autowired private RoomsRepository roomsRepository;

  private String accessToken;

  private Room room;

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

    // remove all managed tickets
    roomsRepository.deleteAll();
    // add tickets
    room = new Room(55);
    roomsRepository.save(room);
    Room room2 = new Room(45);
    roomsRepository.save(room2);
  }

  @Test
  void getAllRooms() {
    // given
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    HttpEntity<String> entity = new HttpEntity<>(headers);
    String room1 = "\"seats\":55";
    String room2 = "\"seats\":45";
    // when
    ResponseEntity<String> response =
        testRestTemplate.exchange(
            HOST + CINEMA_MANAGEMENT + ROOMS, HttpMethod.GET, entity, String.class);
    // then
    assertThat(response.getBody()).contains(room1);
    assertThat(response.getBody()).contains(room2);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void getExistingRoom() {
    // given
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    HttpEntity<String> entity = new HttpEntity<>(headers);
    Integer roomId = room.getId();
    // when
    ResponseEntity<String> response =
        testRestTemplate.exchange(
            HOST + CINEMA_MANAGEMENT + ROOMS + "/" + roomId, HttpMethod.GET, entity, String.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).contains("\"seats\":55");
  }

  @Test
  void getNotExistingRoom() {
    // given
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    HttpEntity<String> entity = new HttpEntity<>(headers);
    int roomId = -1;
    String responseBody = "{\"message\":\"Room not found!\"}";
    // when
    ResponseEntity<String> response =
        testRestTemplate.exchange(
            HOST + CINEMA_MANAGEMENT + ROOMS + "/" + roomId, HttpMethod.GET, entity, String.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isEqualTo(responseBody);
  }

  @Test
  void addRoomSuccessful() {
    // given
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    headers.setContentType(MediaType.APPLICATION_JSON);
    String rawJson = "{\"seats\": 50}";
    HttpEntity<String> entity = new HttpEntity<>(rawJson, headers);
    String responseBody = "{\"message\":\"Room saved successfully!\"}";
    // when
    ResponseEntity<String> response =
        testRestTemplate.postForEntity(HOST + CINEMA_MANAGEMENT + ROOMS, entity, String.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(responseBody);
  }

  @Test
  void updateExistingRoom() {
    // given
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    headers.setContentType(MediaType.APPLICATION_JSON);
    Integer roomId = room.getId();
    String rawJson = "{\"seats\": 69}";
    HttpEntity<String> entity = new HttpEntity<>(rawJson, headers);
    String responseBody = "{\"message\":\"Room updated successfully!\"}";
    // when
    ResponseEntity<String> response =
        testRestTemplate.exchange(
            HOST + CINEMA_MANAGEMENT + ROOMS + "/" + roomId, HttpMethod.PUT, entity, String.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(responseBody);
  }

  @Test
  void updateNotExistingRoom() {
    // given
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    headers.setContentType(MediaType.APPLICATION_JSON);
    String rawJson = "{\"seats\": 69}";
    HttpEntity<String> entity = new HttpEntity<>(rawJson, headers);
    int roomId = -1;
    String responseBody = "{\"message\":\"Room not found!\"}";
    // when
    ResponseEntity<String> response =
        testRestTemplate.exchange(
            HOST + CINEMA_MANAGEMENT + ROOMS + "/" + roomId,
            HttpMethod.DELETE,
            entity,
            String.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isEqualTo(responseBody);
  }

  @Test
  void deleteExistingRoom() {
    // given
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    HttpEntity<String> entity = new HttpEntity<>(headers);
    Integer roomId = room.getId();
    String responseBody = "{\"message\":\"Room deleted successfully!\"}";
    // when
    ResponseEntity<String> response =
        testRestTemplate.exchange(
            HOST + "/api/cinema-management/rooms/" + roomId,
            HttpMethod.DELETE,
            entity,
            String.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(responseBody);
  }

  @Test
  void deleteNotExistingRoom() {
    // given
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    HttpEntity<String> entity = new HttpEntity<>(headers);
    int roomId = -1;
    String responseBody = "{\"message\":\"Room not found!\"}";
    // when
    ResponseEntity<String> response =
        testRestTemplate.exchange(
            HOST + "/api/cinema-management/rooms/" + roomId,
            HttpMethod.DELETE,
            entity,
            String.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isEqualTo(responseBody);
  }
}
