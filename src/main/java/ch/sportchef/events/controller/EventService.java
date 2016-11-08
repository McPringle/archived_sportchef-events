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
import com.google.common.collect.ImmutableList;
import lombok.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class EventService {

    private final Map<Long, Event> allEvents = new ConcurrentHashMap<>();
    private final AtomicLong eventSequence = new AtomicLong(0);

    public Event create(@NonNull final Event event) {
        final Long eventId = eventSequence.incrementAndGet();
        final Event createdEvent = event.toBuilder().eventId(eventId).build();
        allEvents.put(eventId, createdEvent);
        return createdEvent;
    }

    public List<Event> read() {
        return ImmutableList.copyOf(allEvents.values());
    }

    public Optional<Event> read(@NonNull final Long eventId) {
        return Optional.ofNullable(allEvents.get(eventId));
    }

    public Event update(@NonNull final Event event) {
        final Long eventId = event.getEventId();
        allEvents.put(eventId, event);
        return event;
    }

    public Event delete(@NonNull final Long eventId) {
        return allEvents.remove(eventId);
    }
}
