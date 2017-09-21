
// @GENERATOR:play-routes-compiler
// @SOURCE:/home/redline/project/java/tutorial/conf/routes
// @DATE:Thu Sep 21 21:04:36 SAMT 2017

import play.api.mvc.Call


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:6
package controllers {

  // @LINE:16
  class ReverseNewsController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:16
    def getNews(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "news")
    }
  
    // @LINE:17
    def createNews(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "news")
    }
  
    // @LINE:19
    def deleteNews(ID:java.util.UUID): Call = {
      
      Call("DELETE", _prefix + { _defaultPrefix } + "news/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[java.util.UUID]].unbind("ID", ID)))
    }
  
    // @LINE:18
    def modifyNews(ID:java.util.UUID): Call = {
      
      Call("PATCH", _prefix + { _defaultPrefix } + "news/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[java.util.UUID]].unbind("ID", ID)))
    }
  
  }

  // @LINE:6
  class ReverseHomeController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:6
    def index(): Call = {
      
      Call("GET", _prefix)
    }
  
    // @LINE:7
    def test(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "test")
    }
  
  }

  // @LINE:10
  class ReverseAssets(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:10
    def versioned(file:Asset): Call = {
      implicit val _rrc = new play.core.routing.ReverseRouteContext(Map(("path", "/public")))
      Call("GET", _prefix + { _defaultPrefix } + "assets/" + implicitly[play.api.mvc.PathBindable[Asset]].unbind("file", file))
    }
  
  }

  // @LINE:13
  class ReverseApiController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:13
    def signup(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "signup")
    }
  
  }


}
