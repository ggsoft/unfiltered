package app

import app.action.Site
import unfiltered.netty

object Server {
  def main(args: Array[String]) {
    netty.Server
      .http(8000)
      .plan(Site)
      .run()
  }
}