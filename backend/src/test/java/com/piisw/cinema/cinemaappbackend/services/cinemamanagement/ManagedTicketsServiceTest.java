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
package com.piisw.cinema.cinemaappbackend.services.cinemamanagement;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.piisw.cinema.cinemaappbackend.models.cinemamanagement.ManagedTicket;
import com.piisw.cinema.cinemaappbackend.repository.cinemamanagement.ManagedTicketsRepository;

@ExtendWith(MockitoExtension.class)
class ManagedTicketsServiceTest {

  @Mock private ManagedTicketsRepository managedTicketsRepository;

  @InjectMocks private ManagedTicketsService managedTicketsService;

  @Test
  void saveWhenTicketNotExist() {
    // given
    ManagedTicket ticket = new ManagedTicket("NORMAL", 19.99);
    Mockito.when(managedTicketsRepository.existsByNameAndIsActive("NORMAL", true))
        .thenReturn(false);

    // when
    boolean result = managedTicketsService.save(ticket);

    // then
    assertThat(result).isTrue();
  }

  @Test
  void saveWhenTicketExists() {
    // given
    ManagedTicket ticket = new ManagedTicket("NORMAL", 19.99);
    Mockito.when(managedTicketsRepository.existsByNameAndIsActive("NORMAL", true)).thenReturn(true);

    // when
    boolean result = managedTicketsService.save(ticket);

    // then
    assertThat(result).isFalse();
  }

  @Test
  void updateWhenTicketNotExist() {
    // given
    ManagedTicket ticket = new ManagedTicket(1, "NORMAL", 19.99);
    Mockito.when(managedTicketsRepository.existsById(1)).thenReturn(false);

    // when
    boolean result = managedTicketsService.update(1, ticket);

    // then
    assertThat(result).isFalse();
  }

  @Test
  void updateWhenTicketExists() {
    // given
    ManagedTicket ticket = new ManagedTicket(1, "NORMAL", 19.99);
    Mockito.when(managedTicketsRepository.existsById(1)).thenReturn(true);

    // when
    boolean result = managedTicketsService.update(1, ticket);

    // then
    assertThat(result).isTrue();
  }

  @Test
  void deleteWhenTicketNotExist() {
    // given
    Mockito.when(managedTicketsRepository.findById(1)).thenReturn(Optional.empty());

    // when
    boolean result = managedTicketsService.delete(1);

    // then
    assertThat(result).isFalse();
  }

  @Test
  void deleteWhenTicketExists() {
    // given
    ManagedTicket ticket = new ManagedTicket(1, "NORMAL", 19.99);
    Mockito.when(managedTicketsRepository.findById(1)).thenReturn(Optional.of(ticket));

    // when
    boolean result = managedTicketsService.delete(1);

    // then
    assertThat(result).isTrue();
  }
}
