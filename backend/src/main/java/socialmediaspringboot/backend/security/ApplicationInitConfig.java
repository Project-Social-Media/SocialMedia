package socialmediaspringboot.backend.security;

import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import socialmediaspringboot.backend.constant.PredefinedRoles;
import socialmediaspringboot.backend.model.Role;
import socialmediaspringboot.backend.model.User.User;
import socialmediaspringboot.backend.repository.RoleRepository;
import socialmediaspringboot.backend.repository.UserRepository;

import java.util.HashSet;

/**
 * This class create an admin account if not exist, when run the application
 */
@Configuration
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
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
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

                User user = User.builder()
                        .email(ADMIN_EMAIL)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .roles(roles)
                        .build();

                userRepository.save(user);
                log.warn("admin user has been created with default password: 123admin456");
            }
        };
    }
}
