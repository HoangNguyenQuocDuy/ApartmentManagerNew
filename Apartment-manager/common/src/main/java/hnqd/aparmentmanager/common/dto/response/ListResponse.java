package hnqd.aparmentmanager.common.dto.response;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

public record ListResponse<T>(
        int currentItemCount,
        int itemsPerPage,
        long totalItems,
        int pageIndex,
        int totalPages,
        List<T> items
) {

    public static <T> ListResponse<T> of(Page<T> page) {
        return new ListResponse<>(
                page.getNumberOfElements(),
                page.getSize(),
                page.getTotalElements(),
                page.getNumber() + 1,
                page.getTotalPages(),
                page.getContent()
        );
    }

    public static <T> ListResponse<T> of(List<T> list) {
        return of(new PageImpl<>(list));
    }

}
