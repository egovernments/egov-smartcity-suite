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

package org.egov.api.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.egov.api.adapter.ComplaintActionAdapter;
import org.egov.api.adapter.ComplaintAdapter;
import org.egov.api.adapter.ComplaintStatusAdapter;
import org.egov.api.adapter.ComplaintTypeAdapter;
import org.egov.api.controller.core.ApiController;
import org.egov.api.controller.core.ApiUrl;
import org.egov.api.model.ComplaintAction;
import org.egov.infra.admin.master.entity.CrossHierarchy;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.CrossHierarchyService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.persistence.entity.enums.UserType;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.FileStoreUtils;
import org.egov.infra.utils.StringUtils;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.ComplaintStatus;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.entity.ComplaintTypeCategory;
import org.egov.pgr.entity.ReceivingMode;
import org.egov.pgr.entity.enums.CitizenFeedback;
import org.egov.pgr.service.ComplaintHistoryService;
import org.egov.pgr.service.ComplaintProcessFlowService;
import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.service.ComplaintStatusMappingService;
import org.egov.pgr.service.ComplaintStatusService;
import org.egov.pgr.service.ComplaintTypeCategoryService;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.service.PriorityService;
import org.egov.pgr.service.ReceivingModeService;
import org.egov.pgr.utils.constants.PGRConstants;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/v1.0")
public class ComplaintController extends ApiController {

    public static final String COMPLAINT_TYPE_ID = "complaintTypeId";
    public static final String COMPLAINT_DETAILS = "details";
    private static final Logger LOGGER = Logger.getLogger(ComplaintController.class);
    private static final String SATISFACTORY = "SATISFACTORY";
    private static final String UNSATISFACTORY = "UNSATISFACTORY";
    private static final String EGOV_API_ERROR = "EGOV-API ERROR ";
    private static final String SERVER_ERROR = "server.error";
    private static final String COMPLAINANT_NAME = "complainantName";
    private static final String COMPLAINANT_MOBILE_NO = "complainantMobileNo";
    private static final String COMPLAINANT_EMAIL = "complainantEmail";
    private static final String LOCATION_ID = "locationId";
    private static final String INVALID_PAGE_NUMBER_ERROR = "Invalid Page Number";
    private static final String HAS_NEXT_PAGE = "hasNextPage";
    @Autowired
    private ComplaintStatusService complaintStatusService;

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private ComplaintHistoryService complaintHistoryService;

    @Autowired
    private ComplaintTypeCategoryService complaintTypeCategoryService;

    @Autowired
    private ComplaintTypeService complaintTypeService;

    @Autowired
    private CrossHierarchyService crossHierarchyService;

    @Autowired
    private FileStoreUtils fileStoreUtils;

    @Autowired
    private ComplaintStatusMappingService complaintStatusMappingService;

    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private ReceivingModeService receivingModeService;

    @Autowired
    private PriorityService priorityService;

    @Autowired
    private ComplaintProcessFlowService complaintProcessFlowService;

    /**
     * This will returns all complaint types
     *
     * @return ComplaintType
     */
    @RequestMapping(value = {ApiUrl.COMPLAINT_TYPES_BY_CATEGORIES}, method = GET, produces = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> getAllComplaintCategories() {
        try {

            final List<ComplaintTypeCategory> complaintCategories = complaintTypeCategoryService.findAll();

            final JsonArray jAryComplaintCategories = new JsonArray();

            for (final ComplaintTypeCategory complaintTypeCategory : complaintCategories) {

                final JsonObject jComplaintCategory = new JsonObject();
                jComplaintCategory.addProperty("categoryId", complaintTypeCategory.getId());
                jComplaintCategory.addProperty("categoryName", complaintTypeCategory.getName());
                jComplaintCategory.addProperty("localName", complaintTypeCategory.getLocalName());

                final List<ComplaintType> complaintTypes = complaintTypeService
                        .findActiveComplaintTypesByCategory(complaintTypeCategory.getId());

                final JsonArray jAryComplaintTypes = new JsonArray();

                for (final ComplaintType complaintType : complaintTypes) {
                    final JsonObject jComplaintType = new JsonObject();
                    jComplaintType.addProperty("id", complaintType.getId());
                    jComplaintType.addProperty("name", complaintType.getName());
                    jComplaintType.addProperty("description", complaintType.getDescription());
                    jComplaintType.addProperty("localName", complaintType.getLocalName());
                    jAryComplaintTypes.add(jComplaintType);
                }

                jComplaintCategory.add("complaintTypes", jAryComplaintTypes);
                jAryComplaintCategories.add(jComplaintCategory);
            }

            final JsonObject jresp = new JsonObject();
            jresp.add("complaintCategories", jAryComplaintCategories);

            return getResponseHandler().success(jresp);

        } catch (final Exception e) {
            LOGGER.error(EGOV_API_ERROR, e);
            return getResponseHandler().error(getMessage(SERVER_ERROR));
        }
    }

    // --------------------------------------------------------------------------------//

    /**
     * This will returns all complaint types
     *
     * @return ComplaintType
     */
    @RequestMapping(value = {ApiUrl.COMPLAINT_GET_TYPES}, method = GET, produces = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> getAllTypes() {
        try {
            final List<ComplaintType> complaintTypes = complaintTypeService.findActiveComplaintTypes();
            return getResponseHandler().setDataAdapter(new ComplaintTypeAdapter()).success(complaintTypes);
        } catch (final Exception e) {
            LOGGER.error(EGOV_API_ERROR, e);
            return getResponseHandler().error(getMessage(SERVER_ERROR));
        }
    }

    // --------------------------------------------------------------------------------//

    /**
     * This will returns complaint types which is frequently filed.
     *
     * @return ComplaintType
     */
    @RequestMapping(value = {ApiUrl.COMPLAINT_GET_FREQUENTLY_FILED_TYPES}, method = GET, produces = {
            MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> getFrequentTypes() {
        try {
            final List<ComplaintType> complaintTypes = complaintTypeService.getFrequentlyFiledComplaints();
            return getResponseHandler().setDataAdapter(new ComplaintTypeAdapter()).success(complaintTypes);
        } catch (final Exception e) {
            LOGGER.error(EGOV_API_ERROR, e);
            return getResponseHandler().error(getMessage(SERVER_ERROR));
        }
    }

    @RequestMapping(value = ApiUrl.COMPLAINT_CREATE, method = RequestMethod.POST)
    public ResponseEntity<String> complaintCreate(
            @RequestParam(value = "json_complaint", required = false) final String complaintJSON,
            @RequestParam("files") final MultipartFile[] files) {
        try {

            final JSONObject complaintRequest = (JSONObject) JSONValue.parse(complaintJSON);

            final Complaint complaint = new Complaint();

            if (securityUtils.currentUserType().equals(UserType.EMPLOYEE))
                if (complaintRequest.containsKey(COMPLAINANT_NAME) &&
                        complaintRequest.containsKey(COMPLAINANT_MOBILE_NO)) {

                    if (org.apache.commons.lang.StringUtils.isEmpty(complaintRequest.get(COMPLAINANT_NAME).toString())
                            || org.apache.commons.lang.StringUtils
                            .isEmpty(complaintRequest.get(COMPLAINANT_MOBILE_NO).toString()))
                        return getResponseHandler().error(getMessage("msg.complaint.reg.failed.user"));

                    complaint.getComplainant().setName(complaintRequest.get(COMPLAINANT_NAME).toString());
                    complaint.getComplainant().setMobile(complaintRequest.get(COMPLAINANT_MOBILE_NO).toString());

                    if (complaintRequest.containsKey(COMPLAINANT_EMAIL)) {
                        final String email = complaintRequest.get(COMPLAINANT_EMAIL).toString();
                        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$"))
                            return getResponseHandler().error(getMessage("msg.invalid.mail"));
                        complaint.getComplainant().setEmail(email);
                    }

                } else if (!complaintRequest.containsKey(COMPLAINANT_NAME) &&
                        !complaintRequest.containsKey(COMPLAINANT_MOBILE_NO) &&
                        !complaintRequest.containsKey(COMPLAINANT_EMAIL)) {
                    final User currentUser = securityUtils.getCurrentUser();
                    complaint.getComplainant().setName(currentUser.getName());
                    complaint.getComplainant().setMobile(currentUser.getMobileNumber());
                    if (!org.apache.commons.lang.StringUtils.isEmpty(currentUser.getEmailId()))
                        complaint.getComplainant().setEmail(currentUser.getEmailId());
                } else
                    return getResponseHandler().error(getMessage("msg.complaint.reg.failed.user"));

            if (!complaintRequest.containsKey(COMPLAINT_TYPE_ID))
                return getResponseHandler().error(getMessage("msg.complaint.type.required"));

            if (!complaintRequest.containsKey(COMPLAINT_DETAILS) || StringUtils.isBlank(complaintRequest.get(COMPLAINT_DETAILS).toString()))
                return getResponseHandler().error(getMessage("msg.complaint.desc.required"));
            else if (complaintRequest.get(COMPLAINT_DETAILS).toString().length() < 10)
                return getResponseHandler().error(getMessage("msg.complaint.desc.min.required"));
            else if (complaintRequest.get(COMPLAINT_DETAILS).toString().length() > 500)
                return getResponseHandler().error(getMessage("msg.complaint.desc.max.required"));


            final long complaintTypeId = (long) complaintRequest.get(COMPLAINT_TYPE_ID);
            if (complaintRequest.get(LOCATION_ID) != null && (long) complaintRequest.get(LOCATION_ID) > 0) {
                final long locationId = (long) complaintRequest.get(LOCATION_ID);
                final CrossHierarchy crosshierarchy = crossHierarchyService.findById(locationId);
                complaint.setLocation(crosshierarchy.getParent());
                complaint.setChildLocation(crosshierarchy.getChild());
            }
            if (complaintRequest.get("lng") != null && (double) complaintRequest.get("lng") > 0) {
                final double lng = (double) complaintRequest.get("lng");
                complaint.setLng(lng);
            }
            if (complaintRequest.get("lat") != null && (double) complaintRequest.get("lat") > 0) {
                final double lat = (double) complaintRequest.get("lat");
                complaint.setLat(lat);
            }
            if (complaint.getLocation() == null && (complaint.getLat() <= 0D || complaint.getLng() <= 0D))
                return getResponseHandler().error(getMessage("location.required"));
            complaint.setDetails(complaintRequest.get(COMPLAINT_DETAILS).toString());
            complaint.setLandmarkDetails(complaintRequest.get("landmarkDetails").toString());
            if (complaintTypeId > 0) {
                final ComplaintType complaintType = complaintTypeService.findBy(complaintTypeId);
                complaint.setComplaintType(complaintType);
            }


            String receivingModeKey = "receivingMode";
            ReceivingMode receivingMode;
            String[] highPriorityComplaintSource = {"VMC", "TLC", "DRONE"};

            String priorityCode = "NORMAL";

            if (complaintRequest.get(receivingModeKey) != null && StringUtils.isNotBlank(complaintRequest.get(receivingModeKey).toString())) {
                String receivingModeVal = complaintRequest.get(receivingModeKey).toString();
                receivingMode = receivingModeService.getReceivingModeByCode(receivingModeVal);
                if (Arrays.asList(highPriorityComplaintSource).contains(receivingModeVal)) {
                    priorityCode = "HIGH";
                }
            } else {
                receivingMode = receivingModeService.getReceivingModeByCode("MOBILE");
            }

            if (receivingMode == null)
                return getResponseHandler().error(getMessage("msg.invalid.receiving.mode"));

            complaint.setPriority(priorityService.getPriorityByCode(priorityCode));
            complaint.setReceivingMode(receivingMode);

            if (files.length > 0)
                complaint.setSupportDocs(addToFileStore(files));
            complaintService.createComplaint(complaint);
            return getResponseHandler().setDataAdapter(new ComplaintAdapter()).success(complaint,
                    getMessage("msg.complaint.reg.success"));
        } catch (final ValidationException e) {
            LOGGER.error(EGOV_API_ERROR, e);
            return getResponseHandler().error(getMessage(e.getMessage()));
        } catch (final Exception e) {
            LOGGER.error(EGOV_API_ERROR, e);
            return getResponseHandler().error(getMessage(SERVER_ERROR));
        }
    }

    protected Set<FileStoreMapper> addToFileStore(final MultipartFile[] files) {
        if (ArrayUtils.isNotEmpty(files))
            return Arrays.asList(files).stream().filter(file -> !file.isEmpty()).map(file -> {
                try {
                    return fileStoreService.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType(),
                            PGRConstants.MODULE_NAME);
                } catch (final Exception e) {
                    throw new ApplicationRuntimeException("err.input.stream", e);
                }
            }).collect(Collectors.toSet());
        else
            return Collections.emptySet();
    }

    // --------------------------------------------------------------------------------//

    /**
     * This will upload complaint support document
     *
     * @param complaintNo
     * @param file
     * @return
     */
    @RequestMapping(value = {ApiUrl.COMPLAINT_UPLOAD_SUPPORT_DOCUMENT}, method = RequestMethod.POST)
    public ResponseEntity<String> uploadSupportDocs(@PathVariable final String complaintNo,
                                                    @RequestParam("files") final MultipartFile file) {
        try {
            final Complaint complaint = complaintService.getComplaintByCRN(complaintNo);

            final FileStoreMapper uploadFile = fileStoreService.store(
                    file.getInputStream(), file.getOriginalFilename(),
                    file.getContentType(), PGRConstants.MODULE_NAME);
            complaint.getSupportDocs().add(uploadFile);
            complaint.nextOwnerId(null);
            complaint.approverComment(null);
            complaint.sendToPreviousOwner(false);
            complaintService.updateComplaint(complaint);
            return getResponseHandler().success("", getMessage("msg.complaint.update.success"));
        } catch (final Exception e) {
            LOGGER.error(EGOV_API_ERROR, e);
            return getResponseHandler().error(getMessage(SERVER_ERROR));
        }
    }

    // --------------------------------------------------------------------------------//

    /**
     * This will display the detail of the complaint
     *
     * @param complaintNo
     * @return Complaint
     */
    @RequestMapping(value = {ApiUrl.COMPLAINT_DETAIL}, method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getDetail(@PathVariable final String complaintNo) {
        try {
            if (complaintNo == null)
                return getResponseHandler().error("Invalid number");
            final Complaint complaint = complaintService.getComplaintByCRN(complaintNo);

            Boolean isSkippableForward = false;

            //this condition is only applicable for employee
            if (securityUtils.getCurrentUser().getType().equals(UserType.EMPLOYEE))
                isSkippableForward = complaintProcessFlowService.canSendToPreviousAssignee(complaint);

            if (complaint == null)
                return getResponseHandler().error("no complaint information");
            return getResponseHandler().setDataAdapter(new ComplaintAdapter(isSkippableForward)).success(complaint);

        } catch (final Exception e) {
            LOGGER.error(EGOV_API_ERROR, e);
            return getResponseHandler().error(getMessage(SERVER_ERROR));
        }
    }

    // --------------------------------------------------------------------------------//

    /**
     * This will display the status history of the complaint( status : REGISTERED, FORWARDED..).
     *
     * @param complaintNo
     * @return Complaint
     */
    @RequestMapping(value = {ApiUrl.COMPLAINT_STATUS}, method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getStatus(@PathVariable final String complaintNo) {
        try {
            if (complaintNo == null)
                return getResponseHandler().error("Invalid number");
            final Complaint complaint = complaintService
                    .getComplaintByCRN(complaintNo);
            if (complaint == null)
                return getResponseHandler().error("no complaint information");
            else
                return getResponseHandler().setDataAdapter(
                        new ComplaintStatusAdapter()).success(
                        complaint.getStateHistory());
        } catch (final Exception e) {
            LOGGER.error(EGOV_API_ERROR, e);
            return getResponseHandler().error(getMessage(SERVER_ERROR));
        }
    }

    // --------------------------------------------------------------------------------//

    /**
     * This will display the latest complaint except current user.
     *
     * @param page
     * @param pageSize
     * @return Complaint
     */

    @RequestMapping(value = {ApiUrl.COMPLAINT_LATEST}, method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getLatest(@PathVariable("page") final int page, @PathVariable("pageSize") final int pageSize) {

        if (page < 1)
            return getResponseHandler().error(INVALID_PAGE_NUMBER_ERROR);
        try {
            final Page<Complaint> pagelist = complaintService.getLatest(page, pageSize);
            final boolean hasNextPage = pagelist.getTotalElements() > page * pageSize;
            return getResponseHandler().putStatusAttribute(HAS_NEXT_PAGE, String.valueOf(hasNextPage))
                    .setDataAdapter(new ComplaintAdapter()).success(pagelist.getContent());
        } catch (final Exception e) {
            LOGGER.error(EGOV_API_ERROR, e);
            return getResponseHandler().error(getMessage(SERVER_ERROR));
        }

    }

    // --------------------------------------------------------------------------------//

    /**
     * This will returns the location(name, address) based on given characters.
     *
     * @param locationName
     * @return
     */
    @RequestMapping(value = {ApiUrl.COMPLAINT_GET_LOCATION}, method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getLocation(@RequestParam("locationName") final String locationName) {
        try {
            if (locationName == null || locationName.isEmpty() || locationName.length() < 3)
                return getResponseHandler().error(getMessage("location.search.invalid"));
            final List<Map<String, Object>> list = boundaryService.getBoundaryDataByNameLike(locationName);
            return getResponseHandler().success(list);
        } catch (final Exception e) {
            LOGGER.error(EGOV_API_ERROR, e);
            return getResponseHandler().error(getMessage(SERVER_ERROR));
        }
    }

    // --------------------------------------------------------------------------------//

    /**
     * This will returns resolved and unresolved complaints count in the city.
     *
     * @return Complaint
     */

    @RequestMapping(value = {
            ApiUrl.COMPLAINT_RESOLVED_UNRESOLVED_COUNT}, method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getComplaintsTotalCount() {
        try {
            return getResponseHandler().success(complaintService.getComplaintsTotalCount());
        } catch (final Exception e) {
            LOGGER.error(EGOV_API_ERROR, e);
            return getResponseHandler().error(getMessage(SERVER_ERROR));
        }
    }

    // --------------------------------------------------------------------------------//

    /**
     * This will returns complaints count by status of current user.
     *
     * @return Complaint
     */

    @RequestMapping(value = {
            ApiUrl.CITIZEN_COMPLAINT_COUNT}, method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getComplaintsCount() {
        try {
            return getResponseHandler().success(complaintService.getMyComplaintsCount());
        } catch (final Exception e) {
            LOGGER.error(EGOV_API_ERROR, e);
            return getResponseHandler().error(getMessage(SERVER_ERROR));
        }
    }

    // --------------------------------------------------------------------------------//

    /**
     * This will returns complaint list of current user.
     *
     * @param page
     * @param pageSize
     * @return Complaint
     */

    @RequestMapping(value = {
            ApiUrl.CITIZEN_GET_MY_COMPLAINT}, method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getMyComplaint(@PathVariable("page") final int page,
                                                 @PathVariable("pageSize") final int pageSize,
                                                 @RequestParam(value = "complaintStatus", required = false) final String complaintStatus) {

        if (page < 1)
            return getResponseHandler().error(INVALID_PAGE_NUMBER_ERROR);
        try {

            Page<Complaint> pagelist = null;
            boolean hasNextPage = false;
            if (org.apache.commons.lang.StringUtils.isEmpty(complaintStatus)
                    || complaintStatus.equals(PGRConstants.COMPLAINT_ALL)) {
                pagelist = complaintService.getMyComplaint(page, pageSize);
                hasNextPage = pagelist.getTotalElements() > page * pageSize;
            } else if (complaintStatus.equals(PGRConstants.COMPLAINT_PENDING)) {
                pagelist = complaintService.getMyPendingGrievances(page, pageSize);
                hasNextPage = pagelist.getTotalElements() > page * pageSize;
            } else if (complaintStatus.equals(PGRConstants.COMPLAINT_COMPLETED)) {
                pagelist = complaintService.getMyCompletedGrievances(page, pageSize);
                hasNextPage = pagelist.getTotalElements() > page * pageSize;
            } else if (complaintStatus.equals(PGRConstants.COMPLAINT_REJECTED)) {
                pagelist = complaintService.getMyRejectedGrievances(page, pageSize);
                hasNextPage = pagelist.getTotalElements() > page * pageSize;
            }

            if (pagelist != null)
                return getResponseHandler().putStatusAttribute(HAS_NEXT_PAGE, String.valueOf(hasNextPage))
                        .setDataAdapter(new ComplaintAdapter()).success(pagelist.getContent());
            else
                return getResponseHandler().error("Invalid Complaint Status!");

        } catch (final Exception e) {
            LOGGER.error(EGOV_API_ERROR, e);
            return getResponseHandler().error(getMessage(SERVER_ERROR));
        }

    }

    // --------------------------------------------------------------------------------//

    /**
     * This will returns nearest user complaint list.
     *
     * @param page
     * @param pageSize
     * @param lat
     * @param lng
     * @param distance
     * @return Complaint
     */
    @RequestMapping(value = ApiUrl.COMPLAINT_NEARBY, method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getNearByComplaint(@PathVariable("page") final int page, @RequestParam("lat") final float lat,
                                                     @RequestParam("lng") final float lng, @RequestParam("distance") final long distance,
                                                     @PathVariable("pageSize") final int pageSize) {

        if (page < 1)
            return getResponseHandler().error(INVALID_PAGE_NUMBER_ERROR);
        try {
            final List<Complaint> list = complaintService.getNearByComplaint(page, pageSize, lat, lng, distance);
            boolean hasNextPage = false;
            if (list.size() > pageSize) {
                hasNextPage = true;
                list.remove(pageSize);
            }
            return getResponseHandler().putStatusAttribute(HAS_NEXT_PAGE, String.valueOf(hasNextPage))
                    .setDataAdapter(new ComplaintAdapter()).success(list);
        } catch (final Exception e) {
            LOGGER.error(EGOV_API_ERROR, e);
            return getResponseHandler().error(getMessage(SERVER_ERROR));
        }
    }

    // ------------------------------------------

    /**
     * This will download the support document of the complaint.
     *
     * @param complaintNo
     * @param fileNo
     * @param isThumbnail
     * @return file
     */

    @RequestMapping(value = ApiUrl.COMPLAINT_DOWNLOAD_SUPPORT_DOCUMENT, method = RequestMethod.GET)
    public void getComplaintDoc(@PathVariable final String complaintNo,
                                @RequestParam(value = "fileNo", required = false) Integer fileNo,
                                @RequestParam(value = "isThumbnail", required = false, defaultValue = "false") final boolean isThumbnail,
                                final HttpServletResponse response) throws IOException {
        try {
            final Complaint complaint = complaintService.getComplaintByCRN(complaintNo);
            final Set<FileStoreMapper> files = complaint.getSupportDocs();
            int i = 1;
            final Iterator<FileStoreMapper> it = files.iterator();
            Integer noOfFiles = fileNo;
            if (noOfFiles == null)
                noOfFiles = files.size();
            File downloadFile;
            while (it.hasNext()) {
                final FileStoreMapper fm = it.next();
                if (i == noOfFiles) {
                    downloadFile = fileStoreService.fetch(fm.getFileStoreId(), PGRConstants.MODULE_NAME);
                    final ByteArrayOutputStream thumbImg = new ByteArrayOutputStream();
                    long contentLength = downloadFile.length();

                    if (isThumbnail) {
                        final BufferedImage img = Thumbnails.of(downloadFile).size(200, 200).asBufferedImage();
                        ImageIO.write(img, "jpg", thumbImg);
                        thumbImg.close();
                        contentLength = thumbImg.size();
                    }

                    response.setHeader("Content-Length", String.valueOf(contentLength));
                    response.setHeader("Content-Disposition", "attachment;filename=" + fm.getFileName());
                    response.setContentType(Files.probeContentType(downloadFile.toPath()));
                    final OutputStream out = response.getOutputStream();
                    IOUtils.write(isThumbnail ? thumbImg.toByteArray() : FileUtils.readFileToByteArray(downloadFile),
                            out);
                    IOUtils.closeQuietly(out);
                    break;
                }
                i++;
            }
        } catch (final Exception e) {
            LOGGER.error(EGOV_API_ERROR, e);
            throw new IOException();
        }
    }

    @RequestMapping(value = ApiUrl.COMPLAINT_DOWNLOAD_SUPPORT_DOCUMENT_BY_ID)
    public void download(@PathVariable final String fileStoreId, final HttpServletResponse response) throws IOException {
        fileStoreUtils.fetchFileAndWriteToStream(fileStoreId, PGRConstants.MODULE_NAME, false, response);
    }

    // ---------------------------------------------------------------------//

    /**
     * This will update the status of the complaint.
     *
     * @param complaintNo
     * @param jsonData    a json object ( action, comment)
     * @return Complaint
     */

    @RequestMapping(value = ApiUrl.COMPLAINT_UPDATE_STATUS, method = RequestMethod.PUT, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> updateComplaintStatus(
            @PathVariable final String complaintNo, @RequestBody final JSONObject jsonData) {

        try {
            final Complaint complaint = complaintService.getComplaintByCRN(complaintNo);
            final ComplaintStatus cmpStatus = complaintStatusService.getByName(jsonData.get("action").toString());
            String citizenfeedback = jsonData.get("feedback").toString();
            if ("COMPLETED".equals(complaint.getStatus().getName())) {
                if (UNSATISFACTORY.equals(citizenfeedback))
                    citizenfeedback = CitizenFeedback.TWO.name();
                else if (SATISFACTORY.equals(citizenfeedback))
                    citizenfeedback = CitizenFeedback.FIVE.name();
                complaint.setCitizenFeedback(CitizenFeedback.valueOf(citizenfeedback));
            }
            complaint.setStatus(cmpStatus);
            complaint.nextOwnerId(0L);
            complaint.approverComment(jsonData.get("comment").toString());
            complaint.sendToPreviousOwner(false);
            complaintService.updateComplaint(complaint);
            return getResponseHandler().success("", getMessage("msg.complaint.status.update.success"));
        } catch (final Exception e) {
            LOGGER.error(EGOV_API_ERROR, e);
            return getResponseHandler().error(getMessage(SERVER_ERROR));
        }

    }

    @RequestMapping(value = ApiUrl.COMPLAINT_HISTORY, method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getComplaintHistory(@PathVariable final String complaintNo) {
        try {
            final HashMap<String, Object> container = new HashMap<>();
            final Complaint complaint = complaintService.getComplaintByCRN(complaintNo);
            container.put("comments", complaintHistoryService.getHistory(complaint));
            return getResponseHandler().setDataAdapter(new ComplaintTypeAdapter()).success(container);
        } catch (final Exception e) {
            LOGGER.error(EGOV_API_ERROR, e);
            return getResponseHandler().error(getMessage(SERVER_ERROR));
        }
    }

    /* EMPLOYEE RELATED COMPLAINT OPERATIONS START */

    // get complaint available status and forward departments
    @RequestMapping(value = ApiUrl.EMPLOYEE_COMPLAINT_ACTIONS, method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getComplaintActions(@PathVariable final String complaintNo) {
        try {

            final Complaint complaint = complaintService.getComplaintByCRN(complaintNo);

            final ComplaintAction complaintActions = new ComplaintAction();
            complaintActions.setStatus(
                    complaintStatusMappingService.getStatusByRoleAndCurrentStatus(securityUtils.getCurrentUser().getRoles(),
                            complaint.getStatus()));
            complaintActions.setApprovalDepartments(departmentService.getAllDepartments());

            return getResponseHandler().setDataAdapter(new ComplaintActionAdapter()).success(complaintActions);

        } catch (final Exception e) {
            LOGGER.error(EGOV_API_ERROR, e);
            return getResponseHandler().error(getMessage(SERVER_ERROR));
        }
    }

    @RequestMapping(value = ApiUrl.EMPLOYEE_UPDATE_COMPLAINT, method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> updateComplaintFromEmployee(@PathVariable final String complaintNo,
                                                              @RequestParam(value = "jsonParams", required = false) final String complaintJsonStr,
                                                              @RequestParam("files") final MultipartFile[] files) {

        try {

            final JsonObject complaintJson = new JsonParser().parse(complaintJsonStr).getAsJsonObject();
            final Complaint complaint = complaintService.getComplaintByCRN(complaintNo);
            final ComplaintStatus cmpStatus = complaintStatusService.getByName(complaintJson.get("action").getAsString());
            final String keyApprovalPosition = "approvalposition";
            final String keyIsSendBack = "issendback";

            complaint.setStatus(cmpStatus);

            Long approvalPosition = 0l;
            Boolean isSendBack = false;

            if (complaintJson.has(keyApprovalPosition))
                approvalPosition = Long.valueOf(complaintJson.get(keyApprovalPosition).getAsString());

            if (complaintJson.has(keyIsSendBack))
                isSendBack = complaintJson.get(keyIsSendBack).getAsBoolean();

            if (files.length > 0)
                complaint.getSupportDocs().addAll(addToFileStore(files));
            complaint.nextOwnerId(approvalPosition);
            complaint.approverComment(complaintJson.get("comment").getAsString());
            complaint.sendToPreviousOwner(isSendBack);
            complaintService.updateComplaint(complaint);
            return getResponseHandler().success("", getMessage("msg.complaint.status.update.success"));
        } catch (final Exception e) {
            LOGGER.error(EGOV_API_ERROR, e);
            return getResponseHandler().error(getMessage(SERVER_ERROR));
        }

    }

}
