
// @GENERATOR:play-routes-compiler
// @SOURCE:/home/redline/project/java/tutorial/conf/routes
// @DATE:Thu Sep 21 21:04:36 SAMT 2017


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
