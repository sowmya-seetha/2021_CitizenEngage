package controllers;

import play.mvc.*;

//import views.html.*;

public class  HomeController extends Controller{
    public Result index(){
        String s= "Hello";
        return ok(s);
    }
}
