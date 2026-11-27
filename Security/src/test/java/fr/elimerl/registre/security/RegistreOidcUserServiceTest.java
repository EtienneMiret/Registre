package fr.elimerl.registre.security;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;

import fr.elimerl.registre.entities.User;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RegistreOidcUserServiceTest {

  @InjectMocks
  private RegistreOidcUserService service;

  @Mock
  private UserLoader userLoader;

  @Mock
  private AuthoritiesMapper authoritiesMapper;

  @Mock
  private OidcUserService delegate;

  @Mock
  private OidcUserRequest request;

  private OidcUser oidcUser;

  private final User user = new User("Me", "email@example.com");

  @Before
  public void setUp() {
    OidcIdToken idToken = new OidcIdToken("token", Instant.now(),
        Instant.now().plusSeconds(60),
        Map.of("sub", "1234", "email", "email@example.com"));
    oidcUser = new DefaultOidcUser(emptySet(), idToken);
    when(delegate.loadUser(request)).thenReturn(oidcUser);
    when(userLoader.loadUser(oidcUser)).thenReturn(user);
  }

  @Test
  public void should_return_user_without_authorities_given_unregistered_user() {
    when(userLoader.loadUser(oidcUser)).thenReturn(null);
    when(authoritiesMapper.mapAuthorities(null)).thenReturn(emptySet());

    OidcUser actual = service.loadUser(request);

    assertThat(actual).isNotInstanceOf(RegistreUser.class);
    assertThat(actual.getAuthorities()).isEmpty();
    assertThat(actual.getName()).isEqualTo("email@example.com");
    assertThat(actual.getIdToken()).isEqualTo(oidcUser.getIdToken());
  }

  @Test
  public void should_return_RegistreUser_given_user_with_authorities() {
    Collection<GrantedAuthority> authorities =
        singleton(new SimpleGrantedAuthority("ROLE_USER"));
    when(authoritiesMapper.mapAuthorities(user)).thenReturn(authorities);

    OidcUser actual = service.loadUser(request);

    assertThat(actual).isInstanceOf(RegistreUser.class);
    assertThat(((RegistreUser) actual).getUser()).isSameAs(user);
    assertThat(actual.getName()).isEqualTo("Me");
    Assertions.<GrantedAuthority>assertThat(actual.getAuthorities())
        .containsExactlyInAnyOrderElementsOf(authorities);
    assertThat(actual.getIdToken()).isEqualTo(oidcUser.getIdToken());
  }

}
