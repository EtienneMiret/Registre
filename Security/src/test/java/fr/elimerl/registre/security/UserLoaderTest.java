package fr.elimerl.registre.security;

import java.time.Instant;
import java.util.Map;

import fr.elimerl.registre.entities.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext.xml")
public class UserLoaderTest {

  @Autowired
  private UserLoader userLoader;

  @Test
  public void should_return_null_given_null () {
    User actual = userLoader.loadUser (null);

    assertThat (actual).isNull ();
  }

  @Test
  public void should_return_null_given_UserInfo_without_email () {
    var idToken = new OidcIdToken("foo", Instant.EPOCH, Instant.MAX, Map.of(
        StandardClaimNames.SUB, "foo"
    ));
    var userInfo = new DefaultOidcUser(null, idToken);

    User actual = userLoader.loadUser (userInfo);

    assertThat (actual).isNull ();
  }

  @Test
  public void should_return_null_given_OidcUSer_without_email_verified_claim() {
    var idToken = new OidcIdToken("foo", Instant.EPOCH, Instant.MAX, Map.of(
        StandardClaimNames.SUB, "foo",
        StandardClaimNames.EMAIL, "etienne@example.com"
    ));
    var userInfo = new DefaultOidcUser(null, idToken);

    User actual = userLoader.loadUser(userInfo);

    assertThat(actual).isNull();
  }

  @Test
  public void should_return_null_given_UserInfo_with_unverified_email () {
    var idToken = new OidcIdToken("foo", Instant.EPOCH, Instant.MAX, Map.of(
        StandardClaimNames.SUB, "foo",
        StandardClaimNames.EMAIL, "etienne@example.com",
        StandardClaimNames.EMAIL_VERIFIED, Boolean.FALSE
    ));
    var userInfo = new DefaultOidcUser(null, idToken);

    User actual = userLoader.loadUser (userInfo);

    assertThat (actual).isNull ();
  }

  @Test
  public void should_return_null_given_UserInfo_with_unknown_email () {
    var idToken = new OidcIdToken("foo", Instant.EPOCH, Instant.MAX, Map.of(
        StandardClaimNames.SUB, "foo",
        StandardClaimNames.EMAIL, "foo@example.com",
        StandardClaimNames.EMAIL_VERIFIED, Boolean.TRUE
    ));
    var userInfo = new DefaultOidcUser(null, idToken);

    User actual = userLoader.loadUser (userInfo);

    assertThat (actual).isNull ();
  }

  @Test
  public void should_return_User_given_UserInfo_with_verified_known_email () {
    var idToken = new OidcIdToken("foo", Instant.EPOCH, Instant.MAX, Map.of(
        StandardClaimNames.SUB, "foo",
        StandardClaimNames.EMAIL, "etienne@example.com",
        StandardClaimNames.EMAIL_VERIFIED, Boolean.TRUE
    ));
    var userInfo = new DefaultOidcUser(null, idToken);

    User actual = userLoader.loadUser (userInfo);

    assertThat (actual).isNotNull ();
    assertThat (actual.getName ()).isEqualTo ("Etienne");
  }

}
