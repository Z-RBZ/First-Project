package com.example.finance.common.api;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiErrorTest {

    @Test
    void shouldCreateNormalError(){
        ApiError error = new ApiError(
                404,
                "CATEGORY_NOT_FOUND",
                "未找到 id 为 1 的分类"
        );

        assertEquals(404, error.getStatus());
        assertEquals("CATEGORY_NOT_FOUND", error.getCode());
        assertEquals("未找到 id 为 1 的分类", error.getMessage());
    }
}
