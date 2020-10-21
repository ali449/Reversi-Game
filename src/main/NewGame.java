package main;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class NewGame
{
    public RadioButton blkBtn;

    public RadioButton autoBtn;

    public Button startBtn;

    private GridPane grid;

    private Stage stage;

    public NewGame()
    {
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text title = new Text("New Game");

        Text colorTxt = new Text("Color: ");
        ToggleGroup group = new ToggleGroup();
        blkBtn = new RadioButton("Black");
        blkBtn.setToggleGroup(group);
        blkBtn.setSelected(true);
        RadioButton whtBtn = new RadioButton("White");
        whtBtn.setToggleGroup(group);

        Text modeTxt = new Text("Mode: ");
        ToggleGroup group1 = new ToggleGroup();
        RadioButton norBtn = new RadioButton("Normal");
        norBtn.setToggleGroup(group1);
        norBtn.setSelected(true);
        autoBtn = new RadioButton("Auto");
        autoBtn.setToggleGroup(group1);

        startBtn = new Button("Start");

        GridPane.setHalignment(title, HPos.CENTER);
        grid.add(title, 0, 0, 3, 1);
        grid.add(colorTxt, 0, 1);
        grid.add(blkBtn, 0, 2);
        grid.add(whtBtn, 1, 2);
        grid.add(modeTxt, 0, 3);
        grid.add(norBtn, 0, 4);
        grid.add(autoBtn, 1, 4);
        grid.add(startBtn, 0, 6, 3, 1);
        GridPane.setHalignment(startBtn, HPos.CENTER);

        grid.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

    }

    public void show()
    {
        stage = new Stage();
        stage.setScene(new Scene(grid, 250, 200));
        stage.setTitle("New Game");
        stage.setResizable(false);

        stage.show();
    }

    public void close()
    {
        stage.close();
    }

}
