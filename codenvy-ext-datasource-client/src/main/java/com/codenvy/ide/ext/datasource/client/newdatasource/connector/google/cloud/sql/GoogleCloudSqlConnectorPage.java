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
package com.codenvy.ide.ext.datasource.client.newdatasource.connector.google.cloud.sql;

import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.ext.datasource.client.DatasourceClientService;
import com.codenvy.ide.ext.datasource.client.DatasourceUiResources;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardMessages;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.DefaultNewDatasourceConnectorPage;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.DefaultNewDatasourceConnectorView;
import com.codenvy.ide.ext.datasource.client.store.DatasourceManager;
import com.codenvy.ide.ext.datasource.shared.DatabaseType;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Created by Wafa on 26/03/14.
 */
public class GoogleCloudSqlConnectorPage  extends DefaultNewDatasourceConnectorPage {

    public static final String GOOGLECLOUDSQL_DB_ID        = "googleCloudSql";
    private static final int   DEFAULT_PORT_MYSQL = 3306;

    @Inject
    public GoogleCloudSqlConnectorPage (final DefaultNewDatasourceConnectorView view,
                                        final NotificationManager notificationManager,
                                        final DtoFactory dtoFactory,
                                        final DatasourceManager datasourceManager,
                                        final EventBus eventBus,
                                        final DatasourceClientService service,
                                        final DatasourceUiResources resources,
                                        final NewDatasourceWizardMessages messages) {
        super(view, messages.googlecloudsql(), resources.getGoogleCloudSQLLogo(), GOOGLECLOUDSQL_DB_ID, datasourceManager, eventBus, service,
                notificationManager, dtoFactory, messages, DEFAULT_PORT_MYSQL, DatabaseType.GOOGLECLOUDSQL);
    }
}