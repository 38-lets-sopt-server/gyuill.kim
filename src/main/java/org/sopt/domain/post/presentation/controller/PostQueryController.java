package org.sopt.domain.post.presentation.controller;

import org.sopt.domain.post.application.dto.PostPageResult;
import org.sopt.domain.post.application.dto.PostResult;
import org.sopt.domain.post.application.service.PostQueryService;
import org.sopt.domain.post.domain.model.BoardType;
import org.sopt.domain.post.presentation.code.PostSuccessCode;
import org.sopt.domain.post.presentation.dto.request.GetPostsRequest;
import org.sopt.domain.post.presentation.dto.response.PostPageResponse;
import org.sopt.domain.post.presentation.dto.response.PostResponse;
import org.sopt.global.response.CommonApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostQueryController {

    private final PostQueryService postQueryService;

    public PostQueryController(PostQueryService postQueryService) {
        this.postQueryService = postQueryService;
    }

    @GetMapping
    public ResponseEntity<CommonApiResponse<PostPageResponse>> getAllPosts(
            @RequestParam(required = false) BoardType boardType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        GetPostsRequest request = new GetPostsRequest(boardType, page, size);
        request.validate();

        PostPageResult result = postQueryService.getPosts(request.boardType(), request.page(), request.size());
        List<PostResponse> responses = result.content().stream()
                .map(postResult -> new PostResponse(
                        postResult.id(),
                        postResult.boardType(),
                        postResult.title(),
                        postResult.content(),
                        postResult.author(),
                        postResult.createdAt()
                ))
                .toList();
        return CommonApiResponse.success(PostSuccessCode.POST_LIST_READ, new PostPageResponse(
                responses,
                result.page(),
                result.size(),
                result.totalElements(),
                result.totalPages(),
                result.hasNext()
        ));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<CommonApiResponse<PostResponse>> getPost(@PathVariable Long postId) {
        PostResult result = postQueryService.getPost(postId);
        return CommonApiResponse.success(PostSuccessCode.POST_READ, new PostResponse(
                result.id(),
                result.boardType(),
                result.title(),
                result.content(),
                result.author(),
                result.createdAt()
        ));
    }
}
