package jp.ed.nnn.nightcoreplayer

import java.io.File

import javafx.application.Application
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.collections.FXCollections
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.control.{Label, TableColumn, TableView}
import javafx.scene.layout.{BorderPane, HBox}
import javafx.scene.media.{Media, MediaPlayer, MediaView}
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.util.Duration

object Main extends App {
  Application.launch(classOf[Main], args: _*)
}

class Main extends Application {
  private[this] val MediaViewFitWidth = 800
  private[this] val MediaViewFitHeight = 450
  private[this] val ToolBarMinHeight = 50
  private[this] val TableMinWidth = 300

  override def start(primaryStage: Stage): Unit = {
    val path = "downloads/video.mp4"
    val media = new Media(new File(path).toURI.toString)
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

    val tableView = new TableView[Movie]()
    tableView.setMinWidth(TableMinWidth)
    val movies = FXCollections.observableArrayList[Movie]()
    tableView.setItems(movies)

    val fileNameColumn = new TableColumn[Movie, String]("ファイル名")
    fileNameColumn.setCellValueFactory(new PropertyValueFactory("fileName"))
    fileNameColumn.setPrefWidth(160)
    val timeColumn = new TableColumn[Movie, String]("時間")
    timeColumn.setCellValueFactory(new PropertyValueFactory("time"))
    timeColumn.setPrefWidth(80)

    tableView.getColumns.setAll(fileNameColumn, timeColumn)

      // TODO 後で消す
    movies.addAll(Movie(1L, "video.mp4", "00:00:00", "./video.mp4", null))


    val baseBorderPane = new BorderPane()

    baseBorderPane.setStyle("-fx-background-color: Black")
    baseBorderPane.setCenter(mediaView)
    baseBorderPane.setBottom(toolBar)
    baseBorderPane.setRight(tableView)

    val scene = new Scene(baseBorderPane, MediaViewFitWidth + TableMinWidth, MediaViewFitHeight + ToolBarMinHeight)

    scene.setFill(Color.BLACK)
    mediaView.fitWidthProperty().bind(scene.widthProperty().subtract(TableMinWidth))
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