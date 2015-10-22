package controllers

import javax.inject.Inject

import play.api.libs.ws._
import play.api.mvc._

class Application @Inject()(ws: WSClient) extends Controller {

  implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext

  def index = Action {
    Ok(views.html.index("Zengularity Homework"))
  }

  def project(fullName: String) = Action.async {
    ws.url(s"https://api.github.com/repos/$fullName").get() map {
      response =>
        if (response.status == 200) {

          val id = (response.json \ "id").as[Int]
          val name = (response.json \ "name").as[String]

          Ok(s"Found '$name' | id=$id")

        } else
          BadRequest(s"Repository '$fullName' does not exists.")
    }
  }
}
