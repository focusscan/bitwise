package bitwise.gui.filesystem;

import java.io.IOException;
import java.util.function.Predicate;

import bitwise.engine.supervisor.Supervisor;
import bitwise.filesystem.Directory;
import bitwise.filesystem.Node;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class FileSystem extends StackPane {
	private static class NodeWrapper {
		public final Node node;
		
		public NodeWrapper(Node in_node) {
			node = in_node;
		}
		
		@Override
		public String toString() {
			return node.getPath().getFileName().toString();
		}
	}
	
	@FXML private TreeView<NodeWrapper> fileSystem;
	
	public FileSystem() throws IOException {
		super();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FileSystem.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.load();
		
		TreeItem<NodeWrapper> root = buildTreeItem(Supervisor.getInstance().getFileSystemServiceHandle().getWorkpath());
		fileSystem.setRoot(root);
		root.setExpanded(true);
	}
	
	private TreeItem<NodeWrapper> buildTreeItem(Node in) {
		Directory dir = in.asDirectory();
		if (null == dir)
			return new TreeItem<>(new NodeWrapper(in));
		Label icon = new Label("D");
		icon.setTextFill(Color.BLUE);
		TreeItem<NodeWrapper> ret = new TreeItem<>(new NodeWrapper(dir), icon);
		for (Node child : dir.getChildren())
			ret.getChildren().add(buildTreeItem(child));
		dir.getChildren().addListener(new ListChangeListener<Node>() {
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Node> change) {
				while (change.next()) {
					for (Node child : change.getAddedSubList())
						ret.getChildren().add(buildTreeItem(child));
					if (0 < change.getRemovedSize()) {
						ret.getChildren().removeIf(new Predicate<TreeItem<NodeWrapper>>() {
							@Override
							public boolean test(TreeItem<NodeWrapper> t) {
								return change.getRemoved().contains(t.getValue().node);
							}
						});
					}
				}
			}
		});
		return ret;
	}
}
