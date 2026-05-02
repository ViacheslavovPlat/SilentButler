package com.silentbutler.config;

import com.silentbutler.domain.DeviceCategory;
import com.silentbutler.domain.Role;
import com.silentbutler.repository.DeviceCategoryRepository;
import com.silentbutler.repository.RoleRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {

    private final RoleRepository roleRepository;
    private final DeviceCategoryRepository deviceCategoryRepository;

    public DataInitializer(RoleRepository roleRepository,
                        DeviceCategoryRepository deviceCategoryRepository) {
        this.roleRepository = roleRepository;
        this.deviceCategoryRepository = deviceCategoryRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        // Roles
        if (roleRepository.findByName("USER").isEmpty()) {
            roleRepository.save(Role.builder().name("USER").build());
        }
        if (roleRepository.findByName("ADMIN").isEmpty()) {
            roleRepository.save(Role.builder().name("ADMIN").build());
        }

        // Device categories
        seedCategory("LIGHT",      "💡", "Lighting devices");
        seedCategory("THERMOSTAT", "🌡️", "Temperature control");
        seedCategory("LOCK",       "🔒", "Smart locks and security");
        seedCategory("CAMERA",     "📷", "Surveillance cameras");
        seedCategory("SPEAKER",    "🔊", "Audio devices");
        seedCategory("BLIND",      "🪟", "Smart blinds and curtains");
    }

    private void seedCategory(String name, String icon, String description) {
        if (deviceCategoryRepository.existsByName(name)) return;
        deviceCategoryRepository.save(DeviceCategory.builder()
                .name(name)
                .icon(icon)
                .description(description)
                .build());
    }
}