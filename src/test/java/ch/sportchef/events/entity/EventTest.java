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
package ch.sportchef.events.entity;

import lombok.val;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class EventTest {

    private static final Long EVENT_ID = 1L;
    private static final String EVENT_TITLE = "Test Event";
    private static final String EVENT_LOCATION = "Test Location";
    private static final LocalDate EVENT_DATE = LocalDate.of(1974, Month.JANUARY, 30);
    private static final LocalTime EVENT_TIME = LocalTime.of(1, 0);

    private static Event event;

    @BeforeClass
    public static void setUp() {
        event = Event.builder()
                .eventId(EVENT_ID)
                .title(EVENT_TITLE)
                .location(EVENT_LOCATION)
                .date(EVENT_DATE)
                .time(EVENT_TIME)
                .build();
    }

    @AfterClass
    public static void tearDown() {
        event = null;
    }

    @Test
    public void getEventId() {
        assertThat(event.getEventId(), is(EVENT_ID));
    }

    @Test
    public void getTitle() {
        assertThat(event.getTitle(), is(EVENT_TITLE));
    }

    @Test
    public void getLocation() {
        assertThat(event.getLocation(), is(EVENT_LOCATION));
    }

    @Test
    public void getDate() {
        assertThat(event.getDate(), is(EVENT_DATE));
    }

    @Test
    public void getTime() {
        assertThat(event.getTime(), is(EVENT_TIME));
    }

    @Test
    public void toStringTest() {
        // arrange
        val toStringExpect = String.format(
                "Event(eventId=%d, title=%s, location=%s, date=%s, time=%s, version=%d)",
                EVENT_ID, EVENT_TITLE, EVENT_LOCATION, EVENT_DATE, EVENT_TIME, event.getVersion());

        // act
        val toString = event.toString();

        // assert
        assertThat(toString, is(toStringExpect));
    }

}