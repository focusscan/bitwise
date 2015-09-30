package bitwise.model.project.internal;

import bitwise.model.entity.internal.EntityData;
import bitwise.model.project.ProjectID;
import bitwise.model.project.ProjectKind;
import bitwise.model.project.ProjectMutable;

public class ProjectData extends EntityData implements ProjectMutable {
	public ProjectData() {
		super(new ProjectID());
	}
	
	public ProjectData(ProjectData that) {
		super(that);
	}
	
	@Override
	public ProjectData mutableClone() {
		return new ProjectData(this);
	}
	
	@Override
	public ProjectKind getKind() {
		return ProjectKind.INSTANCE;
	}
	
	@Override
	public ProjectID getID() {
		return (ProjectID)getIDFast();
	}
}
