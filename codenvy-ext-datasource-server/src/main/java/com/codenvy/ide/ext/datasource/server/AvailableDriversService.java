/*
 * Copyright 2014 Codenvy, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codenvy.ide.ext.datasource.server;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codenvy.dto.server.DtoFactory;
import com.codenvy.ide.ext.datasource.shared.DriversDTO;
import com.codenvy.ide.ext.datasource.shared.ServicePaths;

/**
 * A service that lists all available JDBC drivers.
 * 
 * @author "Mickaël Leduque"
 */
@Path(ServicePaths.DATABASE_TYPES_PATH)
@Singleton
public class AvailableDriversService {

    /** The logger. */
    private static final Logger LOG = LoggerFactory.getLogger(AvailableDriversService.class);
    // try to load all supported drivers
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            LOG.debug("postgresql driver not present");
            LOG.trace("postgresql driver not present", e);
        }
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            LOG.debug("MySQL driver not present");
            LOG.trace("MySQL driver not present", e);
        }
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            LOG.debug("Oracle driver not present");
            LOG.trace("Oracle driver not present", e);
        }
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            LOG.debug("JTDS driver not present");
            LOG.trace("JTDS driver not present", e);
        }
        try {
            Class.forName("com.nuodb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            LOG.debug("NuoDB driver not present");
            LOG.trace("NuoDB driver not present", e);
        }
    }

    /**
     * Lists all supported JDBC drivers that are present in the classloader.
     * 
     * @return the list of drivers
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public String getAvailableDatabaseDrivers() {
        final Enumeration<Driver> loadedDrivers = DriverManager.getDrivers();
        final List<String> drivers = new ArrayList<>();
        while (loadedDrivers.hasMoreElements()) {
            Driver driver = loadedDrivers.nextElement();
            drivers.add(driver.getClass().getCanonicalName());
        }
        final DriversDTO driversDTO = DtoFactory.getInstance().createDto(DriversDTO.class).withDrivers(drivers);
        final String msg = DtoFactory.getInstance().toJson(driversDTO);
        LOG.debug(msg);
        return msg;
    }
}
