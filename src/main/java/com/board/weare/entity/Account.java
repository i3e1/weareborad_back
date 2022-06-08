package com.board.weare.entity;

import com.board.weare.dto.AccountDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "tb_account")
@Getter
@ToString
//@ToString(exclude = "shop")
@NoArgsConstructor
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id" )
//@JsonIdentityReference(alwaysAsId = true) //직렬화시 id로만 출력된다
public class Account implements UserDetails {
    @Id
    @Column(length = 32, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; // 암호화 진행.

    @Column(length = 20, nullable = false)
    private String name;
    @Column(length = 20, nullable = false)
    private String role;

    @Column(length = 4)
    private String phone1="";

    @Column(length = 4)
    private String phone2="";

    @Column(length = 4)
    private String phone3="";

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column//생년월일
    private LocalDateTime birthday = LocalDateTime.now();

    @Column(length = 100)
    private String email = "";

    @Column(length = 50)
    private String address = "";

    @Column(length = 50)
    private String addressDetail = "";

    @Column(name = "updated_time")
    private LocalDateTime accessTime;

    @Builder
    public Account(String username, String password, String name, String role, String phone1, String phone2, String phone3, LocalDateTime birthday, String email, String address, String addressDetail, LocalDateTime accessTime) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.phone3 = phone3;
        this.birthday = birthday;
        this.email = email;
        this.address = address;
        this.addressDetail = addressDetail;
        this.accessTime = accessTime;
    }




    /**
     * accountDto를 통한 name, role 등 계정정보 변경
     * @param accountDto
     */
    public void updateInfo(AccountDto.Info accountDto) {
        String name = accountDto.getName();
        String role = accountDto.getRole();
        String phone1 = accountDto.getPhone1();
        String phone2 = accountDto.getPhone2();
        String phone3 = accountDto.getPhone3();
        String email = accountDto.getEmail();
        String address = accountDto.getAddress();
        String addressDetail = accountDto.getAddressDetail();
        LocalDateTime birthday = accountDto.getBirthday();
        updateInfo(name, role, phone1, phone2, phone3, email, address, addressDetail, birthday);
    }

    public void updateInfo(String name, String role, String phone1,String phone2,String phone3, String email, String address, String addressDetail, LocalDateTime birthday) {
        if (name!=null) this.name = name;
        if (role != null) this.role = role;
        if (phone1!=null) this.phone1 = phone1;
        if (phone2!=null) this.phone2 = phone2;
        if (phone3!=null) this.phone3 = phone3;
        if (email!=null) this.email = email;
        if (address!=null) this.address = address;
        if (addressDetail!=null) this.addressDetail = addressDetail;
        if (birthday != null && !birthday.equals(this.birthday)) this.birthday = birthday;
    }

    public void updatePassword(String pw) {
        if (pw!=null) this.password = pw;
    }

    public void updateLog() {
        this.accessTime = LocalDateTime.now(); // 접속로그
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(role));
        return authorities;
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

    public boolean isNotRoot(){
        return !isRoot();
    }

    public boolean isRoot(){
        System.out.println("isRoot ? " +this.role.toUpperCase().contains("ROOT"));
        return this.role.toUpperCase().contains("ROOT");
    }

}
