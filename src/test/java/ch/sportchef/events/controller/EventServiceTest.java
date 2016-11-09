/*
 * SportChef – Sports Competition Management Software
 * Copyright (C) 2016 Marcus Fihlon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.sportchef.events.controller;

import ch.sportchef.events.entity.Event;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static java.lang.Boolean.TRUE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class EventServiceTest {

    private EventService eventService;

    @Before
    public void setUp() {
        this.eventService = new EventService();
    }

    @Test
    public void create() {
        // arrange
        final Event event = Event.builder()
                .title("Testevent")
                .location("Testlocation")
                .date(LocalDate.of(2099, Month.DECEMBER, 31))
                .time(LocalTime.of(22, 0))
                .build();

        // act
        create(event);

        // assert
    }

    private Event create(final Event testEvent) {
        // arrange

        // act
        final Event createdEvent = this.eventService.create(testEvent);

        // assert
        assertThat(createdEvent, is(testEvent.toBuilder().eventId(1L).build()));

        return createdEvent;
    }

    @Test
    public void readAll() {
        // arrange
        final Event testEvent = create(Event.builder()
                .eventId(1L)
                .title("Testevent")
                .location("Testlocation")
                .date(LocalDate.of(2099, Month.DECEMBER, 31))
                .time(LocalTime.of(22, 0))
                .build());

        // act
        final List<Event> allEvents = this.eventService.read();

        // assert
        assertThat(allEvents.size(), is(1));
        assertThat(allEvents.get(0), is(testEvent));
    }

    @Test
    public void readOne() {
        // arrange
        final Event testEvent = create(Event.builder()
                .eventId(1L)
                .title("Testevent")
                .location("Testlocation")
                .date(LocalDate.of(2099, Month.DECEMBER, 31))
                .time(LocalTime.of(22, 0))
                .build());

        // act
        final Optional<Event> event = this.eventService.read(1L);

        // assert
        assertThat(event.isPresent(), is(TRUE));
        assertThat(event.get(), is(testEvent));
    }

    @Test
    public void update() {
        // arrange
        final Event testEvent = Event.builder()
                .eventId(1L)
                .title("Testevent Updated")
                .location("Testlocation")
                .date(LocalDate.of(2099, Month.DECEMBER, 31))
                .time(LocalTime.of(22, 0))
                .build();

        // act
        final Event updatedEvent = this.eventService.update(testEvent);

        // assert
        assertThat(updatedEvent, is(testEvent));
    }

    @Test
    public void delete() {
        // arrange
        final Event testEvent = create(Event.builder()
                .title("Testevent")
                .location("Testlocation")
                .date(LocalDate.of(2099, Month.DECEMBER, 31))
                .time(LocalTime.of(22, 0))
                .build());

        // act
        final Event deletedEvent = this.eventService.delete(testEvent.getEventId());

        // assert
        assertThat(deletedEvent, is(testEvent));
    }

}