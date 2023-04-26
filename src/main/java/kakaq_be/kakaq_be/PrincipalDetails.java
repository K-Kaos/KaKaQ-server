package kakaq_be.kakaq_be;

import kakaq_be.kakaq_be.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class PrincipalDetails implements UserDetails {
    private User user;

    public PrincipalDetails(User user){
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() { //GrantedAuthority를 Collection안에 담기
            @Override
            public String getAuthority() {
                return user.getRole(); //여기서 역할 뽑기
            }
        });
        return collect;
    }

    //비밀번호 가져오기
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    //유저 이름 가져오기
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    //계정만료 안됨?
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정 안 잠김?
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //계정 Credential 안 만료됨?
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //비활성화 되지 않음? (오랫동안 사용하지 않은 경우 등)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
