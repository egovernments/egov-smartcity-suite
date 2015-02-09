package org.egov.infra.filestore.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

import org.apache.commons.io.FileUtils;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.filestore.FileStoreMapper;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class LocalDiskFileStoreServiceTest {
    private  static Path tempFilePath = Paths.get(System.getProperty("user.home")+ File.separator+"testtmpr");
    private  static Path storePath = Paths.get(System.getProperty("user.home")+ File.separator+"testfilestore");
    private LocalDiskFileStoreService diskFileService;
    
    private void deleteTempFiles(final File newFile, final FileStoreMapper map) throws IOException {
	Files.deleteIfExists(newFile.toPath());
	Files.deleteIfExists(Paths.get(storePath.toString(), map.getFileStoreId().toString()));
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

    @AfterClass
    public static void afterTest() throws IOException {
        Files.deleteIfExists(tempFilePath);
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
            e.printStackTrace();
          }
    }

    @Test
    public final void testUploadFile() throws IOException {
	final File newFile = createTempFileWithContent();
	final FileStoreMapper map = diskFileService.store(newFile, "test");
	deleteTempFiles(newFile, map);
	assertNotNull(map.getFileStoreId());
    }

    @Test(expected = EGOVRuntimeException.class)
    public final void testUploadFileFail() throws IOException {
	final File newFile = new File(tempFilePath.toString() + "file.txt");
	diskFileService.store(newFile, "test");
    }

    @Test
    public final void testUploadInputStream() throws IOException {
	final File newFile = createTempFileWithContent();
	final FileInputStream fin = new FileInputStream(newFile);
	final FileStoreMapper map = diskFileService.store(fin, "test");
        fin.close();
	deleteTempFiles(newFile, map);
	assertNotNull(map.getFileStoreId());
    }

    @Test
    public final void testUploadSetOfFile() throws IOException {
	Set<File> files = new HashSet<File>();
	for(int no=0; no <10 ; no++)  {
	    final File newFile = Files.createTempFile(tempFilePath, "xyz"+no, "txt").toFile();
	    FileUtils.write(newFile, "Test");
	    files.add(newFile);
	}
	final Set<FileStoreMapper> maps = diskFileService.store(files, "test");
	for(File file : files) {
	    Files.deleteIfExists(file.toPath());
	}
	for(FileStoreMapper map : maps) {
	    assertNotNull(map.getFileStoreId());
	    Files.deleteIfExists(Paths.get(storePath.toString(), map.getFileStoreId().toString()));
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
	final Set<FileStoreMapper> maps = diskFileService.storeStreams(files, "test");
	for (InputStream in :  files) {
	    in.close();
	}
	FileUtils.deleteDirectory(tempFilePath.toFile());
	for(FileStoreMapper map : maps) {
	    assertNotNull(map.getFileStoreId());
	    Files.deleteIfExists(Paths.get(storePath.toString(), map.getFileStoreId().toString()));
	}
    }
    
    @Test
    public final void testFetch() throws IOException {
	final File newFile = createTempFileWithContent();
	final FileStoreMapper map = diskFileService.store(newFile, "test");
	final File file = diskFileService.fetch(map);
	assertNotNull(file);
	assertTrue(file.getName().equals(map.getFileStoreId().toString()));
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
	final Set<FileStoreMapper> maps = diskFileService.store(files, "test");
	final Set<File> returnfiles = diskFileService.fetchAll(maps);
	assertNotNull(returnfiles);
	assertTrue(returnfiles.size()== 10);
	
	for(File file : files) {
	    Files.deleteIfExists(file.toPath());
	}
	for(FileStoreMapper map : maps) {
	    Files.deleteIfExists(Paths.get(storePath.toString(), map.getFileStoreId().toString()));
	}
    }

}
