package controllers;

import models.User;
import models.UserRepository;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import com.fasterxml.jackson.databind.JsonNode;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import static play.libs.Json.toJson;
//import views.html.*;

public class UserController extends Controller{

    private final FormFactory formFactory;
    private final UserRepository userRepository;
    private final HttpExecutionContext ec;

    @Inject
    public UserController(FormFactory formFactory, UserRepository userRepository, HttpExecutionContext ec) {
        this.formFactory = formFactory;
        this.userRepository = userRepository;
        this.ec = ec;
    }

    public Result index() {
        return ok();
    }

    public CompletionStage<Result> addUser() {

        User user= Json.fromJson(request().body().asJson(),User.class);

        return userRepository.add(user).thenApplyAsync(p -> {

            return ok();
        }, ec.current());
    }

    public CompletionStage<Result> login() {

        JsonNode j = request().body().asJson();

        String Email = j.get("Email").asText();
        String Password = j.get("Password").asText();
        return userRepository.login(Email,Password).thenApplyAsync(us->{

            String s="{\"id\":\""+us.id+"\",\"Email\":\""+us.Email+"\",\"Name\":\""+us.Name+"\"}";

            return ok(Json.parse(s));

        }).exceptionally(e->{return ok("Not a valid user");});

    }
    public CompletionStage<Result> profile() {

        JsonNode j = request().body().asJson();
        int Id = Integer.parseInt(j.get("Id").asText());
        System.out.println(Id);
        return userRepository.profile(Id).thenApplyAsync(us->{

            String s="{ \"Id\":\""+us.id+"\",\"Name\":\""+us.Name+"\",\"Email\":\""+us.Email+"\",\"Mobile\":\""+us.Mobile+"\"}";

            return ok(Json.parse(s));

        }).exceptionally(e->{return ok("Not a valid user");});

    }

    public CompletionStage<Result> getUsers() {
        return userRepository.list().thenApplyAsync(userStream -> {
            return ok(toJson(userStream.collect(Collectors.toList())));
        }, ec.current());
    }

//    public CompletionStage<Result> deleteUser(){
//        JsonNode requestJson= request().body().asJson();
//        String Name=requestJson.get("name").asText();
//        return userRepository.del(Name).thenApplyAsync(p -> {
//            return ok();
//        }, ec.current());
//    }
    public CompletionStage<Result> editProfile() {

        JsonNode j = request().body().asJson();
        String Name = j.get("Name").asText();
        String Email = j.get("Email").asText();
        String Mobile = j.get("Mobile").asText();
        int Id = Integer.parseInt(j.get("Id").asText());
        return userRepository.editProfile(Id,Name,Email,Mobile).thenApplyAsync(us->{

           // String s="{\"id\":\""+us.id+"\",\"Email\":\""+us.Email+"\",\"Name\":\""+us.Name+"\"}";

            return ok("Edited");

        }).exceptionally(e->{
            System.out.println(Email);
            return badRequest("Not a valid complaint");});
    }

}
