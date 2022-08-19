package team.nine.booknutsbackend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import team.nine.booknutsbackend.domain.archive.ArchiveBoard;
import team.nine.booknutsbackend.domain.reaction.Heart;
import team.nine.booknutsbackend.domain.reaction.Nuts;
import team.nine.booknutsbackend.dto.request.SignUpRequest;
import team.nine.booknutsbackend.enumerate.Role;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static team.nine.booknutsbackend.enumerate.Role.ROLE_USER;

@Entity
@Getter
@NoArgsConstructor
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
    private String refreshToken = "";

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
    private List<ArchiveBoard> archiveBoards = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Heart> hearts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Nuts> nutsList = new ArrayList<>();

    @OneToMany(mappedBy = "follower")
    @JsonIgnore
    private List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "following")
    @JsonIgnore
    private List<Follow> followings = new ArrayList<>();

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

    public User(SignUpRequest signUpRequest, String password, String profileImgUrl) {
        this.loginId = signUpRequest.getLoginId();
        this.password = password;
        this.username = signUpRequest.getUsername();
        this.nickname = signUpRequest.getNickname();
        this.email = signUpRequest.getEmail();
        this.profileImgUrl = profileImgUrl;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
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
        this.refreshToken = "";
        this.profileImgUrl = "";
        this.enabled = false;
        this.requestedDeleteAt = LocalDateTime.now();
    }

    public void clearUser() {
        this.email = null;
        this.loginId = null;
    }

}