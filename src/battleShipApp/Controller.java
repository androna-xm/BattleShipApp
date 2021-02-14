package battleShipApp;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import battleShipApp.Ocean.Cell;

public class Controller {
    @FXML Rectangle cell;
    public void cellShoot(MouseEvent event){
        Cell cell = (Cell) event.getSource();
        cell.setFill(Color.web("#ed4b00"));

    }

}
