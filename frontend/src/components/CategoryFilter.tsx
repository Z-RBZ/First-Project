import type { CategoryType } from "../types/category";

interface CategoryFilterProps {
    selectedType : CategoryType | undefined,
    onChangeType : (type:CategoryType | undefined) => void,
}

export function CategoryFilter({
    selectedType,
    onChangeType,
}:CategoryFilterProps){
    return (
        <div className="category-filter">
            <button
                type="button"
                className={selectedType === undefined ? 'category-filter__button category-filter__button--active' : 'category-filter__button'}
                onClick={()=>onChangeType(undefined)}
            >
                全部
            </button>
            <button
                type="button"
                className={selectedType === 'INCOME' ? 'category-filter__button category-filter__button--active' : 'category-filter__button'}
                onClick={()=>onChangeType('INCOME')}
            >
                收入
            </button>
            <button
                type="button"
                className={selectedType === 'EXPENSE' ? 'category-filter__button category-filter__button--active' : 'category-filter__button'}
                onClick={()=>onChangeType('EXPENSE')}
            >
                支出
            </button>
        </div>
    )
}
