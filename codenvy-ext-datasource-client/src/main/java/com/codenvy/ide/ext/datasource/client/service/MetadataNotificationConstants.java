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
package com.codenvy.ide.ext.datasource.client.service;

import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;
import com.google.gwt.i18n.client.Messages;

@DefaultLocale("en")
public interface MetadataNotificationConstants extends Messages {

    @DefaultMessage("Fetching database metadata...")
    String notificationFetchStart();

    @DefaultMessage("Succesfully fetched database metadata")
    String notificationFetchSuccess();

    @DefaultMessage("Failed fetching database metadata")
    String notificationFetchFailure();

    @DefaultMessage("Invalid configuration for this datasource")
    String invalidConfigurationNotification();
}
