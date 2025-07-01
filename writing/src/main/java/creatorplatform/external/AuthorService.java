package creatorplatform.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "author", url = "${authors.url}")   // application.yml에 authors.url 설정
public interface AuthorService {

    @GetMapping("/authors/{id}/exists")
    boolean checkAuthor(@PathVariable("id") Long authorId);
}
