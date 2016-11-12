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

import ch.sportchef.events.PersistenceManager;
import ch.sportchef.events.entity.Event;
import lombok.NonNull;
import pl.setblack.airomem.core.SimpleController;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

@Singleton
public class EventService {

    private SimpleController<EventRepository> eventController;

    @PostConstruct
    public void setupResources() {
        this.eventController = PersistenceManager.createSimpleController(Event.class, EventRepository::new);
    }

    @PreDestroy
    public void cleanupResources() {
        this.eventController.close();
    }

    public Event create(@NonNull final Event event) {
        return this.eventController.executeAndQuery(mgr -> mgr.create(event));
    }

    public List<Event> read() {
        return this.eventController.readOnly().read();
    }

    public Optional<Event> read(@NonNull final Long eventId) {
        return this.eventController.readOnly().read(eventId);
    }

    public Event update(@NonNull final Event event) {
        return this.eventController.executeAndQuery(mgr -> mgr.update(event));
    }

    public void delete(@NonNull final Long eventId) {
        this.eventController.execute(mgr -> mgr.delete(eventId));
    }
}
