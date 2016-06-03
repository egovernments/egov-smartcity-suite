/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */
package org.egov.ptis.web.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.core.header.ContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.FormDataParam;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.aadhaar.webservice.client.AadhaarInfoServiceClient;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.persistence.entity.enums.Gender;
import org.egov.infra.persistence.entity.enums.UserType;
import org.egov.infra.rest.client.SimpleRestClient;
import org.egov.infra.web.utils.WebUtils;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.pims.commons.Position;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Document;
import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.entity.property.PropertyMutationMaster;
import org.egov.ptis.domain.entity.property.PropertyMutationTransferee;
import org.egov.ptis.domain.model.ErrorDetails;
import org.egov.ptis.domain.model.MasterCodeNamePairDetails;
import org.egov.ptis.domain.model.OwnerDetails;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.domain.service.transfer.PropertyTransferService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.egov.ptis.constants.PropertyTaxConstants.NATURE_TITLE_TRANSFER;

/**
 * The PropertyTransferRestService class is used as the RESTFul service to
 * handle user request and response.
 * 
 * @author Pradeep
 */
@Component
@Path("/transfer")
public class PropertyTransferRestService {
    private static final String WTMS_TAXDUE_RESTURL = "%s/wtms/rest/watertax/due/byptno/%s";
    private static final String DOCUMENT_TYPE_ADDRESSPROOF = "Address Proof Of Parties";
    private static final String DOCUMENT_TYPE_PROPERTYDOCUMENT = "Attested Copies Of Property Documents";
    private static final String DOCUMENT_TYPE_DEEDISSUEDBYREVENUEDEPT = "Title Deeds Issued By Revenue Department";

    @Autowired
    private PropertyTransferService propertyTransferService;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<PropertyMutation> transferWorkflowService;
    @Autowired
    private AadhaarInfoServiceClient aadhaarInfoServiceClient;
    @Autowired
    private SimpleRestClient simpleRestClient;
    @Autowired
    private EisCommonService eisCommonService;

    @Context
    HttpServletRequest context;

    String USER_NAME = "mahesh";
    String PASSWORD = "demo";
    String LOGIN_USERID = "16";

    /**
     * @param accessmentnumber
     * @param username
     * @param password
     * @param details
     * @param receivedon
     * @param recievedBy
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @POST
    @Path("/propertyTransfer/createPropertyTransfer")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({ MediaType.MULTIPART_FORM_DATA })
    public String createRevisionPetitionFromRest(@FormDataParam("accessmentnumber") String accessmentnumber,
            @FormDataParam("username") String username, @FormDataParam("password") String password,
            @FormDataParam("ownerDetails") String ownerDetails,
            @FormDataParam("mutationReasonCode") String mutationReason, @FormDataParam("saleDetail") String saleDetail,
            @FormDataParam("deedNo") String deedNo, @FormDataParam("deedDate") String deedDate,
            FormDataMultiPart formTransferDocument) throws JsonGenerationException, JsonMappingException, IOException {
        List<DocumentType> documentTypes = new ArrayList<>();
        String responseJson = new String();
        PropertyMutationMaster mutationMaster = null;
        BasicProperty basicProperty = null;

        Boolean isAuthenticatedUser = authenticateUser(username, password);
        if (isAuthenticatedUser) {

            ApplicationThreadLocals.setUserId(Long.valueOf(LOGIN_USERID));

            List<OwnerDetails> ownerDetailsList = null;
            if (ownerDetails != null && ownerDetails.trim().length() > 0)
                ownerDetailsList = new ObjectMapper().readValue(ownerDetails.toString(),
                        new TypeReference<Collection<OwnerDetails>>() {
                        });
            documentTypes = propertyTransferService.getPropertyTransferDocumentTypes();

            if (accessmentnumber != null)
                basicProperty = propertyTransferService.getBasicPropertyByUpicNo(accessmentnumber);
            if (mutationReason != null)
                mutationMaster = propertyTransferService.getPropertyTransferReasonsByCode(mutationReason);

            ErrorDetails errorDetails = validateTransferPropertyParams(formTransferDocument, documentTypes,
                    mutationMaster, accessmentnumber, basicProperty, ownerDetailsList, mutationReason, saleDetail,
                    deedNo, deedDate);
            if (null != errorDetails) {
                responseJson = getJSONResponse(errorDetails);
            } else {

                 List<Document> documents = getDocumentList(formTransferDocument, documentTypes);

                PropertyMutation propertyMutation = buildPropertyMutationObject(mutationReason, saleDetail, deedNo,
                        deedDate, ownerDetailsList, basicProperty);

                if (documents != null && documents.size() > 0)
                    propertyMutation.setDocuments(documents);

                transitionWorkFlow(propertyMutation, basicProperty);
                propertyTransferService.initiatePropertyTransfer(basicProperty, propertyMutation);
                responseJson = convertPropertyMutationToJson(propertyMutation);
            }
        } else {
            ErrorDetails errorDetails = getInvalidCredentialsErrorDetails();
            responseJson = getJSONResponse(errorDetails);
            ;
        }
        return responseJson;
    }
    /**
     * 
     * @param username
     * @param password
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @POST
    @Path("/propertyTransfer/getMutationReasons")
    @Produces(MediaType.APPLICATION_JSON)
    public String getMutationReasons(@FormParam("username") String username, @FormParam("password") String password)
                    throws JsonGenerationException, JsonMappingException, IOException {
            List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
            ErrorDetails errorDetails = null;
            String responseJson = null;
            Boolean isAuthenticatedUser = authenticateUser(username, password);
            if (isAuthenticatedUser) {
                List<PropertyMutationMaster>  mutationMasterList = propertyTransferService.getPropertyTransferReasons();
                
                for(PropertyMutationMaster mutationMaster: mutationMasterList )
                    {
                    MasterCodeNamePairDetails masterCodeObj= new MasterCodeNamePairDetails();
                        masterCodeObj.setCode(mutationMaster.getCode());
                        masterCodeObj.setName(mutationMaster.getMutationName());
                        mstrCodeNamePairDetailsList.add(masterCodeObj);
                    }
                    responseJson = getJSONResponse(mstrCodeNamePairDetailsList);
            } else {
                    errorDetails = getInvalidCredentialsErrorDetails();
                    responseJson = getJSONResponse(errorDetails);
            }
            return responseJson;
    }
    /**
     * 
     * @param username
     * @param password
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @POST
    @Path("/propertyTransfer/getTransferDocumentType")
    @Produces(MediaType.APPLICATION_JSON)
    public String getPropertyTransferDocumentType(@FormParam("username") String username, @FormParam("password") String password)
                    throws JsonGenerationException, JsonMappingException, IOException {
            List<MasterCodeNamePairDetails> mstrCodeNamePairDetailsList = new ArrayList<MasterCodeNamePairDetails>();
            ErrorDetails errorDetails = null;
            String responseJson = null;
            Boolean isAuthenticatedUser = authenticateUser(username, password);
            if (isAuthenticatedUser) {
                List<DocumentType>  documentTypeList = propertyTransferService.getPropertyTransferDocumentTypes();
                
                for(DocumentType documentType: documentTypeList )
                    
                    {
                    MasterCodeNamePairDetails masterCodeObj= new MasterCodeNamePairDetails();
                        masterCodeObj.setCode(documentType.getName());
                        masterCodeObj.setName(documentType.getName());
                        mstrCodeNamePairDetailsList.add(masterCodeObj);
                    }
                    responseJson = getJSONResponse(mstrCodeNamePairDetailsList);
            } else {
                    errorDetails = getInvalidCredentialsErrorDetails();
                    responseJson = getJSONResponse(errorDetails);
            }
            return responseJson;
    }
    
/**
 * 
 * @param object
 * @return
 */
    private String convertPropertyMutationToJson(final Object object) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.registerTypeAdapter(PropertyMutation.class, new PropertyMutationAdaptor()).create();
        String json = gson.toJson(object);
        return json;
    }
/**
 * 
 * @param formTransferDocument
 * @param documentTypes
 * @return
 */
    private List<Document> getDocumentList(FormDataMultiPart formTransferDocument, List<DocumentType> documentTypes) {
        InputStream fileInputStream;
        ContentDisposition headerOfFilePart;
        List<FormDataBodyPart> fields;
        List<Document> documents = new ArrayList<Document>();
        DocumentType documentType = null;
        Document document = null;

        fields = formTransferDocument.getFields("addressProofOfParties");

        if (fields != null) {
            for (FormDataBodyPart field : fields) {
                fileInputStream = (field.getValueAs(InputStream.class));
                headerOfFilePart = field.getContentDisposition();

                if (fileInputStream != null && headerOfFilePart != null) {
                    documentType = getDocumentTypeByCode(DOCUMENT_TYPE_ADDRESSPROOF, documentTypes);
                    document = createDocument(fileInputStream, headerOfFilePart);
                    document.setType(documentType);
                    documents.add(document);
                }
            }
        }
        fields = formTransferDocument.getFields("attestedPropertyDocument");
        if (fields != null) {
            for (FormDataBodyPart field : fields) {
                fileInputStream = (field.getValueAs(InputStream.class));
                headerOfFilePart = field.getContentDisposition();

                if (fileInputStream != null && headerOfFilePart != null) {
                    documentType = getDocumentTypeByCode(DOCUMENT_TYPE_PROPERTYDOCUMENT, documentTypes);
                    document = createDocument(fileInputStream, headerOfFilePart);
                    document.setType(documentType);
                    documents.add(document);
                }
            }
        }
        fields = formTransferDocument.getFields("titleDeedDocument");
        if (fields != null) {
            for (FormDataBodyPart field : fields) {
                fileInputStream = (field.getValueAs(InputStream.class));
                headerOfFilePart = field.getContentDisposition();

                if (fileInputStream != null && headerOfFilePart != null) {
                    documentType = getDocumentTypeByCode(DOCUMENT_TYPE_DEEDISSUEDBYREVENUEDEPT, documentTypes);
                    document = createDocument(fileInputStream, headerOfFilePart);
                    document.setType(documentType);
                    documents.add(document);
                }
            }
        }

        return documents;
    }
/**
 * 
 * @param fileInputStream
 * @param headerOfFilePart
 * @return
 */
    private Document createDocument(InputStream fileInputStream, ContentDisposition headerOfFilePart) {

        Document document = new Document();
        List<File> files = new ArrayList<File>();
        List<String> contentTypes = new ArrayList<String>();
        List<String> fileNames = new ArrayList<String>();
        File file = null;
        if (fileInputStream != null && headerOfFilePart != null) {
            fileNames.add(headerOfFilePart.getFileName());
            document.setUploadsFileName(fileNames);
            file = writeToFile(fileInputStream, headerOfFilePart.getFileName());
            files.add(file);
            document.setUploads(files);
            contentTypes.add(MessageFormat.format(PropertyTaxConstants.THIRD_PARTY_CONTENT_TYPE,
                    FilenameUtils.getExtension(file.getPath())));
            document.setUploadsContentType(contentTypes);
        }
        return document;

    }
/**
 * 
 * @param uploadedInputStream
 * @param fileName
 * @return
 */
    private File writeToFile(InputStream uploadedInputStream, String fileName) {
        File file = new File(fileName);
        try {
            OutputStream out = new FileOutputStream(new File(fileName));
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
/**
 * 
 * @param thirdPartyPhotoOfAssessmentCode
 * @param documentTypes
 * @return
 */
    private DocumentType getDocumentTypeByCode(String thirdPartyPhotoOfAssessmentCode, List<DocumentType> documentTypes) {
        for (DocumentType docType : documentTypes) {
            if (docType.getName().equalsIgnoreCase(thirdPartyPhotoOfAssessmentCode))
                return docType;
        }
        return null;
    }
/**
 * 
 * @param assessmentNo
 * @return
 */
    public BigDecimal getWaterTaxDues(final String assessmentNo) {
        final String wtmsRestURL = String.format(WTMS_TAXDUE_RESTURL, WebUtils.extractRequestDomainURL(context, false),
                assessmentNo);
        final HashMap<String, Object> waterTaxInfo = simpleRestClient.getRESTResponseAsMap(wtmsRestURL);
        return waterTaxInfo.get("totalTaxDue") == null ? BigDecimal.ZERO : new BigDecimal(
                Double.valueOf((Double) waterTaxInfo.get("totalTaxDue")));
    }
/**
 * 
 * @param mutationReason
 * @param saleDetail
 * @param deedNo
 * @param deedDate
 * @param ownerDetailsList
 * @param basicProperty
 * @return
 */
    private PropertyMutation buildPropertyMutationObject(String mutationReason, String saleDetail, String deedNo,
            String deedDate, List<OwnerDetails> ownerDetailsList, BasicProperty basicProperty) {
        PropertyMutation propertyMutation = new PropertyMutation();
        propertyMutation.setBasicProperty(basicProperty);
        SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            propertyMutation.setDeedDate(dateformat.parse(deedDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        propertyMutation.setDeedNo(deedNo);
        propertyMutation.setSaleDetail(saleDetail);
        propertyMutation.setMutationReason(propertyTransferService.getPropertyTransferReasonsByCode(mutationReason));
        propertyMutation.setTransfereeInfos(getPropertyOwnerInfoList(ownerDetailsList,propertyMutation));
        return propertyMutation;
    }
/**
 * 
 * @param propertyMutation
 * @param basicProperty
 */
    private void transitionWorkFlow(PropertyMutation propertyMutation, BasicProperty basicProperty) {
        String currentState = "Created";
        Position pos = null;
        DateTime currentDate = new DateTime();
        User user = propertyTransferService.getLoggedInUser();
        Assignment assignment = propertyService.getUserPositionByZone(basicProperty);
        pos = assignment.getPosition();

        if (null == propertyMutation.getState()) {
            final WorkFlowMatrix wfmatrix = transferWorkflowService.getWfMatrix(propertyMutation.getStateType(), null,
                    null, null, currentState, null);
            if (pos != null)
                user = eisCommonService.getUserForPosition(pos.getId(), new Date());

            propertyMutation.transition().start().withSenderName(user.getName()).withComments("")
                    .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(pos)
                    .withSenderName((user != null && user.getName() != null) ? user.getName() : "").withOwner(user)
                    .withNextAction(wfmatrix.getNextAction()).withNatureOfTask(NATURE_TITLE_TRANSFER);
        }

    }
/**
 * 
 * @param ownerDetailsList
 * @return
 */
    private List<PropertyMutationTransferee> getPropertyOwnerInfoList(List<OwnerDetails> ownerDetailsList,PropertyMutation propertyMutation) {
        List<PropertyMutationTransferee> proeprtyOwnerInfoList = new ArrayList<PropertyMutationTransferee>();
        for (OwnerDetails ownerDetais : ownerDetailsList) {
            PropertyMutationTransferee pmt = new PropertyMutationTransferee();
            User owner = new User();
            owner.setAadhaarNumber(ownerDetais.getAadhaarNo());
            owner.setSalutation(ownerDetais.getSalutationCode());
            owner.setType(UserType.CITIZEN);
            owner.setName(ownerDetais.getName());
            owner.setGender(Gender.valueOf(ownerDetais.getGender()));
            owner.setMobileNumber(ownerDetais.getMobileNumber());
            owner.setEmailId(ownerDetais.getEmailId());
            owner.setGuardianRelation(ownerDetais.getGuardianRelation());
            owner.setGuardian(ownerDetais.getGuardian());
            pmt.setTransferee(owner);
            pmt.setPropertyMutation(propertyMutation);
            proeprtyOwnerInfoList.add(pmt);
        }
        return proeprtyOwnerInfoList;
    }
/**
 * 
 * @param formTransferDocument
 * @param documentTypes
 * @param mutationMaster
 * @param accessmentnumber
 * @param basicProperty
 * @param ownerDetailsList
 * @param mutationReason
 * @param saleDetail
 * @param deedNo
 * @param deedDate
 * @return
 */
    private ErrorDetails validateTransferPropertyParams(FormDataMultiPart formTransferDocument,
            List<DocumentType> documentTypes, PropertyMutationMaster mutationMaster, String accessmentnumber,
            BasicProperty basicProperty, List<OwnerDetails> ownerDetailsList, String mutationReason, String saleDetail,
            String deedNo, String deedDate) {
        BigDecimal currentPropertyTaxDue;
        BigDecimal currentWaterTaxDue;
        BigDecimal arrearPropertyTaxDue;
        ErrorDetails errorDetails = null;
        if (accessmentnumber == null || accessmentnumber.trim().length() == 0) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_ASSESSMENT_NO_REQUIRED);
            errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_ASSESSMENT_NO_REQUIRED);
        } else {

            if (basicProperty == null) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_ASSESSMENT_NO_NOT_FOUND);
                errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_ASSESSMENT_NO_NOT_FOUND);
            } else if (accessmentnumber.trim().length() > 0 && accessmentnumber.trim().length() < 10) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_ASSESSMENT_NO_LEN);
                errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_ASSESSMENT_NO_LEN);
            } else if (basicProperty != null && basicProperty.isUnderWorkflow()) {
                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_PROPERTYTRANSFER_ALREADYINWORKFLOW);
                errorDetails
                        .setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_PROPERTYTRANSFER_ALREADYINWORKFLOW);
            }

            currentWaterTaxDue = getWaterTaxDues(accessmentnumber);
            Map<String, BigDecimal> propertyTaxDetails = propertyService.getCurrentPropertyTaxDetails(basicProperty
                    .getActiveProperty());
            currentPropertyTaxDue = propertyTaxDetails.get(PropertyTaxConstants.CURR_DMD_STR).subtract(
                    propertyTaxDetails.get(PropertyTaxConstants.CURR_COLL_STR));
            arrearPropertyTaxDue = propertyTaxDetails.get(PropertyTaxConstants.ARR_DMD_STR).subtract(
                    propertyTaxDetails.get(PropertyTaxConstants.ARR_COLL_STR));

            if (currentWaterTaxDue.add(currentPropertyTaxDue).add(arrearPropertyTaxDue).longValue() > 0) {

                errorDetails = new ErrorDetails();
                errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_PROPERTYTRANSFER_TAXPENDING);
                errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_PROPERTYTRANSFER_TAXPENDING);

            }

        }
        if (documentTypes != null) {
            for (final DocumentType docTypes : documentTypes) {
                if (docTypes.isMandatory()) {

                    if (docTypes.getName().equalsIgnoreCase(DOCUMENT_TYPE_ADDRESSPROOF)) {
                        if (!checkDocumentDetailsAvailable(DOCUMENT_TYPE_ADDRESSPROOF, formTransferDocument)) {
                            errorDetails = new ErrorDetails();
                            errorDetails
                                    .setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_PROPERTYTRANSFER_REQUIREDDOCUMENTMISSING);
                            errorDetails
                                    .setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_PROPERTYTRANSFER_REQUIREDDOCUMENTMISSING
                                            + DOCUMENT_TYPE_ADDRESSPROOF);

                            break;
                        }

                    } else if (docTypes.getName().equalsIgnoreCase(DOCUMENT_TYPE_PROPERTYDOCUMENT)) {
                        if (!checkDocumentDetailsAvailable(DOCUMENT_TYPE_PROPERTYDOCUMENT, formTransferDocument)) {
                            errorDetails = new ErrorDetails();
                            errorDetails
                                    .setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_PROPERTYTRANSFER_REQUIREDDOCUMENTMISSING);
                            errorDetails
                                    .setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_PROPERTYTRANSFER_REQUIREDDOCUMENTMISSING
                                            + DOCUMENT_TYPE_ADDRESSPROOF);
                            break;
                        }

                    }
                    if (docTypes.getName().equalsIgnoreCase(DOCUMENT_TYPE_DEEDISSUEDBYREVENUEDEPT)) {
                        if (!checkDocumentDetailsAvailable(DOCUMENT_TYPE_DEEDISSUEDBYREVENUEDEPT, formTransferDocument)) {
                            errorDetails = new ErrorDetails();
                            errorDetails
                                    .setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_PROPERTYTRANSFER_REQUIREDDOCUMENTMISSING);
                            errorDetails
                                    .setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_PROPERTYTRANSFER_REQUIREDDOCUMENTMISSING
                                            + DOCUMENT_TYPE_ADDRESSPROOF);
                            break;
                        }

                    }

                }
            }
        }

        if (mutationMaster == null) {
            errorDetails = new ErrorDetails();
            errorDetails
                    .setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_PROPERTYTRANSFER_MUTATIONREASON_MANDATORY);
            errorDetails
                    .setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_PROPERTYTRANSFER_MUTATIONREASON_MANDATORY);
        } else if (ownerDetailsList == null || ownerDetailsList.size() == 0) {
            errorDetails = new ErrorDetails();
            errorDetails
                    .setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_PROPERTYTRANSFER_TRANSFEREENAME_MANDATORY);
            errorDetails
                    .setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_PROPERTYTRANSFER_TRANSFEREENAME_MANDATORY);
        } else if (ownerDetailsList != null && ownerDetailsList.size() > 0) {
            for (OwnerDetails owner : ownerDetailsList) {

                if (!StringUtils.isBlank(owner.getAadhaarNo())) {
                    try {
                        if (aadhaarInfoServiceClient.getAadhaarInfo(owner.getAadhaarNo()) != null) {
                            errorDetails = new ErrorDetails();
                            errorDetails
                                    .setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_AADHAAR_NUMBER_NOTEXISTS);
                            errorDetails
                                    .setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_AADHAAR_NUMBER_NOTEXISTS
                                            + owner.getAadhaarNo());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        errorDetails = new ErrorDetails();
                        errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_AADHAAR_NUMBER_NOTEXISTS);
                        errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_AADHAAR_NUMBER_NOTEXISTS
                                + owner.getAadhaarNo());
                    }
                } else if (StringUtils.isBlank(owner.getName())) {
                    errorDetails = new ErrorDetails();
                    errorDetails
                            .setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_PROPERTYTRANSFER_TRANSFEREE_NAMEMANDATORY);
                    errorDetails
                            .setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_PROPERTYTRANSFER_TRANSFEREE_NAMEMANDATORY);
                } else if (StringUtils.isBlank(owner.getMobileNumber())) {
                    errorDetails = new ErrorDetails();
                    errorDetails
                            .setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_PROPERTYTRANSFER_TRANSFEREE_MOBILENUMBERMANDATORY);
                    errorDetails
                            .setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_PROPERTYTRANSFER_TRANSFEREE_MOBILENUMBERMANDATORY);
                } else if (StringUtils.isBlank(owner.getGender())) {
                    errorDetails = new ErrorDetails();
                    errorDetails
                            .setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_PROPERTYTRANSFER_TRANSFEREE_GENDERMANDATORY);
                    errorDetails
                            .setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_PROPERTYTRANSFER_TRANSFEREE_GENDERMANDATORY);
                }
            }

        } else if (mutationReason == null || mutationReason.trim().length() == 0) {
            errorDetails = new ErrorDetails();
            errorDetails
                    .setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_PROPERTYTRANSFER_MUTATIONREASON_MANDATORY);
            errorDetails
                    .setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_PROPERTYTRANSFER_MUTATIONREASON_MANDATORY);
        } else if (mutationReason != null && mutationReason.equalsIgnoreCase("SALE")
                && (saleDetail == null || saleDetail.trim().length() == 0)) {
            errorDetails = new ErrorDetails();
            errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_PROPERTYTRANSFER_SALEDETAIL_MANDATORY);
            errorDetails
                    .setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_PROPERTYTRANSFER_SALEDETAIL_MANDATORY);
        } else if (deedNo == null || deedNo.trim().length() == 0) {
            errorDetails = new ErrorDetails();
            errorDetails
                    .setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_PROPERTYTRANSFER_MUTATIONDEEDNUMBER_MANDATORY);
            errorDetails
                    .setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_PROPERTYTRANSFER_MUTATIONDEEDNUMBER_MANDATORY);
        } else if (deedDate == null || deedDate.trim().length() == 0) {
            errorDetails = new ErrorDetails();
            errorDetails
                    .setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_PROPERTYTRANSFER_MUTATIONRDEEDDATE_MANDATORY);
            errorDetails
                    .setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_PROPERTYTRANSFER_MUTATIONRDEEDDATE_MANDATORY);
        }

        return errorDetails;
    }
/**
 * 
 * @param documentTypeAddressproof
 * @param formTransferDocument
 * @return
 */
    private boolean checkDocumentDetailsAvailable(String documentTypeAddressproof,
            FormDataMultiPart formTransferDocument) {
        if (formTransferDocument != null) {
            if (documentTypeAddressproof != null
                    && documentTypeAddressproof.equals(DOCUMENT_TYPE_ADDRESSPROOF)
                    && (formTransferDocument.getFields("addressProofOfParties") == null || formTransferDocument
                            .getFields("addressProofOfParties").size() == 0)) {
                return false;
            } else if (documentTypeAddressproof != null
                    && documentTypeAddressproof.equals(DOCUMENT_TYPE_PROPERTYDOCUMENT)
                    && (formTransferDocument.getFields("attestedPropertyDocument") == null || formTransferDocument
                            .getFields("attestedPropertyDocument").size() == 0)) {
                return false;
            } else if (documentTypeAddressproof != null
                    && documentTypeAddressproof.equals(DOCUMENT_TYPE_DEEDISSUEDBYREVENUEDEPT)
                    && (formTransferDocument.getFields("titleDeedDocument") == null || formTransferDocument.getFields(
                            "titleDeedDocument").size() == 0)) {
                return false;
            }
        }
        return true;
    }
/**
 * 
 * @param obj
 * @return
 * @throws JsonGenerationException
 * @throws JsonMappingException
 * @throws IOException
 */
    private String getJSONResponse(Object obj) throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
        String jsonResponse = objectMapper.writeValueAsString(obj);
        return jsonResponse;
    }

    /**
     * This method is used to get the error details for invalid credentials.
     * 
     * @return
     */
    private ErrorDetails getInvalidCredentialsErrorDetails() {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setErrorCode(PropertyTaxConstants.THIRD_PARTY_ERR_CODE_INVALIDCREDENTIALS);
        errorDetails.setErrorMessage(PropertyTaxConstants.THIRD_PARTY_ERR_MSG_INVALIDCREDENTIALS);
        return errorDetails;
    }

    public Boolean authenticateUser(String username, String password) {
        Boolean isAuthenticated = false;

        if (username != null && password != null && username.equals(USER_NAME) && password.equals(PASSWORD)) {
            isAuthenticated = true;
        }
        return isAuthenticated;
    }

}