package jp.ed.nnn.nightcoreplayer

import java.io.File
import javafx.application.Application
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.{BorderPane, HBox}
import javafx.scene.media.{Media, MediaPlayer, MediaView}
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.util.Duration

object Main extends App {
  Application.launch(classOf[Main], args: _*)
}

class Main extends Application {
  private[this] val MediaViewFitWidth  = 800
  private[this] val MediaViewFitHeight = 450
  private[this] val ToolBarMinHeight   = 50

  override def start(primaryStage: Stage): Unit = {
    val path = "downloads/video.mp4"

    val media       = new Media(new File(path).toURI.toString)
    val mediaPlayer = new MediaPlayer(media)

    mediaPlayer.setRate(1.25)
    mediaPlayer.play()

    val mediaView = new MediaView(mediaPlayer)

    val timeLabel = new Label()

    mediaPlayer.currentTimeProperty().addListener(new ChangeListener[Duration] {
      override def changed(observable: ObservableValue[_ <: Duration], oldValue: Duration, newValue: Duration): Unit =
        timeLabel.setText(formatTime(mediaPlayer.getCurrentTime, mediaPlayer.getTotalDuration))
    })
    mediaPlayer.setOnReady(new Runnable {
      override def run(): Unit =
        timeLabel.setText(formatTime(mediaPlayer.getCurrentTime, mediaPlayer.getTotalDuration))
    })
    timeLabel.setText("00:00:00/00:00:00")
    timeLabel.setTextFill(Color.WHITE)

    val toolBar = new HBox(timeLabel)

    toolBar.setMinHeight(ToolBarMinHeight)
    toolBar.setAlignment(Pos.CENTER)
    toolBar.setStyle("-fx-background-color: Black")

    val baseBorderPane = new BorderPane()

    baseBorderPane.setStyle("-fx-background-color: Black")
    baseBorderPane.setCenter(mediaView)
    baseBorderPane.setBottom(toolBar)

    val scene = new Scene(baseBorderPane, MediaViewFitWidth, MediaViewFitHeight + ToolBarMinHeight)

    scene.setFill(Color.BLACK)
    mediaView.fitWidthProperty().bind(scene.widthProperty())
    mediaView.fitHeightProperty().bind(scene.heightProperty().subtract(ToolBarMinHeight))

    primaryStage.setScene(scene)
    primaryStage.show()
  }

  private[this] def formatTime(elapsed: Duration, duration: Duration): String = {
    "%02d:%02d:%02d/%02d:%02d:%02d".format(
      elapsed.toHours.toInt,
      elapsed.toMinutes.toInt % 60,
      elapsed.toSeconds.toInt % 60,
      duration.toHours.toInt,
      duration.toMinutes.toInt % 60,
      duration.toSeconds.toInt % 60)
  }

}