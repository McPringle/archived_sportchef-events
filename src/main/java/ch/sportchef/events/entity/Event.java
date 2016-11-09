/*
 * SportChef – Sports Competition Management Software
 * Copyright (C) 2015, 2016 Marcus Fihlon
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

import com.google.common.base.Strings;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Value
@Builder(toBuilder = true)
@EqualsAndHashCode(exclude={"version"})
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long eventId;

    private String title;

    private String location;

    private LocalDate date;

    private LocalTime time;

    private Long version;

    public boolean isValid() {
        return !Strings.isNullOrEmpty(title);
    }

}