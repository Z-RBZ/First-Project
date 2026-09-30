export type CategoryType = "INCOME" | "EXPENSE";

export interface Category{
    name : string,
    id : number,
    type : CategoryType,
    active : boolean,
    sortOrder : number,
}

export interface CreateCategoryRequest{
    name : string,
    type : CategoryType,
    sortOrder : number,
}