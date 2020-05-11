package org.egov.ptis.domain.service.search;

import static java.lang.String.format;
import static org.egov.ptis.constants.PropertyTaxConstants.DATE_FORMAT_DDMMYYY;

import java.util.ArrayList;
import java.util.List;

import org.egov.infra.admin.master.entity.User;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.utils.DateUtils;
import org.egov.ptis.domain.entity.property.CourtVerdict;
import org.egov.ptis.domain.entity.property.WriteOff;
import org.egov.ptis.domain.entity.property.view.SearchCourtCaseWriteoffRequest;
import org.egov.ptis.domain.repository.courtverdict.CourtVerdictRepository;
import org.egov.ptis.domain.repository.writeOff.WriteOffRepository;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.repository.spec.SearchCourtCaseWriteoffSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchCourtCaseWriteOffService {

    public static final String APPLICATION_VIEW_URL = "/ptis/view/viewProperty-viewForm.action?applicationNo=%s&applicationType=%s";

    @Autowired
    private WriteOffRepository writeOffRepository;

    @Autowired
    private CourtVerdictRepository courtVerdictRepository;

    @Autowired
    private PropertyService propertyService;

    @ReadOnly
    public List<WriteOff> getWriteOffRecord(
            final SearchCourtCaseWriteoffRequest searchCourtCaseWriteoffRequest) {
        return writeOffRepository
                .findAll(SearchCourtCaseWriteoffSpec.writeoffSpecification(searchCourtCaseWriteoffRequest));
    }

    @ReadOnly
    public List<CourtVerdict> getCourtVerdictRecord(
            final SearchCourtCaseWriteoffRequest searchCourtCaseWriteoffRequest) {
        return courtVerdictRepository
                .findAll(SearchCourtCaseWriteoffSpec.courtVerdictSpecification(searchCourtCaseWriteoffRequest));
    }

    public List<SearchCourtCaseWriteoffRequest> getResult(final SearchCourtCaseWriteoffRequest searchCourtCaseWriteoffRequest) {
        List<SearchCourtCaseWriteoffRequest> list = new ArrayList<>();
        if (searchCourtCaseWriteoffRequest.getApplicationType().equals("Write Off")) {
            List<WriteOff> data = getWriteOffRecord(searchCourtCaseWriteoffRequest);
            for (WriteOff details : data) {
                SearchCourtCaseWriteoffRequest searchCourtCaseWriteoffReq = new SearchCourtCaseWriteoffRequest();
                searchCourtCaseWriteoffReq.setApplicationNumber(details.getApplicationNumber());
                searchCourtCaseWriteoffReq.setApplicationAddress(details.getBasicProperty().getAddress().toString());
                searchCourtCaseWriteoffReq
                        .setApplicationDate(DateUtils.getFormattedDate(details.getCreatedDate(), DATE_FORMAT_DDMMYYY));
                searchCourtCaseWriteoffReq.setApplicationStatus(details.getState().getValue());
                searchCourtCaseWriteoffReq.setSource(details.getProperty().getSource());
                searchCourtCaseWriteoffReq.setApplicationType(details.getState().getNatureOfTask());
                final User stateOwner = propertyService.getOwnerName(details);
                searchCourtCaseWriteoffReq.setOwnerName(stateOwner.getUsername() + "::" + stateOwner.getName().trim());
                searchCourtCaseWriteoffReq.setApplicantName(details.getBasicProperty().getPrimaryOwner().getName());
                searchCourtCaseWriteoffReq.setUrl(format(APPLICATION_VIEW_URL, details.getApplicationNumber(),
                        searchCourtCaseWriteoffRequest.getApplicationType()));
                list.add(searchCourtCaseWriteoffReq);
            }
        } else {
            List<CourtVerdict> data = getCourtVerdictRecord(searchCourtCaseWriteoffRequest);
            for (CourtVerdict courtDetails : data) {
                SearchCourtCaseWriteoffRequest searchCourtCaseWriteoffReq = new SearchCourtCaseWriteoffRequest();
                searchCourtCaseWriteoffReq.setApplicationNumber(courtDetails.getApplicationNumber());
                searchCourtCaseWriteoffReq.setApplicationAddress(courtDetails.getBasicProperty().getAddress().toString());
                searchCourtCaseWriteoffReq
                        .setApplicationDate(DateUtils.getFormattedDate(courtDetails.getCreatedDate(), DATE_FORMAT_DDMMYYY));
                searchCourtCaseWriteoffReq.setApplicationStatus(courtDetails.getState().getValue());
                searchCourtCaseWriteoffReq.setSource(courtDetails.getProperty().getSource());
                searchCourtCaseWriteoffReq.setApplicationType(courtDetails.getState().getNatureOfTask());
                final User stateOwner = propertyService.getOwnerName(courtDetails);
                searchCourtCaseWriteoffReq.setOwnerName(stateOwner.getUsername() + "::" + stateOwner.getName().trim());
                searchCourtCaseWriteoffReq.setApplicantName(courtDetails.getBasicProperty().getPrimaryOwner().getName());
                searchCourtCaseWriteoffReq.setUrl(format(APPLICATION_VIEW_URL, courtDetails.getApplicationNumber(),
                        searchCourtCaseWriteoffRequest.getApplicationType()));
                list.add(searchCourtCaseWriteoffReq);
            }
        }
        return list;
    }

}
