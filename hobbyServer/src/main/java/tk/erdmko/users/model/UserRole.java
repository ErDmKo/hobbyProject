package tk.erdmko.users.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_roles")
public class UserRole {
    private Integer userRoleId;

    public void setUser(User user) {
        this.user = user;
    }

    private User user;

    public void setRole(String role) {
        this.role = role;
    }

    private String role;

    public void setUserRoleId(Integer userRoleId) {
        this.userRoleId = userRoleId;
    }

    @Id
    @Column(name="id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Integer getUserRoleId() {
        return this.userRoleId;
    }

    @Column(name = "role", nullable = false, length = 45)
    public String getRole() {
        return role;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return user;
    }
}
