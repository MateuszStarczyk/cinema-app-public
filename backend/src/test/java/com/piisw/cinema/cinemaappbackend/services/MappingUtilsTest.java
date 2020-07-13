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
package com.piisw.cinema.cinemaappbackend.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import com.piisw.cinema.cinemaappbackend.CinemaAppBackendApplication;
import com.piisw.cinema.cinemaappbackend.models.*;
import com.piisw.cinema.cinemaappbackend.models.cinemamanagement.ManagedTicket;
import com.piisw.cinema.cinemaappbackend.payload.dtos.MovieDTO;
import com.piisw.cinema.cinemaappbackend.payload.dtos.ScreeningDetailedDTO;
import com.piisw.cinema.cinemaappbackend.payload.dtos.SimpleScreeningDTO;
import com.piisw.cinema.cinemaappbackend.payload.dtos.TicketKindDTO;
import com.piisw.cinema.cinemaappbackend.payload.request.reservations.ReservationRequest;
import com.piisw.cinema.cinemaappbackend.payload.response.MovieResponse;
import com.piisw.cinema.cinemaappbackend.payload.response.TicketValidationResponse;
import com.piisw.cinema.cinemaappbackend.payload.response.reservations.ReservationResponse;
import com.piisw.cinema.cinemaappbackend.payload.response.reservations.TicketResponse;
import com.piisw.cinema.cinemaappbackend.services.utils.MappingUtils;

@SpringBootTest(classes = CinemaAppBackendApplication.class)
@WebAppConfiguration
@ContextConfiguration
public class MappingUtilsTest {

  private static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";

  @Autowired private MappingUtils mappingUtils;

  private Ticket ticket;
  private Room room;
  private Movie movie;
  private Screening screening;

  @BeforeEach
  void beforeEach() throws ParseException {
    ticket = new Ticket();
    ticket.setId(UUID.randomUUID());
    ticket.setSelectedSeats(Collections.singletonList(new Seat(1, "A-1", ticket)));

    User user = new User("test_name", "test_last", "test@test.com", "test_pass");
    ticket.setUser(user);
    ticket.setTicketTypes(
        Collections.singletonList(
            new TicketManagedTicket(ticket, new ManagedTicket(1, "test_ticket_name", 11.99), 2)));

    room = new Room();
    room.setId(1);
    room.setSeats(2);

    SeatsRow seatsRow = new SeatsRow();
    seatsRow.setRoom(room);
    seatsRow.setSymbol("A");
    seatsRow.setNumberOfSeats(2);

    room.setSeatsRows(Collections.singletonList(seatsRow));

    movie = new Movie();
    movie.setTitle("Test");
    movie.setId(UUID.randomUUID());
    movie.setDescription("test1");
    movie.setDuration(1);
    movie.setPosterFilename("test2");
    movie.setTrailerUrl("test3");

    screening = new Screening();
    screening.setId(1);
    screening.setRoom(room);
    screening.setMovie(movie);
    screening.setStartDate(parseToDate("23-05-2020 20:00:00"));
    screening.setEndDate(parseToDate("23-05-2020 20:01:00"));
    screening.setTickets(Collections.singletonList(ticket));

    ticket.setScreening(screening);
  }

  @Test
  void listOfScreeningToListOfSimpleScreeningDTO() throws ParseException {
    // given
    Screening screening2 = new Screening();
    screening2.setId(2);
    screening2.setRoom(room);
    screening2.setMovie(movie);
    screening2.setStartDate(parseToDate("24-05-2020 20:00:00"));
    screening2.setEndDate(parseToDate("24-05-2020 20:01:00"));
    screening2.setTickets(Collections.singletonList(ticket));
    List<Screening> screenings = Arrays.asList(screening, screening2);

    // when
    List<SimpleScreeningDTO> result =
        mappingUtils.listOfScreeningToListOfSimpleScreeningDTO(screenings);

    // then
    assertThat(result.get(0).getStartDate()).isEqualTo(screening.getStartDate());
    assertThat(result.get(0).getId()).isEqualTo(screening.getId());
    assertThat(result.get(1).getStartDate()).isEqualTo(screening2.getStartDate());
    assertThat(result.get(1).getId()).isEqualTo(screening2.getId());
  }

  @Test
  void screeningToSimpleScreeningDTO() {
    // when
    SimpleScreeningDTO result = mappingUtils.screeningToSimpleScreeningDTO(screening);

    // then
    assertThat(result.getStartDate()).isEqualTo(screening.getStartDate());
    assertThat(result.getId()).isEqualTo(screening.getId());
  }

  @Test
  void screeningToScreeningDetailedDTO() {
    // when
    ScreeningDetailedDTO result = mappingUtils.screeningToScreeningDetailedDTO(screening);

    // then
    assertThat(result.getStartDate()).isEqualTo(screening.getStartDate());
    assertThat(result.getId()).isEqualTo(screening.getId());
    assertThat(result.getMovieId()).isEqualTo(screening.getMovie().getId());
    assertThat(result.getMovieTitle()).isEqualTo(screening.getMovie().getTitle());
    assertThat(result.getMovieDuration()).isEqualTo(screening.getMovie().getDuration());
    assertThat(result.getRoomId()).isEqualTo(screening.getRoom().getId());
    assertThat(result.getFreeSeatsQuantity()).isNull();
  }

  @Test
  void screeningToMovieResponse() {
    // when
    MovieResponse result = mappingUtils.screeningToMovieResponse(screening);

    // then
    assertThat(result.getMovieId()).isEqualTo(screening.getMovie().getId());
    assertThat(result.getMovieTitle()).isEqualTo(screening.getMovie().getTitle());
    assertThat(result.getMovieDuration()).isEqualTo(screening.getMovie().getDuration());
    assertThat(result.getScreenings()).isNull();
  }

  @Test
  void ticketToReservationResponse() {
    // when
    ReservationResponse result = mappingUtils.ticketToReservationResponse(ticket);

    // then
    assertThat(result.getId()).isEqualTo(ticket.getId().toString());
    assertThat(result.getCustomerName()).isEqualTo(ticket.getUser().getName());
    assertThat(result.getUserId()).isEqualTo(ticket.getUser().getId());
    assertThat(result.getUserEmail()).isEqualTo(ticket.getUser().getEmail());

    assertThat(result.getScreeningId()).isEqualTo(ticket.getScreening().getId());
    assertThat(result.getScreeningMovieId())
        .isEqualTo(ticket.getScreening().getMovie().getId().toString());
    assertThat(result.getScreeningMovieTitle())
        .isEqualTo(ticket.getScreening().getMovie().getTitle());
    assertThat(result.getScreeningMovieDuration())
        .isEqualTo(ticket.getScreening().getMovie().getDuration());
    assertThat(result.getScreeningRoomId()).isEqualTo(ticket.getScreening().getRoom().getId());
    assertThat(result.getScreeningStartDate()).isEqualTo(ticket.getScreening().getStartDate());

    assertThat(result.getTicketTypes().get(0).getTicketTypeId())
        .isEqualTo(ticket.getTicketTypes().get(0).getTicketType().getId());
    assertThat(result.getTicketTypes().get(0).getTicketTypeName())
        .isEqualTo(ticket.getTicketTypes().get(0).getTicketType().getName());
    assertThat(result.getTicketTypes().get(0).getTicketTypePrice())
        .isEqualTo(ticket.getTicketTypes().get(0).getTicketType().getPrice());
    assertThat(result.getTicketTypes().get(0).getQuantity())
        .isEqualTo(ticket.getTicketTypes().get(0).getQuantity());
    assertThat(result.getSelectedSeats().get(0))
        .isEqualTo(ticket.getSelectedSeats().get(0).getSeatNumber());
  }

  @Test
  void ticketToTicketValidationResponse() {
    // when
    TicketValidationResponse result = mappingUtils.ticketToTicketValidationResponse(ticket);

    // then
    assertThat(result.getIsValid()).isNull();

    assertThat(result.getScreeningMovieId())
        .isEqualTo(ticket.getScreening().getMovie().getId().toString());
    assertThat(result.getScreeningMovieTitle())
        .isEqualTo(ticket.getScreening().getMovie().getTitle());
    assertThat(result.getScreeningMovieDuration())
        .isEqualTo(ticket.getScreening().getMovie().getDuration());
    assertThat(result.getScreeningRoomId()).isEqualTo(ticket.getScreening().getRoom().getId());
    assertThat(result.getScreeningStartDate()).isEqualTo(ticket.getScreening().getStartDate());

    assertThat(result.getTicketTypes().get(0).getTicketTypeId())
        .isEqualTo(ticket.getTicketTypes().get(0).getTicketType().getId());
    assertThat(result.getTicketTypes().get(0).getTicketTypeName())
        .isEqualTo(ticket.getTicketTypes().get(0).getTicketType().getName());
    assertThat(result.getTicketTypes().get(0).getTicketTypePrice())
        .isEqualTo(ticket.getTicketTypes().get(0).getTicketType().getPrice());
    assertThat(result.getTicketTypes().get(0).getQuantity())
        .isEqualTo(ticket.getTicketTypes().get(0).getQuantity());
    assertThat(result.getSelectedSeats().get(0))
        .isEqualTo(ticket.getSelectedSeats().get(0).getSeatNumber());
  }

  @Test
  void ticketToTicketResponse() {
    // when
    TicketResponse result = mappingUtils.ticketToTicketResponse(ticket);

    // then
    assertThat(result.getId()).isEqualTo(ticket.getId().toString());

    assertThat(result.getScreeningId()).isEqualTo(ticket.getScreening().getId());
    assertThat(result.getUserId()).isEqualTo(ticket.getUser().getId());

    assertThat(result.getTicketTypes().get(0).getTicketTypeId())
        .isEqualTo(ticket.getTicketTypes().get(0).getTicketType().getId());
    assertThat(result.getTicketTypes().get(0).getTicketTypeName())
        .isEqualTo(ticket.getTicketTypes().get(0).getTicketType().getName());
    assertThat(result.getTicketTypes().get(0).getTicketTypePrice())
        .isEqualTo(ticket.getTicketTypes().get(0).getTicketType().getPrice());
    assertThat(result.getTicketTypes().get(0).getQuantity())
        .isEqualTo(ticket.getTicketTypes().get(0).getQuantity());
    assertThat(result.getSelectedSeats().get(0))
        .isEqualTo(ticket.getSelectedSeats().get(0).getSeatNumber());
  }

  @Test
  void reservationRequestToTicket() {
    // given
    ReservationRequest reservationRequest = new ReservationRequest();
    reservationRequest.setScreeningId(1);
    reservationRequest.setSelectedSeats(Collections.singletonList("A-1"));
    reservationRequest.setTicketKinds(
        Collections.singletonList(new TicketKindDTO(1, "test_ticket_name", 11.99, 1)));
    reservationRequest.setUserId(1L);

    // when
    Ticket result = mappingUtils.reservationRequestToTicket(reservationRequest);

    // then
    assertThat(result.getId()).isNull();
    assertThat(result.getScreening().getId()).isEqualTo(reservationRequest.getScreeningId());
    assertThat(result.getScreening().getStartDate()).isNull();
    assertThat(result.getScreening().getEndDate()).isNull();
    assertThat(result.getScreening().getMovie()).isNull();
    assertThat(result.getScreening().getRoom()).isNull();
    assertThat(result.getScreening().getTickets()).isEmpty();

    assertThat(result.getTicketTypes()).isEmpty();

    assertThat(result.getSelectedSeats().get(0).getSeatNumber())
        .isEqualTo(reservationRequest.getSelectedSeats().get(0));
    assertThat(result.getSelectedSeats().get(0).getId()).isNull();
    assertThat(result.getSelectedSeats().get(0).getTicket()).isNotNull();
    assertThat(result.getSelectedSeats().get(0).getTicket()).isNotNull();
    assertThat(result.getSelectedSeats().get(0).getId()).isNull();
    assertThat(result.getSelectedSeats().get(0).getSeatNumber())
        .isEqualTo(reservationRequest.getSelectedSeats().get(0));

    assertThat(result.getUser().getId()).isEqualTo(reservationRequest.getUserId());
    assertThat(result.getUser().getEmail()).isNull();
    assertThat(result.getUser().getFirstName()).isNull();
    assertThat(result.getUser().getLastName()).isNull();
    assertThat(result.getUser().getPassword()).isNull();
    assertThat(result.getUser().getRoles()).isEmpty();
    assertThat(result.getUser().getTickets()).isEmpty();
  }

  @Test
  void ticketsToReservationResponses() {
    // when
    List<Ticket> tickets = Arrays.asList(ticket, ticket);
    List<ReservationResponse> result = mappingUtils.ticketsToReservationResponses(tickets);

    // then
    for (int i = 0; i < result.size(); i++) {
      assertThat(result.get(0).getId()).isEqualTo(tickets.get(i).getId().toString());
      assertThat(result.get(0).getCustomerName()).isEqualTo(tickets.get(i).getUser().getName());
      assertThat(result.get(0).getUserId()).isEqualTo(tickets.get(i).getUser().getId());
      assertThat(result.get(0).getUserEmail()).isEqualTo(tickets.get(i).getUser().getEmail());

      assertThat(result.get(0).getScreeningId()).isEqualTo(tickets.get(i).getScreening().getId());
      assertThat(result.get(0).getScreeningMovieId())
          .isEqualTo(tickets.get(i).getScreening().getMovie().getId().toString());
      assertThat(result.get(0).getScreeningMovieTitle())
          .isEqualTo(tickets.get(i).getScreening().getMovie().getTitle());
      assertThat(result.get(0).getScreeningMovieDuration())
          .isEqualTo(tickets.get(i).getScreening().getMovie().getDuration());
      assertThat(result.get(0).getScreeningRoomId())
          .isEqualTo(tickets.get(i).getScreening().getRoom().getId());
      assertThat(result.get(0).getScreeningStartDate())
          .isEqualTo(tickets.get(i).getScreening().getStartDate());

      assertThat(result.get(0).getTicketTypes().get(0).getTicketTypeId())
          .isEqualTo(tickets.get(i).getTicketTypes().get(0).getTicketType().getId());
      assertThat(result.get(0).getTicketTypes().get(0).getTicketTypeName())
          .isEqualTo(tickets.get(i).getTicketTypes().get(0).getTicketType().getName());
      assertThat(result.get(0).getTicketTypes().get(0).getTicketTypePrice())
          .isEqualTo(tickets.get(i).getTicketTypes().get(0).getTicketType().getPrice());
      assertThat(result.get(0).getTicketTypes().get(0).getQuantity())
          .isEqualTo(tickets.get(i).getTicketTypes().get(0).getQuantity());
      assertThat(result.get(0).getSelectedSeats().get(0))
          .isEqualTo(tickets.get(i).getSelectedSeats().get(0).getSeatNumber());
    }
  }

  @Test
  void movieToMovieDTO() {
    // when
    MovieDTO result = mappingUtils.movieToMovieDTO(movie);

    // then
    assertThat(result.getId()).isEqualTo(movie.getId().toString());
    assertThat(result.getTitle()).isEqualTo(movie.getTitle());
    assertThat(result.getDescription()).isEqualTo(movie.getDescription());
    assertThat(result.getDuration()).isEqualTo(movie.getDuration());
    assertThat(result.getPosterFilename()).isEqualTo(movie.getPosterFilename());
    assertThat(result.getTrailerUrl()).isEqualTo(movie.getTrailerUrl());
  }

  @Test
  void movieDTOToMovie() {
    // given
    MovieDTO movieDTO = new MovieDTO();
    movieDTO.setTitle("Test");
    movieDTO.setId(UUID.randomUUID().toString());
    movieDTO.setDescription("test1");
    movieDTO.setDuration(1);
    movieDTO.setPosterFilename("test2");
    movieDTO.setTrailerUrl("test3");

    // when
    Movie result = mappingUtils.movieDTOToMovie(movieDTO);

    // then
    assertThat(result.getId().toString()).isEqualTo(movieDTO.getId());
    assertThat(result.getTitle()).isEqualTo(movieDTO.getTitle());
    assertThat(result.getDescription()).isEqualTo(movieDTO.getDescription());
    assertThat(result.getDuration()).isEqualTo(movieDTO.getDuration());
    assertThat(result.getPosterFilename()).isEqualTo(movieDTO.getPosterFilename());
    assertThat(result.getTrailerUrl()).isEqualTo(movieDTO.getTrailerUrl());
  }

  @Test
  void screeningToMovieDTO() {
    // when
    MovieDTO result = mappingUtils.screeningToMovieDTO(screening);

    // then
    assertThat(result.getId()).isEqualTo(screening.getMovie().getId().toString());
    assertThat(result.getTitle()).isEqualTo(screening.getMovie().getTitle());
    assertThat(result.getDescription()).isEqualTo(screening.getMovie().getDescription());
    assertThat(result.getDuration()).isEqualTo(screening.getMovie().getDuration());
    assertThat(result.getPosterFilename()).isEqualTo(screening.getMovie().getPosterFilename());
    assertThat(result.getTrailerUrl()).isEqualTo(screening.getMovie().getTrailerUrl());
  }

  private Date parseToDate(String date) throws ParseException {
    SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
    return formatter.parse(date);
  }
}
