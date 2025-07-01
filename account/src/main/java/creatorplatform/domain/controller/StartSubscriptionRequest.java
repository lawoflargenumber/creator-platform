package creatorplatform.controller;
import lombok.Data;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
@Data public class StartSubscriptionRequest{
    private String plan;
    public Instant calculateExpiry(){
        if("MONTHLY".equalsIgnoreCase(plan))
            return Instant.now().plus(30,ChronoUnit.DAYS);
        if("YEARLY".equalsIgnoreCase(plan))
            return Instant.now().plus(365,ChronoUnit.DAYS);
        throw new IllegalArgumentException("Unknown plan");
    }
}
