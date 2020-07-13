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
package com.piisw.cinema.cinemaappbackend.repository.cinemamanagement;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.piisw.cinema.cinemaappbackend.models.cinemamanagement.ManagedTicket;

@DataJpaTest
class ManagedTicketsRepositoryTest {

  @Autowired private TestEntityManager testEntityManager;

  @Autowired private ManagedTicketsRepository managedTicketsRepository;

  @Test
  void setInactiveById() {
    // given
    ManagedTicket normal = new ManagedTicket("NORMAL", 24.99);
    ManagedTicket senior = new ManagedTicket("SENIOR", 19.99);
    testEntityManager.persist(normal);
    testEntityManager.persist(senior);
    testEntityManager.flush();

    // when
    managedTicketsRepository.setInactiveById(senior.getId());

    // then
    assertThat(managedTicketsRepository.existsByNameAndIsActive("NORMAL", true)).isTrue();
    assertThat(managedTicketsRepository.existsByNameAndIsActive("NORMAL", false)).isFalse();
    assertThat(managedTicketsRepository.existsByNameAndIsActive("SENIOR", true)).isFalse();
    assertThat(managedTicketsRepository.existsByNameAndIsActive("SENIOR", false)).isTrue();
  }
}
