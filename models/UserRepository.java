package models;

import com.google.inject.ImplementedBy;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

/**
 * This interface provides a non-blocking API for possibly blocking operations.
 */
@ImplementedBy(JPAUserRepository.class)
public interface UserRepository {

    CompletionStage<User> add(User user);

    CompletionStage<User> del(String Name);

    CompletionStage<User> login(String Email,String Password);

    CompletionStage<String> editProfile(int Id,String Name,String Email,String Mobile);

    CompletionStage<Stream<User>> list();

    CompletionStage<User> profile(int Id);
    //abstract User profile(int Id);
}