package jp.ed.nnn.nightcoreplayer

import javafx.event.{ActionEvent, EventHandler}
import javafx.geometry.Pos
import javafx.scene.control.{Button, Label, TableView}
import javafx.scene.image.{Image, ImageView}
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.media.{MediaPlayer, MediaView}
import javafx.stage.Stage
import javafx.util.Duration

import jp.ed.nnn.nightcoreplayer.SizeConstants._

object ToolbarCreator {

  def create(mediaView: MediaView, tableView: TableView[Movie],
             timeLabel: Label, primaryStage: Stage): HBox = {
    val toolBar = new HBox()
    toolBar.setMinHeight(toolBarMinHeight)
    toolBar.setAlignment(Pos.CENTER)
    toolBar.setStyle("-fx-background-color: Black")

    // first button
    val firstButton = createButton("first.png", new EventHandler[ActionEvent] {
      override def handle(t: ActionEvent): Unit =
        if (mediaView.getMediaPlayer != null)
          MoviePlayer.playPre(tableView, mediaView, timeLabel)
    })

    // back button
    val backButton = createButton("back.png", new EventHandler[ActionEvent] {
      override def handle(t: ActionEvent): Unit =
        if (mediaView.getMediaPlayer != null)
          mediaView.getMediaPlayer.seek(
            mediaView.getMediaPlayer.getCurrentTime.subtract(new Duration(10000)))
    })

    // play button
    val playButton = createButton("play.png", new EventHandler[ActionEvent] {
      override def handle(t: ActionEvent): Unit =
        if (mediaView.getMediaPlayer != null && !tableView.getSelectionModel.isEmpty)
          mediaView.getMediaPlayer.play()
    })

    // pause button
    val pauseButton = createButton("pause.png", new EventHandler[ActionEvent] {
      override def handle(t: ActionEvent): Unit =
        if (mediaView.getMediaPlayer != null) {
          if (mediaView.getMediaPlayer.getStatus == MediaPlayer.Status.PLAYING) {
            mediaView.getMediaPlayer.pause()
          } else {
            if (!tableView.getSelectionModel.isEmpty) mediaView.getMediaPlayer.play()
          }
        }
    })

    // forward button
    val forwardButton = createButton("forward.png", new EventHandler[ActionEvent] {
      override def handle(t: ActionEvent): Unit =
        if (mediaView.getMediaPlayer != null) {
          mediaView.getMediaPlayer.seek(
            mediaView.getMediaPlayer.getCurrentTime.add(new Duration(10000)))
        }
    })

    // last button
    val lastButton = createButton("last.png", (t: ActionEvent) =>
      if (mediaView.getMediaPlayer != null) MoviePlayer.playNext(tableView, mediaView, timeLabel))

    // fullscreen button
    val fullscreenButton = createButton("fullscreen.png", (t: ActionEvent) =>
      primaryStage.setFullScreen(true))

    toolBar.getChildren.addAll(
      firstButton, backButton, playButton, pauseButton,
      forwardButton, lastButton, fullscreenButton, timeLabel)
    toolBar
  }

  private[this] def createButton(imagePath: String, event: EventHandler[ActionEvent]): Button = {
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
