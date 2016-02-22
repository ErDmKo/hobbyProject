package tk.erdmko.users.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import tk.erdmko.users.model.User;

// @Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    public User findByUserName(String username) {
        List<User> users = new ArrayList<User>();

        users = sessionFactory.getCurrentSession()
                .createQuery("from User where username=?")
                .setParameter(0, username)
                .list();
        if (users.size() > 0) {
            return users.get(0);
        }
        return null;
    }
}
