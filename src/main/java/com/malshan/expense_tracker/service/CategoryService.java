package com.malshan.expense_tracker.service;

import com.malshan.expense_tracker.entity.Category;
import com.malshan.expense_tracker.entity.User;
import com.malshan.expense_tracker.exception.ResourceNotFoundException;
import com.malshan.expense_tracker.exception.UnauthorizedException;
import com.malshan.expense_tracker.repository.CategoryRepository;
import com.malshan.expense_tracker.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public Category createCategory(String name) {
        User currentUser = getCurrentUser();

        Category category = new Category();
        category.setName(name);
        category.setUser(currentUser);

        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        User currentUser = getCurrentUser();
        return categoryRepository.findByUserId(currentUser.getId());
    }

    public void deleteCategory(Long categoryId) {
        User currentUser = getCurrentUser();

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (!category.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You don't have permission to delete this category");
        }

        categoryRepository.delete(category);
    }
}