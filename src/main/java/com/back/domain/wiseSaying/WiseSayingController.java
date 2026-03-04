package com.back.domain.wiseSaying;

import com.back.domain.wiseSaying.entity.WiseSaying;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class WiseSayingController {

    private final List<WiseSaying> wiseSayings=new ArrayList<>(){{
        add(new WiseSaying(1,"1","1"));
        add(new WiseSaying(2,"2","2"));
        add(new WiseSaying(3,"3","3"));
        add(new WiseSaying(4,"4","4"));
        add(new WiseSaying(5,"5","5"));
    }};
    int lastId =5;
    @GetMapping("/write")
    @ResponseBody
    public String write(
            @RequestParam String content,
            @RequestParam String author
    ) {

        if(content == null || content.trim().length() == 0) {
            throw new RuntimeException("명언을 입력해주세요.");
        }

        if(author == null || author.trim().length() == 0) {
            throw new RuntimeException("작가를 입력해주세요.");
        }

        WiseSaying wiseSaying = new WiseSaying(++lastId, content, author);
        wiseSayings.add(wiseSaying);

        return "%d번 명언이 등록되었습니다.".formatted(wiseSaying.getId());
    }
    @GetMapping("/list")
    @ResponseBody
    public String list() {
        String wiseSayingList = wiseSayings.stream()
                .map(w -> "<li>%s / %s / %s</li>".formatted(w.getId(), w.getContent(), w.getAuthor()))
                .collect(Collectors.joining("\n"));

        return """
                <ul>
                %s
                </ul>
                """.formatted(wiseSayingList);
    }
    @GetMapping("/delete/{id}")
    @ResponseBody
    public String delete(
            @PathVariable int id
    ) {
                Optional<WiseSaying> wiseSaying = wiseSayings.stream()
                .filter(w -> w.getId() == id)
                .findFirst();

        if(wiseSaying.isEmpty()) {
            throw new RuntimeException("%d번 명언은 존재하지 않습니다.".formatted(id));
        }

        wiseSayings.remove(wiseSaying.get());

        return "%d번 명언이 삭제되었습니다".formatted(id);
    }
}
