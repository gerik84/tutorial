
// @GENERATOR:play-routes-compiler
// @SOURCE:/home/redline/project/java/tutorial/conf/routes
// @DATE:Thu Sep 21 21:04:36 SAMT 2017

import play.api.routing.JavaScriptReverseRoute


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:6
package controllers.javascript {

  // @LINE:16
  class ReverseNewsController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:16
    def getNews: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.NewsController.getNews",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "news"})
        }
      """
    )
  
    // @LINE:17
    def createNews: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.NewsController.createNews",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "news"})
        }
      """
    )
  
    // @LINE:19
    def deleteNews: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.NewsController.deleteNews",
      """
        function(ID0) {
          return _wA({method:"DELETE", url:"""" + _prefix + { _defaultPrefix } + """" + "news/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[java.util.UUID]].javascriptUnbind + """)("ID", ID0))})
        }
      """
    )
  
    // @LINE:18
    def modifyNews: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.NewsController.modifyNews",
      """
        function(ID0) {
          return _wA({method:"PATCH", url:"""" + _prefix + { _defaultPrefix } + """" + "news/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[java.util.UUID]].javascriptUnbind + """)("ID", ID0))})
        }
      """
    )
  
  }

  // @LINE:6
  class ReverseHomeController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:6
    def index: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.index",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + """"})
        }
      """
    )
  
    // @LINE:7
    def test: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.test",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "test"})
        }
      """
    )
  
  }

  // @LINE:10
  class ReverseAssets(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:10
    def versioned: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.Assets.versioned",
      """
        function(file1) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "assets/" + (""" + implicitly[play.api.mvc.PathBindable[Asset]].javascriptUnbind + """)("file", file1)})
        }
      """
    )
  
  }

  // @LINE:13
  class ReverseApiController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:13
    def signup: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.ApiController.signup",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "signup"})
        }
      """
    )
  
  }


}
