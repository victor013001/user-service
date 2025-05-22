package com.pragma.challenge.user_service.infrastructure.entrypoints.dto;

public record DefaultServerResponse<T, E>(T data, E error) {}
