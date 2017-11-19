/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
package org.egov.works.abstractestimate.service;

import org.apache.commons.lang.StringUtils;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.EstimatePhotographSearchRequest;
import org.egov.works.abstractestimate.entity.EstimatePhotographs;
import org.egov.works.abstractestimate.entity.EstimatePhotographs.WorkProgress;
import org.egov.works.abstractestimate.repository.EstimatePhotographRepository;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.utils.WorksConstants;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class EstimatePhotographService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private EstimatePhotographRepository estimatePhotographRepository;

    @Autowired
    private FileStoreService fileStoreService;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Transactional
    public void save(final EstimatePhotographs estimatePhotographs) {
        estimatePhotographRepository.save(estimatePhotographs);
    }

    public List<EstimatePhotographs> getEstimatePhotographs(final MultipartFile[] files, final Object object)
            throws IOException {
        final List<EstimatePhotographs> estimatePhotographsList = new ArrayList<EstimatePhotographs>();
        Method method = null;
        try {
            method = object.getClass().getMethod("getId", null);
            method.invoke(object, null);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            throw new ApplicationRuntimeException("lineestimate.document.error", e);
        }
        File compressedImageFile = null;
        if (files != null && files.length > 0 && !files[0].isEmpty())
            compressedImageFile = compressImage(files);

        final EstimatePhotographs estimatePhotographs = new EstimatePhotographs();
        estimatePhotographs.setFileStore(fileStoreService.store(compressedImageFile, files[0].getOriginalFilename(),
                files[0].getContentType(), WorksConstants.FILESTORE_MODULECODE));
        estimatePhotographsList.add(estimatePhotographs);
        compressedImageFile.delete();

        return estimatePhotographsList;
    }

    public File compressImage(final MultipartFile[] files) throws IOException, FileNotFoundException {

        final BufferedImage image = ImageIO.read(files[0].getInputStream());
        final File compressedImageFile = new File(files[0].getOriginalFilename());
        final OutputStream os = new FileOutputStream(compressedImageFile);
        String fileExtenstion = files[0].getOriginalFilename();
        fileExtenstion = fileExtenstion.substring(fileExtenstion.lastIndexOf(".") + 1);
        final Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(fileExtenstion);
        final ImageWriter writer = writers.next();
        final ImageOutputStream ios = ImageIO.createImageOutputStream(os);
        writer.setOutput(ios);
        final ImageWriteParam param = writer.getDefaultWriteParam();

        if (!fileExtenstion.equalsIgnoreCase("png")) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.5f);
        }
        writer.write(null, new IIOImage(image, null, null), param);

        os.close();
        ios.close();
        writer.dispose();
        return compressedImageFile;
    }

    public List<EstimatePhotographs> getEstimatePhotographByLineEstimateDetail(final Long lineEstimateDetailId) {
        return estimatePhotographRepository.findByLineEstimateDetails_id(lineEstimateDetailId);
    }

    public List<EstimatePhotographs> getEstimatePhotographsByEstimatePhotographStageAndLineEstimateDetails(
            final WorkProgress estimatePhotographtrackStage, final Long lineEstimateDetailId) {
        return estimatePhotographRepository.findByEstimatePhotographAndLineEstimateDetails(estimatePhotographtrackStage,
                lineEstimateDetailId);
    }

    public List<LineEstimateDetails> searchEstimatePhotograph(
            final EstimatePhotographSearchRequest estimatePhotographSearchRequest) {
        final StringBuilder queryStr = new StringBuilder(500);

        buildWhereClause(estimatePhotographSearchRequest, queryStr);
        final Query query = setParameterForEstimatePhotograph(estimatePhotographSearchRequest, queryStr);
        final List<LineEstimateDetails> estimatePhotographsList = query.getResultList();
        return estimatePhotographsList;
    }

    private void buildWhereClause(final EstimatePhotographSearchRequest estimatePhotographSearchRequest,
            final StringBuilder queryStr) {

        queryStr.append(
                "select distinct(ep.lineEstimateDetails) from EstimatePhotographs as ep where ep.lineEstimateDetails.lineEstimate.status.code != :lineEstimateStatus and ep.workProgress is not null");

        if (StringUtils.isNotBlank(estimatePhotographSearchRequest.getWorkIdentificationNumber()))
            queryStr.append(
                    " and upper(ep.lineEstimateDetails.projectCode.code) = :workIdentificationNumber");

        // if (StringUtils.isNotBlank(estimatePhotographSearchRequest.getEstimateNumber()))
        // queryStr.append(" and upper(ep.lineEstimateDetails.estimateNumber) = :estimateNumber");

        if (StringUtils.isNotBlank(estimatePhotographSearchRequest.getWorkOrderNumber()))
            queryStr.append(
                    " and ep.lineEstimateDetails.id in (select distinct(woe.estimate.lineEstimateDetails.id) from WorkOrderEstimate as woe where woe.workOrder.egwStatus.code =:workOrderStatus and upper(woe.workOrder.workOrderNumber) =:workOrderNumber) ");
        if (estimatePhotographSearchRequest.getEstimateCreatedBy() != null)
            queryStr.append(
                    " and ep.lineEstimateDetails.id in (select distinct(ae.lineEstimateDetails.id) from AbstractEstimate as ae where ae.egwStatus.code =:abstractEstimateStatus and ae.createdBy.id =:aeCreatedById) ");
        if (StringUtils.isNotBlank(estimatePhotographSearchRequest.getContractorName()))
            queryStr.append(
                    " and ep.lineEstimateDetails.id in (select distinct(woe.estimate.lineEstimateDetails.id) from WorkOrderEstimate as woe where woe.workOrder.egwStatus.code =:workOrderStatus and upper(woe.workOrder.contractor.name) =:contractorName or upper(woe.workOrder.contractor.code) =:contractorName) ");

    }

    private Query setParameterForEstimatePhotograph(final EstimatePhotographSearchRequest estimatePhotographSearchRequest,
            final StringBuilder queryStr) {
        final Query qry = entityManager.createQuery(queryStr.toString());

        qry.setParameter("lineEstimateStatus", WorksConstants.CANCELLED_STATUS);

        if (estimatePhotographSearchRequest != null) {
            if (StringUtils.isNotBlank(estimatePhotographSearchRequest.getWorkIdentificationNumber()))
                qry.setParameter("workIdentificationNumber",
                        estimatePhotographSearchRequest.getWorkIdentificationNumber().toUpperCase());
            if (StringUtils.isNotBlank(estimatePhotographSearchRequest.getEstimateNumber()))
                qry.setParameter("estimateNumber", estimatePhotographSearchRequest.getEstimateNumber().toUpperCase());
            if (StringUtils.isNotBlank(estimatePhotographSearchRequest.getWorkOrderNumber())) {
                qry.setParameter("workOrderStatus", WorksConstants.APPROVED);
                qry.setParameter("workOrderNumber", estimatePhotographSearchRequest.getWorkOrderNumber().toUpperCase());
            }
            if (estimatePhotographSearchRequest.getEstimateCreatedBy() != null) {
                qry.setParameter("abstractEstimateStatus", AbstractEstimate.EstimateStatus.TECH_SANCTIONED.toString());
                qry.setParameter("aeCreatedById", estimatePhotographSearchRequest.getEstimateCreatedBy());
            }
            if (StringUtils.isNotBlank(estimatePhotographSearchRequest.getContractorName())) {
                qry.setParameter("workOrderStatus", WorksConstants.APPROVED);
                qry.setParameter("contractorName", estimatePhotographSearchRequest.getContractorName().toUpperCase());
            }

        }
        return qry;
    }

    public EstimatePhotographs getEstimatePhotographByFilestore(final Long filestoreId) {
        return estimatePhotographRepository.findByFileStore_id(filestoreId);
    }

    @Transactional
    public void delete(final EstimatePhotographs estimatePhotographs) {
        estimatePhotographRepository.delete(estimatePhotographs);
    }

    public List<String> getEstimateNumbersForViewEstimatePhotograph(final String estimateNumber) {
        return estimatePhotographRepository.findEstimateNumbersForViewEstimatePhotograph("%" + estimateNumber + "%",
                WorksConstants.CANCELLED_STATUS);
    }

    public List<String> getWinForViewEstimatePhotograph(final String workIdentificationNumber) {
        return estimatePhotographRepository.findWorkIdentificationNumberForViewEstimatePhotograph(
                "%" + workIdentificationNumber + "%", WorksConstants.CANCELLED_STATUS);
    }

}
