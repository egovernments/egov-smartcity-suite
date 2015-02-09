package org.egov.pgr.utils;

import java.io.File;
import java.io.IOException;

import org.egov.exceptions.EGOVRuntimeException;
import org.springframework.web.multipart.MultipartFile;

public class FileUtils {

    public static File multipartToFile(MultipartFile multipart) {
        try {
            File tmpFile = new File(multipart.getOriginalFilename());
            multipart.transferTo(tmpFile);
            return tmpFile;
        } catch (IllegalStateException | IOException e) {
           throw new EGOVRuntimeException("Error occurred while converting multipart to file",e);
        }
    }
}
