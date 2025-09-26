package com.example.jpademo.service;

import com.example.jpademo.dto.PostDto;
import com.example.jpademo.entity.Member;
import com.example.jpademo.entity.Post;
import com.example.jpademo.exception.NotFoundException;
import com.example.jpademo.repository.MemberRepository;
import com.example.jpademo.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepo;
    private final MemberRepository memberRepo;

    @Transactional
    public PostDto create(Long memberId, String title, String content) {
        Member m = memberRepo.findById(memberId)
                .orElseThrow(() -> new NotFoundException("Member not found: " + memberId));
        Post p = Post.builder().title(title).content(content).member(m).build();
        Post saved = postRepo.save(p);
        return new PostDto(saved.getId(), saved.getTitle(), saved.getContent(), m.getId());
    }

    @Transactional(readOnly = true)
    public PostDto get(Long id) {
        Post p = postRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Post not found: " + id));
        Long memberId = (p.getMember() != null) ? p.getMember().getId() : null;
        return new PostDto(p.getId(), p.getTitle(), p.getContent(), memberId);
    }

    @Transactional
    public PostDto update(Long id, String title, String content) {
        Post p = postRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Post not found: " + id));
        p.setTitle(title);
        p.setContent(content);
        return new PostDto(p.getId(), p.getTitle(), p.getContent(),
                p.getMember() != null ? p.getMember().getId() : null);
    }

    @Transactional
    public void delete(Long id) {
        if (!postRepo.existsById(id)) throw new NotFoundException("Post not found: " + id);
        postRepo.deleteById(id);
    }
}

