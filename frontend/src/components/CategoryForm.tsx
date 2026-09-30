import { useState, type FormEvent } from 'react';
import { createCategory } from '../api/categoryApi';
import type { CategoryType } from '../types/category';

interface CategoryFormProps {
  onCreated: () => void;
}

export function CategoryForm({ onCreated }: CategoryFormProps) {
  const [name, setName] = useState('');
  const [type, setType] = useState<CategoryType>('EXPENSE');
  const [sortOrder, setSortOrder] = useState('0');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);
  

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    if (!name.trim()) {
      setErrorMessage('分类名称不能为空。');
      return;
    }

    setIsSubmitting(true);
    setErrorMessage(null);
    setSuccessMessage(null);

    try {
      await createCategory({
        name: name.trim(),
        type,
        sortOrder: Number(sortOrder),
      });

      setName('');
      setSortOrder('0');
      setSuccessMessage('分类创建成功。');
      onCreated();
    } catch (error) {
      if (error instanceof Error) {
        setErrorMessage(error.message);
      } else {
        setErrorMessage('创建分类时出现未知错误。');
      }
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <form className="category-form" onSubmit={handleSubmit}>
      <h2 className="category-form__title">新增分类</h2>

      <div className="category-form__field">
        <label htmlFor="category-name">分类名称</label>
        <input
          id="category-name"
          type="text"
          value={name}
          onChange={(event) => setName(event.target.value)}
          maxLength={50}
          placeholder="请输入分类名称"
        />
      </div>

      <div className="category-form__field">
        <span className="category-form__label">分类类型</span>

        <div className="category-form__type-options">
          <label className={type === 'INCOME' ? 'category-form__type-option category-form__type-option--active' : 'category-form__type-option'}>
            <input
              type="radio"
              name="category-type"
              value="INCOME"
              checked={type === 'INCOME'}
              onChange={() => setType('INCOME')}
            />
            收入
          </label>

          <label className={type === 'EXPENSE' ? 'category-form__type-option category-form__type-option--active' : 'category-form__type-option'}>
            <input
              type="radio"
              name="category-type"
              value="EXPENSE"
              checked={type === 'EXPENSE'}
              onChange={() => setType('EXPENSE')}
            />
            支出
          </label>
        </div>
      </div>

      <div className="category-form__field">
        <label htmlFor="category-sort-order">排序值</label>
        <input
          id="category-sort-order"
          type="number"
          value={sortOrder}
          onChange={(event) => setSortOrder(event.target.value)}
          min={0}
        />
      </div>

      <button className="category-form__submit" type="submit" disabled={isSubmitting}>
        {isSubmitting ? '正在创建...' : '添加分类'}
      </button>

      {errorMessage && <p className="category-form__message category-form__message--error" role="alert">{errorMessage}</p>}

      {successMessage && <p className="category-form__message category-form__message--success">{successMessage}</p>}
    </form>
  );
}
