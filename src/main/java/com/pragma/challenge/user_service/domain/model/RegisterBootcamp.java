package com.pragma.challenge.user_service.domain.model;

import com.pragma.challenge.user_service.domain.validation.annotation.ValidList;
import java.util.List;

public record RegisterBootcamp(@ValidList(min = 1, max = 5) List<Long> bootcampIds) {}
