package controllers;

import models.App;
import models.User;
import play.mvc.Controller;
import utils.Auth;

/**
 * Created by pavel on 21.09.17.
 */
abstract class BaseController extends Controller {

    protected User getUser(boolean allowFromCookie) {
        return Auth.getUser(request(), allowFromCookie);
    }

    protected App getApp() {
        return Auth.getApp(request());
    }

}
