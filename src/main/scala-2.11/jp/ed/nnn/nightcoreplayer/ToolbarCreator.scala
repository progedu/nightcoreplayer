package jp.ed.nnn.nightcoreplayer

import javafx.event.{ActionEvent, EventHandler}
import javafx.geometry.Pos
import javafx.scene.control.{Label, TableView}
import javafx.scene.layout.HBox
import javafx.scene.media.MediaView
import javafx.stage.Stage
import javafx.util.Duration

import jp.ed.nnn.nightcoreplayer.SizeConstants._

object ToolbarCreator {

  def create(mediaView: MediaView, tableView: TableView[Movie], timeLabel: Label, primaryStage: Stage): HBox = {
    val toolBar = new HBox()
    toolBar.setMinHeight(toolBarMinHeight)
    toolBar.setAlignment(Pos.CENTER)
    toolBar.setStyle("-fx-background-color: Black")

    // 倍速ボタン
    val speedupButton =  ButtonCreator.create("arrow47_002.png", new EventHandler[ActionEvent]() {
      override def handle(event: ActionEvent): Unit =
        if (mediaView.getMediaPlayer != null) {
          MoviePlayer.playX(2.0f)
        }
    })
    // first button
    val firstButton = ButtonCreator.create("first.png", new EventHandler[ActionEvent]() {
      override def handle(event: ActionEvent): Unit =
        if (mediaView.getMediaPlayer != null) {
          MoviePlayer.playPre(tableView, mediaView, timeLabel)
        }
    })

    // back button
    val backButton = ButtonCreator.create("back.png", new EventHandler[ActionEvent]() {
      override def handle(event: ActionEvent): Unit =
        if (mediaView.getMediaPlayer != null) {
          mediaView.getMediaPlayer.seek(
            mediaView.getMediaPlayer.getCurrentTime.subtract(new Duration(10000)))
        }
    })

    // play button
    val playButton = ButtonCreator.create("play.png", new EventHandler[ActionEvent]() {
      override def handle(event: ActionEvent): Unit = {
        val selectionModel = tableView.getSelectionModel
        if (mediaView.getMediaPlayer != null && !selectionModel.isEmpty) {
          mediaView.getMediaPlayer.play()
        }
      }
    })

    // pause button
    val pauseButton = ButtonCreator.create("pause.png", new EventHandler[ActionEvent]() {
      override def handle(event: ActionEvent): Unit = {
        if (mediaView.getMediaPlayer != null) mediaView.getMediaPlayer.pause()
      }
    })

    // forward button
    val forwardButton = ButtonCreator.create("forward.png", new EventHandler[ActionEvent]() {
      override def handle(event: ActionEvent): Unit =
        if (mediaView.getMediaPlayer != null) {
          val player = mediaView.getMediaPlayer
          if (player.getTotalDuration.greaterThan(player.getCurrentTime.add(new Duration(10000)))) {
            player.seek(player.getCurrentTime.add(new Duration(10000)))
          }
        }
    })

    // last button
    val lastButton = ButtonCreator.create("last.png", new EventHandler[ActionEvent]() {
      override def handle(event: ActionEvent): Unit =
        if (mediaView.getMediaPlayer != null) {
          MoviePlayer.playNext(tableView, mediaView, timeLabel)
        }
    })

    // fullscreen button
    val fullscreenButton = ButtonCreator.create("fullscreen.png", new EventHandler[ActionEvent]() {
      override def handle(event: ActionEvent): Unit =
        primaryStage.setFullScreen(true)
    })

    toolBar.getChildren.addAll(
      speedupButton, firstButton, backButton, playButton, pauseButton, forwardButton, lastButton, fullscreenButton, timeLabel)
    toolBar
  }
}