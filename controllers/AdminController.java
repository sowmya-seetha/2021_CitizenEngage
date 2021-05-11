package controllers;

import models.Admin;
import models.AdminRepository;
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

public class AdminController extends Controller{

    private final FormFactory formFactory;
    private final AdminRepository adminRepository;
    private final HttpExecutionContext ec;

    @Inject
    public AdminController(FormFactory formFactory, AdminRepository adminRepository, HttpExecutionContext ec) {
        this.formFactory = formFactory;
        this.adminRepository = adminRepository;
        this.ec = ec;
    }

    public Result index() {
        return ok();
    }


    public CompletionStage<Result> login() {

        JsonNode j = request().body().asJson();

        String Adminname = j.get("Adminname").asText();
        String Password = j.get("Password").asText();
        return adminRepository.login(Adminname,Password).thenApplyAsync(us->{

            String s="{\"Adminname\":\""+us.Adminname+"\",\"Password\":\""+us.Password+"\"}";

            return ok(Json.parse(s));

        }).exceptionally(e->{return badRequest("Not a valid user");});

    }


}
