package controllers

import javax.inject.Inject

import play.api.libs.ws._
import play.api.mvc._

class Application @Inject()(ws: WSClient) extends Controller {

  val title = "Zengularity Homework"

  def index = Action {
    Ok(views.html.index(title))
  }

  def stats(repo: String) = Action { // FIXME: Create the 'stats' page.
    Ok(views.html.index(title))
  }
}
