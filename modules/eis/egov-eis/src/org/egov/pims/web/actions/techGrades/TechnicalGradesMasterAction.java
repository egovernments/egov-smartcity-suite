package org.egov.pims.web.actions.techGrades;

import org.egov.pims.model.SkillMaster;
import org.egov.pims.model.TechnicalGradesMaster;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.Query;

public class TechnicalGradesMasterAction extends BaseFormAction {

	TechnicalGradesMaster technicalGrades = new TechnicalGradesMaster();
	private String mode = "";
	private static final String SEARCH = "search";
	SkillMaster skill;
	private Integer skillId;

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return technicalGrades;
	}

	public String beforeCreate() {
		mode = "save";
		return NEW;
	}

	public void prepare() {
		super.prepare();
		addDropdownData("skillsList",
				persistenceService.findAllBy("from SkillMaster"));
		addDropdownData("techGradesList",
				persistenceService.findAllBy("from TechnicalGradesMaster"));
	}

	@ValidationErrorPage(value = NEW)
	public String save() {
		skill = (SkillMaster) persistenceService.find(
				"from SkillMaster where  id = ? ", skillId);
		technicalGrades.setSkillMaster(skill);
		persistenceService.getSession().save(technicalGrades);
		addActionMessage(getText("techGrade.create.success",
				new String[] { technicalGrades.getGradeName() }));
		mode = "view";
		return NEW;
	}

	public String saveOrUpdate() {
		skill = (SkillMaster) persistenceService.find(
				"from SkillMaster where  id = ? ", skillId);
		technicalGrades.setSkillMaster(skill);
		persistenceService.getSession().merge(technicalGrades);
		addActionMessage(getText("techGrades.modify.success",
				new String[] { technicalGrades.getGradeName() }));
		mode = "view";
		return NEW;
	}

	public String update() {
		return SEARCH;
	}

	public String viewTechGrades() {
		mode = "view";
		technicalGrades = gettechGradesByName();
		skillId = technicalGrades.getSkillMaster().getId();
		return NEW;
	}

	public String modifyTechGrades() {
		mode = "edit";
		technicalGrades = gettechGradesByName();
		skillId = technicalGrades.getSkillMaster().getId();
		return NEW;
	}

	private TechnicalGradesMaster gettechGradesByName() {
		Query query = persistenceService.getSession().createQuery(
				"from TechnicalGradesMaster where gradeName=:gradeName");
		query.setParameter("gradeName", technicalGrades.getGradeName());
		return (TechnicalGradesMaster) query.uniqueResult();

	}

	public String gradeNameUniqueCheck() {
		return "gradeNameUniqueCheck";
	}

	public boolean getGradeNameUniqueCheck() throws Exception {
		return checkGradeName(technicalGrades.getGradeName(),
				technicalGrades.getId());
	}

	private boolean checkGradeName(String name, Integer gradeId) {
		String qry = "from TechnicalGradesMaster where upper(gradeName)=:gradeName";
		if (null != mode && !mode.equals("save")) {
			qry = qry + " and id not in(:id)";
		}

		Query query = persistenceService.getSession().createQuery(qry);
		query.setParameter("gradeName", name.toUpperCase());

		if (null != mode && !mode.equals("save")) {
			query.setParameter("id", gradeId);
		}

		if (query.uniqueResult() == null)
			return false;
		else
			return true;

	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Integer getSkillId() {
		return skillId;
	}

	public void setSkillId(Integer skillId) {
		this.skillId = skillId;
	}



}
