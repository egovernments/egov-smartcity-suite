/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

package org.egov.infra.security.utils.scanner;

import org.egov.infra.exception.ApplicationRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ClamAVScanner implements VirusScanner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClamAVScanner.class);

    private static final byte[] INSTREAM = "zINSTREAM\0".getBytes();
    private static final Pattern FOUND_RESPONSE_PATTERN = Pattern.compile("^stream: (.+) FOUND$");
    private static final String NOT_FOUND_RESPONSE = "stream: OK";
    private static final int CHUNK = 4096;

    private static final byte[] PING = "zPING\0".getBytes();
    private static final String PONG = "PONG";

    private InetSocketAddress address;

    @Value("${virus.scanner.conn.timeout:0}")
    private int timeout;

    @Value("${virus.scanner.conn.host:localhost}")
    private String scannerHost;

    @Value("${virus.scanner.conn.port:3310}")
    private int scannerPort;

    @Value("${virus.scanner.enabled:false}")
    private boolean virusScannerEnabled;

    @PostConstruct
    public void initClamAVScanner() {
        this.address = new InetSocketAddress(scannerHost, scannerPort);
    }

    @Override
    public String scan(FileChannel fileChannel) {
        try (SocketChannel socketChannel = SocketChannel.open(this.address)) {
            socketChannel.write(ByteBuffer.wrap(INSTREAM));
            ByteBuffer size = ByteBuffer.allocate(4);
            size.clear();
            size.putInt((int) fileChannel.size()).flip();
            socketChannel.write(size);
            fileChannel.transferTo(0, (int) fileChannel.size(), socketChannel);
            size.clear();
            size.putInt(0).flip();
            socketChannel.write(size);

            return parseScannerOutput(socketChannel);
        } catch (IOException ioe) {
            throw new ApplicationRuntimeException("Error occurred while Clam AV File Channel scan", ioe);
        }
    }

    @Override
    public String scan(InputStream inputStream) {
        try (SocketChannel socketChannel = SocketChannel.open(this.address)) {
            socketChannel.write(ByteBuffer.wrap(INSTREAM));
            ByteBuffer size = ByteBuffer.allocate(4);
            byte[] b = new byte[CHUNK];
            int chunk = CHUNK;
            while (chunk == CHUNK) {
                chunk = inputStream.read(b);
                if (chunk > 0) {
                    size.clear();
                    size.putInt(chunk).flip();
                    socketChannel.write(size);
                    socketChannel.write(ByteBuffer.wrap(b, 0, chunk));
                }
            }
            size.clear();
            size.putInt(0).flip();
            socketChannel.write(size);

            return parseScannerOutput(socketChannel);
        } catch (IOException ioe) {
            throw new ApplicationRuntimeException("Error occurred while Clam AV File Inputstream scan", ioe);
        }
    }

    @Override
    public boolean enabled() {
        if (virusScannerEnabled) {
            try (SocketChannel socketChannel = SocketChannel.open(this.address)) {
                socketChannel.write(ByteBuffer.wrap(PING));
                virusScannerEnabled = PONG.equals(readResponse(socketChannel));
            } catch (IOException ioe) {
                LOGGER.warn("Error occurred while Clam AV ping", ioe);
                return false;
            }
        }
        return virusScannerEnabled;
    }

    private String parseScannerOutput(SocketChannel socketChannel) throws IOException {
        String status = readResponse(socketChannel);
        Matcher matcher = FOUND_RESPONSE_PATTERN.matcher(status);
        if (matcher.matches()) {
            LOGGER.warn("Virus found on uploaded file, Details : {}", status);
            return FOUND;
        } else if (NOT_FOUND_RESPONSE.equals(status)) {
            return NOT_FOUND;
        }
        throw new ApplicationRuntimeException(status);
    }

    private String readResponse(final SocketChannel socketChannel) throws IOException {
        socketChannel.socket().setSoTimeout(this.timeout);
        ByteBuffer data = ByteBuffer.allocate(1024);
        socketChannel.read(data);
        String status = new String(data.array());
        return status.substring(0, status.indexOf(0));
    }
}
