package jp.ed.nnn

import javafx.util.Duration

package object nightcoreplayer {

  def formatTime(duration: Duration): String = {
    "%02d:%02d:%02d.%03d".format(
      duration.toHours.toInt,
      duration.toMinutes.toInt % 60,
      duration.toSeconds.toInt % 60,
      duration.toMillis.toInt % 60)
  }

  def formatTime(elapsed: Duration, duration: Duration): String =
    s"${formatTime(elapsed)}/${formatTime(duration)}"
}
