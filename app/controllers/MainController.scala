package controllers

import javax.inject.Inject

import play.api.libs.ws._
import play.api.mvc._
import play.api.libs.json._

class MainController @Inject()(ws: WSClient) extends Controller {

  implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext

  def definition = Action { // FIXME: Fill API URLs from routes.
    val result = Json.obj (
      "users_stats" -> "/api/users/{repo}{?range}",
      "commits_projection" -> "/api/commits/{repo}{?range}" )
    Ok(result)
  }

  def users(repo: String, range: Int) = Action.async {
    ws.url(s"https://api.github.com/repos/$repo/contributors").get() map {
      response =>
        if (response.status == 200) {
          Ok(Json.toJson(response.json \\ "login"))
        } else
          BadRequest(error(s"Repository '$repo' does not exists."))
    }
  }

  def commits(repo: String, range: Int) = Action.async {
    ws.url(s"https://api.github.com/repos/$repo/commits?page=1&per_page=$range").get() map {
      response =>
        if (response.status == 200) {
          Ok(Json.toJson(response.json \\ "commit" ))
        } else
          BadRequest(error(s"Repository '$repo' does not exists."))
    }
  }

  private def error(message: String) = { // FIXME: Fill documentation URL from routes.
    Json.obj("message" -> message, "documentation_url" -> "/api")
  }
}
