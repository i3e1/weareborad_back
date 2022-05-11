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
@Builder
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

    @Column(length = 20, nullable = false)
    private String phone="";

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column//생년월일
    private LocalDateTime birthday = LocalDateTime.now();

    @Column(length = 100, nullable = false)
    private String email = "";

    @Column(length = 50)
    private String address = "";

    @Column(length = 50)
    private String addressDetail = "";

    @Column(length = 10, nullable = false)
    private String zipcode = "";

    private Long shopId;

    @Column(name = "updated_time")
    private LocalDateTime accessTime;


    /**
     * accountDto를 통한 name, role 등 계정정보 변경
     * @param accountDto
     */
    public void updateInfo(AccountDto.Info accountDto) {
        String name = accountDto.getName();
        String role = accountDto.getRole();
        String phone = accountDto.getPhone();
        String email = accountDto.getEmail();
        String address = accountDto.getAddress();
        String addressDetail = accountDto.getAddressDetail();
        String zipcode = accountDto.getZipcode();
        LocalDateTime birthday = accountDto.getBirthday();
        updateInfo(name, role, phone, email, address, addressDetail, zipcode, birthday);
    }

    public void updateInfo(String name, String role, String phone, String email, String address, String addressDetail, String zipcode, LocalDateTime birthday) {
        if (name!=null) this.name = name;
        if (role != null) this.role = role;
        if (phone!=null) this.phone = phone;
        if (email!=null) this.email = email;
        if (address!=null) this.address = address;
        if (addressDetail!=null) this.addressDetail = addressDetail;
        if(zipcode!=null)this.zipcode = zipcode;
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
        System.out.println("isJsol ? " +this.role.toUpperCase().startsWith("ROLE_JSOL"));
        return this.role.toUpperCase().startsWith("ROLE_ROOT");
    }

}
