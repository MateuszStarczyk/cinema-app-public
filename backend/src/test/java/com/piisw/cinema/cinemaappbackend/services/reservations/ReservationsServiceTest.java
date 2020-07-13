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
package com.piisw.cinema.cinemaappbackend.services.reservations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.piisw.cinema.cinemaappbackend.exceptions.ScreeningIsOutdatedException;
import com.piisw.cinema.cinemaappbackend.exceptions.ScreeningNotFoundException;
import com.piisw.cinema.cinemaappbackend.exceptions.SeatIsNotFreeException;
import com.piisw.cinema.cinemaappbackend.exceptions.TicketNotFoundException;
import com.piisw.cinema.cinemaappbackend.models.*;
import com.piisw.cinema.cinemaappbackend.models.cinemamanagement.ManagedTicket;
import com.piisw.cinema.cinemaappbackend.payload.dtos.ScreeningDetailedDTO;
import com.piisw.cinema.cinemaappbackend.payload.dtos.SimpleScreeningDTO;
import com.piisw.cinema.cinemaappbackend.payload.dtos.TicketKindDTO;
import com.piisw.cinema.cinemaappbackend.payload.request.reservations.ReservationRequest;
import com.piisw.cinema.cinemaappbackend.payload.response.MovieResponse;
import com.piisw.cinema.cinemaappbackend.payload.response.reservations.ScreeningSeatResponse;
import com.piisw.cinema.cinemaappbackend.payload.response.reservations.TicketResponse;
import com.piisw.cinema.cinemaappbackend.repository.ScreeningsRepository;
import com.piisw.cinema.cinemaappbackend.repository.SeatRepository;
import com.piisw.cinema.cinemaappbackend.repository.TicketsRepository;
import com.piisw.cinema.cinemaappbackend.repository.cinemamanagement.ManagedTicketsRepository;
import com.piisw.cinema.cinemaappbackend.services.utils.MappingUtils;

@ExtendWith(MockitoExtension.class)
class ReservationsServiceTest {

  private static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";

  private static final UUID MOVIE_UUID = new UUID(1234, 5678);

  @Mock private MappingUtils mappingUtils;

  @Mock private ScreeningsRepository screeningsRepository;

  @Mock private TicketsRepository ticketsRepository;

  @Mock private SeatRepository seatRepository;

  @Mock private ManagedTicketsRepository managedTicketsRepository;

  @InjectMocks private ReservationsService reservationsService;

  private List<Screening> screenings;

  @BeforeEach
  void setUp() throws ParseException {
    Room room = new Room(10);
    room.setId(1);
    room.getSeatsRows().add(new SeatsRow("A", 5, room));
    room.getSeatsRows().add(new SeatsRow("B", 5, room));

    Movie movie = new Movie("Movie1", 120, "poster", "https://trailer.com", "description");
    movie.setId(MOVIE_UUID);

    Date sDate1 = parseToDate("23-05-2020 18:00:00");
    Date eDate1 = parseToDate("23-05-2020 20:00:00");
    Date sDate2 = parseToDate("23-05-2020 20:00:00");
    Date eDate2 = parseToDate("23-05-2020 22:00:00");

    Ticket ticket = new Ticket();
    ticket.getSelectedSeats().add(new Seat(1, "A-1", ticket));

    Screening screening1 = new Screening(sDate1, eDate1, room, movie);
    screening1.setId(1);
    screening1.getTickets().add(ticket);

    Screening screening2 = new Screening(sDate2, eDate2, room, movie);
    screening2.setId(2);

    screenings = Arrays.asList(screening1, screening2);
  }

  @Test
  void getAllMovieScreeningsForDate() throws ParseException {
    // given
    Date sDate1 = parseToDate("23-05-2020 18:00:00");
    Mockito.when(screeningsRepository.findAllByYearAndMonthAndDay(2020, 5, 23))
        .thenReturn(screenings);

    MovieResponse movieResponse = new MovieResponse();
    movieResponse.setMovieId(MOVIE_UUID);
    Mockito.when(mappingUtils.screeningToMovieResponse(Mockito.any())).thenReturn(movieResponse);

    SimpleScreeningDTO ssd1 = new SimpleScreeningDTO(1, parseToDate("23-05-2020 18:00:00"));
    SimpleScreeningDTO ssd2 = new SimpleScreeningDTO(1, parseToDate("23-05-2020 20:00:00"));
    Mockito.when(mappingUtils.listOfScreeningToListOfSimpleScreeningDTO(Mockito.anyList()))
        .thenReturn(Arrays.asList(ssd1, ssd2));

    // when
    List<MovieResponse> moviesWithScreenings =
        reservationsService.getAllMovieScreeningsForDate(sDate1);

    // then
    assertThat(moviesWithScreenings.size()).isEqualTo(1);
    assertThat(moviesWithScreenings.get(0).getMovieId()).isEqualTo(MOVIE_UUID);
    assertThat(moviesWithScreenings.get(0).getScreenings().size()).isEqualTo(2);
    assertThat(moviesWithScreenings.get(0).getScreenings().get(0).getStartDate())
        .isEqualTo(parseToDate("23-05-2020 18:00:00"));
    assertThat(moviesWithScreenings.get(0).getScreenings().get(1).getStartDate())
        .isEqualTo(parseToDate("23-05-2020 20:00:00"));
  }

  @Test
  void getScreeningDetailed() {
    // given
    Mockito.when(screeningsRepository.findById(1)).thenReturn(Optional.of(screenings.get(0)));
    ScreeningDetailedDTO dto = new ScreeningDetailedDTO();
    dto.setId(screenings.get(0).getId());
    dto.setRoomId(screenings.get(0).getRoom().getId());
    dto.setStartDate(screenings.get(0).getStartDate());
    dto.setMovieId(screenings.get(0).getMovie().getId());
    dto.setMovieTitle(screenings.get(0).getMovie().getTitle());
    dto.setMovieDuration(screenings.get(0).getMovie().getDuration());
    Mockito.when(mappingUtils.screeningToScreeningDetailedDTO(Mockito.any())).thenReturn(dto);

    // when
    ScreeningDetailedDTO result = reservationsService.getScreeningDetailed(1);

    // then
    assertThat(result.getStartDate()).isEqualTo(dto.getStartDate());
    assertThat(result.getId()).isEqualTo(dto.getId());
    assertThat(result.getMovieId()).isEqualTo(dto.getMovieId());
    assertThat(result.getMovieTitle()).isEqualTo(dto.getMovieTitle());
    assertThat(result.getMovieDuration()).isEqualTo(dto.getMovieDuration());
    assertThat(result.getRoomId()).isEqualTo(dto.getRoomId());
    assertThat(result.getFreeSeatsQuantity()).isEqualTo(9);
  }

  @Test
  void getNotExistingScreeningDetailed() {
    // given
    Mockito.when(screeningsRepository.findById(1)).thenReturn(Optional.empty());

    // when
    Exception exception =
        assertThrows(
            ScreeningNotFoundException.class, () -> reservationsService.getScreeningDetailed(1));

    // then
    assertThat(exception.getMessage()).isEqualTo("Screening with id 1 not found!");
  }

  @Test
  void getScreeningSeatsForExistingScreening() {
    // given
    Mockito.when(screeningsRepository.findById(1)).thenReturn(Optional.of(screenings.get(0)));

    // when
    List<ScreeningSeatResponse> screeningSeatResponses = reservationsService.getScreeningSeats(1);

    // then
    assertThat(screeningSeatResponses.size()).isEqualTo(10);
    assertThat(screeningSeatResponses.get(0).getSeatNumber()).isEqualTo("A-1");
    assertThat(screeningSeatResponses.get(0).isAvailable()).isFalse();
    assertThat(screeningSeatResponses.get(1).getSeatNumber()).isEqualTo("A-2");
    assertThat(screeningSeatResponses.get(1).isAvailable()).isTrue();
  }

  @Test()
  void getScreeningSeatsForNotExistingScreening() {
    // given
    Mockito.when(screeningsRepository.findById(1)).thenReturn(Optional.empty());

    // when
    Exception exception =
        assertThrows(
            ScreeningNotFoundException.class, () -> reservationsService.getScreeningSeats(1));

    // then
    assertThat(exception.getMessage()).isEqualTo("Screening with id 1 not found!");
  }

  @Test
  void addReservationForNotExistingScreening() {
    // given
    ReservationRequest request = new ReservationRequest();
    request.setScreeningId(1);
    Mockito.when(screeningsRepository.findById(1)).thenReturn(Optional.empty());

    // when
    Exception exception =
        assertThrows(
            ScreeningNotFoundException.class, () -> reservationsService.addReservation(request));

    // then
    assertThat(exception.getMessage()).isEqualTo("Screening with id 1 not found!");
  }

  @Test
  void addReservationForOutdatedScreening() {
    // given
    ReservationRequest request = new ReservationRequest();
    request.setScreeningId(1);
    Mockito.when(screeningsRepository.findById(1)).thenReturn(Optional.of(screenings.get(0)));

    // when
    Exception exception =
        assertThrows(
            ScreeningIsOutdatedException.class, () -> reservationsService.addReservation(request));

    // then
    assertThat(exception.getMessage())
        .contains("This screening has already taken place - start date: Sat May 23 18:00:00");
  }

  @Test
  void addReservationForReservedSeat() {
    // given
    ReservationRequest request = new ReservationRequest();
    request.setScreeningId(1);
    request.getSelectedSeats().add("A1");

    Screening screening = new Screening();
    screening.setStartDate(new Date(new Date().getTime() + (1000 * 60 * 60))); // + 1 hour

    Mockito.when(screeningsRepository.findById(1)).thenReturn(Optional.of(screening));
    Mockito.when(mappingUtils.reservationRequestToTicket(Mockito.any())).thenReturn(new Ticket());
    Mockito.when(seatRepository.existsBySeatNumberAndScreeningId("A1", 1)).thenReturn(true);

    // when
    Exception exception =
        assertThrows(
            SeatIsNotFreeException.class, () -> reservationsService.addReservation(request));

    // then
    assertThat(exception.getMessage()).isEqualTo("These places have already been reserved: A1");
  }

  @Test
  void addReservationSuccessfully() {
    // given
    ReservationRequest request = new ReservationRequest();
    request.setScreeningId(1);
    request.getSelectedSeats().add("A1");
    request.getTicketKinds().add(new TicketKindDTO(1, "NORMAL", 15.99, 1));

    Screening screening = new Screening();
    screening.setStartDate(new Date(new Date().getTime() + (1000 * 60 * 60))); // + 1 hour

    Mockito.when(screeningsRepository.findById(1)).thenReturn(Optional.of(screening));
    Mockito.when(mappingUtils.reservationRequestToTicket(Mockito.any())).thenReturn(new Ticket());
    Mockito.when(seatRepository.existsBySeatNumberAndScreeningId("A1", 1)).thenReturn(false);
    Mockito.when(managedTicketsRepository.findByNameAndIsActive("NORMAL", true))
        .thenReturn(new ManagedTicket());
    Mockito.when(mappingUtils.ticketToTicketResponse(Mockito.any()))
        .thenReturn(new TicketResponse());

    // when
    Optional<TicketResponse> ticketResponse = reservationsService.addReservation(request);

    // then
    assertThat(ticketResponse.isPresent()).isTrue();
  }

  @Test
  void getNotExistingReservation() {
    // given
    Mockito.when(ticketsRepository.findById(new UUID(12, 34))).thenReturn(Optional.empty());

    // when
    Exception exception =
        assertThrows(
            TicketNotFoundException.class,
            () -> reservationsService.getReservation(new UUID(12, 34)));

    // then
    assertThat(exception.getMessage())
        .isEqualTo("Ticket with UUID 00000000-0000-000c-0000-000000000022 not found!");
  }

  private Date parseToDate(String date) throws ParseException {
    SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
    return formatter.parse(date);
  }
}
