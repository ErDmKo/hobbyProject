package tk.erdmko.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tk.erdmko.users.dao.UserDao;
import tk.erdmko.users.model.User;
import tk.erdmko.users.model.UserRole;

// @Service("userDetailsService")
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUserName(username);
        List<GrantedAuthority> authorities =
                buildUserAuthority(user.getUserRole());
        return buildUserForAuthentication(user, authorities);
    }

    private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                authorities
            );
    }

    private List<GrantedAuthority> buildUserAuthority(Set<UserRole> userRole) {
        Set<GrantedAuthority> setAuth = new HashSet<GrantedAuthority>();
        for (UserRole role: userRole) {
            setAuth.add(new SimpleGrantedAuthority(role.getRole()));
        }
        return new ArrayList<GrantedAuthority>(setAuth);
    }
}
