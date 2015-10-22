package controllers

import javax.inject.Inject

import play.api.libs.ws._
import play.api.mvc._

class Application @Inject()(ws: WSClient) extends Controller {

  implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext

  def index = Action {
    Ok(views.html.index("Zengularity Homework"))
  }
}
