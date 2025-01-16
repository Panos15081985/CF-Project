package gr.aueb.cf.dance_school.core.filters;

import io.micrometer.common.util.StringUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class GenericFilters {

    private final static int DEFAULT_PAGE_SIZE = 10;
    private final static String DEFAULT_SORT_COLUMN = "id";
    private final static Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.ASC;

    private int page;
    private int pageSize;
    private Sort.Direction sortDirection;
    private String sortBy;

    public int getPageSize() {
        return pageSize <= 0 ? DEFAULT_PAGE_SIZE : pageSize;
    }

    public int getPage() {
        return Math.max(page, 0);
    }

    public Sort.Direction getSortDirection(){
        if (this.sortDirection == null) return DEFAULT_SORT_DIRECTION;
        return this.sortDirection;
    }

    public String getSortBy() {
        if (this.sortBy == null || StringUtils.isBlank(this.sortBy)) {
            return DEFAULT_SORT_COLUMN; // Προεπιλεγμένη στήλη ταξινόμησης
        }
        return this.sortBy; // Επιστρέφει το "day" όπως το δίνει ο χρήστης
    }

    public Pageable getPageable(){
        return PageRequest.of(getPage(), getPageSize(), getSort());
    }

    public Sort getSort(){
        return Sort.by(this.getSortDirection(), this.getSortBy());
    }

}
