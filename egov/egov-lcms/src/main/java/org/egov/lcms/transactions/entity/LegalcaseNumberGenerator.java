package org.egov.lcms.transactions.entity;

import java.util.Calendar;

import javax.persistence.SequenceGenerator;

import org.egov.infra.script.entity.Script;
import org.egov.infstr.services.PersistenceService;
import org.egov.lcms.utils.LcmsConstants;

public class LegalcaseNumberGenerator {
    private SequenceGenerator seqGenerator;
    private PersistenceService<Script, Long> scriptService;

    public String getLcNumber(Legalcase legalcase, String manualNumber,
            Integer caseYear) {
        /*
         * Script validTransitionScript = scriptService.findAllByNamedQuery( Script.BY_NAME,
         * LcmsConstants.SCRIPT_LEGALCASE_LCNUMBER).get(0); if (legalcase.getLcNumberType().equals(
         * LcmsConstants.LC_NUMBER_AUTOMATED_TYPE)) return (String) validTransitionScript.eval(Script.createContext( "legalcase",
         * legalcase, "seqGenerator", seqGenerator, "currentYear", getCurrentYear())); else if
         * (legalcase.getLcNumberType().equals( LcmsConstants.LC_NUMBER_OPTIONAL_TYPE)) return (String)
         * validTransitionScript.eval(Script.createContext( "legalcase", legalcase, "manualNumber", manualNumber, "caseYear",
         * caseYear.toString()));
         */
        return null;
    }

    public String getCaseNumber(Legalcase legalcase, String caseNumber) {
        /*
         * Script validTransitionScript = scriptService.findAllByNamedQuery( Script.BY_NAME,
         * LcmsConstants.SCRIPT_LEGALCASE_CASENUMBER).get( 0); return (String) validTransitionScript.eval(Script.createContext(
         * "legalcase", legalcase, "caseNumber", caseNumber));
         */
        return null;
    }

    public void setSeqGenerator(SequenceGenerator seqGenerator) {
        this.seqGenerator = seqGenerator;
    }

    public void setScriptService(
            PersistenceService<Script, Long> persistenceService) {
        this.scriptService = persistenceService;
    }

    protected String getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        return Integer.valueOf(cal.get(Calendar.YEAR)).toString();
    }
}
