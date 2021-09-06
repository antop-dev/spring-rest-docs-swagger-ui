package org.antop.model;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@FieldNameConstants
@Builder
public class User {
    private long id;
    @NotNull
    @NotEmpty
    private String name;
    private String address;
}
