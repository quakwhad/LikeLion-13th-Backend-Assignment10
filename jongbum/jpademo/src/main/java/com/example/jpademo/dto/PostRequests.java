package com.example.jpademo.dto;

public class PostRequests {
    public record Create(Long memberId, String title, String content) { }
    public record Update(String title, String content) { }
}

