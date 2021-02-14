package battleShipApp;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import battleShipApp.Ocean.Cell;

public class Controller {
    @FXML Rectangle shot_cell ;
    public void cellShoot(MouseEvent event){
        shot_cell = (Rectangle) event.getSource();
        Cell cell = (Cell) event.getSource(); //the cell that the user clicked to shoot
        //human plays
        if (cell.wasShot)
            return;
        shot_cell.setFill(Color.web("#000000"));
        if( cell.shoot())
            shot_cell.setFill(Color.web("#da0000"));


    }

}
