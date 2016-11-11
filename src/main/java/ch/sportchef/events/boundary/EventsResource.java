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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.File;
import java.net.URI;
import java.util.List;

@Path("events")
@Produces(MediaType.APPLICATION_JSON)
public class EventsResource {

    private EventService eventService;

    @Inject
    public EventsResource(@NonNull final EventService eventService) {
        this.eventService = eventService;
    }

    public Response create(@NonNull @Valid final Event event,
                           @NonNull @Context final UriInfo info) {
        final Event createdEvent = eventService.create(event);
        final long eventId = createdEvent.getEventId();
        final URI uri = info.getAbsolutePathBuilder().path(File.separator + eventId).build();
        return Response.created(uri).build();
    }

    @GET
    public Response read() {
        final List<Event> events = eventService.read();
        return Response.ok(events).build();
    }

}
