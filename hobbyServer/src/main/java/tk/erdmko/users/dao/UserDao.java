package tk.erdmko.users.dao;

import tk.erdmko.users.model.User;

public interface UserDao {
    User findByUserName(String username);
}
