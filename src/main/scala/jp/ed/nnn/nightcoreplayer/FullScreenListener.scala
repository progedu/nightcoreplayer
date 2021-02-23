package jp.ed.nnn.nightcoreplayer

import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.scene.Scene
import javafx.scene.control.TableView
import javafx.scene.layout.HBox
import javafx.scene.media.MediaView
import jp.ed.nnn.nightcoreplayer.SizeConstants.{screenHeight, screenWidth, tableMinWidth, toolBarMinHeight}

import java.lang

class FullScreenListener (scene: Scene, mediaView: MediaView, tableView: TableView[Movie], toolBar: HBox) extends ChangeListener[java.lang.Boolean] {
  override def changed(observableValue: ObservableValue[_ <: lang.Boolean], t: lang.Boolean, t1: lang.Boolean): Unit = {

    toolBar.visibleProperty().setValue(!observableValue.getValue)
    toolBar.managedProperty().setValue(!observableValue.getValue)
    tableView.visibleProperty().setValue(!observableValue.getValue)
    tableView.managedProperty().setValue(!observableValue.getValue)

    if (observableValue.getValue) {
      mediaView.fitWidthProperty().unbind()
      mediaView.fitHeightProperty().unbind()
      mediaView.setFitWidth(screenWidth)
      mediaView.setFitHeight(screenHeight)
    } else {
      mediaView.fitWidthProperty().bind(scene.widthProperty().subtract(tableMinWidth))
      mediaView.fitHeightProperty().bind(scene.heightProperty().subtract(toolBarMinHeight))
    }
  }
}