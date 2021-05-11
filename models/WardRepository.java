package models;

import com.google.inject.ImplementedBy;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

/**
 * This interface provides a non-blocking API for possibly blocking operations.
 */
@ImplementedBy(JPAWardRepository.class)
public interface WardRepository {

    CompletionStage<Ward> login(String Name,String Password);

}