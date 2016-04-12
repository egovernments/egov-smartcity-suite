package org.egov.commons.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.CFinancialYear;
import org.egov.commons.CFiscalPeriod;
import org.egov.commons.repository.CFinancialYearRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CFinancialYearService {

    private final CFinancialYearRepository cFinancialYearRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public CFinancialYearService(final CFinancialYearRepository cFinancialYearRepository) {
        this.cFinancialYearRepository = cFinancialYearRepository;
    }

    @Transactional
    public CFinancialYear create(final CFinancialYear cFinancialYear) {
        return cFinancialYearRepository.save(cFinancialYear);
    }

    @Transactional
    public CFinancialYear update(final CFinancialYear cFinancialYear) {
        return cFinancialYearRepository.save(cFinancialYear);
    }

    public List<CFinancialYear> findAll() {
        return cFinancialYearRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    public CFinancialYear findOne(final Long id) {
        return cFinancialYearRepository.findOne(id);
    }

    public List<CFinancialYear> search(final CFinancialYear cFinancialYear) {
        if (cFinancialYear.getFinYearRange() != null)
            return cFinancialYearRepository.findByFinancialYearRange(cFinancialYear.getFinYearRange());
        else
            return cFinancialYearRepository.findAll();
    }

    public Date getNextFinancialYearStartingDate() {
        final List<CFinancialYear> cFinYear = cFinancialYearRepository.getFinYearLastDate();
        final Calendar cal = Calendar.getInstance();
        cal.setTime(cFinYear.get(0).getEndingDate());
        cal.add(Calendar.DATE, +1);
        return cal.getTime();
    }

    public CFiscalPeriod findByFiscalName(final String name) {
        return cFinancialYearRepository.findByFiscalName(name);
    }

}