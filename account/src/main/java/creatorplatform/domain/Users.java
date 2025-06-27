package creatorplatform.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import creatorplatform.AccountApplication;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Users_table")
@Data
//<<< DDD / Aggregate Root
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nickname;

    private String authorshipStatus;

    private Date createdAt;

    private Boolean subscriber;

    private String accountId;

    private String password;

    private String authorsProfile;

    private String autornickname;

    private Boolean agreedToMarketing;

    public static UsersRepository repository() {
        UsersRepository usersRepository = AccountApplication.applicationContext.getBean(
            UsersRepository.class
        );
        return usersRepository;
    }

    //<<< Clean Arch / Port Method
    public void registerUser(RegisterUserCommand registerUserCommand) {
        //implement business logic here:

        UserRegistered userRegistered = new UserRegistered(this);
        userRegistered.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public void applyForAuthorship(
        ApplyForAuthorshipCommand applyForAuthorshipCommand
    ) {
        //implement business logic here:

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public void declineApplication(
        DeclineApplicationCommand declineApplicationCommand
    ) {
        //implement business logic here:

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public void startSubscribe() {
        //implement business logic here:

        SubscriptionStarted subscriptionStarted = new SubscriptionStarted(this);
        subscriptionStarted.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public void accpetApplication(
        AccpetApplicationCommand accpetApplicationCommand
    ) {
        //implement business logic here:

        AuthorshipAccepted authorshipAccepted = new AuthorshipAccepted(this);
        authorshipAccepted.publishAfterCommit();
    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
