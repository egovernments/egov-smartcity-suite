package org.egov.infra.filestore.service.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.filestore.FileStoreMap;
import org.egov.infra.filestore.service.impl.LocalDiskFileStoreService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LocalDiskFileStoreServiceTest {
    private  Path tempFilePath = Paths.get(System.getProperty("user.home")+"/testtmpr");
    private  Path storePath = Paths.get(System.getProperty("user.home")+"/testfilestore");
    private LocalDiskFileStoreService diskFileService;
    
    private void deleteTempFiles(final File newFile, final FileStoreMap map) throws IOException {
	Files.deleteIfExists(newFile.toPath());
	Files.deleteIfExists(Paths.get(storePath.toString(), map.getUniqueId().toString()));
    }

    private File createTempFileWithContent() throws IOException {
	final File newFile = Files.createTempFile(tempFilePath, "xyz", "txt").toFile();
	FileUtils.write(newFile, "Test");
	return newFile;
    }
    
    @Before
    public void beforeTest() throws IOException {
	if (!Files.exists(tempFilePath))
	    Files.createDirectories(tempFilePath);
	diskFileService = new LocalDiskFileStoreService(storePath.toString());
    }

    @After
    public void afterTest() throws IOException {
	Files.deleteIfExists(tempFilePath);
	Files.deleteIfExists(storePath);
    }

    @Test
    public final void testUploadFile() throws IOException {
	final File newFile = createTempFileWithContent();
	final FileStoreMap map = diskFileService.store(newFile);
	deleteTempFiles(newFile, map);
	assertNotNull(map.getUniqueId());
    }

    @Test(expected = EGOVRuntimeException.class)
    public final void testUploadFileFail() throws IOException {
	final File newFile = new File(tempFilePath.toString() + "file.txt");
	diskFileService.store(newFile);
    }

    @Test
    public final void testUploadInputStream() throws IOException {
	final File newFile = createTempFileWithContent();
	final FileInputStream fin = new FileInputStream(newFile);
	final FileStoreMap map = diskFileService.store(fin);
        fin.close();
	deleteTempFiles(newFile, map);
	assertNotNull(map.getUniqueId());
    }

    @Test
    public final void testUploadSetOfFile() throws IOException {
	Set<File> files = new HashSet<File>();
	for(int no=0; no <10 ; no++)  {
	    final File newFile = Files.createTempFile(tempFilePath, "xyz"+no, "txt").toFile();
	    FileUtils.write(newFile, "Test");
	    files.add(newFile);
	}
	final Set<FileStoreMap> maps = diskFileService.store(files);
	for(File file : files) {
	    Files.deleteIfExists(file.toPath());
	}
	for(FileStoreMap map : maps) {
	    assertNotNull(map.getUniqueId());
	    Files.deleteIfExists(Paths.get(storePath.toString(), map.getUniqueId().toString()));
	}
    }

    @Test
    public final void testUploadStreams() throws IOException {
	Set<InputStream> files = new HashSet<InputStream>();
	for(int no=0; no <10 ; no++)  {
	    final File newFile = Files.createTempFile(tempFilePath, "xyz"+no, "txt").toFile();
	    FileUtils.write(newFile, "Test");
	    FileInputStream fin = new FileInputStream(newFile);
	    files.add(fin);
	}
	final Set<FileStoreMap> maps = diskFileService.storeStreams(files);
	for (InputStream in :  files) {
	    in.close();
	}
	FileUtils.deleteDirectory(tempFilePath.toFile());
	for(FileStoreMap map : maps) {
	    assertNotNull(map.getUniqueId());
	    Files.deleteIfExists(Paths.get(storePath.toString(), map.getUniqueId().toString()));
	}
    }
    
    @Test
    public final void testFetch() throws IOException {
	final File newFile = createTempFileWithContent();
	final FileStoreMap map = diskFileService.store(newFile);
	final File file = diskFileService.fetch(map);
	assertNotNull(file);
	assertTrue(file.getName().equals(map.getUniqueId().toString()));
	deleteTempFiles(newFile, map);
    }

    @Test
    public final void testFetchAll()  throws IOException {
	Set<File> files = new HashSet<File>();
	for(int no=0; no <10 ; no++)  {
	    final File newFile = Files.createTempFile(tempFilePath, "xyz"+no, "txt").toFile();
	    FileUtils.write(newFile, "Test");
	    files.add(newFile);
	}
	final Set<FileStoreMap> maps = diskFileService.store(files);
	final Set<File> returnfiles = diskFileService.fetchAll(maps);
	assertNotNull(returnfiles);
	assertTrue(returnfiles.size()== 10);
	
	for(File file : files) {
	    Files.deleteIfExists(file.toPath());
	}
	for(FileStoreMap map : maps) {
	    Files.deleteIfExists(Paths.get(storePath.toString(), map.getUniqueId().toString()));
	}
    }

}
