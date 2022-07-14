package team.nine.booknutsbackend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import team.nine.booknutsbackend.domain.archive.ArchiveBoard;
import team.nine.booknutsbackend.domain.reaction.Heart;
import team.nine.booknutsbackend.domain.reaction.Nuts;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
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

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Column(nullable = false)
    private boolean enabled = true;

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

}