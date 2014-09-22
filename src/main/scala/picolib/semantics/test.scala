package picolib.semantics

import java.awt.Dimension
import java.io.File
import scala.language.postfixOps
import scala.swing._
import javax.swing.table.AbstractTableModel
import picolib.maze.Maze
import picolib.maze.Position
import scala.swing.event.TableUpdated
import scala.swing.Button
import scala.swing.BoxPanel
import scala.swing.event.ButtonClicked

object PicobotSwingApplication extends SimpleSwingApplication {
  val bot = EmptyRoomBot
  def top = new MainFrame {

    preferredSize = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE)
    
    val runButton = new Button("run") {
      reactions += {
        case ButtonClicked(b) ⇒ bot.run()
      }
    }
    
    val tableModel = new PicobotModel(bot)
    val table = new Table(bot.maze.width, bot.maze.height) { 
      model = tableModel
      
      override def updateCell(row: Int, col: Int) = {
        println(row, col)
        super.updateCell(row, col)
      }
    }
    
    listenTo(table)
    reactions += {case e: TableUpdated => println("updated")}
    
    contents = new BoxPanel(Orientation.Vertical) {
      contents += runButton
      contents += table
    }
    
    //bot.run()

    /*val mazePanel = new GridPanel(bot.maze.width, bot.maze.height) {
     contents ++= bot.maze.positions map ( (p: Position) ⇒ new Button(p.toString))
    }*/
    
    /*val mazePanel = new Table(bot.maze.width, bot.maze.height) {
      gridColor = Color.RED
    }*/
    
   // contents = mazePanel
  }
}

class PicobotModel(val bot: Picobot) extends AbstractTableModel {
  override def getColumnCount() = bot.maze.width 
  override def getRowCount() = bot.maze.height
  override def getValueAt(row: Int, col: Int) = valueFor(Position(col, row))

  def valueFor(p: Position) =
    if (bot.maze.wallPositions contains p)
      PicobotModel.WALL
    else if (bot.position == p)
      PicobotModel.BOT
    else if (bot.visited contains p)
      PicobotModel.VISITED
    else
      PicobotModel.UNVISITED
}

object PicobotModel {
  val VISITED = "V"
  val WALL = "W"
  val BOT = "B"
  val UNVISITED = " "
}

object EmptyRoomBot extends Picobot(
  Maze("resources" + File.separator + "empty.txt"),
  List(Rule(State("0"),
    Surroundings(Anything, Anything, Open, Anything),
    West,
    State("0")),

    Rule(State("0"),
      Surroundings(Open, Anything, Blocked, Anything),
      North,
      State("1")),

    Rule(State("0"),
      Surroundings(Blocked, Open, Blocked, Anything),
      South,
      State("2")),

    Rule(State("1"),
      Surroundings(Open, Anything, Anything, Anything),
      North,
      State("1")),

    Rule(State("1"),
      Surroundings(Blocked, Anything, Anything, Open),
      South,
      State("2")),

    Rule(State("2"),
      Surroundings(Anything, Anything, Anything, Open),
      South,
      State("2")),

    Rule(State("2"),
      Surroundings(Anything, Open, Anything, Blocked),
      East,
      State("3")),

    Rule(State("3"),
      Surroundings(Open, Anything, Anything, Anything),
      North,
      State("3")),

    Rule(State("3"),
      Surroundings(Blocked, Open, Anything, Anything),
      East,
      State("2"))))
