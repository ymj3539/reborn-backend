package com.rainbowbridge.reborn.domain;

import com.rainbowbridge.reborn.dto.user.UserUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    private String id;

    private String password;    // 비밀번호

    private String name;        // 이름

    private String phoneNum;    // 전화번호

    private LocalDate birthday; // 생일

    @Enumerated(EnumType.STRING)
    private Gender gender;      // 성별

    private String postalCode;  // 우편번호

    private String baseAddress;     // 기본 주소

    private String detailAddress;   // 상세 주소

    private String firebaseToken; // firebase 토큰

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Pet> pets = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Heart> hearts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ChatRoom> chatRooms = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Search> searches = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    public void addRole(String role) {
        if(role != null) {
            roles.add(role);
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void updateInfo(UserUpdateDto dto) {
        Optional.ofNullable(dto.getName()).ifPresent(this::setName);
        Optional.ofNullable(dto.getPhoneNum()).ifPresent(this::setPhoneNum);
        Optional.ofNullable(dto.getBirthday()).ifPresent(this::setBirthday);
        Optional.ofNullable(dto.getGender()).ifPresent(this::setGender);
        Optional.ofNullable(dto.getPostalCode()).ifPresent(this::setPostalCode);
        Optional.ofNullable(dto.getBaseAddress()).ifPresent(this::setBaseAddress);
        Optional.ofNullable(dto.getDetailAddress()).ifPresent(this::setDetailAddress);
    }

}
