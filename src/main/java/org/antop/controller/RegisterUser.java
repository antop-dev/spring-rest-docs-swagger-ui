package org.antop.controller;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
@Builder
@FieldNameConstants
@ToString
public class RegisterUser {
    @NotNull
    @NotEmpty
    private final String name;
    private final String address;
}
