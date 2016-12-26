package jp.ed.nnn.nightcoreplayer

import java.lang.Boolean
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.event.{ActionEvent, EventHandler}
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.{Label, Slider, TableView}
import javafx.scene.layout.{BorderPane, HBox}
import javafx.scene.media.MediaView
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.util.Duration

import jp.ed.nnn.nightcoreplayer.SizeConstants._
import jp.ed.nnn.nightcoreplayer.ToolBarButtonCreator.createButton

object ToolBarCreator {

  def create(scene: Scene, mediaView: MediaView, tableView: TableView[Movie],
             timeLabel: Label, primaryStage: Stage, baseBorderPane: BorderPane,
             volSlider: Slider, rateLabel: Label): HBox = {
    val toolBar = new HBox()
    toolBar.setMinHeight(toolBarMinHeight)
    toolBar.setAlignment(Pos.CENTER)
    toolBar.setStyle("-fx-background-color: Black")

    // first button
    val firstButton = createButton("first.png", new EventHandler[ActionEvent]() {
      override def handle(event: ActionEvent): Unit =
        if (mediaView.getMediaPlayer != null) {
          MoviePlayer.playPre(tableView, mediaView, timeLabel, rateLabel, volSlider)
        }
    })

    // back button
    val backButton = createButton("back.png", new EventHandler[ActionEvent]() {
      override def handle(event: ActionEvent): Unit =
        if (mediaView.getMediaPlayer != null) {
          mediaView.getMediaPlayer.seek(
            mediaView.getMediaPlayer.getCurrentTime.subtract(new Duration(10000)))
        }
    })

    // play button
    val playButton = createButton("play.png", new EventHandler[ActionEvent]() {
      override def handle(event: ActionEvent): Unit = {
        val selectionModel = tableView.getSelectionModel
        if (mediaView.getMediaPlayer != null && !selectionModel.isEmpty) {
          mediaView.getMediaPlayer.play()
        }
      }
    })

    // pause button
    val pauseButton = createButton("pause.png", new EventHandler[ActionEvent]() {
      override def handle(event: ActionEvent): Unit =
        if (mediaView.getMediaPlayer != null) {
          mediaView.getMediaPlayer.pause()
        }
    })

    // forward button
    val forwardButton = createButton("forward.png", new EventHandler[ActionEvent]() {
      override def handle(event: ActionEvent): Unit = {
        if (mediaView.getMediaPlayer != null) {
          val forwardedTime = mediaView.getMediaPlayer.getCurrentTime.add(new Duration(10000))
          val durationCompare = mediaView.getMediaPlayer.getTotalDuration.greaterThan(forwardedTime)
          if (durationCompare) {
            mediaView.getMediaPlayer.seek(forwardedTime)
          }
        }
      }
    })

    // last button
    val lastButton = createButton("last.png", new EventHandler[ActionEvent]() {
      override def handle(event: ActionEvent): Unit =
        if (mediaView.getMediaPlayer != null) {
          MoviePlayer.playNext(tableView, mediaView, timeLabel, rateLabel, volSlider)
        }
    })

    // fullscreen button
    val fullscreenButton = createButton("fullscreen.png", new EventHandler[ActionEvent]() {
      override def handle(event: ActionEvent): Unit = {
        primaryStage.setFullScreen(true)
      }

      primaryStage.fullScreenProperty().addListener(new ChangeListener[java.lang.Boolean]{
        override def changed(observable: ObservableValue[_ <: Boolean], oldValue: Boolean, newValue: Boolean): Unit = {
          val isMediaPlayer = mediaView.getMediaPlayer != null
          val isFullscreen = primaryStage.isFullScreen
          if (isMediaPlayer) {
            if (isFullscreen) {
              baseBorderPane.setRight(null)
              baseBorderPane.setBottom(null)
              mediaView.fitHeightProperty().bind(scene.heightProperty())
              mediaView.fitWidthProperty().bind(scene.widthProperty())
            } else if(!isFullscreen) {
              baseBorderPane.setRight(tableView)
              baseBorderPane.setBottom(toolBar)
              mediaView.fitWidthProperty().bind(scene.widthProperty().subtract(tableMinWidth))
              mediaView.fitHeightProperty().bind(scene.heightProperty().subtract(toolBarMinHeight))
            }
          }
        }
      })

    })

    // volume button label
    val volLabel = new Label("Vol.")
    volLabel.setTextFill(Color.WHITE)

    // play-rate change button
    // UP
    val rateUpButton = createButton("arrow-up.png", new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent) = {
        if (mediaView.getMediaPlayer != null) {
          val mediaPlayer = mediaView.getMediaPlayer
          val nowRate = mediaPlayer.getRate
          val newRate = nowRate + 0.25
          if (newRate <= 10) {
            mediaPlayer.setRate(newRate)
            rateLabel.setText(formatRate(newRate))
          }
        }
      }
    })
    // DOWN
    val rateDownButton = createButton("arrow-down.png", new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent) = {
        if (mediaView.getMediaPlayer != null) {
          val mediaPlayer = mediaView.getMediaPlayer
          val nowRate = mediaPlayer.getRate
          val newRate = nowRate - 0.25
          if (newRate > 0) {
            mediaPlayer.setRate(newRate)
            rateLabel.setText(formatRate(newRate))
          }
        }
      }
    })


    toolBar.getChildren.addAll(
      timeLabel, firstButton, backButton, playButton, pauseButton, forwardButton, lastButton,
      fullscreenButton, volLabel, volSlider, rateLabel, rateUpButton, rateDownButton)
    toolBar
  }
}

