package jp.ed.nnn.nightcoreplayer

import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.scene.control.{Label, Slider, TableView}
import javafx.scene.media.{MediaPlayer, MediaView}
import javafx.util.Duration

object MoviePlayer {

  def play(movie: Movie, tableView: TableView[Movie],
           mediaView: MediaView, timeLabel: Label, rateLabel: Label, volSlider: Slider): Unit = {
    if (mediaView.getMediaPlayer != null) {
      val oldPlayer = mediaView.getMediaPlayer
      oldPlayer.stop()
      oldPlayer.dispose()
    }

    val mediaPlayer = new MediaPlayer(movie.media)
    mediaPlayer.currentTimeProperty().addListener(new ChangeListener[Duration] {
      override def changed(observable: ObservableValue[_ <: Duration], oldValue: Duration, newValue: Duration): Unit =
        timeLabel.setText(formatTime(mediaPlayer.getCurrentTime, mediaPlayer.getTotalDuration))
    })

    mediaPlayer.setOnReady(new Runnable {
      override def run(): Unit = {
        timeLabel.setText(formatTime(mediaPlayer.getCurrentTime, mediaPlayer.getTotalDuration))
        rateLabel.setText(formatRate(mediaPlayer.getRate))
      }
    })
    mediaPlayer.setOnEndOfMedia(new Runnable {
      override def run(): Unit = playNext(tableView, mediaView, timeLabel, rateLabel, volSlider)
    })

    mediaView.setMediaPlayer(mediaPlayer)
    mediaPlayer.setVolume(volSlider.getValue)
    mediaPlayer.play()
  }

  sealed trait Track
  object Pre extends Track
  object Next extends Track

  def playAt(track: Track, tableView: TableView[Movie], mediaView: MediaView, timeLabel: Label, rateLabel: Label, volSlider: Slider): Unit = {
    val selectionModel = tableView.getSelectionModel
    if (selectionModel.isEmpty) return
    val index = selectionModel.getSelectedIndex
    val changedIndex = track match {
      case Pre => (tableView.getItems.size() + index - 1) % tableView.getItems.size()
      case Next => (index + 1) % tableView.getItems.size()
    }
      selectionModel.select(changedIndex)
    val movie = selectionModel.getSelectedItem
    play(movie, tableView, mediaView, timeLabel, rateLabel, volSlider)
  }

  def playPre(tableView: TableView[Movie], mediaView: MediaView, timeLabel: Label, rateLabel: Label, volSlider: Slider): Unit =
    playAt(Pre, tableView, mediaView, timeLabel, rateLabel, volSlider)

  def playNext(tableView: TableView[Movie], mediaView: MediaView, timeLabel: Label, rateLabel: Label, volSlider: Slider): Unit =
    playAt(Next, tableView, mediaView, timeLabel, rateLabel, volSlider)

}