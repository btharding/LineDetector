package main;
import java.io.File;

import javafx.application.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.*;

public class Display extends Application{
	
	public static final int WIDTH = 500;
	public static final int HEIGHT = 500;
	
	public Image image;
	public Image filteredImage;
	public ImageView imgView;
	public EdgeDetector ed = new EdgeDetector();
	
	@Override
	public void start(Stage stage) {
		stage.setTitle("Edge Detector");
		VBox root = new VBox();
		
		MenuBar menuBar = new MenuBar();
		
		Menu fileMenu = new Menu("File");
		MenuItem openItem = new MenuItem("Open");
		MenuItem closeItem = new MenuItem("Close");
		closeItem.setDisable(true);
		fileMenu.getItems().addAll(openItem, closeItem);
		menuBar.getMenus().add(fileMenu);
		
		Menu toolsMenu = new Menu("Tools");
		toolsMenu.setDisable(true);
		MenuItem edgeDetect = new MenuItem("Edge Detection");
		MenuItem revert = new MenuItem("Revert");
		revert.setDisable(true);
		toolsMenu.getItems().addAll(edgeDetect, revert);
		menuBar.getMenus().add(toolsMenu);
		
		root.getChildren().add(menuBar);
		
		stage.setScene(new Scene(root, WIDTH, HEIGHT));
		stage.show();
		
		openItem.setOnAction(new EventHandler<ActionEvent>(){
			
			@Override
			public void handle(ActionEvent t) {
				
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Image File");
				File file = fileChooser.showOpenDialog(stage);
				
				if(file != null) {
					
					image = new Image("file:"+file.getPath());
					imgView = new ImageView(image);
					root.getChildren().add(imgView);
					
					openItem.setDisable(true);
					closeItem.setDisable(false);
					toolsMenu.setDisable(false);
					
				}
				
			}
			
		});
		
		closeItem.setOnAction(new EventHandler<ActionEvent>(){
			
			@Override
			public void handle(ActionEvent t) {
				
				root.getChildren().remove(imgView);
				image = null;
				
				closeItem.setDisable(true);
				openItem.setDisable(false);
				toolsMenu.setDisable(true);
				edgeDetect.setDisable(false);
				revert.setDisable(true);
				
			}
			
		});
		
		edgeDetect.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent t) {
				
				filteredImage = ed.filterImage(image);
				
				root.getChildren().remove(imgView);
				imgView = new ImageView(filteredImage);
				root.getChildren().add(imgView);
				
				revert.setDisable(false);
				edgeDetect.setDisable(true);
				
			}
			
		});
		
		revert.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent t) {
				
				root.getChildren().remove(imgView);
				imgView = new ImageView(image);
				root.getChildren().add(imgView);
				
				revert.setDisable(true);
				edgeDetect.setDisable(false);			
				
			}
			
		});
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
