package rw.heritier.jwt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rw.heritier.jwt.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findUserByEmail(String email);

}
