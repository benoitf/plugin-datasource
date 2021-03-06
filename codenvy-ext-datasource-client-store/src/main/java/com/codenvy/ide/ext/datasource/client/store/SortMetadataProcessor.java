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

package com.codenvy.ide.ext.datasource.client.store;

import java.util.SortedMap;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.ext.datasource.shared.ColumnDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseDTO;
import com.codenvy.ide.ext.datasource.shared.SchemaDTO;
import com.codenvy.ide.ext.datasource.shared.TableDTO;

/**
 * {@link PreStoreProcessor} that sorts schemas, tables and columns in lexicographical order.
 * 
 * @author "Mickaël Leduque"
 */
public class SortMetadataProcessor implements PreStoreProcessor {

    /** The client version of the DTO factory. */
    private final DtoFactory dtoFactory;

    @Inject
    public SortMetadataProcessor(final @NotNull DtoFactory dtoFactory) {
        this.dtoFactory = dtoFactory;
    }

    @Override
    public DatabaseDTO execute(final DatabaseDTO databaseDto) throws PreStoreProcessorException {
        // create a copy
        final String json = this.dtoFactory.toJson(databaseDto);
        final DatabaseDTO modified = this.dtoFactory.createDtoFromJson(json, DatabaseDTO.class);

        sortSchemas(modified);

        return modified;
    }

    /**
     * Sort the schemas in the database metadata DTO.
     * 
     * @param database the metadata DTO.
     */
    private void sortSchemas(final DatabaseDTO database) {
        for (final SchemaDTO schema : database.getSchemas().values()) {
            sortTables(schema);
        }

        SortedMap<String, SchemaDTO> sortedSchemas = new TreeMap<String, SchemaDTO>(database.getSchemas());
        database.setSchemas(sortedSchemas);
    }

    /**
     * Sort the tables in the schema metadata DTO.
     * 
     * @param schema the metadata DTO
     */
    private void sortTables(final SchemaDTO schema) {
        for (final TableDTO table : schema.getTables().values()) {
            sortColumns(table);
        }

        SortedMap<String, TableDTO> sortedTables = new TreeMap<String, TableDTO>(schema.getTables());
        schema.setTables(sortedTables);
    }

    /**
     * Sort the columns in the table metadata DTO.
     * 
     * @param table the metadata DTO
     */
    private void sortColumns(final TableDTO table) {
        SortedMap<String, ColumnDTO> sortedColumn = new TreeMap<String, ColumnDTO>(table.getColumns());
        table.setColumns(sortedColumn);
    }
}
