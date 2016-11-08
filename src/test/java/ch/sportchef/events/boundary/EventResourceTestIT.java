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

import ch.sportchef.events.Application;
import ch.sportchef.events.entity.Event;
import com.google.gson.Gson;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.NonNull;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Spark;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;

import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

public class EventResourceTestIT {

    @BeforeClass
    public static void beforeClass() {
        Application.main();
    }

    @AfterClass
    public static void afterClass() {
        Spark.stop();
    }

    @Test
    public void crud() {
        // arrange
        final Event testEvent = Event.builder()
                .title("Testevent")
                .location("Testlocation")
                .date(LocalDate.of(2099, Month.DECEMBER, 31))
                .time(LocalTime.of(22, 0))
                .build();

        // act
        readAllEvents();
        final Event createdEvent = createEvent(testEvent);
        readAllEvents(createdEvent);
        readExistingEvent(createdEvent);
        final Event updatedEvent = updateExistingEvent(createdEvent.toBuilder().title("Testevent Updated").build());
        updateNonExistingEvent(createdEvent.toBuilder().eventId(1234L).title("Testevent Updated").build());
        readExistingEvent(updatedEvent);
        deleteExistingEvent(updatedEvent);
        deleteNonExistingEvent(updatedEvent);
        readNonExistingEvent(updatedEvent);

        // assert
    }

    private Event createEvent(@NonNull final Event eventToCreate) {
        // arrange
        final String json = new Gson().toJson(eventToCreate);

        // act
        final Response response = given().contentType(ContentType.JSON).body(json).post("/events");
        final Event createdEvent = response.as(Event.class);

        // assert
        assertThat(createdEvent, is(eventToCreate.toBuilder().eventId(1L).build()));
        assertThat(response.statusCode(), is(201));

        return createdEvent;
    }

    private void readAllEvents(@NonNull final Event... testEvents) {
        // arrange

        // act
        final Response response = get("/events");
        final Event[] allEvents = response.as(Event[].class);

        // assert
        assertThat(allEvents.length, is(testEvents.length));
        if (testEvents.length > 0) assertThat(Arrays.asList(allEvents), containsInAnyOrder(testEvents));
        assertThat(response.statusCode(), is(200));
    }

    private void readExistingEvent(@NonNull final Event eventToRead) {
        // arrange
        final Long eventId = eventToRead.getEventId();

        // act
        final Response response = get("/event/{eventId}", eventId);
        final Event readEvent = response.as(Event.class);

        // assert
        assertThat(readEvent, is(eventToRead));
        assertThat(response.statusCode(), is(200));
    }

    private void readNonExistingEvent(@NonNull final Event updatedEvent) {
        // arrange
        final Long eventId = updatedEvent.getEventId();

        // act
        final Response response = get("/event/{eventId}", eventId);
        final String body = response.as(String.class);

        // assert
        assertThat(body, is(eventId.toString()));
        assertThat(response.statusCode(), is(404));
    }

    private Event updateExistingEvent(@NonNull final Event eventToUpdate) {
        // arrange
        final Long eventId = eventToUpdate.getEventId();
        final String json = new Gson().toJson(eventToUpdate);

        // act
        final Response response = given().contentType(ContentType.JSON).body(json).put("/event/{eventId}", eventId);
        final Event updatedEvent = response.as(Event.class);

        // assert
        assertThat(updatedEvent, is(eventToUpdate));
        assertThat(response.statusCode(), is(200));

        return updatedEvent;
    }

    private void updateNonExistingEvent(@NonNull final Event eventToUpdate) {
        // arrange
        final Long eventId = eventToUpdate.getEventId();
        final String json = new Gson().toJson(eventToUpdate);

        // act
        final Response response = given().contentType(ContentType.JSON).body(json).post("/event/{eventId}", eventId);

        // assert
        assertThat(response.statusCode(), is(404));
    }

    private void deleteExistingEvent(@NonNull final Event eventToDelete) {
        // arrange
        final Long eventId = eventToDelete.getEventId();

        // act
        final Response response = delete("/event/{eventId}", eventId);

        // assert
        assertThat(response.statusCode(), is(204));
    }

    private void deleteNonExistingEvent(@NonNull final Event eventToDelete) {
        // arrange
        final Long eventId = eventToDelete.getEventId();

        // act
        final Response response = delete("/event/{eventId}", eventId);

        // assert
        assertThat(response.statusCode(), is(404));
    }

}