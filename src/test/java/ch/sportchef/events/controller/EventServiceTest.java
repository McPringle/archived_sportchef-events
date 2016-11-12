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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import pl.setblack.airomem.core.SimpleController;
import pl.setblack.airomem.core.VoidCommand;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PersistenceManager.class)
public class EventServiceTest {

    @Test
    public void create() {
        // arrange
        final SimpleController<Serializable> simpleControllerMock = mock(SimpleController.class);
        mockStatic(PersistenceManager.class);
        when(PersistenceManager.createSimpleController(any(), any())).thenReturn(simpleControllerMock);
        final EventService eventService = new EventService();
        eventService.setupResources();
        final Event testEvent = Event.builder()
                .title("Testevent")
                .location("Testlocation")
                .date(LocalDate.of(2099, Month.DECEMBER, 31))
                .time(LocalTime.of(22, 0))
                .build();

        // act
        eventService.create(testEvent);

        // assert
        verify(simpleControllerMock, times(1)).executeAndQuery(anyObject());
    }

    @Test
    public void readAll() {
        // arrange
        final SimpleController<Serializable> simpleControllerMock = mock(SimpleController.class);
        final EventRepository eventRepositoryMock = mock(EventRepository.class);
        when(simpleControllerMock.readOnly()).thenReturn(eventRepositoryMock);
        mockStatic(PersistenceManager.class);
        when(PersistenceManager.createSimpleController(any(), any())).thenReturn(simpleControllerMock);
        final EventService eventService = new EventService();
        eventService.setupResources();

        // act
        eventService.read();

        // assert
        verify(simpleControllerMock, times(1)).readOnly();
        verify(eventRepositoryMock, times(1)).read();
    }

    @Test
    public void readOne() {
        // arrange
        final SimpleController<Serializable> simpleControllerMock = mock(SimpleController.class);
        final EventRepository eventRepositoryMock = mock(EventRepository.class);
        when(simpleControllerMock.readOnly()).thenReturn(eventRepositoryMock);
        mockStatic(PersistenceManager.class);
        when(PersistenceManager.createSimpleController(any(), any())).thenReturn(simpleControllerMock);
        final EventService eventService = new EventService();
        eventService.setupResources();

        // act
        eventService.read(1L);

        // assert
        verify(simpleControllerMock, times(1)).readOnly();
        verify(eventRepositoryMock, times(1)).read(1L);
    }

    @Test
    public void update() {
        // arrange
        final SimpleController<Serializable> simpleControllerMock = mock(SimpleController.class);
        mockStatic(PersistenceManager.class);
        when(PersistenceManager.createSimpleController(any(), any())).thenReturn(simpleControllerMock);
        final EventService eventService = new EventService();
        eventService.setupResources();
        final Event testEvent = Event.builder()
                .eventId(1L)
                .title("Testevent")
                .location("Testlocation")
                .date(LocalDate.of(2099, Month.DECEMBER, 31))
                .time(LocalTime.of(22, 0))
                .build();

        // act
        eventService.update(testEvent);

        // assert
        verify(simpleControllerMock, times(1)).executeAndQuery(anyObject());
    }

    @Test
    public void delete() {
        // arrange
        final SimpleController<Serializable> simpleControllerMock = mock(SimpleController.class);
        mockStatic(PersistenceManager.class);
        when(PersistenceManager.createSimpleController(any(), any())).thenReturn(simpleControllerMock);
        final EventService eventService = new EventService();
        eventService.setupResources();

        // act
        eventService.delete(1L);

        // assert
        verify(simpleControllerMock, times(1)).execute(any(VoidCommand.class));
    }

}