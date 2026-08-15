package com.malshan.expense_tracker.controller;
import com.malshan.expense_tracker.dto.CategoryResponse;
import com.malshan.expense_tracker.entity.Category;
import com.malshan.expense_tracker.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody Map<String, String> request) {
        Category category = categoryService.createCategory(request.get("name"));
        CategoryResponse response = new CategoryResponse(category.getId(), category.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        List<CategoryResponse> response = categories.stream()
                .map(c -> new CategoryResponse(c.getId(), c.getName()))
                .toList();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}