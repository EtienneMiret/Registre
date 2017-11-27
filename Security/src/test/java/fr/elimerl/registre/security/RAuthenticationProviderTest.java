package fr.elimerl.registre.security;

import fr.elimerl.registre.entities.User;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mitre.openid.connect.client.UserInfoFetcher;
import org.mitre.openid.connect.model.PendingOIDCAuthenticationToken;
import org.mitre.openid.connect.model.UserInfo;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith (MockitoJUnitRunner.class)
public class RAuthenticationProviderTest {

  @InjectMocks
  private RAuthenticationProvider authenticationProvider;

  @Mock
  private UserInfoFetcher userInfoFetcher;

  @Mock
  private UserLoader userLoader;

  @Mock
  private AuthoritiesMapper authoritiesMapper;

  @Mock
  private Authentication authentication;

  @Mock
  private PendingOIDCAuthenticationToken token;

  @Mock
  private UserInfo userInfo;

  @Mock
  private User user;

  @Before
  public void setUp () {
    when (userInfoFetcher.loadUserInfo (token)).thenReturn (userInfo);
    when (userLoader.loadUser (userInfo)).thenReturn (user);
    when (token.getAccessTokenValue ()).thenReturn ("SUPER_SECRET_TOKEN");
    when (userInfo.getEmail ()).thenReturn ("email@example.com");
  }

  @Test
  public void should_support_PendingOIDCAuthenticationToken () {
    boolean actual =
        authenticationProvider.supports (PendingOIDCAuthenticationToken.class);

    assertThat (actual).isTrue ();
  }

  @Test
  public void should_no_support_arbitrary_Authentication () {
    boolean actual =
        authenticationProvider.supports (Authentication.class);

    assertThat (actual).isFalse ();
  }

  @Test
  public void should_return_null_given_arbitrary_Authentication () {
    Authentication actual = authenticationProvider.authenticate (authentication);

    assertThat (actual).isNull ();
  }

  @Test
  public void should_return_NullAuthenticationToken_given_user_with_no_authorities () {
    when (authoritiesMapper.mapAuthorities (user)).thenReturn (emptySet ());

    Authentication actual = authenticationProvider.authenticate (token);

    assertThat (actual).isNotNull ();
    assertThat (actual).isInstanceOf (NullAuthenticationToken.class);
    assertThat (actual.getCredentials ()).isEqualTo ("SUPER_SECRET_TOKEN");
    assertThat (actual.getPrincipal ()).isEqualTo ("email@example.com");
    assertThat (actual.getAuthorities ()).isEmpty ();
  }

  @Test
  public void should_return_RAuthenticationToken_given_user_with_authorities () {
    Collection<GrantedAuthority> authorities =
        singleton (new SimpleGrantedAuthority ("ROLE_USER"));
    when (authoritiesMapper.mapAuthorities (user)).thenReturn (authorities);

    Authentication actual = authenticationProvider.authenticate (token);

    assertThat (actual).isNotNull ();
    assertThat (actual).isInstanceOf (RAuthenticationToken.class);
    assertThat (actual.getCredentials ()).isEqualTo ("SUPER_SECRET_TOKEN");
    assertThat (actual.getPrincipal ()).isEqualTo (user);
    Assertions.<GrantedAuthority>assertThat (actual.getAuthorities ())
        .containsOnlyElementsOf (authorities);
  }

}
