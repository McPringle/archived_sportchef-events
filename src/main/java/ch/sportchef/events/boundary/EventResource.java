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
import lombok.NonNull;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Optional;

@Path("events/{eventId}")
@Produces(MediaType.APPLICATION_JSON)
public class EventResource {

    private EventService eventService;

    @Inject
    public EventResource(@NonNull final EventService eventService) {
        this.eventService = eventService;
    }

    @GET
    public Response read(@NonNull @PathParam("eventId") final Long eventId) {
        final Optional<Event> event = eventService.read(eventId);
        if (event.isPresent()) {
            return Response.ok(event.get()).build();
        }
        throw new NotFoundException(String.format("Event with id '%d' not found.", eventId));
    }

    @PUT
    public Response update(@NonNull @PathParam("eventId") final Long eventId,
                           @NonNull @Valid final Event event,
                           @NonNull @Context final UriInfo info) {
        read(eventId); // only update existing events
        final Event eventToUpdate = event.toBuilder()
                .eventId(eventId)
                .build();
        final Event updatedEvent = eventService.update(eventToUpdate);
        final URI uri = info.getAbsolutePathBuilder().build();
        return Response.ok(updatedEvent).header("Location", uri.toString()).build();
    }

    @DELETE
    public Response delete(@NonNull @PathParam("eventId") final Long eventId) {
        read(eventId); // only delete existing events
        eventService.delete(eventId);
        return Response.noContent().build();
    }
}
