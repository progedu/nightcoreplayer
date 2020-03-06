package jp.ed.nnn.nightcoreplayer

import javafx.event.ActionEvent
import javafx.geometry.Pos
import javafx.scene.control.{Label, TableView}
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
    val firstButton = ButtonCreator.create("first.png", (t: ActionEvent) =>
      if (mediaView.getMediaPlayer != null)
        MoviePlayer.playPre(tableView, mediaView, timeLabel))

    // back button
    val backButton = ButtonCreator.create("back.png", (t: ActionEvent) =>
      if (mediaView.getMediaPlayer != null)
        mediaView.getMediaPlayer.seek(
          mediaView.getMediaPlayer.getCurrentTime.subtract(new Duration(10000))))

    // play button
    val playButton = ButtonCreator.create("play.png", (t: ActionEvent) =>
      if (mediaView.getMediaPlayer != null && !tableView.getSelectionModel.isEmpty)
        mediaView.getMediaPlayer.play())

    // pause button
    val pauseButton = ButtonCreator.create("pause.png", (t: ActionEvent) =>
      if (mediaView.getMediaPlayer != null) {
        if (mediaView.getMediaPlayer.getStatus == MediaPlayer.Status.PLAYING) {
          mediaView.getMediaPlayer.pause()
        } else {
          if (!tableView.getSelectionModel.isEmpty) mediaView.getMediaPlayer.play()
        }
      }
    )

    // forward button
    val forwardButton = ButtonCreator.create("forward.png", (t: ActionEvent) =>
      if (mediaView.getMediaPlayer != null)
        mediaView.getMediaPlayer.seek(
          mediaView.getMediaPlayer.getCurrentTime.add(new Duration(10000))))

    // last button
    val lastButton = ButtonCreator.create("last.png", (t: ActionEvent) =>
      if (mediaView.getMediaPlayer != null) MoviePlayer.playNext(tableView, mediaView, timeLabel))

    // fullscreen button
    val fullscreenButton = ButtonCreator.create("fullscreen.png", (t: ActionEvent) =>
      primaryStage.setFullScreen(true))

    toolBar.getChildren.addAll(
      firstButton, backButton, playButton, pauseButton,
      forwardButton, lastButton, fullscreenButton, timeLabel)
    toolBar
  }
}
