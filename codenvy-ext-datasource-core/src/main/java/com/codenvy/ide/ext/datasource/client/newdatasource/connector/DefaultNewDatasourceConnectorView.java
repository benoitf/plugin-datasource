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
package com.codenvy.ide.ext.datasource.client.newdatasource.connector;

import com.google.inject.ImplementedBy;

/**
 * The view of datasource wizard connectors.
 */
@ImplementedBy(DefaultNewDatasourceConnectorViewImpl.class)
public interface DefaultNewDatasourceConnectorView extends AbstractNewDatasourceConnectorView {

    /**
     * Returns the database server host.
     * 
     * @return the server host
     */
    String getHostname();


    /**
     * Returns the database server port.
     * 
     * @return the server port
     */
    int getPort();

    /**
     * Sets the port in the displayed configuration.
     * 
     * @param port the new value
     */
    void setPort(int port);

}
