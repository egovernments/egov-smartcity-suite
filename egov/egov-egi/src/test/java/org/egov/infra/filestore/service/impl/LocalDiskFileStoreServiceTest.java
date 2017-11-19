/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
package org.egov.infra.filestore.service.impl;

import org.apache.commons.io.FileUtils;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LocalDiskFileStoreServiceTest {
    private static Path tempFilePath = Paths.get(System.getProperty("user.home") + File.separator + "testtmpr");
    private LocalDiskFileStoreService diskFileService;

    @AfterClass
    public static void afterTest() throws IOException {
        Files.deleteIfExists(tempFilePath);
        Path storePath = Paths.get(System.getProperty("user.home") + File.separator + "testfilestore");
        try {
            Files.walkFileTree(storePath, new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if (exc == null) {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    } else {
                        throw exc;
                    }
                }

            });
        } catch (IOException e) {

        }
    }

    private void deleteTempFiles(final File newFile, final FileStoreMapper map) throws IOException {
        Files.deleteIfExists(newFile.toPath());
        Path storePath = Paths.get(System.getProperty("user.home") + File.separator + "testfilestore");
        Files.deleteIfExists(Paths.get(storePath.toString(), map.getFileStoreId().toString()));
    }

    private File createTempFileWithContent() throws IOException {
        final File newFile = Files.createTempFile(tempFilePath, "xyz", "txt").toFile();
        FileUtils.write(newFile, "Test", UTF_8);
        return newFile;
    }

    @Before
    public void beforeTest() throws IOException {
        if (!Files.exists(tempFilePath))
            Files.createDirectories(tempFilePath);
        diskFileService = new LocalDiskFileStoreService(System.getProperty("user.home") + File.separator + "testfilestore");
    }

    @Test
    public final void testUploadFile() throws IOException {
        final File newFile = createTempFileWithContent();
        final FileStoreMapper map = diskFileService.store(newFile, "fileName", "testmodule", "text/plain");
        deleteTempFiles(newFile, map);
        assertNotNull(map.getFileStoreId());
    }

    @Test(expected = ApplicationRuntimeException.class)
    public final void testUploadFileFail() throws IOException {
        final File newFile = new File("file.txt");
        diskFileService.store(newFile, "fileName", "testmodule", "text/plain");
    }

    @Test
    public final void testUploadInputStream() throws IOException {
        final File newFile = createTempFileWithContent();
        final FileStoreMapper map = diskFileService.store(new FileInputStream(newFile), "nofile", "text/plain", "testmodule");
        deleteTempFiles(newFile, map);
        assertNotNull(map.getFileStoreId());
    }

    @Test
    public final void testUploadSetOfFile() throws IOException {
        Set<File> files = new HashSet<>();
        for (int no = 0; no < 10; no++) {
            final File newFile = Files.createTempFile(tempFilePath, "xyz" + no, "txt").toFile();
            FileUtils.write(newFile, "Test", UTF_8);
            files.add(newFile);
        }
        for (File file : files) {
            Files.deleteIfExists(file.toPath());
        }
    }

    @Test
    public final void testUploadStreams() throws IOException {
        Set<InputStream> files = new HashSet<>();
        for (int no = 0; no < 10; no++) {
            final File newFile = Files.createTempFile(tempFilePath, "xyz" + no, "txt").toFile();
            FileUtils.write(newFile, "Test", UTF_8);
            FileInputStream fin = new FileInputStream(newFile);
            files.add(fin);
        }
        FileUtils.deleteDirectory(tempFilePath.toFile());
    }

    @Test
    public final void testFetch() throws IOException {
        final File newFile = createTempFileWithContent();
        final FileStoreMapper map = diskFileService.store(newFile, "fileName", "text/plain", "test");
        final File file = diskFileService.fetch(map, "testmodule");
        assertNotNull(file);
        assertTrue(file.getName().equals(map.getFileStoreId().toString()));
        deleteTempFiles(newFile, map);
    }

    @Test(expected = ApplicationRuntimeException.class)
    public final void testFetchFailNonExisting() throws IOException {
        final FileStoreMapper map = new FileStoreMapper(UUID.randomUUID().toString(), "fileName");
        diskFileService.fetch(map, "testmoduleNo");
    }

    @Test
    public final void testFetchAll() throws IOException {
        Set<File> files = new HashSet<>();
        for (int no = 0; no < 10; no++) {
            final File newFile = Files.createTempFile(tempFilePath, "xyz" + no, "txt").toFile();
            FileUtils.write(newFile, "Test", UTF_8);
            files.add(newFile);
        }
        final Set<FileStoreMapper> maps = new HashSet<>();
        for (File file : files)
            maps.add(diskFileService.store(file, "fileName", "text/plain", "testmodule"));

        final Set<File> returnfiles = diskFileService.fetchAll(maps, "testmodule");
        assertNotNull(returnfiles);
        assertTrue(returnfiles.size() == 10);

        for (File file : files) {
            Files.deleteIfExists(file.toPath());
        }
    }

    @Test
    public final void testDeleteFile() throws IOException {
        final File newFile = Files.createTempFile(tempFilePath, "xyz", "txt").toFile();
        FileUtils.write(newFile, "Test", UTF_8);
        FileStoreMapper fileStoreMapper = diskFileService.store(newFile, "fileName", "text/plain", "testmodule");
        diskFileService.delete(fileStoreMapper.getFileStoreId(), "testmodule");
        Files.deleteIfExists(newFile.toPath());
    }

}