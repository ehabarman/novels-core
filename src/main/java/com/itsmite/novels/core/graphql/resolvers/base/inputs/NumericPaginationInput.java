package com.itsmite.novels.core.graphql.resolvers.base.inputs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

import static java.util.Optional.ofNullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NumericPaginationInput {

    @Min(1)
    private Integer size;

    @Min(0)
    private Integer offset;

    public int getSize() {
        return ofNullable(size).orElse(10);
    }

    public int getOffset() {
        return ofNullable(offset).orElse(0);
    }
}
