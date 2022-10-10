package com.fsoft.team.controllers;

import com.fsoft.team.entity.Category;
import com.fsoft.team.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({"USER"})
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categorylist")
    public String categorylist(Model model) {
        List<Category> listcategory = categoryService.findAll();
        model.addAttribute("listcategory", listcategory);
        
        return "Categories";
    }

    @GetMapping("/deletecategory")
    public String deletecategory(@RequestParam("id") Long id) {
        categoryService.deleteById(id);
        return "redirect:/categorylist";
    }

    @GetMapping("/addcategoryform")
    public String addcategoryform() {
        return "Addcategory";
    }

    @GetMapping("/addcategory")
    public String addcategory(@RequestParam("catename") String name, @RequestParam("description") String des) {
        Category category = new Category();
        category.setCategoryName(name);
        category.setCategoryDes(des);
        categoryService.save(category);
        return "redirect:/categorylist";
    }

    @GetMapping("/editcategoryform")
    public String editcategoryform(@RequestParam("id") Long id, Model model) {
        Category category = categoryService.getbyId(id);
        model.addAttribute("category", category);
        return "updatecategory";
    }

    @GetMapping("/editcategory")
    public String editcategory(@RequestParam("id") Long id, @RequestParam("description") String description, @RequestParam("catename") String name ) {
        Category category = new Category();
        category.setCategoryName(name);
        category.setCategoryDes(description);
        category.setCategoryID(id);
        categoryService.save(category);
        return "redirect:/categorylist";
    }
}
