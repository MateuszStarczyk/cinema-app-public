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

import java.util.Objects;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.piisw.cinema.cinemaappbackend.models.cinemamanagement.ManagedTicket;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@IdClass(TicketManagedTicketId.class)
@Table(name = "TICKET_MANAGED_TICKETS")
public class TicketManagedTicket {

  @Getter
  @Setter
  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("id")
  private Ticket ticket;

  @JsonIgnore
  @Getter
  @Setter
  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("id")
  private ManagedTicket ticketType;

  @Getter
  @Setter
  @Column(name = "QUANTITY")
  private Integer quantity;

  public TicketManagedTicket(Ticket ticket, ManagedTicket ticketType, Integer quantity) {
    this.ticket = ticket;
    this.ticketType = ticketType;
    this.quantity = quantity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TicketManagedTicket that = (TicketManagedTicket) o;
    return ticket.equals(that.ticket) && ticketType.equals(that.ticketType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ticket, ticketType);
  }
}
