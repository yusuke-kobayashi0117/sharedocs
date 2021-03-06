package controller

import java.util.Locale

import model.typebinder.ArticleId
import operation.ArticleOperation

/**
 * The controller for root(/) path.
 */
class RootController extends ApplicationController {
  protectFromForgery()

  // --------------
  // GET /
  def index = {
    val articleOperation: ArticleOperation = inject[ArticleOperation]
    loginUser match {
      case Some(user) => {
        set("items", articleOperation.getPage())
        set("sidebar", articleOperation.getIndexSidebar(user))
        render(s"/articles/list")
      }
      case _ => {
        val locale = request.getLocale
        if (locale == null || locale != Locale.JAPANESE) {
          setCurrentLocale("en")
        }
        set("ref", params.get("ref"))
        render(s"/login")
      }
    }
  }

  // --------------
  // GET /more/{maxId}
  def more(maxId: ArticleId) = {
    loginUser match {
      case Some(user) => {
        val articleOperation: ArticleOperation = inject[ArticleOperation]
        set("items", articleOperation.getPage(maxId))
        render(s"/articles/scroll")
      }
      case _ => {
        val locale = request.getLocale.toString
        if (locale != "ja") {
          setCurrentLocale("en")
        }
        render(s"/login")
      }
    }
  }

}

