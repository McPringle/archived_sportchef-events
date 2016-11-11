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
package ch.sportchef.events.boundary;

import ch.sportchef.events.controller.EventService;
import ch.sportchef.events.entity.Event;
import org.junit.Rule;
import org.junit.Test;
import org.needle4j.annotation.ObjectUnderTest;
import org.needle4j.junit.NeedleRule;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ConcurrentModificationException;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EventResourceTest {

    @Rule
    public NeedleRule needleRule = new NeedleRule();

    @Inject
    private EventService eventService;

    @Inject
    private UriInfo info;

    @ObjectUnderTest(postConstruct = true)
    private EventResource eventResource;

    @Test
    public void readExistingEvent() {
        // arrange
        final Event testEvent = Event.builder()
                .eventId(1L)
                .title("Testevent")
                .location("Testlocation")
                .date(LocalDate.of(2099, Month.DECEMBER, 31))
                .time(LocalTime.of(22, 0))
                .build();
        when(eventService.read(1L)).thenReturn(Optional.of(testEvent));

        // act
        final Response response = eventResource.read(1L);

        // assert
        assertThat(response.getStatus(), is(200));
        assertThat(response.getEntity(), is(testEvent));
    }

    @Test(expected = NotFoundException.class)
    public void readNonExistingEvent() {
        // arrange
        when(eventService.read(1L)).thenReturn(Optional.empty());

        // act
        eventResource.read(1L);

        // assert
    }

    @Test
    public void updateExistingEvent() throws URISyntaxException {
        // arrange
        final Event testEvent = Event.builder()
                .eventId(1L)
                .title("Testevent")
                .location("Testlocation")
                .date(LocalDate.of(2099, Month.DECEMBER, 31))
                .time(LocalTime.of(22, 0))
                .build();
        when(eventService.read(1L)).thenReturn(Optional.of(testEvent));
        final UriBuilder uriBuilder = mock(UriBuilder.class);
        when(uriBuilder.build()).thenReturn(new URI("/event/1"));
        when(info.getAbsolutePathBuilder()).thenReturn(uriBuilder);

        // act
        eventResource.update(1L, testEvent, info);

        // assert
    }

    @Test(expected = NotFoundException.class)
    public void updateNonExistingEvent() {
        // arrange
        final Event testEvent = Event.builder()
                .eventId(1L)
                .title("Testevent")
                .location("Testlocation")
                .date(LocalDate.of(2099, Month.DECEMBER, 31))
                .time(LocalTime.of(22, 0))
                .build();
        when(eventService.read(1L)).thenReturn(Optional.empty());

        // act
        eventResource.update(1L, testEvent, info);

        // assert
    }

    @Test(expected = ConcurrentModificationException.class)
    public void updateConflictingEvent() throws URISyntaxException {
        // arrange
        final Event testEvent = Event.builder()
                .eventId(1L)
                .title("Testevent")
                .location("Testlocation")
                .date(LocalDate.of(2099, Month.DECEMBER, 31))
                .time(LocalTime.of(22, 0))
                .build();
        when(eventService.read(1L)).thenReturn(Optional.of(testEvent));
        when(eventService.update(testEvent)).thenThrow(ConcurrentModificationException.class);

        // act
        eventResource.update(1L, testEvent, info);

        // assert
    }

    @Test
    public void deleteExistingEvent() {
        // arrange
        final Event testEvent = Event.builder()
                .eventId(1L)
                .title("Testevent")
                .location("Testlocation")
                .date(LocalDate.of(2099, Month.DECEMBER, 31))
                .time(LocalTime.of(22, 0))
                .build();
        when(eventService.read(1L)).thenReturn(Optional.of(testEvent));
        when(eventService.delete(1L)).thenReturn(testEvent);

        // act
        final Response response = eventResource.delete(1L);

        // assert
        assertThat(response.getStatus(), is(204));
    }

    @Test(expected = NotFoundException.class)
    public void deleteNonExistingEvent() {
        // arrange
        when(eventService.read(1L)).thenReturn(Optional.empty());

        // act
        eventResource.delete(1L);

        // assert
    }

}