package controllers

import javax.inject.Inject

import play.api.libs.ws._
import play.api.mvc._
import play.api.libs.json._

class MainController @Inject()(ws: WSClient) extends Controller {

  implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext

  private val api = "https://api.github.com"

  def definition = Action { // FIXME: Fill API URLs from routes.
    val result = Json.obj (
      "users_stats" -> "/api/users/{repo}{?range}",
      "commits_projection" -> "/api/commits/{repo}{?range}" )
    Ok(result)
  }

  def users(repo: String, range: Int) = Action.async {
    for { contributorsResponse <- ws.url(s"$api/repos/$repo/contributors").get()
          commitsResponse <- ws.url(s"$api/repos/$repo/commits?page=1&per_page=$range").get()
    } yield {
        if (contributorsResponse.status == 200) {
          // Get the commit ratio by contributors in a map.
          val impacts = commitsResponse.json.as[List[JsObject]] groupBy {
            x => (x \ "committer" \ "id").as[Int]
          } mapValues {
            x => x.size * 100 / commitsResponse.json.as[List[JsObject]].size
          }

          // Get the contributors list as JSON objects, with login and impact.
          val contributors = contributorsResponse.json.as[List[JsObject]] map {
            x =>
              val login = (x \ "login").as[String]
              val impact =  impacts.getOrElse((x \ "id").as[Int], 0)

              Json.obj("login" -> login, "impact" -> impact)
          }

          // Sort contributors by descending impact order.
          Ok(Json.toJson(contributors sortBy { x => -(x \ "impact").as[Int] }))
        } else
          BadRequest(error(s"Repository '$repo' does not exists."))
    }
  }

  def commits(repo: String, range: Int) = Action.async {
    ws.url(s"$api/repos/$repo/commits?page=1&per_page=$range").get() map {
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
