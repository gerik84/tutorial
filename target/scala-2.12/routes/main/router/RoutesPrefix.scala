
// @GENERATOR:play-routes-compiler
// @SOURCE:/home/pavel/projects/java/backend/tutorial/conf/routes
// @DATE:Thu Sep 21 16:59:40 SAMT 2017


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
