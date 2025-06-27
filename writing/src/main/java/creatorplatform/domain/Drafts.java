package creatorplatform.domain;

import creatorplatform.WritingApplication;
import javax.persistence.*;
import java.util.List;
import lombok.Data;
import java.util.Date;
import java.time.LocalDate;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;


@Entity
@Table(name="Drafts_table")
@Data

//<<< DDD / Aggregate Root
public class Drafts  {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    
    
    
private Long id;    
    
    
private Long authorId;    
    
    
private String authorNickname;    
    
    
private String title;    
    
    
private String content;    
    
    
private Date createdAt;    
    
    
private Date lastUpdatedAt;


    public static DraftsRepository repository(){
        DraftsRepository draftsRepository = WritingApplication.applicationContext.getBean(DraftsRepository.class);
        return draftsRepository;
    }



//<<< Clean Arch / Port Method
    public void saveDraft(SaveDraftCommand saveDraftCommand){
        
        //implement business logic here:
        

          = DraftsApplication.applicationContext
            .getBean(creatorplatform.external.Service.class)
            .checkAuthors(get??);

    }
//>>> Clean Arch / Port Method
//<<< Clean Arch / Port Method
    public void requestPublication(RequestPublicationCommand requestPublicationCommand){
        
        //implement business logic here:
        


        RequestedPublication requestedPublication = new RequestedPublication(this);
        requestedPublication.publishAfterCommit();
    }
//>>> Clean Arch / Port Method

//<<< Clean Arch / Port Method
    public static void deleteDraft(CompletedPublication completedPublication){
        
        //implement business logic here:
        
        /** Example 1:  new item 
        Drafts drafts = new Drafts();
        repository().save(drafts);

        */

        /** Example 2:  finding and process
        

        repository().findById(completedPublication.get???()).ifPresent(drafts->{
            
            drafts // do something
            repository().save(drafts);


         });
        */

        
    }
//>>> Clean Arch / Port Method


}
//>>> DDD / Aggregate Root
