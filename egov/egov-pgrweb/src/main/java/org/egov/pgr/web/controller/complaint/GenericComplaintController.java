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
package org.egov.pgr.web.controller.complaint;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.service.ReceivingCenterService;
import org.egov.pgr.utils.constants.PGRConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

public abstract class GenericComplaintController {

    public static final String ERROR = "error";
    public static final String MESSAGE = "message";

    @Autowired
    protected ComplaintTypeService complaintTypeService;

    @Autowired(required=true)
    protected ComplaintService complaintService;
    
    @Autowired
    protected ReceivingCenterService receivingCenterService;

    public @ModelAttribute("complaintTypes") List<ComplaintType> frequentlyFiledComplaintTypes() {
        return complaintTypeService.getFrequentlyFiledComplaints();
    }
    
    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;


    protected Set<FileStoreMapper> addToFileStore(final MultipartFile[] files) {
        if(ArrayUtils.isNotEmpty(files)) {
            return fileStoreService.storeStreams(Arrays.asList(files)
                    .stream()
                    .filter(file -> !file.isEmpty())
                    .map(file -> {
                        try {
                            return file.getInputStream();
                        } catch (Exception e) {
                            throw new EGOVRuntimeException("Error occurred while getting inputstream",e);
                        }
                    })
                    .collect(Collectors.toSet()), PGRConstants.MODULE_NAME);
        } else {
            return null;
        }
    }
}