package dev.lysmux.web4.api.resource;

import dev.lysmux.web4.api.filter.Secured;
import dev.lysmux.web4.api.schema.APIResponse;
import dev.lysmux.web4.api.schema.HitCheckRequest;
import dev.lysmux.web4.api.schema.Pagination;
import dev.lysmux.web4.auth.UserPrincipal;
import dev.lysmux.web4.dto.PaginationDto;
import dev.lysmux.web4.dto.hit.HitDto;
import dev.lysmux.web4.service.HitService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import java.util.List;

@Path("/hits")
@Secured
public class HitResource {
    @Inject
    HitService hitService;

    @Inject
    UserPrincipal user;

    @GET
    @Path("/list")
    public APIResponse getUserHits(@Valid @BeanParam Pagination pagination) {
        PaginationDto<List<HitDto>> hits = hitService.getUserHits(
                user.getId(),
                pagination.page(),
                pagination.perPage()
        );

        return APIResponse.builder()
                .data(hits)
                .build();
    }

    @POST
    @Path("/check")
    public APIResponse checkHit(@Valid HitCheckRequest request) {
        HitDto hit = hitService.checkHit(
                user.getId(),
                request.x(),
                request.y(),
                request.r()
        );

        return APIResponse.builder()
                .data(hit)
                .build();
    }

    @POST
    @Path("/clear")
    public APIResponse clearHits() {
        hitService.clearUserHits(user.getId());

        return APIResponse.builder()
                .data("Hits cleared")
                .build();
    }
}
