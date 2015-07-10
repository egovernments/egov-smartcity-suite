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
package org.egov.api.controller.core;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.ArrayUtils;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.pgr.utils.constants.PGRConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

public abstract class ApiController {

	protected static final Logger Log = LoggerFactory.getLogger(ApiController.class);

	@PersistenceContext
	protected EntityManager entityManager;

	@Autowired
	@Qualifier("fileStoreService")
	protected FileStoreService fileStoreService;

	@Autowired
	private MessageSource messageSource;

	public String getMessage(String key) {
		return this.messageSource.getMessage(key, null, Locale.getDefault());
	}

	public ApiResponse getResponseHandler() {
		return ApiResponse.newInstance();
	}

	@ExceptionHandler({ HttpMessageNotReadableException.class })
	public ResponseEntity<String> apiExceptionHandler(HttpMessageNotReadableException ex) {
		ex.printStackTrace();
		if (ex.getRootCause() instanceof UnrecognizedPropertyException) {
			UnrecognizedPropertyException e = (UnrecognizedPropertyException) ex.getRootCause();
			Object[] r = e.getKnownPropertyIds().toArray();
			for (Object t : r) {
				System.out.println(t.toString());
			}
		}
		return getResponseHandler().error(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ MissingServletRequestParameterException.class })
	public ResponseEntity<String> apiExceptionHandler(MissingServletRequestParameterException ex) {
		ex.printStackTrace();
		return getResponseHandler().error(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ Exception.class })
	public ResponseEntity<String> apiExceptionHandler(Exception ex) {
		ex.printStackTrace();
		return getResponseHandler().error(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ MethodArgumentNotValidException.class })
	public ResponseEntity<String> apiExceptionHandler(MethodArgumentNotValidException ex) {
		return getResponseHandler().error(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}	
}
