package tk.erdmko.users.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import tk.erdmko.users.model.User;

public interface UserDao extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
