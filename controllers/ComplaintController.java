package controllers;

import models.Complaint;
import models.ComplaintRepository;
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

public class ComplaintController extends Controller{

    private final FormFactory formFactory;
    private final ComplaintRepository complaintRepository;
    private final HttpExecutionContext ec;

    @Inject
    public ComplaintController(FormFactory formFactory, ComplaintRepository complaintRepository, HttpExecutionContext ec) {
        this.formFactory = formFactory;
        this.complaintRepository = complaintRepository;
        this.ec = ec;
    }

    public Result index() {
        return ok();
    }

    public CompletionStage<Result> addComplaint() {

        Complaint complaint= Json.fromJson(request().body().asJson(),Complaint.class);

        return complaintRepository.add(complaint).thenApplyAsync(p -> {

            return ok();
        }, ec.current());
    }

//    public CompletionStage<Result> loginUser() {
//
//        User user= Json.fromJson(request().body().asJson(),User.class);
//
//        String EMAIL = requestJson.get("Email").asText();
//        String PASSWORD = requestJson.get("Password").asText();
//
//        return userRepository.log(EMAIL,PASSWORD).thenApplyAsync(p -> {
//
//            return ok();
//        }, ec.current());
//    }


    public CompletionStage<Result> getComplaints() {
        return complaintRepository.alllist().thenApplyAsync(complaintStream -> {
            return ok(toJson(complaintStream.collect(Collectors.toList())));
        }, ec.current());
    }

    public CompletionStage<Result> getPendingComplaints() {
        String Status = "Pending";
        return complaintRepository.conditionlist(Status).thenApplyAsync(complaintStream -> {
            return ok(toJson(complaintStream.collect(Collectors.toList())));
        }, ec.current());
    }
    public CompletionStage<Result> getClosedComplaints() {
        String Status = "Closed";
        return complaintRepository.conditionlist(Status).thenApplyAsync(complaintStream -> {
            return ok(toJson(complaintStream.collect(Collectors.toList())));
        }, ec.current());
    }
    public CompletionStage<Result> getUserComplaints() {
        JsonNode j = request().body().asJson();
        int id = Integer.parseInt(j.get("id").asText());
        return complaintRepository.allUserList(id).thenApplyAsync(complaintStream -> {
            return ok(toJson(complaintStream.collect(Collectors.toList())));
        }, ec.current());
    }
    public CompletionStage<Result> getUserClosedComplaints() {
        JsonNode j = request().body().asJson();
        int id = Integer.parseInt(j.get("id").asText());
        String Status = "Closed";
        return complaintRepository.conditionUserList(id,Status).thenApplyAsync(complaintStream -> {
            return ok(toJson(complaintStream.collect(Collectors.toList())));
        }, ec.current());
    }
    public CompletionStage<Result> getUserPendingComplaints() {
        JsonNode j = request().body().asJson();
        int id = Integer.parseInt(j.get("id").asText());
        String Status = "Pending";
        return complaintRepository.conditionUserList(id,Status).thenApplyAsync(complaintStream -> {
            return ok(toJson(complaintStream.collect(Collectors.toList())));
        }, ec.current());
    }

    public CompletionStage<Result> getUserLeaderboard() {
        return complaintRepository.userleaderboard().thenApplyAsync(complaintStream -> {
            return ok(toJson(complaintStream.collect(Collectors.toList())));
        }, ec.current());
    }

    public CompletionStage<Result> viewAndClose() {

        JsonNode j = request().body().asJson();
        int Cid = Integer.parseInt(j.get("Cid").asText());
        String Image = j.get("ClosedImage").asText();
        String Description = j.get("ClosedDescription").asText();
        String Status = j.get("Status").asText();
        String Timestamp = j.get("ClosedAt").asText();
        return complaintRepository.viewAndClose(Cid,Image,Description,Status,Timestamp).thenApplyAsync(c->{
            return ok("Closed");
         }).exceptionally(e->{
            System.out.println(Image);
            return badRequest("Complaint cannot be closed");});

    }

    public CompletionStage<Result> getRecentlyCreated() {
        JsonNode j = request().body().asJson();
        String logout = j.get("logout").asText();
        return complaintRepository.recentlycreated(logout).thenApplyAsync(complaintStream -> {
            return ok(toJson(complaintStream.collect(Collectors.toList())));
        }, ec.current());
    }

    public CompletionStage<Result> getRecentlyClosed() {
        JsonNode j = request().body().asJson();
        String login = j.get("login").asText();
        return complaintRepository.recentlyclosed(login).thenApplyAsync(complaintStream -> {
            return ok(toJson(complaintStream.collect(Collectors.toList())));
        }, ec.current());
    }

    public CompletionStage<Result> getAdminIconMap() {
        return complaintRepository.adminIconMap().thenApplyAsync(complaintStream -> {
            return ok(toJson(complaintStream.collect(Collectors.toList())));
        }, ec.current());
    }

    public CompletionStage<Result> getUserIconMap() {
        JsonNode j = request().body().asJson();
        int Id = Integer.parseInt(j.get("Id").asText());
        return complaintRepository.userIconMap(Id).thenApplyAsync(complaintStream -> {
            return ok(toJson(complaintStream.collect(Collectors.toList())));
        }, ec.current());
    }
    public CompletionStage<Result> forgotPassword() {

        JsonNode j = request().body().asJson();
        String Email = j.get("Email").asText();
        String Password = j.get("Password").asText();
        return complaintRepository.forgotPassword(Email,Password).thenApplyAsync(c->{
            return ok("Updated");

        }).exceptionally(e->{
            System.out.println(Email);
            return badRequest("Not possible to update");});

    }

    public CompletionStage<Result> getTopRankedComplaint() {
        return complaintRepository.topRankedComplaint().thenApplyAsync(complaintStream -> {
            return ok(toJson(complaintStream.collect(Collectors.toList())));
        }, ec.current());
    }

    public CompletionStage<Result> getActiveRegions() {
        return complaintRepository.activeRegions().thenApplyAsync(complaintStream -> {
            return ok(toJson(complaintStream.collect(Collectors.toList())));
        }, ec.current());
    }

    public CompletionStage<Result> getLocationComplaints() {
        JsonNode j = request().body().asJson();
        String location = j.get("Location").asText();
        return complaintRepository.locationComplaints(location).thenApplyAsync(complaintStream -> {
            return ok(toJson(complaintStream.collect(Collectors.toList())));
        }, ec.current());
    }

    public CompletionStage<Result> getTopRankedList() {
        JsonNode j = request().body().asJson();
        String location = j.get("Location").asText();
        String category = j.get("Category").asText();
        return complaintRepository.topRankedList(location,category).thenApplyAsync(complaintStream -> {
            return ok(toJson(complaintStream.collect(Collectors.toList())));
        }, ec.current());
    }
//    public CompletionStage<Result> deleteUser(){
//        JsonNode requestJson= request().body().asJson();
//        String Name=requestJson.get("name").asText();
//        return userRepository.del(Name).thenApplyAsync(p -> {
//            return ok();
//        }, ec.current());
    //}

}
