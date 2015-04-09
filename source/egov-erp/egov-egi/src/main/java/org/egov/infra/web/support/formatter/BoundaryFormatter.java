package org.egov.infra.web.support.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Component
public class BoundaryFormatter implements Formatter<Boundary> {

    private final BoundaryService boundaryService;

    @Autowired
    public BoundaryFormatter(final BoundaryService boundaryService) {
        this.boundaryService = boundaryService;
    }

    @Override
    public Boundary parse(final String boundaryId, final Locale locale) throws ParseException {
        return boundaryId.isEmpty() ? null : (Boundary) boundaryService.getBoundaryById(Long.valueOf(boundaryId));
    }

    @Override
    public String print(final Boundary boundary, final Locale locale) {
        return boundary.getName();
    }

}
