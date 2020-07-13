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
import java.util.List;
import java.util.UUID;

import javax.persistence.*;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.piisw.cinema.cinemaappbackend.models.cinemamanagement.ManagedTicket;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Table(name = "TICKETS")
public class Ticket {

  @Getter
  @Setter
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @ColumnDefault("random_uuid()")
  @Type(type = "uuid-char")
  private UUID id;

  @Getter
  @Setter
  @ManyToOne
  @JoinColumn(name = "SCREENING_ID")
  private Screening screening;

  @Getter
  @Setter
  @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
  private List<TicketManagedTicket> ticketTypes = new ArrayList<>();

  @Getter
  @Setter
  @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
  private List<Seat> selectedSeats = new ArrayList<>();

  @Getter
  @Setter
  @ManyToOne
  @JoinColumn(name = "USER_ID")
  private User user;

  public Ticket(Screening screening, User user) {
    this.screening = screening;
    this.user = user;
  }

  public void addTicketType(ManagedTicket ticket, Integer quantity) {
    TicketManagedTicket ticketManagedTicket = new TicketManagedTicket(this, ticket, quantity);
    ticketTypes.add(ticketManagedTicket);
    ticket.getTickets().add(ticketManagedTicket);
  }
}
