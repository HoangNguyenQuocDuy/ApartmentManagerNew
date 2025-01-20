package hnqd.aparmentmanager.authservice.repository;


import hnqd.aparmentmanager.authservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepo extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    Optional<User> findById(int id);

    Page<User> findAllByRoleName(String roleName, Pageable pageable);

    User findByRoleName(String roleName);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
