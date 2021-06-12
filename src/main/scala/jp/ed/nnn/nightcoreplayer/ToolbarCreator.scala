package jp.ed.nnn.nightcoreplayer

import javafx.event.{ActionEvent, EventHandler}
import javafx.geometry.Pos
import javafx.scene.control.{Button, Label, TableView}
import javafx.scene.image.{Image, ImageView}
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.media.MediaView
import javafx.stage.Stage
import javafx.util.Duration
import jp.ed.nnn.nightcoreplayer.MoviePlayer.{playNext, playPre}
import jp.ed.nnn.nightcoreplayer.SizeConstants._

object ToolbarCreator {
  def create(
      mediaView: MediaView,
      tableView: TableView[Movie],
      timeLabel: Label,
      primaryStage: Stage
  ): HBox = {
    val toolBar = new HBox()
    toolBar.setMinHeight(toolBarMinHeight)
    toolBar.setAlignment(Pos.CENTER)
    toolBar.setStyle("-fx-background-color: Black")
    val firstButton = createButton(
      "first.png",
      (_: ActionEvent) => {
        if (mediaView.getMediaPlayer != null) {
          playPre(tableView, mediaView, timeLabel)
        }
      }
    )
    val backButton = createButton(
      "back.png",
      (_: ActionEvent) => {
        if (mediaView.getMediaPlayer != null) {
          mediaView.getMediaPlayer.seek(
            mediaView.getMediaPlayer.getCurrentTime
              .subtract(new Duration(10000))
          )
        }
      }
    )
    val playButton = createButton(
      "play.png",
      (_: ActionEvent) => {
        val selectionModel = tableView.getSelectionModel
        if (mediaView.getMediaPlayer != null && !selectionModel.isEmpty) {
          mediaView.getMediaPlayer.play()
        }
      }
    )
    val pauseButton = createButton(
      "pause.png",
      (_: ActionEvent) => {
        if (mediaView.getMediaPlayer != null) {
          mediaView.getMediaPlayer.pause()
        }
      }
    )
    val forwardButton = createButton(
      "forward.png",
      (_: ActionEvent) => {
        if (mediaView.getMediaPlayer != null) {
          mediaView.getMediaPlayer.seek(
            mediaView.getMediaPlayer.getCurrentTime.add(new Duration(10000))
          )
        }
      }
    )
    val lastButton = createButton(
      "last.png",
      (_: ActionEvent) => {
        if (mediaView.getMediaPlayer != null) {
          playNext(tableView, mediaView, timeLabel)
        }
      }
    )
    val fullscreenButton = createButton(
      "fullscreen.png",
      (_: ActionEvent) => {
        primaryStage.setFullScreen(true)
      }
    )
    toolBar.getChildren.addAll(
      firstButton,
      backButton,
      playButton,
      pauseButton,
      forwardButton,
      lastButton,
      fullscreenButton,
      timeLabel
    )
    toolBar
  }

  private[this] def createButton(
      imagePath: String,
      eventHandler: EventHandler[ActionEvent]
  ): Button = {
    val buttonImage = new Image(getClass.getResourceAsStream(imagePath))
    val button = new Button()
    button.setGraphic(new ImageView(buttonImage))
    button.setStyle("-fx-background-color:Black")
    button.setOnAction(eventHandler)
    button.addEventHandler(
      MouseEvent.MOUSE_ENTERED,
      (_: MouseEvent) => {
        button.setStyle("-fx-body-color: Black")
      }
    )
    button.addEventHandler(
      MouseEvent.MOUSE_EXITED,
      (_: MouseEvent) => {
        button.setStyle("-fx-background-color: Black")
      }
    )
    button
  }
}
