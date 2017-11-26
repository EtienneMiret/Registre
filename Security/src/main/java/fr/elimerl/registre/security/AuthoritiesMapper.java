package fr.elimerl.registre.security;

import fr.elimerl.registre.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

/**
 * This service is responsible for mapping {@link User}s to their
 * {@link GrantedAuthority}s.
 * <p>
 * For now, we have a simple mapping where unknown users have no authorities,
 * and known ones have exactly ROLE_USER.
 */
@Service
public class AuthoritiesMapper {

  private static final GrantedAuthority ROLE_USER =
      new SimpleGrantedAuthority ("ROLE_USER");

  /**
   * Map a user to their authorities.
   *
   * @param user
   *          the user whose authorities we want to known. May be {@code null}.
   * @return the authorities granted to {@code user}.
   */
  public Collection<GrantedAuthority> mapAuthorities (User user) {
    if (user == null) {
      return Collections.emptySet ();
    } else {
      return Collections.singleton (ROLE_USER);
    }
  }

}
