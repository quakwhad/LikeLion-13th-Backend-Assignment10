package com.example.jpademo.repository;

import com.example.jpademo.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // N+1 완화: 회원 + posts를 함께 로딩
    @EntityGraph(attributePaths = "posts")
    Optional<Member> findWithPostsById(Long id);
}

