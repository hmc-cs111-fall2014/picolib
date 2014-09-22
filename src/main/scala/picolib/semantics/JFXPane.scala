package picolib.semantics

import scala.swing.Component
import javax.swing.JLabel
import javafx.embed.swing.JFXPanel
import scalafx.scene.Scene
import scalafx.application.Platform
import scalafx.scene.control.Button
import scalafx.scene.paint.Color

abstract class JFXPane extends Component {
  override lazy val peer = new JFXPanel with SuperMixin
  Platform.runLater({ peer.setScene(scene.delegate) })

  def scene: Scene
}
