package bitwise.gui.filesystem;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import java.util.Comparator;
import java.util.function.Predicate;

import bitwise.engine.supervisor.Supervisor;
import bitwise.filesystem.Directory;
import bitwise.filesystem.File;
import bitwise.filesystem.Node;
import bitwise.gui.Workbench;
import bitwise.log.Log;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
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
	private final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.{jpg,jpeg}");
	private Workbench workbench = null;
	
	public FileSystem() throws IOException {
		super();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FileSystem.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.load();
		
		fileSystem.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (null == workbench)
					return;
				if(event.getClickCount() == 2) {
		            TreeItem<NodeWrapper> item = fileSystem.getSelectionModel().getSelectedItem();
		            if (null == item)
		            	return;
		            File file = item.getValue().node.asFile();
		            if (null == file)
		            	return;
					if (!matcher.matches(file.getPath()))
						return;
					Thread loadImageThread = new Thread(new Runnable() {
						@Override
						public void run() {
							final Image image = new Image(file.getPath().toUri().toString());
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									try {
										ImageTab tab = new ImageTab();
										tab.setGraphic(new Label(file.getPath().getFileName().toString()));
										workbench.addTab(tab);
										tab.setImage(image);
									} catch (IOException e) {
										Log.logException(Supervisor.getInstance(), e);
									}
								}
							});
						}
					});
					loadImageThread.setName(String.format("Image load thread for %s", file.getPath()));
					loadImageThread.start();
		        }
			}
		});
		
		TreeItem<NodeWrapper> root = buildTreeItem(Supervisor.getInstance().getFileSystemServiceHandle().getWorkpath());
		fileSystem.setRoot(root);
		root.setExpanded(true);
	}
	
	public void setWorkbench(Workbench in) {
		workbench = in;
	}
	
	private TreeItem<NodeWrapper> buildTreeItem(Node in) {
		if (null != in.asDirectory())
			return buildTreeItemDir(in.asDirectory());
		if (null != in.asFile())
			return buildTreeItemFile(in.asFile());
		return null;
	}
	
	private TreeItem<NodeWrapper> buildTreeItemFile(File file) {
		TreeItem<NodeWrapper> ret = new TreeItem<>(new NodeWrapper(file));
		if (matcher.matches(file.getPath())) {
			Label icon = new Label("I");
			icon.setTextFill(Color.GREEN);
			ret.setGraphic(icon);
		}
		return ret;
	}
	
	private TreeItem<NodeWrapper> buildTreeItemDir(Directory dir) {
		Label icon = new Label("D");
		icon.setTextFill(Color.BLUE);
		TreeItem<NodeWrapper> ret = new TreeItem<>(new NodeWrapper(dir), icon);
		for (Node child : dir.getChildren())
			ret.getChildren().add(buildTreeItem(child));
		sortTreeItem(ret);
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
				sortTreeItem(ret);
			}
		});
		return ret;
	}
	
	private void sortTreeItem(TreeItem<NodeWrapper> ti) {
		FXCollections.sort(ti.getChildren(), new Comparator<TreeItem<NodeWrapper>>() {
			@Override
			public int compare(TreeItem<NodeWrapper> n1, TreeItem<NodeWrapper> n2) {
				boolean n1_directory = null != n1.getValue().node.asDirectory();
				boolean n2_directory = null != n2.getValue().node.asDirectory();
				if (n1_directory && !n2_directory)
					return -1;
				if (!n1_directory && n2_directory)
					return 1;
				return n1.toString().compareTo(n2.toString());
			}
		});
	}
}
