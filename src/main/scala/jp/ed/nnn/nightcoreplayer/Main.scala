package jp.ed.nnn.nightcoreplayer

import javafx.application.Application
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.collections.FXCollections
import javafx.scene.Scene
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.control.{Label, TableColumn, TableRow, TableView}
import javafx.scene.input.MouseEvent
import javafx.scene.layout.BorderPane
import javafx.scene.media.MediaView
import javafx.scene.paint.Color
import javafx.stage.Stage
import jp.ed.nnn.nightcoreplayer.SizeConstants._

object Main extends App {
  Application.launch(classOf[Main], args: _*)
}

class Main extends Application {

  override def start(primaryStage: Stage): Unit = {

    val mediaView = new MediaView()

    val timeLabel = new Label()
    timeLabel.setText("00:00:00.000/00:00:00.000")
    timeLabel.setTextFill(Color.WHITE)

    val tableView = new TableView[Movie]()
    tableView.setMinWidth(tableMinWidth)
    val movies = FXCollections.observableArrayList[Movie]()
    tableView.setItems(movies)
    tableView.setRowFactory((param: TableView[Movie]) => {
      val row = new TableRow[Movie]()
      row.setOnMouseClicked((event: MouseEvent) => {
        if (event.getClickCount >= 1 && !row.isEmpty) {
          MoviePlayer.play(row.getItem, tableView, mediaView, timeLabel)
        }
      })
      row
    })

    val fileNameColumn = new TableColumn[Movie, String]("ファイル名")
    fileNameColumn.setCellValueFactory(new PropertyValueFactory("fileName"))
    fileNameColumn.setPrefWidth(160)
    val timeColumn = new TableColumn[Movie, String]("時間")
    timeColumn.setCellValueFactory(new PropertyValueFactory("time"))
    timeColumn.setPrefWidth(80)
    val deleteActionColumn = new TableColumn[Movie, Long]("削除")
    deleteActionColumn.setCellValueFactory(new PropertyValueFactory("id"))
    deleteActionColumn.setPrefWidth(60)
    deleteActionColumn.setCellFactory((param: TableColumn[Movie, Long]) => {
      new DeleteCell(movies, mediaView, tableView)
    })

    tableView.getColumns.setAll(fileNameColumn, timeColumn, deleteActionColumn)

    val toolBar = ToolbarCreator.create(mediaView, tableView, timeLabel, primaryStage)

    val baseBorderPane = new BorderPane()
    baseBorderPane.setStyle("-fx-background-color: Black")
    baseBorderPane.setCenter(mediaView)
    baseBorderPane.setBottom(toolBar)
    baseBorderPane.setRight(tableView)

    val scene = new Scene(baseBorderPane,
      mediaViewFitWidth + tableMinWidth,
      mediaViewFitHeight + toolBarMinHeight)
    scene.setFill(Color.BLACK)

    mediaView.fitWidthProperty().bind(scene.widthProperty().subtract(tableMinWidth))
    mediaView.fitHeightProperty().bind(scene.heightProperty().subtract(toolBarMinHeight))

    scene.setOnDragOver(new MovieFileDragOverEventHandler(scene))
    scene.setOnDragDropped(new MovieFileDragDroppedEventHandler(movies))

    primaryStage.fullScreenProperty().addListener(new ChangeListener[java.lang.Boolean] {
      override def changed(observableValue: ObservableValue[_ <: java.lang.Boolean],
                           t: java.lang.Boolean, t1: java.lang.Boolean): Unit = {
        if (t1) {
          mediaView.fitWidthProperty().bind(scene.widthProperty())
          mediaView.fitHeightProperty().bind(scene.heightProperty())
        } else {
          mediaView.fitWidthProperty().bind(scene.widthProperty().subtract(tableMinWidth))
          mediaView.fitHeightProperty().bind(scene.heightProperty().subtract(toolBarMinHeight))
        }
      }
    })
    primaryStage.setTitle("mp4ファイルをドラッグ＆ドロップしてください")
    primaryStage.setScene(scene)
    primaryStage.show()
  }
}