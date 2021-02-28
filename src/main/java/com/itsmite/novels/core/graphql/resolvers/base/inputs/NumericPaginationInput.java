package com.itsmite.novels.core.graphql.resolvers.base.inputs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NumericPaginationInput {

    @Min(1)
    private int size;

    @Min(0)
    private int offset;
}
