package com.example.firstproject.service;

import com.example.firstproject.Entity.Article;
import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service // 서비스 선언(서비스 객체를 스프링부트에 생성)
@Slf4j
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> index() {
        return articleRepository.findAll();
    }

    public Article show(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    public Article create(ArticleForm dto) {
        Article article = dto.toEntity();
        if (article.getId() != null) {
            return null;
        }
        return articleRepository.save(article);
    }

    public Article update(Long id, ArticleForm dto) {
        Article article = dto.toEntity();
        log.info("id : {}, article : {}", id, article.toString());
        Article target = articleRepository.findById(id).orElse(null);
        if (target == null || id != article.getId()) {
            log.info("잘못된 요청! id: {}, article: {}", id, article.toString());
            return null;
        }

        target.patch(article);
        Article updated = articleRepository.save(target);
        return updated;
    }

    public Article delete(Long id) {
        // 대상 찾기
        Article target = articleRepository.findById(id).orElse(null);

        // 잘못된 요청 처리
        if (target == null) {
            return null;
        }
        // 대상 삭제
        articleRepository.delete(target);
        return target;
    }

    @Transactional // 해당 메소드를 트랜잭션으로 묶는다
    public List<Article> createArticles(List<ArticleForm> dtos) {

        // dto 묶음을 entity 묶음으로 변환
        List<Article> articleList = dtos.stream()
                .map(dto -> dto.toEntity())
                .collect(Collectors.toList());
        // Entity 묶음을 DB로 저장
        articleList.stream()
                .forEach(article -> articleRepository.save(article));

        // 강제 예외 발생
        articleRepository.findById(-1L).orElseThrow(
                () -> new IllegalArgumentException("결제 실패")
        );
        // 결과값 반환
        return articleList;
    }
}
