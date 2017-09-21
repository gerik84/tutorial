
// @GENERATOR:play-routes-compiler
// @SOURCE:/home/redline/project/java/tutorial/conf/routes
// @DATE:Thu Sep 21 21:04:36 SAMT 2017

package router

import play.core.routing._
import play.core.routing.HandlerInvokerFactory._

import play.api.mvc._

import _root_.controllers.Assets.Asset
import _root_.play.libs.F

class Routes(
  override val errorHandler: play.api.http.HttpErrorHandler, 
  // @LINE:6
  HomeController_1: controllers.HomeController,
  // @LINE:10
  Assets_2: controllers.Assets,
  // @LINE:13
  ApiController_0: controllers.ApiController,
  // @LINE:16
  NewsController_3: controllers.NewsController,
  val prefix: String
) extends GeneratedRouter {

   @javax.inject.Inject()
   def this(errorHandler: play.api.http.HttpErrorHandler,
    // @LINE:6
    HomeController_1: controllers.HomeController,
    // @LINE:10
    Assets_2: controllers.Assets,
    // @LINE:13
    ApiController_0: controllers.ApiController,
    // @LINE:16
    NewsController_3: controllers.NewsController
  ) = this(errorHandler, HomeController_1, Assets_2, ApiController_0, NewsController_3, "/")

  def withPrefix(prefix: String): Routes = {
    router.RoutesPrefix.setPrefix(prefix)
    new Routes(errorHandler, HomeController_1, Assets_2, ApiController_0, NewsController_3, prefix)
  }

  private[this] val defaultPrefix: String = {
    if (this.prefix.endsWith("/")) "" else "/"
  }

  def documentation = List(
    ("""GET""", this.prefix, """controllers.HomeController.index"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """test""", """controllers.HomeController.test"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """assets/""" + "$" + """file<.+>""", """controllers.Assets.versioned(path:String = "/public", file:Asset)"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """signup""", """controllers.ApiController.signup"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """news""", """controllers.NewsController.getNews"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """news""", """controllers.NewsController.createNews"""),
    ("""PATCH""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """news/""" + "$" + """ID<[^/]+>""", """controllers.NewsController.modifyNews(ID:java.util.UUID)"""),
    ("""DELETE""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """news/""" + "$" + """ID<[^/]+>""", """controllers.NewsController.deleteNews(ID:java.util.UUID)"""),
    Nil
  ).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
    case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
    case l => s ++ l.asInstanceOf[List[(String,String,String)]]
  }}


  // @LINE:6
  private[this] lazy val controllers_HomeController_index0_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix)))
  )
  private[this] lazy val controllers_HomeController_index0_invoker = createInvoker(
    HomeController_1.index,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "index",
      Nil,
      "GET",
      this.prefix + """""",
      """ An example controller showing a sample home page""",
      Seq()
    )
  )

  // @LINE:7
  private[this] lazy val controllers_HomeController_test1_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("test")))
  )
  private[this] lazy val controllers_HomeController_test1_invoker = createInvoker(
    HomeController_1.test,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "test",
      Nil,
      "GET",
      this.prefix + """test""",
      """""",
      Seq()
    )
  )

  // @LINE:10
  private[this] lazy val controllers_Assets_versioned2_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("assets/"), DynamicPart("file", """.+""",false)))
  )
  private[this] lazy val controllers_Assets_versioned2_invoker = createInvoker(
    Assets_2.versioned(fakeValue[String], fakeValue[Asset]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Assets",
      "versioned",
      Seq(classOf[String], classOf[Asset]),
      "GET",
      this.prefix + """assets/""" + "$" + """file<.+>""",
      """ Map static resources from the /public folder to the /assets URL path""",
      Seq()
    )
  )

  // @LINE:13
  private[this] lazy val controllers_ApiController_signup3_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("signup")))
  )
  private[this] lazy val controllers_ApiController_signup3_invoker = createInvoker(
    ApiController_0.signup,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.ApiController",
      "signup",
      Nil,
      "POST",
      this.prefix + """signup""",
      """""",
      Seq()
    )
  )

  // @LINE:16
  private[this] lazy val controllers_NewsController_getNews4_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("news")))
  )
  private[this] lazy val controllers_NewsController_getNews4_invoker = createInvoker(
    NewsController_3.getNews,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.NewsController",
      "getNews",
      Nil,
      "GET",
      this.prefix + """news""",
      """NEWS""",
      Seq()
    )
  )

  // @LINE:17
  private[this] lazy val controllers_NewsController_createNews5_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("news")))
  )
  private[this] lazy val controllers_NewsController_createNews5_invoker = createInvoker(
    NewsController_3.createNews,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.NewsController",
      "createNews",
      Nil,
      "POST",
      this.prefix + """news""",
      """""",
      Seq()
    )
  )

  // @LINE:18
  private[this] lazy val controllers_NewsController_modifyNews6_route = Route("PATCH",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("news/"), DynamicPart("ID", """[^/]+""",true)))
  )
  private[this] lazy val controllers_NewsController_modifyNews6_invoker = createInvoker(
    NewsController_3.modifyNews(fakeValue[java.util.UUID]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.NewsController",
      "modifyNews",
      Seq(classOf[java.util.UUID]),
      "PATCH",
      this.prefix + """news/""" + "$" + """ID<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:19
  private[this] lazy val controllers_NewsController_deleteNews7_route = Route("DELETE",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("news/"), DynamicPart("ID", """[^/]+""",true)))
  )
  private[this] lazy val controllers_NewsController_deleteNews7_invoker = createInvoker(
    NewsController_3.deleteNews(fakeValue[java.util.UUID]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.NewsController",
      "deleteNews",
      Seq(classOf[java.util.UUID]),
      "DELETE",
      this.prefix + """news/""" + "$" + """ID<[^/]+>""",
      """""",
      Seq()
    )
  )


  def routes: PartialFunction[RequestHeader, Handler] = {
  
    // @LINE:6
    case controllers_HomeController_index0_route(params@_) =>
      call { 
        controllers_HomeController_index0_invoker.call(HomeController_1.index)
      }
  
    // @LINE:7
    case controllers_HomeController_test1_route(params@_) =>
      call { 
        controllers_HomeController_test1_invoker.call(HomeController_1.test)
      }
  
    // @LINE:10
    case controllers_Assets_versioned2_route(params@_) =>
      call(Param[String]("path", Right("/public")), params.fromPath[Asset]("file", None)) { (path, file) =>
        controllers_Assets_versioned2_invoker.call(Assets_2.versioned(path, file))
      }
  
    // @LINE:13
    case controllers_ApiController_signup3_route(params@_) =>
      call { 
        controllers_ApiController_signup3_invoker.call(ApiController_0.signup)
      }
  
    // @LINE:16
    case controllers_NewsController_getNews4_route(params@_) =>
      call { 
        controllers_NewsController_getNews4_invoker.call(NewsController_3.getNews)
      }
  
    // @LINE:17
    case controllers_NewsController_createNews5_route(params@_) =>
      call { 
        controllers_NewsController_createNews5_invoker.call(NewsController_3.createNews)
      }
  
    // @LINE:18
    case controllers_NewsController_modifyNews6_route(params@_) =>
      call(params.fromPath[java.util.UUID]("ID", None)) { (ID) =>
        controllers_NewsController_modifyNews6_invoker.call(NewsController_3.modifyNews(ID))
      }
  
    // @LINE:19
    case controllers_NewsController_deleteNews7_route(params@_) =>
      call(params.fromPath[java.util.UUID]("ID", None)) { (ID) =>
        controllers_NewsController_deleteNews7_invoker.call(NewsController_3.deleteNews(ID))
      }
  }
}
