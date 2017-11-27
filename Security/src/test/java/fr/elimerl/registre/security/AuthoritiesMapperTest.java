package fr.elimerl.registre.security;

import fr.elimerl.registre.entities.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthoritiesMapperTest {

  private AuthoritiesMapper authoritiesMapper;

  @Before
  public void setUp () {
    authoritiesMapper = new AuthoritiesMapper ();
  }

  @Test
  public void should_grant_no_authorities_to_null_user () {
    Collection<GrantedAuthority> actual = authoritiesMapper.mapAuthorities (null);

    assertThat (actual).isEmpty ();
  }

  @Test
  public void should_grante_ROLE_USER_to_real_user () {
    User user = new User ("etienne", "etienne.miret@ens-lyon.org");

    Collection<GrantedAuthority> actual = authoritiesMapper.mapAuthorities (user);

    assertThat (actual).extracting (GrantedAuthority::getAuthority)
        .contains ("ROLE_USER");
  }

}
