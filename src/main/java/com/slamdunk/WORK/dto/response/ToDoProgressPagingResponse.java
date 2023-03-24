package com.slamdunk.WORK.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class ToDoProgressPagingResponse {
    private int nowPage;
    private int pageSize;
    private int totalPage;
    private int totalContent;
    private List<ToDoProgressResponse> content;

    @Builder
    public ToDoProgressPagingResponse(int nowPage, int pageSize, int totalPage,
                                      int totalContent, List<ToDoProgressResponse> content) {
        this.nowPage = nowPage;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
        this.totalContent = totalContent;
        this.content = content;
    }
}