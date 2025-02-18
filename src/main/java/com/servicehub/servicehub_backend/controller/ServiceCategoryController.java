package com.servicehub.servicehub_backend.controller;


import com.servicehub.servicehub_backend.dto.StandardResponse;
import com.servicehub.servicehub_backend.model.ServiceCategoryInfoEntity;
import com.servicehub.servicehub_backend.service.ServiceCategoryServiceImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;


import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@CrossOrigin(origins = "http://localhost:4200")
public class ServiceCategoryController {

    private static final Logger logger = LoggerFactory.getLogger(ServiceCategoryController.class);
    final private ServiceCategoryServiceImpl categoryService;

    @Autowired
    public ServiceCategoryController(ServiceCategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("")
    ResponseEntity<StandardResponse<List<ServiceCategoryInfoEntity>>> getAll() {
        logger.info("Start fetching all service categories");

        List<ServiceCategoryInfoEntity> categories = categoryService.getAllCategories();
        if (categories.isEmpty()) {
            logger.warn("No service categories found");
            return ResponseEntity
                    .ok(new StandardResponse<>(false, "No categories found", categories));
        }
        return ResponseEntity.ok(new StandardResponse<>(false, "Categories fetched successfully", categories));
    }
}
