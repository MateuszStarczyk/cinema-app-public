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
package com.piisw.cinema.cinemaappbackend.services.utils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.piisw.cinema.cinemaappbackend.models.*;
import com.piisw.cinema.cinemaappbackend.payload.dtos.MovieDTO;
import com.piisw.cinema.cinemaappbackend.payload.dtos.ScreeningDetailedDTO;
import com.piisw.cinema.cinemaappbackend.payload.dtos.SimpleScreeningDTO;
import com.piisw.cinema.cinemaappbackend.payload.request.reservations.ReservationRequest;
import com.piisw.cinema.cinemaappbackend.payload.response.MovieResponse;
import com.piisw.cinema.cinemaappbackend.payload.response.TicketValidationResponse;
import com.piisw.cinema.cinemaappbackend.payload.response.reservations.ReservationResponse;
import com.piisw.cinema.cinemaappbackend.payload.response.reservations.TicketResponse;

@Service
public class MappingUtils {

  private final ModelMapper modelMapper;

  public MappingUtils(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  public List<SimpleScreeningDTO> listOfScreeningToListOfSimpleScreeningDTO(
      List<Screening> screenings) {
    return screenings.stream()
        .map(this::screeningToSimpleScreeningDTO)
        .collect(Collectors.toList());
  }

  public SimpleScreeningDTO screeningToSimpleScreeningDTO(Screening screening) {
    return modelMapper.map(screening, SimpleScreeningDTO.class);
  }

  public ScreeningDetailedDTO screeningToScreeningDetailedDTO(Screening screening) {
    return modelMapper.map(screening, ScreeningDetailedDTO.class);
  }

  public MovieResponse screeningToMovieResponse(Screening screening) {
    return modelMapper.map(screening, MovieResponse.class);
  }

  public ReservationResponse ticketToReservationResponse(Ticket ticket) {
    ReservationResponse response = modelMapper.map(ticket, ReservationResponse.class);
    response.setCustomerName(ticket.getUser().getName());
    response.setSelectedSeats(
        ticket.getSelectedSeats().stream().map(Seat::getSeatNumber).collect(Collectors.toList()));
    return response;
  }

  public TicketValidationResponse ticketToTicketValidationResponse(Ticket ticket) {
    TicketValidationResponse response = modelMapper.map(ticket, TicketValidationResponse.class);
    response.setSelectedSeats(
        ticket.getSelectedSeats().stream().map(Seat::getSeatNumber).collect(Collectors.toList()));
    return response;
  }

  public TicketResponse ticketToTicketResponse(Ticket ticket) {
    TicketResponse response = modelMapper.map(ticket, TicketResponse.class);
    response.setSelectedSeats(
        ticket.getSelectedSeats().stream().map(Seat::getSeatNumber).collect(Collectors.toList()));
    return response;
  }

  public Ticket reservationRequestToTicket(ReservationRequest reservationRequest) {
    Ticket ticket = modelMapper.map(reservationRequest, Ticket.class);
    ticket.setSelectedSeats(
        reservationRequest.getSelectedSeats().stream()
            .map(s -> new Seat(s, ticket))
            .collect(Collectors.toList()));
    return ticket;
  }

  public List<ReservationResponse> ticketsToReservationResponses(List<Ticket> tickets) {
    return tickets.stream().map(this::ticketToReservationResponse).collect(Collectors.toList());
  }

  public MovieDTO movieToMovieDTO(Movie movie) {
    return modelMapper.map(movie, MovieDTO.class);
  }

  public Movie movieDTOToMovie(MovieDTO movieDTO) {
    Movie movie = modelMapper.map(movieDTO, Movie.class);
    if (movieDTO.getId() != null) {
      movie.setId(UUID.fromString(movieDTO.getId()));
    }
    return movie;
  }

  public MovieDTO screeningToMovieDTO(Screening screening) {
    return movieToMovieDTO(screening.getMovie());
  }
}
