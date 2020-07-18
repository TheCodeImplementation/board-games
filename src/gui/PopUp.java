/*
 * Created by The Code Implementation, July 2020.
 * Youtube channel: https://www.youtube.com/channel/UCecfXH0CwYv-CA0Oo3-8PFg
 * Github: https://github.com/TheCodeImplementation
 */

package gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PopUp {

    static Object chosen;
    //METHODS
    public static <T> T presentOptions(T[] options, boolean horizontal) {
        chosen = null;
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Pane layout = horizontal ? new HBox() : new VBox();
        for(T option : options) {
            Button button = new Button(option.toString());
            button.getStyleClass().add("popUpChoice");
            button.setOnAction(e -> {
                chosen = option;
                stage.close();
            });
            layout.getChildren().add(button);
        }
        Scene scene = new Scene(layout);
        scene.getStylesheets().add("gui/myStyle.css");
        stage.setScene(scene);
        stage.showAndWait();
        if(chosen == null)
            return options[0];
        return (T)chosen;
    }
    public static void  presentMessage(String message) {
        HBox hbox = new HBox(new Label(message));
        hbox.getStyleClass().add("popUpMessage");
        Scene scene = new Scene(hbox);
        scene.getStylesheets().add("gui/myStyle.css");
        Stage stage = new Stage();
        stage.setScene(scene);
        //if the user clicks anywhere other than the popup, close it.
        stage.focusedProperty().addListener( (observable, oldIsFocusedValue, newIsFocusedValue) -> {
            if(!newIsFocusedValue)
                stage.close();
        });
        stage.show();
    }
}
