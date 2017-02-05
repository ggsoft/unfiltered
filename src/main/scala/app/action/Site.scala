package app.action

import app.view.Index
import app.model._
import io.netty.channel.ChannelHandler.Sharable
import unfiltered.request._
import unfiltered.response._
import unfiltered.netty.cycle._
import unfiltered.netty._

@Sharable
object Site extends Plan with ThreadPool with ServerErrorResponse {

  def intent = {
    case GET(Path("/")) => Ok ~> ResponseString(Index.page("main",0l))
    case GET(Path("/about")) => Ok ~> ResponseString(Index.page("about",0l))
    case GET(Path("/list")) => Ok ~> ResponseString(Index.page("list",0l))
    case GET(Path(Seg("list"::id::Nil))) => Ok ~> ResponseString(Index.page("list",toLong(id)))
    case GET(Path("/addgrp") & Params(params)) => {
      val name = params("name").headOption.getOrElse("")
      if (name != "") Group.add(Group(0l, name))
      Ok ~> ResponseString(grp)
    }
    case GET(Path("/editgrp") & Params(params)) => {
      val id = toLong(params("id").headOption.getOrElse(""))
      val name = params("name").headOption.getOrElse("")
      if (name != "") Group.upd(Group(id, name))
      Ok ~> ResponseString(grp)
    }
    case GET(Path("/delgrp") & Params(params)) => {
      val id = toLong(params("id").headOption.getOrElse(""))
      Group.del(id)
      Ok ~> ResponseString(grp)
    }
    case GET(Path("/updstd") & Params(params)) => {
      val id = toLong(params("id").headOption.getOrElse(""))
      Ok ~> ResponseString(toJson(Student.find(id)))
    }
    case GET(Path("/addstd") & Params(params)) => {
      val id = toLong(params("id").headOption.getOrElse(""))
      val name = params("name").headOption.getOrElse("")
      val score = toInt(params("score").headOption.getOrElse(""))
      if (name != "") Student.add(Student(0l,id,name,score))
      Ok ~> ResponseString(stl(id))
    }
    case GET(Path("/editstd") & Params(params)) => {
      val id = toLong(params("id").headOption.getOrElse(""))
      val idGrp = toLong(params("idGrp").headOption.getOrElse(""))
      val name = params("name").headOption.getOrElse("")
      val score = toInt(params("score").headOption.getOrElse(""))
      if (name != "") Student.upd(Student(id,idGrp,name,score))
      Ok ~> ResponseString(stl(idGrp))
    }
    case GET(Path("/delstd") & Params(params)) => {
      val id = toLong(params("id").headOption.getOrElse(""))
      val idGrp = toLong(params("idGrp").headOption.getOrElse(""))
      Student.del(id)
      Ok ~> ResponseString(stl(idGrp))
    }

    case GET(Path(Seg("css" :: fn :: Nil))) => {
      CssContent ~> ResponseString(load(fn,"public/css"))
    }

    case GET(Path(Seg("fonts" :: fn :: Nil))) => {
      import java.io._
      val f = new File("public/fonts/"+fn)
      try {
        ResponseHeader("Content-Disposition", List("attachment; filename*=UTF-8''"+encodeName(fn))) ~>
          new ResponseStreamer {
            def stream(os: OutputStream) {
              val in = new FileInputStream(f)
              val buffer = new Array[Byte](1024 * 16)
              var len = in.read(buffer, 0, buffer.size)
              while (len != -1) {
                os.write(buffer,0,len)
                len = in.read(buffer, 0, buffer.size)
              }
            }
          }
      } catch {
        case _: Throwable => ResponseString("")
      }
    }

    case GET(Path(Seg("js" :: fn :: Nil))) => {
      JsContent ~> ResponseString(load(fn,"public/js"))
    }

    case _ => NotFound
  }
}
