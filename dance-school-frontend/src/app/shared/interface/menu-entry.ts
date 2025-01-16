export interface MenuEntry {
    text: string,
    routerLink: string
}

export interface PagResponse {
    data: [

    ];
    totalElements: number,
    totalPages: number,
    numberOfElements: number,
    currentPage: number,
    pageSize: number
}

export interface CourseFilters {
    page: number,
    pageSize: number,
    sortDirection: string,
    sortBy: string,
    day: string,
    name: string,
    coachId?: number | null
}
