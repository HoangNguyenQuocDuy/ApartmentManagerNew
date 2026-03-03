package hnqd.aparmentmanager.authservice.config;

import hnqd.aparmentmanager.authservice.entity.User;
import hnqd.aparmentmanager.authservice.repository.IUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final IUserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepo.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("123456@"));
            admin.setFirstname("Eric");
            admin.setLastname("Hoang");
            admin.setEmail("2151050055duy@ou.edu.vn");
            admin.setPhone("admin_phone");
            admin.setRoleName("ROLE_ADMIN");
            admin.setStatus("Active");

            userRepo.save(admin);
        }
    }
}
