-- 分类表：保存收入和支出的分类，例如“工资”“餐饮”
CREATE TABLE categories (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            name VARCHAR(50) NOT NULL,
                            type VARCHAR(20) NOT NULL,
                            active BOOLEAN NOT NULL DEFAULT TRUE,
                            sort_order INT NOT NULL DEFAULT 0,
                            created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                ON UPDATE CURRENT_TIMESTAMP,

                            CONSTRAINT uk_categories_type_name UNIQUE (type, name)
);

-- 收支记录表：保存每一笔收入或支出
CREATE TABLE transactions (
                              id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              type VARCHAR(20) NOT NULL,
                              amount DECIMAL(12, 2) NOT NULL,
                              transaction_date DATE NOT NULL,
                              category_id BIGINT NOT NULL,
                              note VARCHAR(255),
                              created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                  ON UPDATE CURRENT_TIMESTAMP,

                              CONSTRAINT fk_transactions_category
                                  FOREIGN KEY (category_id) REFERENCES categories(id),

                              INDEX idx_transactions_date (transaction_date),
                              INDEX idx_transactions_category_id (category_id)
);

-- 月度总预算表：保存每个月的总支出预算
CREATE TABLE monthly_budgets (
                                 id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                 budget_month DATE NOT NULL,
                                 amount DECIMAL(12, 2) NOT NULL,
                                 created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                     ON UPDATE CURRENT_TIMESTAMP,

                                 CONSTRAINT uk_monthly_budgets_month UNIQUE (budget_month)
);

-- 分类预算表：保存每个月、每个支出分类的预算
CREATE TABLE category_budgets (
                                  id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                  budget_month DATE NOT NULL,
                                  category_id BIGINT NOT NULL,
                                  amount DECIMAL(12, 2) NOT NULL,
                                  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                      ON UPDATE CURRENT_TIMESTAMP,

                                  CONSTRAINT fk_category_budgets_category
                                      FOREIGN KEY (category_id) REFERENCES categories(id),

                                  CONSTRAINT uk_category_budgets_month_category
                                      UNIQUE (budget_month, category_id)
);