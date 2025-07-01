// // package creatorplatform.domain;

// import creatorplatform.domain.event.AuthorshipAcceptedEvent;
// import creatorplatform.domain.event.SubscriptionStartedEvent;
// import creatorplatform.domain.event.UserRegisteredEvent;
// import creatorplatform.domain.Users;
// import creatorplatform.domain.UsersRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.event.EventListener;
// import org.springframework.stereotype.Component;

// @Component
// public class AccountEventListener {

//     @Autowired
//     private UsersRepository usersRepository;

//     @EventListener
//     public void handleUserRegistered(UserRegisteredEvent event) {
//         Users user = new Users();
//         user.setId(Long.parseLong(event.id));
//         user.setNickname(event.nickname);
//         usersRepository.save(user);
//     }

//     @EventListener
//     public void handleSubscriptionStarted(SubscriptionStartedEvent event) {
//         usersRepository.findById(Long.parseLong(event.id)).ifPresent(user -> {
//             user.setSubscriber(true);
//             usersRepository.save(user);
//         });
//     }

//     @EventListener
//     public void handleAuthorshipAccepted(AuthorshipAcceptedEvent event) {
//         usersRepository.findById(Long.parseLong(event.id)).ifPresent(user -> {
//             user.setAuthorshipStatus("ACCEPTED");
//             usersRepository.save(user);
//         });
//     }
// }

