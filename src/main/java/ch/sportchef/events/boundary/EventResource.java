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
import com.google.gson.Gson;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import spark.Request;
import spark.Response;

import java.util.ConcurrentModificationException;
import java.util.Optional;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

@UtilityClass
public class EventResource {

    public static void registerRoutes(@NonNull final EventService eventService) {

        // create
        post("/events", (final Request request, final Response response) -> {
            final String json = request.body();
            final Gson gson = new Gson();
            final Event event = gson.fromJson(json, Event.class);
            if (event.isValid()) {
                final Event createdEvent = eventService.create(event);
                response.status(201); // CREATED
                return gson.toJson(createdEvent);
            }
            response.status(400); // BAD REQUEST
            return json;
        });

        // read
        get("/events", (final Request request, final Response response) -> new Gson().toJson(eventService.read()));
        get("/event/:eventId", (final Request request, final Response response) -> {
            final Long eventId = Long.parseLong(request.params(":eventId"));
            final Optional<Event> optional = eventService.read(eventId);
            if (optional.isPresent()) {
                final Gson gson = new Gson();
                final Event event = optional.get();
                response.status(200); // OK
                return gson.toJson(event);
            }
            response.status(404); // NOT FOUND
            return eventId;
        });

        // update
        put("/event/:eventId", (final Request request, final Response response) -> {
            final Long eventId = Long.parseLong(request.params(":eventId"));
            final String json = request.body();
            final Gson gson = new Gson();
            final Event event = gson.fromJson(json, Event.class).toBuilder().eventId(eventId).build();
            if (event.isValid()) {
                try {
                    final Event updatedEvent = eventService.update(event);
                    if (updatedEvent != null) {
                        response.status(200); // OK
                        return gson.toJson(updatedEvent);
                    }
                    response.status(404); // NOT FOUND
                    return gson.toJson(eventId);
                } catch (final ConcurrentModificationException e) {
                    response.status(409); // CONFLICT
                    return json;
                }
            }
            response.status(400); // BAD REQUEST
            return json;
        });

        // delete
        delete("/event/:eventId", (final Request request, final Response response) -> {
            final Long eventId = Long.parseLong(request.params(":eventId"));
            final Event event = eventService.delete(eventId);
            if (event != null) {
                response.status(204); // NO CONTENT
                return new Gson().toJson(event);
            }
            response.status(404); // NOT FOUND
            return eventId;
        });
    }

}
