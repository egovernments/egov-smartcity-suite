package org.egov.search.util;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Classpath {
    public static String readAsString(String resourceName) {
        try {
            return IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static File readAsFile(String resourceName) {
        URL resource = Thread.currentThread().getContextClassLoader().getResource(resourceName);
        if (resource == null) {
            throw new RuntimeException(String.format("Resource %s not found in classpath", resourceName));
        }

        return new File(resource.getPath());
    }

    public static String filePathFor(String resourceName) {
        return readAsFile(resourceName).getAbsolutePath();
    }

    public static String directoryPathFor(String resourceName) {
        URL resource = Thread.currentThread().getContextClassLoader().getResource(resourceName);
        if (resource == null) {
            throw new RuntimeException(String.format("Resource %s not found in classpath", resourceName));
        }
        return resource.getPath();
    }
}
