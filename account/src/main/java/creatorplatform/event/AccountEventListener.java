package creatorplatform.event;

import creatorplatform.domain.aggregate.Users;
import creatorplatform.domain.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountEventListener {
    private final UsersRepository repo;

    @EventListener
    public void onUserRegistered(Object event) {
        // mapping logic...
    }
}
