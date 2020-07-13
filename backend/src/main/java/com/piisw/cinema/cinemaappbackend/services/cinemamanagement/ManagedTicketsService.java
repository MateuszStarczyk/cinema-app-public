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

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.piisw.cinema.cinemaappbackend.models.cinemamanagement.ManagedTicket;
import com.piisw.cinema.cinemaappbackend.repository.cinemamanagement.ManagedTicketsRepository;

@Service
public class ManagedTicketsService {

  private final ManagedTicketsRepository managedTicketsRepository;

  public ManagedTicketsService(ManagedTicketsRepository managedTicketsRepository) {
    this.managedTicketsRepository = managedTicketsRepository;
  }

  public List<ManagedTicket> getAllActive() {
    return managedTicketsRepository.findAllByIsActive(true);
  }

  public Optional<ManagedTicket> getById(Integer id) {
    return managedTicketsRepository.findById(id);
  }

  public boolean save(ManagedTicket managedTicket) {
    if (managedTicketsRepository.existsByNameAndIsActive(managedTicket.getName(), true)) {
      return false;
    }
    managedTicket.setIsActive(true);
    managedTicketsRepository.save(managedTicket);
    return true;
  }

  @Transactional
  public boolean update(Integer id, ManagedTicket managedTicket) {
    if (managedTicketsRepository.existsById(id)) {
      managedTicketsRepository.setInactiveById(id);
      managedTicket.setId(null);
      managedTicketsRepository.save(managedTicket);
      return true;
    }
    return false;
  }

  public boolean delete(Integer id) {
    Optional<ManagedTicket> managedTicketOptional = managedTicketsRepository.findById(id);
    if (managedTicketOptional.isPresent()) {
      ManagedTicket managedTicket = managedTicketOptional.get();
      managedTicket.setIsActive(false);
      managedTicketsRepository.save(managedTicket);
      return true;
    }
    return false;
  }
}
