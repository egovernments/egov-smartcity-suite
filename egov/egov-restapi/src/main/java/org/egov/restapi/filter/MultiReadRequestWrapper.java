/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.restapi.filter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.egov.infra.utils.ApplicationConstant.DEFAULT_CHARACTER_ENCODING;

public class MultiReadRequestWrapper extends HttpServletRequestWrapper {

    private final ByteArrayOutputStream cachedContent;
    private ServletInputStream inputStream;
    private BufferedReader reader;


    public MultiReadRequestWrapper(final HttpServletRequest request) {
        super(request);
        int contentLength = request.getContentLength();
        this.cachedContent = new ByteArrayOutputStream(contentLength >= 0 ? contentLength : 1024);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (this.inputStream == null) {
            this.inputStream = new ContentCachingInputStream(getRequest().getInputStream());
        }
        return this.inputStream;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        if (this.reader == null) {
            this.reader = new BufferedReader(new InputStreamReader(getInputStream(), getCharacterEncoding()));
        }
        return this.reader;
    }

    @Override
    public String getCharacterEncoding() {
        String enc = super.getCharacterEncoding();
        return enc == null ? DEFAULT_CHARACTER_ENCODING : enc;
    }

    public byte[] getContentAsByteArray() {
        return this.cachedContent.toByteArray();
    }

    private class ContentCachingInputStream extends ServletInputStream {

        private final ServletInputStream servletInputStream;

        public ContentCachingInputStream(ServletInputStream servletInputStream) {
            this.servletInputStream = servletInputStream;
        }

        @Override
        public int read() throws IOException {
            int ch = this.servletInputStream.read();
            if (ch != -1) {
                cachedContent.write(ch);
            }
            return ch;
        }

        @Override
        public boolean isFinished() {
            return this.servletInputStream.isFinished();
        }

        @Override
        public boolean isReady() {
            return this.servletInputStream.isReady();
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            this.servletInputStream.setReadListener(readListener);
        }
    }
}
