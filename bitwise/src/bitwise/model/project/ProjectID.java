package bitwise.model.project;

import bitwise.model.entity.EntityID;

public class ProjectID extends EntityID {
	private static final String idKind = "ProjectID";
	
	public ProjectID() {
		super();
	}
	
	@Override
	public String getIDKind() {
		return idKind;
	}
}
