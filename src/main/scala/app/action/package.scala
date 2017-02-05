package app

import app.model._
import org.json4s.NoTypeHints
import org.json4s.jackson.Serialization
import scala.io.Source

package object action {

  def grp = Group.toHtml.map(_.render).mkString
  def stl(id: Long) = Student.toHtml(id).map(_.render).mkString
  def toLong(s: String) = try {s.toLong} catch {case _: Throwable => 0l}
  def toInt(s: String) = try {s.toInt} catch {case _: Throwable => 0}

  def load(fn: String, dir: String): String = {
    try {
      Source.fromFile(dir+"/"+fn).mkString
    } catch {case _: Throwable => ""}
  }

  def encodeName(fileName: String): String = {
    java.net.URLEncoder.encode(fileName,"UTF-8").replaceAll("\\+","%20")
  }

  def decodeName(fileName: String): String = {
    java.net.URLDecoder.decode(fileName.replaceAll("%20","\\+"),"UTF-8")
  }

  implicit val formats = Serialization.formats(NoTypeHints)
  def toJson(o: AnyRef) = Serialization.write(o)

}
