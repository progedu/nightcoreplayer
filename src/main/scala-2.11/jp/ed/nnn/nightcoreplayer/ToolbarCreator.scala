package jp.ed.nnn.nightcoreplayer

import java.lang

import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.event.{ActionEvent, EventHandler}
import javafx.geometry.Pos
import javafx.scene.control.{Label, TableView}
import javafx.scene.layout.HBox
import javafx.scene.media.MediaView
import javafx.stage.Stage
import javafx.util.Duration
import jp.ed.nnn.nightcoreplayer.SizeConstants._
import jp.ed.nnn.nightcoreplayer.CreateButton._

object ToolbarCreator {

  def create(mediaView: MediaView,tableView: TableView[Movie],timeLabel:Label,primaryStage:Stage):HBox = {

    val toolBar = new HBox()
    toolBar.setMinHeight(toolBarMinHeight)
    toolBar.setAlignment(Pos.CENTER)
    toolBar.setStyle("-fx-background-color: Black")

    //ミュート解除ボタン
    val notMuteButton = createButton("volume.png",new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit =
        if(mediaView.getMediaPlayer != null){
          mediaView.getMediaPlayer.setMute(false)
          mediaView.getMediaPlayer.play()
        }
    })
    //ミュートボタン
    val muteButton = createButton("mute.png",new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit =
        if(mediaView.getMediaPlayer != null){
          mediaView.getMediaPlayer.setMute(true)
          mediaView.getMediaPlayer.play()
        }
    })

    //倍速ボタン
    val highSpeedButton = createButton("rightAllow.png",new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit =
        if(mediaView.getMediaPlayer != null){
          mediaView.getMediaPlayer.setRate(2.0)
          mediaView.getMediaPlayer.play()
        }
    })
    //first button
    val firstButton = createButton("first.png", new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit =
        if (mediaView.getMediaPlayer != null) {
          MoviePlayer.playPre(tableView, mediaView, timeLabel)
        }
    })
    //back button
    val backButton = createButton("back.png", new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit =
        if (mediaView.getMediaPlayer != null) {
          mediaView.getMediaPlayer.seek(
            mediaView.getMediaPlayer.getCurrentTime.subtract(new Duration(10000)))
        }
    })

    //play button
    val playButton = createButton("play.png", new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        val selectionModel = tableView.getSelectionModel
        if (mediaView.getMediaPlayer != null && !selectionModel.isEmpty) {
          mediaView.getMediaPlayer.play()
        }
      }
    })
    //pause button
    val pauseButton = createButton("pause.png", new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        if (mediaView.getMediaPlayer != null) mediaView.getMediaPlayer.pause()
      }
    })

    //forward button
    val forwardButton = createButton("forward.png", new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit =
        if (mediaView.getMediaPlayer != null) {
          mediaView.getMediaPlayer.seek(
            mediaView.getMediaPlayer.getCurrentTime.add(new Duration(10000)))
        }
       if(mediaView.getMediaPlayer != null && mediaView.getMediaPlayer.getCurrentTime.compareTo(mediaView.getMediaPlayer.getTotalDuration) > 0){
            mediaView.getMediaPlayer.getStartTime
       }
    })

    //last button
    val lastButton = createButton("last.png", new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit =
        if (mediaView.getMediaPlayer != null) {
          MoviePlayer.playNext(tableView, mediaView, timeLabel)
        }
    })

    //fullscreen button
    val fullscreenButton = createButton("fullscreen.png", new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        primaryStage.setFullScreen(true)
      }
      primaryStage.fullScreenProperty().addListener(new ChangeListener[java.lang.Boolean]{
        override def changed(observable: ObservableValue[_ <: lang.Boolean], oldValue: lang.Boolean, newValue: lang.Boolean): Unit = {
          if(primaryStage.isFullScreen){
            toolBar.setVisible(false)
            tableView.setVisible(false)
          }else{
            tableView.setVisible(true)
            toolBar.setVisible(true)
          }
        }
      })
    })

    toolBar.getChildren.addAll(notMuteButton,muteButton,firstButton, backButton, playButton, pauseButton, forwardButton, highSpeedButton,lastButton, fullscreenButton, timeLabel)
    toolBar
  }
}