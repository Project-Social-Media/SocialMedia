package socialmediaspringboot.backend.config;

import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import socialmediaspringboot.backend.constant.PredefinedRoles;
import socialmediaspringboot.backend.model.Gender;
import socialmediaspringboot.backend.model.Role;
import socialmediaspringboot.backend.model.User.User;
import socialmediaspringboot.backend.repository.GenderRepository;
import socialmediaspringboot.backend.repository.RoleRepository;
import socialmediaspringboot.backend.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;

/**
 * This class create an admin account if not exist, when run the application
 */
@Configuration
@Profile("!test")
public class ApplicationInitConfig {

    private static final Logger log = LoggerFactory.getLogger(ApplicationInitConfig.class);
    @Autowired
    private PasswordEncoder passwordEncoder;


    @NonFinal
    static final String ADMIN_EMAIL = "admin@admin.com";

    @NonFinal
    static final String ADMIN_PASSWORD = "123admin456";

    @Bean
    /*@ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = ""
    )*/
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository, GenderRepository genderRepository) {
        return args -> {
            if (userRepository.findByEmail(ADMIN_EMAIL).isEmpty()) {
//                roleRepository.save(Role.builder()
//                        .roleName(PredefinedRoles.USER_ROLE)
//                        .roleDescription("User role")
//                        .build());
//
//                Role adminRole = roleRepository.save(Role.builder()
//                        .roleName(PredefinedRoles.ADMIN_ROLE)
//                        .roleDescription("Admin role")
//                        .build());

                Role adminRole = roleRepository.findByRoleName(PredefinedRoles.ADMIN_ROLE)
                        .orElseThrow(() -> new RuntimeException("Role not found"));
                var roles = new HashSet<Role>();
                roles.add(adminRole);
                Gender adminGender = genderRepository.findByGenderName("Male")
                        .orElseThrow(() -> new RuntimeException("Gender not found"));
                var gender = new Gender();
                gender = adminGender;
                User user = User.builder()
                        .email(ADMIN_EMAIL)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .roles(roles)
                        .firstname("admin")
                        .lastname("1")
                        .birth(new Date(0))
                        .gender(gender)
                        .createdAt(LocalDateTime.now())
                        .build();

                userRepository.save(user);
                log.warn("admin user has been created with default password: 123admin456");
            }
        };
    }
}
