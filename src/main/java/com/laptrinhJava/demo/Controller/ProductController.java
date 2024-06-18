package com.laptrinhJava.demo.Controller;

import com.laptrinhJava.demo.Model.Product;
import com.laptrinhJava.demo.service.CategoryService;
import com.laptrinhJava.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@Controller
@RequestMapping("/products")
public class ProductController {

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String showProductList(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "/products/product-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "/products/add-product";
    }

    @PostMapping("/add")
    public String addProduct(@Valid Product product, BindingResult result, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "/products/add-product";
        }

        if (!file.isEmpty()) {
            try {
                String filename = file.getOriginalFilename();
                Path path = Paths.get(uploadPath + filename);
                Files.createDirectories(path.getParent());
                Files.write(path, file.getBytes());
                product.setImagePath("/uploads/" + filename);
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("message", "Failed to upload image: " + e.getMessage());
                return "redirect:/products/add";
            }
        }

        productService.addProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "/products/update-product";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, @Valid Product product, BindingResult result, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            product.setId(id);
            return "/products/update-product";
        }

        if (!file.isEmpty()) {
            try {
                String filename = file.getOriginalFilename();
                Path path = Paths.get(uploadPath + filename);
                Files.createDirectories(path.getParent());
                Files.write(path, file.getBytes());
                product.setImagePath("/uploads/" + filename);
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("message", "Failed to upload image: " + e.getMessage());
                return "redirect:/products/update/" + id;
            }
        }

        productService.updateProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return "redirect:/products";
    }
    @GetMapping("/search")
    public String searchProduct(@RequestParam("keyword") String keyword,
                                @RequestParam(value = "category", required = false) Long categoryId,
                                @RequestParam(value = "categoryName", required = false) String categoryName,
                                Model model) {
        List<Product> searchResults;
        if (categoryId != null) {
            searchResults = productService.searchProductsByKeywordAndCategory(keyword, categoryId);
        } else if (categoryName != null && !categoryName.isEmpty()) {
            searchResults = productService.searchProductsByKeywordAndCategoryName(keyword, categoryName);
        } else {
            searchResults = productService.searchProducts(keyword);
        }
        model.addAttribute("products", searchResults);
        return "products/product-list";
    }
}