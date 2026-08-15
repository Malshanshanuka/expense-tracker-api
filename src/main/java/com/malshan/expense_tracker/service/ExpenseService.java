package com.malshan.expense_tracker.service;

import com.malshan.expense_tracker.dto.ExpenseRequest;
import com.malshan.expense_tracker.entity.Category;
import com.malshan.expense_tracker.entity.Expense;
import com.malshan.expense_tracker.entity.User;
import com.malshan.expense_tracker.repository.CategoryRepository;
import com.malshan.expense_tracker.repository.ExpenseRepository;
import com.malshan.expense_tracker.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public ExpenseService(
            ExpenseRepository expenseRepository,
            CategoryRepository categoryRepository,
            UserRepository userRepository
    ) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Expense createExpense(ExpenseRequest request) {
        User currentUser = getCurrentUser();

        
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!category.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You don't have permission to use this category");
        }

        Expense expense = new Expense();
        expense.setAmount(request.getAmount());
        expense.setDescription(request.getDescription());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setUser(currentUser);
        expense.setCategory(category);

        return expenseRepository.save(expense);
    }

    public List<Expense> getAllExpenses() {
        User currentUser = getCurrentUser();
        return expenseRepository.findByUserId(currentUser.getId());
    }

    public Expense updateExpense(Long expenseId, ExpenseRequest request) {
        User currentUser = getCurrentUser();

        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        // Ownership check
        if (!expense.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You don't have permission to update this expense");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        expense.setAmount(request.getAmount());
        expense.setDescription(request.getDescription());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setCategory(category);

        return expenseRepository.save(expense);
    }

    public void deleteExpense(Long expenseId) {
        User currentUser = getCurrentUser();

        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if (!expense.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You don't have permission to delete this expense");
        }

        expenseRepository.delete(expense);
    }
}