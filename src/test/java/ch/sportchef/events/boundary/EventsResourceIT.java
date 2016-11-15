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

import ch.sportchef.events.entity.Event;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import fish.payara.micro.BootstrapException;
import fish.payara.micro.PayaraMicro;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class EventsResourceIT {

    @BeforeClass
    public static void setUp() throws BootstrapException {
        PayaraMicro.getInstance()
                .addDeployment("./build/libs/sportchef-events.war")
                .bootStrap();
    }

    @AfterClass
    public static void tearDown() throws BootstrapException {
        PayaraMicro.getInstance().shutdown();
    }

    @Test
    public void crud() {
        final Event event = Event.builder()
                .title("Testevent")
                .location("Testlocation")
                .date(LocalDate.of(2099, Month.DECEMBER, 31))
                .time(LocalTime.of(22, 0))
                .build();

        final Long eventId = testCreateEvent(event);
        final Event readEvent = testReadEvent(event.toBuilder().eventId(eventId).build());
        testUpdate(readEvent);
        testDelete(eventId);
    }

    private Long testCreateEvent(final Event event) {
        // create a new event with success
        final Response response = given()
            .when()
                .contentType(ContentType.JSON)
                .body(new Gson().toJson(event))
                .post("/events")
            .then()
                .assertThat()
                .statusCode(201)
                    .header("Location", notNullValue())
                    .extract().response();
        final String[] locationParts = response.header("Location").split("/");
        final Long eventId = Long.parseLong(locationParts[locationParts.length - 1]);

        // create a new event should fail with a bad request
        given()
            .when()
                .contentType(ContentType.JSON)
                .body(new Gson().toJson(event.toBuilder().title(null).build()))
                .post("/events")
            .then()
                .assertThat()
                    .statusCode(400);

        return eventId;
    }

    private Event testReadEvent(final Event event) {
        // read all events should contain the new event
        final String allEventsJson = given()
            .when()
                .get("/events")
            .then()
                .assertThat()
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .extract().response().asString();
        final List<Event> allEvents = Lists.newArrayList(new Gson().fromJson(allEventsJson, Event[].class));
        assertThat(allEvents.contains(event), is(true));

        // read the new event should finish successful
        final String oneEventJson = given()
            .when()
                .get("/events/" + event.getEventId())
            .then()
                .assertThat()
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .extract().response().asString();
        final Event oneEvent = new Gson().fromJson(oneEventJson, Event.class);
        assertThat(oneEvent, is(event));

        // read a non-existing event should return a not found
        given()
            .when()
                .get("/events/" + Long.MAX_VALUE)
            .then()
                .assertThat()
                    .statusCode(404);

        return oneEvent;
    }

    private void testUpdate(final Event event) {
        // update the new event with success
        given()
            .when()
                .contentType(ContentType.JSON)
                .body(new Gson().toJson(event.toBuilder().title("Updated").build()))
                .put("/events/" + event.getEventId())
            .then()
                .assertThat()
                    .statusCode(200)
                    .header("Location", endsWith("/events/" + event.getEventId()));

        // update the new event should fail with a conflict
        given()
            .when()
                .contentType(ContentType.JSON)
                .body(new Gson().toJson(event.toBuilder().title("Conflict").build()))
                .put("/events/" + event.getEventId())
            .then()
                .assertThat()
                    .statusCode(409);

        // update the new event should fail with a bad request
        given()
            .when()
                .contentType(ContentType.JSON)
                .body(new Gson().toJson(event.toBuilder().title(null).build()))
                .put("/events/" + event.getEventId())
            .then()
                .assertThat()
                    .statusCode(400);

        // update a non-existing event should fail with a not found
        given()
            .when()
                .contentType(ContentType.JSON)
                .body(new Gson().toJson(event))
                .put("/events/" + Long.MAX_VALUE)
            .then()
                .assertThat()
                    .statusCode(404);
    }

    private void testDelete(final Long eventId) {
        // delete the new event with success
        given()
            .when()
                .delete("/events/" + eventId)
            .then()
                .assertThat()
                    .statusCode(204);

        // delete a non-existing event should fail with a not found
        given()
            .when()
                .delete("/events/" + eventId)
            .then()
                .assertThat()
                    .statusCode(404);
    }

}
