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

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.output.StringBuilderWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVWriter;

import com.codenvy.ide.ext.datasource.shared.ServicePaths;
import com.codenvy.ide.ext.datasource.shared.exception.CSVExportException;
import com.codenvy.ide.ext.datasource.shared.request.RequestResultDTO;
import com.codenvy.ide.ext.datasource.shared.request.SelectResultDTO;
import com.google.inject.Inject;

/**
 * Service for CSV export of SQL request results.
 * 
 * @author "Mickaël Leduque"
 */
@Path(ServicePaths.RESULT_CSV_PATH)
public class CsvExportService {

    /** The logger. */
    private static final Logger   LOG                          = LoggerFactory.getLogger(CsvExportService.class);

    public final static String    TEXT_CSV                     = "text/csv";
    public final static String    TEXT_CSV_HEADER_OPTION       = "; header=present";
    public final static String    TEXT_CSV_NO_HEADER_OPTION    = "; header=absent";
    public final static String    TEXT_CSV_CHARSET_UTF8_OPTION = "; charset=utf8";
    public final static MediaType TEXT_CSV_TYPE                = new MediaType("text", "csv");

    @Inject
    public CsvExportService() {
    }

    /**
     * Export the SQL request result as CSV string.
     * 
     * @param requestResult the result to convert
     * @return the CSV data
     * @throws CSVExportException any conversion error
     */
    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public String exportAsCSV(final RequestResultDTO requestResult) throws CSVExportException {
        if (requestResult == null) {
            throw new CSVExportException("The parameter doesn't contain a result request");
        }
        if (requestResult.getResultType() != SelectResultDTO.TYPE) {
            throw new CSVExportException("Only request results for select can be converted to CSV");
        }

        String csvResult = convertDataToCsv(requestResult, true);

        byte[] byteResult = csvResult.getBytes(StandardCharsets.UTF_8);
        String encodedResult = Base64.encodeBase64String(byteResult);
        return encodedResult;
    }

    private String convertDataToCsv(final RequestResultDTO requestResult, boolean withHeader) {
        LOG.debug("convertDataToCsv - called for {}, withHeader={}", requestResult, withHeader);

        final StringBuilder sb = new StringBuilder();
        try (
            final Writer writer = new StringBuilderWriter(sb);
            final CSVWriter csvWriter = new CSVWriter(writer)) {

            // header
            if (withHeader) {
                csvWriter.writeNext(requestResult.getHeaderLine().toArray(new String[0]));
            }

            // body
            for (final List<String> line : requestResult.getResultLines()) {
                csvWriter.writeNext(line.toArray(new String[0]));
            }
        } catch (final IOException e) {
            LOG.error("Close failed on resource - expect leaks and incorrect operation", e);
        }

        return sb.toString();
    }
}
