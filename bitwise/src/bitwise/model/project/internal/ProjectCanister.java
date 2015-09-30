package bitwise.model.project.internal;

import bitwise.model.entity.internal.EntityCanister;
import bitwise.model.project.ProjectID;
import bitwise.model.project.ProjectKind;
import bitwise.model.project.ProjectMutable;

public class ProjectCanister<T extends ProjectData> extends EntityCanister<T> implements ProjectMutable {
	public ProjectCanister(T in_original) {
		super(in_original);
	}
	
	@Override
	public ProjectKind getKind() {
		return getOriginal().getKind();
	}
	
	@Override
	public ProjectID getID() {
		return getOriginal().getID();
	}
}
