package com.yunus.research.config;

import com.yunus.research.entity.InstrumentCategory;
import com.yunus.research.repository.InstrumentCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class InstrumentDataInitializer {

    @Bean
    public CommandLineRunner initInstrumentCategories(InstrumentCategoryRepository categoryRepository) {
        return args -> {
            if (categoryRepository.count() == 0) {
                List<String> categoryNames = Arrays.asList(
                    "Spectroscopy",
                    "Microscopy",
                    "Separation & Chromatography",
                    "Sample Preparation",
                    "Molecular Biology"
                );

                for (String name : categoryNames) {
                    InstrumentCategory category = new InstrumentCategory();
                    category.setName(name);
                    categoryRepository.save(category);
                }

                System.out.println("✓ Initialized " + categoryNames.size() + " instrument categories");
            }
        };
    }
}
