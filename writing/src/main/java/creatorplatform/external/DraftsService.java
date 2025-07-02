package creatorplatform.service;

import creatorplatform.domain.*;
import creatorplatform.external.AuthorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DraftsService {

    private final DraftsRepository repo;
    private final CheckAuthorsRepository checkAuthorsRepository;
    private final AuthorService authorService;

    public DraftsService(DraftsRepository repo, CheckAuthorsRepository checkAuthorsRepository, AuthorService authorService) {
        this.repo = repo;
        this.checkAuthorsRepository = checkAuthorsRepository;
        this.authorService = authorService;
    }

    public Drafts saveDraft(SaveDraftCommand cmd) {
        // Read Model 또는 외부 API를 통해 authorNickname 조회
        String nickname = checkAuthorsRepository.findById(cmd.getAuthorId())
                .map(CheckAuthors::getNickname)
                .orElse(null);

        // 외부 API를 사용하려면 아래 코드로 대체 가능
        // String nickname = authorService.getAuthorNickname(cmd.getAuthorId());

        cmd.setAuthorNickname(nickname);

        Drafts draft = new Drafts();
        draft.saveDraft(cmd);
        return repo.save(draft);
    }
}