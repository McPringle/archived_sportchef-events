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

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.stream.Collectors.toList;

class EventRepository implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<Long, Event> allEvents = new ConcurrentHashMap<>();
    private final AtomicLong eventSequence = new AtomicLong(0);

    Event create(@NonNull final Event event) {
        final Long eventId = eventSequence.incrementAndGet();
        final long version = event.hashCode();
        final Event eventToCreate = event.toBuilder().eventId(eventId).version(version).build();
        allEvents.put(eventId, eventToCreate);
        return eventToCreate;
    }

    List<Event> read() {
        return ImmutableList.copyOf(
                allEvents.values().stream()
                        .sorted((event1, event2) -> {
                            final LocalDateTime event1DateTime = LocalDateTime.of(event1.getDate(), event1.getTime());
                            final LocalDateTime event2DateTime = LocalDateTime.of(event2.getDate(), event2.getTime());
                            return event1DateTime.compareTo(event2DateTime);
                        })
                        .collect(toList()));
    }

    Optional<Event> read(@NonNull final Long eventId) {
        return Optional.ofNullable(allEvents.get(eventId));
    }

    Event update(@NonNull final Event event) {
        final Long eventId = event.getEventId();
        final Event previousEvent = read(eventId).orElse(null);
        if (previousEvent == null) {
            return null; // non-existing events can't be updated
        }
        if (!previousEvent.getVersion().equals(event.getVersion())) {
            throw new ConcurrentModificationException("You tried to update an event that was modified concurrently!");
        }
        final long version = event.hashCode();
        final Event eventToUpdate = event.toBuilder().version(version).build();
        allEvents.put(eventId, eventToUpdate);
        return eventToUpdate;
    }

    Event delete(@NonNull final Long eventId) {
        return allEvents.remove(eventId);
    }
}
