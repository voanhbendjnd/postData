package com.djnd.post_data.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultPaginationDTO {
    private Meta meta;
    private Object result;

    @Getter
    @Setter
    public static class Meta {
        private Integer page;
        private Integer pageSize;
        private Integer pages;
        private Long total;
    }
}
