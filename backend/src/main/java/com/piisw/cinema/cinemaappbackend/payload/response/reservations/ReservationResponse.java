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
package com.piisw.cinema.cinemaappbackend.payload.response.reservations;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.piisw.cinema.cinemaappbackend.payload.dtos.TicketKindDTO;

import lombok.Getter;
import lombok.Setter;

public class ReservationResponse {

  @JsonProperty("number")
  @Getter
  @Setter
  private String id;

  @Getter @Setter private Integer screeningId;

  @JsonProperty("customerId")
  @Getter
  @Setter
  private Long userId;

  @JsonProperty("customerEmail")
  @Getter
  @Setter
  private String userEmail;

  @Getter @Setter private String customerName;

  @JsonProperty("ticketKinds")
  @Getter
  @Setter
  private List<TicketKindDTO> ticketTypes;

  @Getter @Setter private List<String> selectedSeats;

  @JsonProperty("startDate")
  @Getter
  @Setter
  private Date screeningStartDate;

  @JsonProperty("roomId")
  @Getter
  @Setter
  private Integer screeningRoomId;

  @JsonProperty("movieId")
  @Getter
  @Setter
  private String screeningMovieId;

  @JsonProperty("movieTitle")
  @Getter
  @Setter
  private String screeningMovieTitle;

  @JsonProperty("duration")
  @Getter
  @Setter
  private Integer screeningMovieDuration;
}
