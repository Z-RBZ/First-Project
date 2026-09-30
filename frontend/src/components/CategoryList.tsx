import type { Category } from "../types/category";
import expenseIcon from "../assets/EXPENSE.svg";
import incomeIcon from "../assets/INCOME.svg";

interface CategoryListProps {
    categories: Category[],
    isLoading: boolean,
    errorMessage: string | null,
}

export function CategoryList({
    categories,
    isLoading,
    errorMessage
}: CategoryListProps) {
    if (isLoading) {
        return <p className="category-list__message">正在加载分类...</p>;
    }

    if (errorMessage) {
        return <p className="category-list__message category-list__message--error" role="alert">加载失败：{errorMessage}</p>;
    }

    if (categories.length == 0) {
        return <p className="category-list__message">暂时没有分类。</p>;
    }

    return (
        <div className="category-list__table-wrapper">
            <table className="category-list__table">
                <thead>
                    <tr>
                        <th>图标</th>
                        <th>分类名称</th>
                        <th>类型</th>
                        <th>排序值</th>
                    </tr>
                </thead>

                <tbody>
                    {
                        categories.map((category) => (
                            <tr key={category.id}>
                                <td>
                                    <span className={category.type === 'INCOME' ? 'category-list__icon category-list__icon--income' : 'category-list__icon category-list__icon--expense'}>
                                        <img
                                            className="category-list__icon-image"
                                            src={category.type === 'INCOME' ? incomeIcon : expenseIcon}
                                            alt={category.type === 'INCOME' ? '收入分类' : '支出分类'}
                                        />
                                    </span>
                                </td>
                                <td className="category-list__name">{category.name}</td>
                                <td>
                                    <span className={category.type === 'INCOME' ? 'category-list__type category-list__type--income' : 'category-list__type category-list__type--expense'}>
                                        {category.type === 'INCOME' ? '收入' : '支出'}
                                    </span>
                                </td>
                                <td>{category.sortOrder}</td>
                            </tr>
                        ))
                    }
                </tbody>
            </table>
        </div>
    )

}
