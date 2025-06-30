package creatorplatform.domain.service;

import creatorplatform.domain.aggregate.Users;
import creatorplatform.domain.command.RegisterUserCommand;
import creatorplatform.domain.command.UpdateUserCommand;
import creatorplatform.domain.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCommandService {
    private final UsersRepository repo;

    public void registerUser(RegisterUserCommand cmd) {
        Users u = Users.builder()
                .id(cmd.getId())
                .nickname(cmd.getNickname())
                .password(cmd.getPassword())
                .agreedToMarketing(cmd.isAgreedToMarketing())
                .build();
        repo.save(u);
    }

    public void updateUser(UpdateUserCommand cmd) {
        Users u = repo.findById(cmd.getId()).orElseThrow();
        u.setNickname(cmd.getNickname());
        u.setPassword(cmd.getPassword());
        u.setAgreedToMarketing(cmd.isAgreedToMarketing());
        repo.save(u);
    }
}
