package controllers

import javax.inject.Inject

import play.api.libs.ws._
import play.api.mvc._

class Application @Inject()(ws: WSClient) extends Controller {

  val title = "Zengularity Homework"

  def index(search: String) = Action {
    Ok(views.html.index(title, search))
  }

  def stats(repo: String) = Action {
    Ok(views.html.stats(title, repo))
  }
}
