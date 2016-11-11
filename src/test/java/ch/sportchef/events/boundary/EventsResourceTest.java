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
import com.google.common.collect.ImmutableList;
import org.junit.Rule;
import org.junit.Test;
import org.needle4j.annotation.ObjectUnderTest;
import org.needle4j.junit.NeedleRule;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

import static java.lang.Boolean.TRUE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EventsResourceTest {

    @Rule
    public NeedleRule needleRule = new NeedleRule();

    @Inject
    private EventService eventService;

    @Inject
    private UriInfo info;

    @ObjectUnderTest(postConstruct = true)
    private EventsResource eventsResource;

    @Test
    public void createEvent() throws URISyntaxException {
        // arrange
        final Event testEvent = Event.builder()
                .title("Testevent")
                .location("Testlocation")
                .date(LocalDate.of(2099, Month.DECEMBER, 31))
                .time(LocalTime.of(22, 0))
                .build();
        when(eventService.create(testEvent)).thenReturn(testEvent.toBuilder().eventId(1L).version(1L).build());
        final UriBuilder uriBuilder = mock(UriBuilder.class);
        final URI uri = new URI("/event/1");
        when(uriBuilder.path(anyString())).thenReturn(uriBuilder);
        when(uriBuilder.build()).thenReturn(uri);
        when(info.getAbsolutePathBuilder()).thenReturn(uriBuilder);

        // act
        final Response response = eventsResource.create(testEvent, info);

        // assert
        assertThat(response.getStatus(), is(201));
        assertThat(response.getLocation(), is(uri));
    }

    @Test
    public void readAllEvents() {
        // arrange
        final Event testEvent = Event.builder()
                .eventId(1L)
                .title("Testevent")
                .location("Testlocation")
                .date(LocalDate.of(2099, Month.DECEMBER, 31))
                .time(LocalTime.of(22, 0))
                .build();
        when(eventService.read()).thenReturn(ImmutableList.of(testEvent));

        // act
        final Response response = eventsResource.read();

        // assert
        final List<Event> events = (List<Event>) response.getEntity();
        assertThat(events.size(), is(1));
        assertThat(events, contains(testEvent));
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void readEmptyEvents() {
        // arrange
        when(eventService.read()).thenReturn(ImmutableList.of());

        // act
        final Response response = eventsResource.read();

        // assert
        final List<Event> events = (List<Event>) response.getEntity();
        assertThat(events.isEmpty(), is(TRUE));
        assertThat(response.getStatus(), is(200));
    }

}