package team.nine.booknutsbackend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import team.nine.booknutsbackend.domain.archive.ArchiveBoard;
import team.nine.booknutsbackend.domain.reaction.Heart;
import team.nine.booknutsbackend.domain.reaction.Nuts;
import team.nine.booknutsbackend.enumerate.Role;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static team.nine.booknutsbackend.enumerate.Role.ROLE_USER;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"authorities"})
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(length = 100, unique = true)
    private String loginId;

    @Column(length = 300, nullable = false)
    @JsonIgnore
    private String password;

    @Column(length = 100)
    private String username;

    @Column(length = 100, unique = true)
    private String nickname;

    @Column(length = 100, unique = true)
    @Email
    private String email;

    @Column(length = 300)
    private String profileImgUrl;

    @Enumerated(EnumType.STRING)
    private Role role = ROLE_USER;

    @Column(nullable = false)
    private boolean enabled = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Column
    private LocalDateTime requestedDeleteAt;

    @OneToMany(mappedBy = "owner")
    @JsonIgnore
    private Set<ArchiveBoard> archiveBoardList = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<Heart> heartList = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<Nuts> nutsList = new HashSet<>();

    @OneToMany(mappedBy = "follower")
    @JsonIgnore
    private Set<Follow> followerList = new HashSet<>();

    @OneToMany(mappedBy = "following")
    @JsonIgnore
    private Set<Follow> followingList = new HashSet<>();

    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    } // userPk -> email

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return enabled;
    } //계정 만료 여부 (true : 만료 X)

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return enabled;
    } //계정 잠김 여부 (true : 잠김 X)

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return enabled;
    } //비밀번호 만료 여부 (ture : 만료 X)

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return enabled;
    } //계정 활성화 여부 (true : 활성화)

    public User(String loginId, String password, String username, String nickname, String email, String profileImgUrl) {
        this.loginId = loginId;
        this.password = password;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.profileImgUrl = profileImgUrl;
    }

    public void updateProfileImgUrl(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void disableUser() {
        this.username = null;
        this.nickname = null;
        this.profileImgUrl = "";
        this.enabled = false;
        this.requestedDeleteAt = LocalDateTime.now();
    }

    public void clearUser() {
        this.email = null;
        this.loginId = null;
    }

}