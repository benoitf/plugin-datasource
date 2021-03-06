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

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.NuoDBBrokerDTO;
import com.codenvy.ide.ext.datasource.shared.exception.DatabaseDefinitionException;

/**
 * Service that builds connections for configured datasources.
 * 
 * @author "Mickaël Leduque"
 */
public class JdbcConnectionFactory {

    /** The logger. */
    private static final Logger LOG                   = LoggerFactory.getLogger(JdbcConnectionFactory.class);

    /** URL pattern for PostgreSQL databases. */
    private static final String URL_TEMPLATE_POSTGRES = "jdbc:postgresql://{0}:{1}/{2}";

    /** URL pattern for MySQL databases. */
    private static final String URL_TEMPLATE_MYSQL    = "jdbc:mysql://{0}:{1}/{2}";

    /** URL pattern for Oracle databases. */
    private static final String URL_TEMPLATE_ORACLE   = "jdbc:oracle:thin:@{0}:{1}:{2}";

    /** URL pattern for SQLServer databases. */
    private static final String URL_TEMPLATE_JTDS     = "jdbc:jtds:sqlserver://{0}:{1}/{2}";

    /** URL pattern for NuoDB databases. */
    private static final String URL_TEMPLATE_NUODB    = "jdbc:com.nuodb://{0}/{1}";

    /**
     * builds a JDBC {@link Connection} for a datasource.
     * 
     * @param configuration the datasource configuration
     * @return a connection
     * @throws SQLException if the creation of the connection failed
     * @throws DatabaseDefinitionException if the configuration is incorrect
     */
    public Connection getDatabaseConnection(final DatabaseConfigurationDTO configuration) throws SQLException, DatabaseDefinitionException {
        if (LOG.isInfoEnabled()) {
            Driver[] drivers = Collections.list(DriverManager.getDrivers()).toArray(new Driver[0]);
            LOG.info("Available jdbc drivers : {}", Arrays.toString(drivers));
        }

        Properties info = new Properties();
        info.setProperty("user", configuration.getUsername());
        info.setProperty("password", configuration.getPassword());
        if (configuration.getUseSSL()) {
            info.setProperty("useSSL", Boolean.toString(configuration.getUseSSL()));
        }
        if (configuration.getVerifyServerCertificate()) {
            info.setProperty("verifyServerCertificate", Boolean.toString(configuration.getVerifyServerCertificate()));
        }

        final Connection connection = DriverManager.getConnection(getJdbcUrl(configuration), info);

        return connection;
    }

    /**
     * Builds a JDBC URL for a datasource.
     * 
     * @param configuration the datasource configuration
     * @return the URL
     * @throws DatabaseDefinitionException in case the datasource configuration is incorrect
     */
    private String getJdbcUrl(final DatabaseConfigurationDTO configuration) throws DatabaseDefinitionException {
        // Should we check and sanitize input values ?
        if (configuration.getDatabaseType() == null) {
            throw new DatabaseDefinitionException("Database type is null in " + configuration.toString());
        }
        switch (configuration.getDatabaseType()) {
            case POSTGRES:
                return getPostgresJdbcUrl(configuration);
            case MYSQL:
                return getMySQLJdbcUrl(configuration);
            case ORACLE:
                return getOracleJdbcUrl(configuration);
            case JTDS:
                return getJTDSJdbcUrl(configuration);
            case NUODB:
                return getNuoDBJdbcUrl(configuration);
            case GOOGLECLOUDSQL:
                return getMySQLJdbcUrl(configuration);
            default:
                throw new DatabaseDefinitionException("Unknown database type "
                                                      + configuration.getDatabaseType()
                                                      + " in "
                                                      + configuration.toString());
        }
    }

    /**
     * Builds a JDBC URL for a PostgreSQL datasource.
     * 
     * @param configuration the datasource configuration
     * @return the URL
     * @throws DatabaseDefinitionException in case the datasource configuration is incorrect
     */
    private String getPostgresJdbcUrl(final DatabaseConfigurationDTO configuration) {
        String url = MessageFormat.format(URL_TEMPLATE_POSTGRES,
                                          configuration.getHostName(),
                                          Integer.toString(configuration.getPort()),
                                          configuration.getDatabaseName());
        return url;
    }

    /**
     * Builds a JDBC URL for a MySQL datasource.
     * 
     * @param configuration the datasource configuration
     * @return the URL
     * @throws DatabaseDefinitionException in case the datasource configuration is incorrect
     */
    private String getMySQLJdbcUrl(final DatabaseConfigurationDTO configuration) {
        String url = MessageFormat.format(URL_TEMPLATE_MYSQL,
                                          configuration.getHostName(),
                                          Integer.toString(configuration.getPort()),
                                          configuration.getDatabaseName());
        return url;
    }

    /**
     * Builds a JDBC URL for an Oracle datasource.
     * 
     * @param configuration the datasource configuration
     * @return the URL
     * @throws DatabaseDefinitionException in case the datasource configuration is incorrect
     */
    private String getOracleJdbcUrl(final DatabaseConfigurationDTO configuration) {
        String url = MessageFormat.format(URL_TEMPLATE_ORACLE,
                                          configuration.getHostName(),
                                          Integer.toString(configuration.getPort()),
                                          configuration.getDatabaseName());
        return url;
    }

    /**
     * Builds a JDBC URL for a JTDS/MsSQL datasource.
     * 
     * @param configuration the datasource configuration
     * @return the URL
     * @throws DatabaseDefinitionException in case the datasource configuration is incorrect
     */
    private String getJTDSJdbcUrl(final DatabaseConfigurationDTO configuration) {
        String url = MessageFormat.format(URL_TEMPLATE_JTDS,
                                          configuration.getHostName(),
                                          Integer.toString(configuration.getPort()),
                                          configuration.getDatabaseName());
        return url;
    }

    /**
     * Builds a JDBC URL for a NuoDB datasource.
     * 
     * @param configuration the datasource configuration
     * @return the URL
     * @throws DatabaseDefinitionException in case the datasource configuration is incorrect
     */
    private String getNuoDBJdbcUrl(final DatabaseConfigurationDTO configuration) throws DatabaseDefinitionException {
        if (configuration.getBrokers() == null || configuration.getBrokers().isEmpty()) {
            throw new DatabaseDefinitionException("no brokers configured");
        }
        StringBuilder hostPart = new StringBuilder();
        boolean first = true;
        for (final NuoDBBrokerDTO brokerConf : configuration.getBrokers()) {
            if (first) {
                first = false;
            } else {
                hostPart.append(",");
            }
            hostPart.append(brokerConf.getHostName())
                    .append(":")
                    .append(brokerConf.getPort());
        }
        String url = MessageFormat.format(URL_TEMPLATE_NUODB,
                                          hostPart.toString(),
                                          configuration.getDatabaseName());
        return url;
    }
}
