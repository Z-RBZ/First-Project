import { useEffect, useState } from "react";
import type { Category, CategoryType } from "../types/category";
import { CategoryFilter } from '../components/CategoryFilter';
import { CategoryList } from '../components/CategoryList';
import { CategoryForm } from '../components/CategoryForm';
import { getCategories } from "../api/categoryApi";

export function CategoryPage() {

    const [selectedType, setSelectType] = useState<CategoryType | undefined>()
    const [categories, setCategories] = useState<Category[]>([])
    const [isLoading, setIsLoading] = useState(true)
    const [errorMessage, setErrorMessage] = useState<string | null>(null)
    const [reloadVersion, setReloadVersion] = useState(0);

    useEffect(() => {
        async function loadCategories() {
            setIsLoading(true),
                setErrorMessage(null)

            try {
                const result = await getCategories(selectedType)
                setCategories(result)
            } catch (error) {
                if (error instanceof Error) {
                    setErrorMessage(error.message)
                } else {
                    setErrorMessage('加载分类时出现未知错误。')
                }
            } finally {
                setIsLoading(false)
            }
        }
        void loadCategories()
    }, [selectedType,reloadVersion])

    function handleCategoryCreated() {
        setReloadVersion((version) => version + 1);
    }

    return (
        <main className="category-page">
            <header className="category-page__header">
                <h1>分类管理</h1>
                <p>管理你的收入与支出分类</p>
            </header>
            <CategoryFilter
                selectedType={selectedType}
                onChangeType={setSelectType}
            >
            </CategoryFilter>

            <section className="category-page__content">
                <div className="category-page__list-card">
                    <h2>分类列表</h2>

                    <CategoryList
                        categories={categories}
                        isLoading={isLoading}
                        errorMessage={errorMessage}
                    />
                </div>

                <CategoryForm onCreated={handleCategoryCreated} />
            </section>

        </main>
    )
}
