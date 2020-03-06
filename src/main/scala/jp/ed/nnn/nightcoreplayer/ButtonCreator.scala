package jp.ed.nnn.nightcoreplayer

import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.control.Button
import javafx.scene.image.{Image, ImageView}
import javafx.scene.input.MouseEvent

object ButtonCreator {
  def create(imagePath: String, event: EventHandler[ActionEvent]): Button = {
    val buttonImage = new Image(getClass.getResourceAsStream(imagePath))
    val button = new Button
    button.setGraphic(new ImageView(buttonImage))
    button.setStyle("-fx-background-color: Black")
    button.setOnAction(event)
    button.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler[MouseEvent] {
      override def handle(t: MouseEvent): Unit =
        button.setStyle("-fx-body-color: Black")
    })
    button.addEventHandler(MouseEvent.MOUSE_EXITED, (t: MouseEvent) =>
      button.setStyle("-fx-background-color: Black"))
    button
  }
}
