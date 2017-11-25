package fr.elimerl.registre.security;

import fr.elimerl.registre.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Resource
public class AuthoritiesMapper {

  private static final GrantedAuthority ROLE_USER =
      new SimpleGrantedAuthority ("ROLE_USER");

  public Collection<GrantedAuthority> mapAuthorities (User user) {
    if (user == null) {
      return Collections.emptySet ();
    } else {
      return Collections.singleton (ROLE_USER);
    }
  }

}
