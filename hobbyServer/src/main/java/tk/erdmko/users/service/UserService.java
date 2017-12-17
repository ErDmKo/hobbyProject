package tk.erdmko.users.service;


import tk.erdmko.users.model.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
}
