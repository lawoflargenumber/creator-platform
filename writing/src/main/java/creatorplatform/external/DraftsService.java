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

        Drafts draft = new Drafts();
        draft.setAuthorNickname(nickname); // Drafts 객체에 직접 설정
        draft.saveDraft(cmd);
        return repo.save(draft);
}
}