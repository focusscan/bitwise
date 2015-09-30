package bitwise.gui.workbench.nav.hierarchy;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.TreeView;

public class HierarchyView extends TreeView<Object> {
	public HierarchyView() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HierarchyView.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.load();
	}
}
