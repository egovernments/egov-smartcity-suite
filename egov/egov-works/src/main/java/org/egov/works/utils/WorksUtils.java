/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.works.utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.works.lineestimate.entity.DocumentDetails;
import org.egov.works.lineestimate.repository.DocumentDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

public class WorksUtils {
    
    @Autowired
    private DocumentDetailsRepository documentDetailsRepository;
    
    @Autowired
    private FileStoreService fileStoreService;

    public void persistDocuments(List<DocumentDetails> documentDetailsList) {
        if(documentDetailsList != null && !documentDetailsList.isEmpty()) {
            for(DocumentDetails doc : documentDetailsList)
                documentDetailsRepository.save(doc);
        }
    }
    
    public List<DocumentDetails> getDocumentDetails(final MultipartFile[] files, Object object, String objectType) throws IOException {
        final List<DocumentDetails> documentDetailsList = new ArrayList<DocumentDetails>();
        
        Long id = null;
        Method method = null;
        try {
            method = object.getClass().getMethod("getId", null);
            id = (Long) method.invoke(object, null);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new ApplicationRuntimeException("lineestimate.document.error", e);
        }
            
        if (files != null && files.length > 0)
            for (int i = 0; i < files.length; i++)
                if (!files[i].isEmpty()) {
                    final DocumentDetails documentDetails = new DocumentDetails();
                    documentDetails.setObjectId(id);
                    documentDetails.setObjectType(objectType);
                    documentDetails.setFileStore(fileStoreService.store(files[i].getInputStream(), files[i].getOriginalFilename(),
                            files[i].getContentType(), WorksConstants.FILESTORE_MODULECODE));
                    documentDetailsList.add(documentDetails);
                }
        return documentDetailsList;
    }
    
    public List<DocumentDetails> findByObjectIdAndObjectType(Long objectId, String objectType) {
        return documentDetailsRepository.findByObjectIdAndObjectType(objectId, objectType);
    }
}
