package org.egov.pims.web.actions.skills;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.pims.model.SkillMaster;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.Query;

/**
 * this class is used to create,view ,modify of Skill master
 * 
 * @author parvati
 * 
 */

@ParentPackage("egov")
public class SkillMasterAction extends BaseFormAction {

	SkillMaster skillMaster = new SkillMaster();
	private String mode = "";
	private static final String SEARCH = "search";

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return skillMaster;
	}

	@SuppressWarnings("unchecked")
	public void prepare() {
		super.prepare();
		addDropdownData("skillsList",
				persistenceService.findAllBy("from SkillMaster"));
	}

	@SkipValidation
	public String beforeCreate() {
		mode = "save";
		return NEW;
	}

	@ValidationErrorPage(value = NEW)
	public String save() {

		persistenceService.getSession().save(skillMaster);
		addActionMessage(getText("skill.create.success",
				new String[] { skillMaster.getName() }));
		mode = "view";
		return NEW;
	}

	public String update() {

		return SEARCH;
	}

	public String saveOrUpdate() {
		persistenceService.getSession().merge(skillMaster);
		addActionMessage(getText("skill.modify.success",
				new String[] { skillMaster.getName() }));
		mode = "view";
		return NEW;
	}

	public String modifySkills() {
		mode = "edit";
		skillMaster = getSkillsByName();
		return NEW;
	}

	public String viewSkills() {
		mode = "view";
		skillMaster = getSkillsByName();
		return NEW;
	}

	private SkillMaster getSkillsByName() {
		Query query = persistenceService.getSession().createQuery(
				"from SkillMaster where name=:name");
		query.setParameter("name", skillMaster.getName());
		return (SkillMaster) query.uniqueResult();
	}

	public String nameUniqueCheck() {
		return "nameUniqueCheck";
	}

	public boolean getNameUniqueCheck() throws Exception {
		return checkNameUnique(skillMaster.getName(), skillMaster.getId());
	}

	private boolean checkNameUnique(String name, Integer skilId) {
		String qryStr = "from SkillMaster where upper(name)=:name";

		// for modify it needs to exclude the current skill id
		if (null != mode && !mode.equals("save")) {
			qryStr = qryStr + " and id not in(:id)";
		}

		Query query = persistenceService.getSession().createQuery(qryStr);
		query.setParameter("name", name.toUpperCase());

		if (null != mode && !mode.equals("save")) {
			query.setParameter("id", skilId);
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


}
