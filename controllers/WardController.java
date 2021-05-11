package controllers;

import models.Ward;
import models.WardRepository;
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

public class WardController extends Controller{

    private final FormFactory formFactory;
    private final WardRepository wardRepository;
    private final HttpExecutionContext ec;

    @Inject
    public WardController(FormFactory formFactory, WardRepository wardRepository, HttpExecutionContext ec) {
        this.formFactory = formFactory;
        this.wardRepository = wardRepository;
        this.ec = ec;
    }

    public Result index() {
        return ok();
    }


    public CompletionStage<Result> login() {

        JsonNode j = request().body().asJson();

        String Name = j.get("Name").asText();
        String Password = j.get("Password").asText();
        return wardRepository.login(Name,Password).thenApplyAsync(us->{

            String s="{\"Name\":\""+us.Name+"\",\"Password\":\""+us.Password+"\"}";

            return ok(Json.parse(s));

        }).exceptionally(e->{return badRequest("Not a valid user");});

    }


}
