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
package com.piisw.cinema.cinemaappbackend.models.cinemamanagement;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import com.piisw.cinema.cinemaappbackend.models.TicketManagedTicket;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Table(name = "MANAGED_TICKETS")
public class ManagedTicket {

  @Expose
  @Getter
  @Setter
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Expose
  @Getter
  @Setter
  @NotBlank
  @Size(max = 20)
  @Column(name = "NAME")
  private String name;

  @Expose
  @Getter
  @Setter
  @PositiveOrZero
  @Digits(integer = 6, fraction = 2)
  @Column(name = "PRICE")
  private Double price;

  @Expose
  @Getter
  @Setter
  @Column(name = "IS_ACTIVE")
  private Boolean isActive;

  @Expose(serialize = false, deserialize = false)
  @JsonIgnore
  @Getter
  @Setter
  @OneToMany(mappedBy = "ticketType", cascade = CascadeType.ALL)
  private List<TicketManagedTicket> tickets = new ArrayList<>();

  public ManagedTicket(Integer id, String name, Double price) {
    this.id = id;
    this.name = name;
    this.price = price;
  }

  public ManagedTicket(String name, Double price) {
    this.name = name;
    this.price = price;
    this.isActive = true;
  }
}
