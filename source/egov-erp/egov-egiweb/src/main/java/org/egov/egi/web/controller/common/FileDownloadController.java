package org.egov.egi.web.controller.common;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.repository.FileStoreMapperRepository;
import org.egov.infra.filestore.service.FileStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/downloadfile")
public class FileDownloadController {

    @Qualifier("localDiskFileStoreService")
    private @Autowired FileStoreService fileStoreService;
    private @Autowired FileStoreMapperRepository fileStoreMapperRepository;

    @RequestMapping
    public void download(@RequestParam final String fileStoreId, @RequestParam final String moduleName, @RequestParam(defaultValue="false") final boolean toSave,
            final HttpServletResponse response) throws IOException {
        final FileStoreMapper fileStoreMapper = fileStoreMapperRepository.findByFileStoreId(fileStoreId);
        if (fileStoreMapper != null) {
            final File file = fileStoreService.fetch(fileStoreMapper, moduleName);
            if(toSave)
                response.setHeader("Content-Disposition", "attachment;filename=" + fileStoreMapper.getFileName());
            response.setContentType(Files.probeContentType(file.toPath()));
            final OutputStream out = response.getOutputStream();
            IOUtils.write(FileUtils.readFileToByteArray(file), out);
            IOUtils.closeQuietly(out);
        }
    }
}
