package com.example.jpademo.service;

import com.example.jpademo.dto.MemberDto;
import com.example.jpademo.entity.Member;
import com.example.jpademo.entity.Post;
import com.example.jpademo.exception.NotFoundException;
import com.example.jpademo.repository.MemberRepository;
import com.example.jpademo.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepo;
    private final PostRepository postRepo;

    @Transactional(readOnly = true)
    public java.util.List<MemberDto> findAll() {
        return memberRepo.findAll().stream()
                .map(m -> new MemberDto(
                        m.getId(),
                        m.getNickname(),
                        m.getPosts().stream()
                                .map(p -> new MemberDto.PostSummary(p.getId(), p.getTitle()))
                                .collect(Collectors.toList())
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public MemberDto findOneWithPosts(Long id) {
        Member m = memberRepo.findWithPostsById(id)
                .orElseThrow(() -> new NotFoundException("Member not found: " + id));
        return new MemberDto(
                m.getId(),
                m.getNickname(),
                m.getPosts().stream()
                        .map(p -> new MemberDto.PostSummary(p.getId(), p.getTitle()))
                        .toList()
        );
    }

    @Transactional
    public MemberDto createMember(String nickname) {
        Member saved = memberRepo.save(Member.builder().nickname(nickname).build());
        return new MemberDto(saved.getId(), saved.getNickname(), java.util.List.of());
    }

    @Transactional
    public MemberDto addPost(Long memberId, String title, String content) {
        Member m = memberRepo.findById(memberId)
                .orElseThrow(() -> new NotFoundException("Member not found: " + memberId));
        Post p = Post.builder().title(title).content(content).build();
        m.addPost(p);           // 양방향 세팅
        postRepo.save(p);       // 주인(Post) 저장
        return new MemberDto(
                m.getId(),
                m.getNickname(),
                m.getPosts().stream()
                        .map(pp -> new MemberDto.PostSummary(pp.getId(), pp.getTitle()))
                        .toList()
        );
    }
}

