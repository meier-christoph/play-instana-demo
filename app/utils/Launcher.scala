package utils

/**
  * @author Christoph MEIER (TOP)
  */
object Launcher {

  def main(args: Array[String]): Unit = {
    play.core.server.ProdServerStart.main(args)
  }
}
