package jp.ed.nnn.nightcoreplayer

import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.control.{Button, Slider}
import javafx.scene.image.{Image, ImageView}
import javafx.scene.input.MouseEvent
import javafx.scene.media.MediaView

object ToolBarButtonCreator {

  // for tool bar button creating
  def createButton(imagePath: String, eventHandler: EventHandler[ActionEvent]): Button = {
    val buttonImage = new Image(getClass.getResourceAsStream(imagePath))
    val button = new Button()
    button.setGraphic(new ImageView(buttonImage))
    button.setStyle("-fx-background-color: Black")
    button.setOnAction(eventHandler)
    button.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler[MouseEvent]() {
      override def handle(event: MouseEvent): Unit = {
        button.setStyle("-fx-body-color: Black")
      }
    })
    button.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler[MouseEvent]() {
      override def handle(event: MouseEvent): Unit = {
        button.setStyle("-fx-background-color: Black")
      }
    })
    button
  }

  // for volume slider creating
  def createVolumeSlider(mediaView: MediaView): Slider = {
    val volSlider = new Slider()
    volSlider.setMax(1.0)
    volSlider.setMin(0.0)
    volSlider.setValue(0.5)
    volSlider.setMaxWidth(100)
    volSlider.setMinWidth(50)

    volSlider.valueProperty().addListener(new ChangeListener[Number] {
      override def changed(observable: ObservableValue[_ <: Number], oldValue: Number, newValue: Number) = {
        if (mediaView.getMediaPlayer != null) {
          mediaView.getMediaPlayer.setVolume(newValue.doubleValue())
        }
      }
    })
    volSlider
  }

}
