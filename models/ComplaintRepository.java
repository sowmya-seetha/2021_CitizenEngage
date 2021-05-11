package models;

import com.google.inject.ImplementedBy;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

/**
 * This interface provides a non-blocking API for possibly blocking operations.
 */
@ImplementedBy(JPAComplaintRepository.class)
public interface ComplaintRepository {

    CompletionStage<Complaint> add(Complaint complaint);

    //CompletionStage<Complaint> del(String Name);

   // CompletionStage<Complaint> log(String EMAIL,String PASSWORD);

    CompletionStage<Stream<Complaint>> alllist();
    CompletionStage<Stream<Complaint>> conditionlist(String Status);
    CompletionStage<Stream<Complaint>> allUserList(int id);
    CompletionStage<Stream<Complaint>> conditionUserList(int id,String Status);
    CompletionStage<Stream<Object>> userleaderboard();
    CompletionStage<String> viewAndClose(int Cid,String ClosedImage,String ClosedDescription,String Status,String ClosedAt);
    CompletionStage<Stream<Complaint>> recentlycreated(String logout);
    CompletionStage<Stream<Complaint>> recentlyclosed(String login);
    CompletionStage<Stream<Complaint>> adminIconMap();
    CompletionStage<Stream<Complaint>> userIconMap(int Id);
    CompletionStage<String> forgotPassword(String Email,String Password);
    CompletionStage<Stream<Object>> topRankedComplaint();
    CompletionStage<Stream<Object>> activeRegions();
    CompletionStage<Stream<Complaint>> locationComplaints(String location);
    CompletionStage<Stream<Complaint>> topRankedList(String location,String category);
}