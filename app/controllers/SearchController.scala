package controllers

import javax.inject.Inject

import play.api.libs.ws._
import play.api.mvc._

class SearchController @Inject()(ws: WSClient) extends Controller {

  implicit val context = play.api.libs.concurrent.Execution.Implicits.defaultContext

  def perform(input: String) = Action.async {
    ws.url(s"https://api.github.com/search/repositories?q=$input&per_page=20").get() map {
      response =>
        if (response.status == 200) {
          Ok(response.json)
        } else
          BadRequest(s"Repository '$input' does not exists.")
    }
  }
}
