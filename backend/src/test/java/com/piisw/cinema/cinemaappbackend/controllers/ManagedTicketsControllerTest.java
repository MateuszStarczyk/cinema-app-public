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

import com.google.gson.GsonBuilder;
import com.piisw.cinema.cinemaappbackend.CinemaAppBackendApplication;
import com.piisw.cinema.cinemaappbackend.models.cinemamanagement.ManagedTicket;
import com.piisw.cinema.cinemaappbackend.repository.cinemamanagement.ManagedTicketsRepository;

@SpringBootTest(
    classes = CinemaAppBackendApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ManagedTicketsControllerTest {

  @Autowired private TestRestTemplate testRestTemplate;

  @Autowired private ManagedTicketsRepository managedTicketsRepository;

  private String accessToken;

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
    managedTicketsRepository.deleteAll();
    // add tickets
    ManagedTicket managedTicket = new ManagedTicket("test", 14.99);
    managedTicketsRepository.save(managedTicket);
    managedTicket = new ManagedTicket("test2", 12.99);
    managedTicketsRepository.save(managedTicket);
    managedTicket = new ManagedTicket("test3", 15.99);
    managedTicketsRepository.save(managedTicket);
  }

  @Test
  void getAllManagedTickets() {
    // given
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    HttpEntity<String> entity = new HttpEntity<>(headers);
    String jsonManagedTickets =
        new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
            .toJson(managedTicketsRepository.findAll());
    // when
    ResponseEntity<String> response =
        testRestTemplate.exchange(
            HOST + CINEMA_MANAGEMENT + MANAGED_TICKETS, HttpMethod.GET, entity, String.class);
    // then
    assertThat(response.getBody()).isEqualTo(jsonManagedTickets);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void getExistingManagedTicket() {
    // given
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    HttpEntity<String> entity = new HttpEntity<>(headers);
    ManagedTicket managedTicket = managedTicketsRepository.findByName("test").get();
    String jsonManagedTicket =
        new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(managedTicket);
    Integer managedTicketId = managedTicket.getId();
    // when
    ResponseEntity<String> response =
        testRestTemplate.exchange(
            HOST + CINEMA_MANAGEMENT + MANAGED_TICKETS + "/" + managedTicketId,
            HttpMethod.GET,
            entity,
            String.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(jsonManagedTicket);
  }

  @Test
  void getNotExistingManagedTicket() {
    // given
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    HttpEntity<String> entity = new HttpEntity<>(headers);
    int managedTicketId = -1;
    String responseBody = "{\"message\":\"Managed ticket not found!\"}";
    // when
    ResponseEntity<String> response =
        testRestTemplate.exchange(
            HOST + CINEMA_MANAGEMENT + MANAGED_TICKETS + "/" + managedTicketId,
            HttpMethod.GET,
            entity,
            String.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isEqualTo(responseBody);
  }

  @Test
  void addManagedTicketSuccessful() {
    // given
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    headers.setContentType(MediaType.APPLICATION_JSON);
    String rawJson = "{\"name\": \"Senior\", \"price\": 14.99}";
    HttpEntity<String> entity = new HttpEntity<>(rawJson, headers);
    String responseBody = "{\"message\":\"Managed ticket saved successfully!\"}";
    // when
    ResponseEntity<String> response =
        testRestTemplate.postForEntity(
            HOST + CINEMA_MANAGEMENT + MANAGED_TICKETS, entity, String.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(responseBody);
  }

  @Test
  void addManagedTicketUnsuccessful() {
    // given
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    headers.setContentType(MediaType.APPLICATION_JSON);
    String rawJson = "{\"name\": \"test\", \"price\": 14.99}";
    HttpEntity<String> entity = new HttpEntity<>(rawJson, headers);
    String responseBody = "{\"message\":\"Name is already in use!\"}";
    // when
    ResponseEntity<String> response =
        testRestTemplate.postForEntity(
            HOST + CINEMA_MANAGEMENT + MANAGED_TICKETS, entity, String.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    assertThat(response.getBody()).isEqualTo(responseBody);
  }

  @Test
  void updateExistingManagedTicket() {
    // given
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    headers.setContentType(MediaType.APPLICATION_JSON);
    Integer managedTicketId = managedTicketsRepository.findByName("test3").get().getId();
    String rawJson = "{\"id\": " + managedTicketId + ", \"name\": \"NIE ULGOWY\", \"price\": 9.99}";
    HttpEntity<String> entity = new HttpEntity<>(rawJson, headers);
    String responseBody = "{\"message\":\"Managed ticket updated successfully!\"}";
    // when
    ResponseEntity<String> response =
        testRestTemplate.exchange(
            HOST + CINEMA_MANAGEMENT + MANAGED_TICKETS + "/" + managedTicketId,
            HttpMethod.PUT,
            entity,
            String.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(responseBody);
  }

  @Test
  void updateNotExistingManagedTicket() {
    // given
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    headers.setContentType(MediaType.APPLICATION_JSON);
    String rawJson = "{\"id\": \"100\", \"name\": \"NIEPOPRAWNY\", \"price\": 999.99}";
    HttpEntity<String> entity = new HttpEntity<>(rawJson, headers);
    int managedTicketId = 100;
    String responseBody = "{\"message\":\"Managed ticket not found!\"}";
    // when
    ResponseEntity<String> response =
        testRestTemplate.exchange(
            HOST + CINEMA_MANAGEMENT + MANAGED_TICKETS + "/" + managedTicketId,
            HttpMethod.DELETE,
            entity,
            String.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isEqualTo(responseBody);
  }

  @Test
  void deleteExistingManagedTicket() {
    // given
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    HttpEntity<String> entity = new HttpEntity<>(headers);
    Integer managedTicketId = managedTicketsRepository.findByName("test").get().getId();
    String responseBody = "{\"message\":\"Managed ticket deleted successfully!\"}";
    // when
    ResponseEntity<String> response =
        testRestTemplate.exchange(
            HOST + CINEMA_MANAGEMENT + MANAGED_TICKETS + "/" + managedTicketId,
            HttpMethod.DELETE,
            entity,
            String.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(responseBody);
  }

  @Test
  void deleteNotExistingManagedTicket() {
    // given
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    HttpEntity<String> entity = new HttpEntity<>(headers);
    int managedTicketId = -1;
    String responseBody = "{\"message\":\"Managed ticket not found!\"}";
    // when
    ResponseEntity<String> response =
        testRestTemplate.exchange(
            HOST + CINEMA_MANAGEMENT + MANAGED_TICKETS + "/" + managedTicketId,
            HttpMethod.DELETE,
            entity,
            String.class);
    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isEqualTo(responseBody);
  }
}
