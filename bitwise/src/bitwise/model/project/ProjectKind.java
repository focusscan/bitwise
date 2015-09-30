package bitwise.model.project;

import bitwise.engine.carousel.CastEnsemble;
import bitwise.model.entity.EntityKind;
import bitwise.model.entity.internal.EntityCanister;
import bitwise.model.entity.internal.EntityData;
import bitwise.model.project.internal.ProjectCanister;
import bitwise.model.project.internal.ProjectData;

public class ProjectKind extends EntityKind {
	public static final CastEnsemble<ProjectID, Project, ProjectMutable, ProjectData, ProjectCanister<?>> ENSEMBLE =
			new CastEnsemble<ProjectID, Project, ProjectMutable, ProjectData, ProjectCanister<?>>() {

				@Override
				protected ProjectData promoteData(EntityData in) {
					return (in instanceof ProjectData) ? (ProjectData)in : null;
				}

				@Override
				protected ProjectCanister<?> promoteCanister(EntityCanister<?> in) {
					return (in instanceof ProjectCanister<?>) ? (ProjectCanister<?>)in : null;
				}

				@Override
				protected ProjectData newData() {
					return new ProjectData();
				}

				@Override
				protected Project readonlyData(ProjectData in) {
					return in;
				}

				@Override
				protected ProjectMutable mutableData(ProjectData in) {
					return in;
				}

				@Override
				protected ProjectCanister<ProjectData> newCanister(ProjectData in) {
					return new ProjectCanister<>(in);
				}

				@Override
				protected Project readonlyCanister(ProjectCanister<?> in) {
					return in;
				}

				@Override
				protected ProjectMutable mutableCanister(ProjectCanister<?> in) {
					return in;
				}
	};
	
	public static final ProjectKind INSTANCE = new ProjectKind();
	
	private ProjectKind() {
		super("Project");
	}
	
	protected ProjectKind(String in_name) {
		super(in_name);
	}
}
