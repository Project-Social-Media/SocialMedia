package socialmediaspringboot.backend.model.User;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Setter
public class MyUserDetails implements UserDetails {
    private User user;
    public MyUserDetails(User user){
        this.user = user;
    }

    @Override
    public String getUsername(){return null;}

    public String getEmail(){return user.getEmail();}

    @Override
    public String getPassword() {return user.getPassword();}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        //create & return a List<GrantedAuthority> from roles

//        Set<GrantedAuthority> authorities = new HashSet<>();
//
//        for(Role role : user.getRoles()){
//            authorities.add(new SimpleGrantedAuthority(("ROLE_" + role.getRole_name())));
//        }
//        return authorities;
        return null;
    }

    @Override
    public boolean isAccountNonExpired(){return true;}

    @Override
    public boolean isAccountNonLocked(){return true;}

    @Override
    public boolean isCredentialsNonExpired(){return true;}

    @Override
    public boolean isEnabled(){return true;}
}
