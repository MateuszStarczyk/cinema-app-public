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
package com.piisw.cinema.cinemaappbackend.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Table(name = "SCREENINGS")
public class Screening {

  @Getter
  @Setter
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Getter
  @Setter
  @NotNull
  @Column(name = "SCREENING_START_DATE")
  private Date startDate;

  @Getter
  @Setter
  @NotNull
  @Column(name = "SCREENING_END_DATE")
  private Date endDate;

  @Getter
  @Setter
  @NotNull
  @ManyToOne(cascade = CascadeType.DETACH)
  @JoinColumn(name = "ROOM_ID")
  private Room room;

  @Getter
  @Setter
  @NotNull
  @ManyToOne(cascade = CascadeType.DETACH)
  @JoinColumn(name = "MOVIE_ID")
  private Movie movie;

  @Getter
  @Setter
  @OneToMany(mappedBy = "screening", cascade = CascadeType.ALL)
  private List<Ticket> tickets = new ArrayList<>();

  public Screening(Date startDate, Date endDate, Room room, Movie movie) {
    this.startDate = startDate;
    this.endDate = endDate;
    this.room = room;
    this.movie = movie;
  }

  public Screening(Date startDate) {
    this.startDate = startDate;
  }
}
