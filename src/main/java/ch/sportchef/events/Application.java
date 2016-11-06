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
package ch.sportchef.events;

import ch.sportchef.events.business.ping.boundary.PingResource;
import ch.sportchef.events.business.ping.controller.PingController;
import lombok.experimental.UtilityClass;
import spark.Request;
import spark.Response;

import static spark.Spark.after;
import static spark.Spark.port;

@UtilityClass
public class Application {

    public static void main(final String... args) {

        // Configure Spark
        port(8080);

        // Register routes
        PingResource.registerRoutes(new PingController());

        // Set up after-filters (called after each request)
        after((Request request, Response response) -> response.header("Content-Encoding", "gzip"));
    }

}
