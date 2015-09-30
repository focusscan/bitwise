package bitwise.model.project;

import bitwise.model.entity.Entity;

public interface Project extends Entity {
	@Override
	public ProjectID getID();
}
