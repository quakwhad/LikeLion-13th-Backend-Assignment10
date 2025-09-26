package com.example.jpademo.dto;

import java.util.List;

public record MemberDto(Long id, String nickname, List<PostSummary> posts) {
    public record PostSummary(Long id, String title) { }
}

