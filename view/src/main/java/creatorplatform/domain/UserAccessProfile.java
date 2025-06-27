package creatorplatform.domain;

import creatorplatform.ViewApplication;
import javax.persistence.*;
import java.util.List;
import lombok.Data;
import java.util.Date;
import java.time.LocalDate;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;


@Entity
@Table(name="UserAccessProfile_table")
@Data

//<<< DDD / Aggregate Root
public class UserAccessProfile  {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    
    
    
private Long id;    
    
    
private Boolean isSubscribed;    
    
    
private Integer points;    
    
    
private Date subscribtionDue;


    public static UserAccessProfileRepository repository(){
        UserAccessProfileRepository userAccessProfileRepository = ViewApplication.applicationContext.getBean(UserAccessProfileRepository.class);
        return userAccessProfileRepository;
    }



//<<< Clean Arch / Port Method
    public void accessToContent(AccessToContentCommand accessToContentCommand){
        
        //implement business logic here:
        

          = UserAccessProfileApplication.applicationContext
            .getBean(creatorplatform.external.Service.class)
            .checkIfBought(get??);

        AccessGranted accessGranted = new AccessGranted(this);
        accessGranted.publishAfterCommit();
        AccessDenied accessDenied = new AccessDenied(this);
        accessDenied.publishAfterCommit();
    }
//>>> Clean Arch / Port Method

//<<< Clean Arch / Port Method
    public static void addUser(UserRegistered userRegistered){
        
        //implement business logic here:
        
        /** Example 1:  new item 
        UserAccessProfile userAccessProfile = new UserAccessProfile();
        repository().save(userAccessProfile);

        */

        /** Example 2:  finding and process
        

        repository().findById(userRegistered.get???()).ifPresent(userAccessProfile->{
            
            userAccessProfile // do something
            repository().save(userAccessProfile);


         });
        */

        
    }
//>>> Clean Arch / Port Method
//<<< Clean Arch / Port Method
    public static void addSubscription(SubscriptionStarted subscriptionStarted){
        
        //implement business logic here:
        
        /** Example 1:  new item 
        UserAccessProfile userAccessProfile = new UserAccessProfile();
        repository().save(userAccessProfile);

        */

        /** Example 2:  finding and process
        

        repository().findById(subscriptionStarted.get???()).ifPresent(userAccessProfile->{
            
            userAccessProfile // do something
            repository().save(userAccessProfile);


         });
        */

        
    }
//>>> Clean Arch / Port Method


}
//>>> DDD / Aggregate Root
