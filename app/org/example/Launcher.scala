package org.example

/**
  * @author Christoph MEIER (TOP)
  */
object Launcher {
  def main(args: Array[String]): Unit = {
    play.core.server.NettyServer.main(args)
  }
}
