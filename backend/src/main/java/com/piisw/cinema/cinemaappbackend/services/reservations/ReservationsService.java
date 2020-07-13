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

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.piisw.cinema.cinemaappbackend.exceptions.ScreeningIsOutdatedException;
import com.piisw.cinema.cinemaappbackend.exceptions.ScreeningNotFoundException;
import com.piisw.cinema.cinemaappbackend.exceptions.SeatIsNotFreeException;
import com.piisw.cinema.cinemaappbackend.exceptions.TicketNotFoundException;
import com.piisw.cinema.cinemaappbackend.models.*;
import com.piisw.cinema.cinemaappbackend.payload.dtos.ScreeningDetailedDTO;
import com.piisw.cinema.cinemaappbackend.payload.dtos.TicketKindDTO;
import com.piisw.cinema.cinemaappbackend.payload.request.reservations.ReservationRequest;
import com.piisw.cinema.cinemaappbackend.payload.response.MovieResponse;
import com.piisw.cinema.cinemaappbackend.payload.response.reservations.ReservationResponse;
import com.piisw.cinema.cinemaappbackend.payload.response.reservations.ScreeningSeatResponse;
import com.piisw.cinema.cinemaappbackend.payload.response.reservations.TicketResponse;
import com.piisw.cinema.cinemaappbackend.repository.ScreeningsRepository;
import com.piisw.cinema.cinemaappbackend.repository.SeatRepository;
import com.piisw.cinema.cinemaappbackend.repository.TicketsRepository;
import com.piisw.cinema.cinemaappbackend.repository.cinemamanagement.ManagedTicketsRepository;
import com.piisw.cinema.cinemaappbackend.services.utils.MappingUtils;

@Service
public class ReservationsService {

  private final ScreeningsRepository screeningsRepository;
  private final TicketsRepository ticketsRepository;
  private final ManagedTicketsRepository managedTicketsRepository;
  private final SeatRepository seatRepository;
  private final MappingUtils mappingUtils;

  public ReservationsService(
      ScreeningsRepository screeningsRepository,
      TicketsRepository ticketsRepository,
      ManagedTicketsRepository managedTicketsRepository,
      SeatRepository seatRepository,
      MappingUtils mappingUtils) {

    this.screeningsRepository = screeningsRepository;
    this.ticketsRepository = ticketsRepository;
    this.managedTicketsRepository = managedTicketsRepository;
    this.seatRepository = seatRepository;
    this.mappingUtils = mappingUtils;
  }

  public List<MovieResponse> getAllMovieScreeningsForDate(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);

    List<Screening> screeningsForDate =
        screeningsRepository.findAllByYearAndMonthAndDay(
            cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));

    Set<MovieResponse> movies =
        screeningsForDate.stream()
            .map(mappingUtils::screeningToMovieResponse)
            .collect(Collectors.toSet());

    movies.forEach(
        movieResponse ->
            movieResponse.setScreenings(
                mappingUtils.listOfScreeningToListOfSimpleScreeningDTO(
                    screeningsForDate.stream()
                        .filter(
                            screening ->
                                screening.getMovie().getId().equals(movieResponse.getMovieId()))
                        .sorted(Comparator.comparing(Screening::getStartDate))
                        .collect(Collectors.toList()))));

    return new ArrayList<>(movies);
  }

  public ScreeningDetailedDTO getScreeningDetailed(Integer id) {
    Screening screening =
        screeningsRepository.findById(id).orElseThrow(() -> new ScreeningNotFoundException(id));
    ScreeningDetailedDTO dto = mappingUtils.screeningToScreeningDetailedDTO(screening);
    Integer freeSeatsQuantity = screening.getRoom().getSeats();
    freeSeatsQuantity -=
        screening.getTickets().stream()
            .map(ticket -> ticket.getSelectedSeats().size())
            .mapToInt(Integer::intValue)
            .sum();
    dto.setFreeSeatsQuantity(freeSeatsQuantity);
    return dto;
  }

  public List<ScreeningSeatResponse> getScreeningSeats(Integer id) {
    Screening screening =
        screeningsRepository.findById(id).orElseThrow(() -> new ScreeningNotFoundException(id));

    List<String> selectedSeats = getSelectedSeats(screening.getTickets());

    List<ScreeningSeatResponse> screeningSeatResponses = new ArrayList<>();
    for (SeatsRow seatsRow : screening.getRoom().getSeatsRows()) {
      for (int i = 1; i <= seatsRow.getNumberOfSeats(); i++) {
        String symbol = seatsRow.getSymbol() + "-" + i;
        screeningSeatResponses.add(
            new ScreeningSeatResponse(symbol, !selectedSeats.contains(symbol)));
      }
    }

    return screeningSeatResponses;
  }

  public Optional<TicketResponse> addReservation(ReservationRequest reservationRequest) {
    Screening screening =
        screeningsRepository
            .findById(reservationRequest.getScreeningId())
            .orElseThrow(() -> new ScreeningNotFoundException(reservationRequest.getScreeningId()));

    if (screening.getStartDate().before(new Date()))
      throw new ScreeningIsOutdatedException(screening.getStartDate().toString());

    Ticket ticket = mappingUtils.reservationRequestToTicket(reservationRequest);
    List<String> alreadyReservedSeats =
        reservationRequest.getSelectedSeats().stream()
            .filter(
                s ->
                    seatRepository.existsBySeatNumberAndScreeningId(
                        s, reservationRequest.getScreeningId()))
            .collect(Collectors.toList());

    if (alreadyReservedSeats.size() > 0)
      throw new SeatIsNotFreeException(String.join(", ", alreadyReservedSeats));

    for (TicketKindDTO tr : reservationRequest.getTicketKinds())
      ticket.addTicketType(
          managedTicketsRepository.findByNameAndIsActive(tr.getTicketTypeName(), true),
          tr.getQuantity());

    ticketsRepository.save(ticket);

    return Optional.of(mappingUtils.ticketToTicketResponse(ticket));
  }

  public ReservationResponse getReservation(UUID uuid) {
    Ticket ticket =
        ticketsRepository.findById(uuid).orElseThrow(() -> new TicketNotFoundException(uuid));

    return mappingUtils.ticketToReservationResponse(ticket);
  }

  private List<String> getSelectedSeats(List<Ticket> tickets) {
    return tickets.stream()
        .map(
            ticket ->
                ticket.getSelectedSeats().stream()
                    .map(Seat::getSeatNumber)
                    .collect(Collectors.toList()))
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }
}
