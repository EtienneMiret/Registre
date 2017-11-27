package fr.elimerl.registre.security;

import fr.elimerl.registre.entities.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mitre.openid.connect.model.DefaultUserInfo;
import org.mitre.openid.connect.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
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
    UserInfo userInfo = new DefaultUserInfo ();
    userInfo.setEmail (null);

    User actual = userLoader.loadUser (userInfo);

    assertThat (actual).isNull ();
  }

  @Test
  public void should_return_null_given_UserInfo_with_unverified_email () {
    UserInfo userInfo = new DefaultUserInfo ();
    userInfo.setEmail ("etienne@example.com");
    userInfo.setEmailVerified (false);

    User actual = userLoader.loadUser (userInfo);

    assertThat (actual).isNull ();
  }

  @Test
  public void should_return_null_given_UserInfo_with_unknown_email () {
    UserInfo userInfo = new DefaultUserInfo ();
    userInfo.setEmail ("foo@example.com");
    userInfo.setEmailVerified (true);

    User actual = userLoader.loadUser (userInfo);

    assertThat (actual).isNull ();
  }

  @Test
  public void should_return_User_given_UserInfo_with_verified_known_email () {
    UserInfo userInfo = new DefaultUserInfo ();
    userInfo.setEmail ("etienne@example.com");
    userInfo.setEmailVerified (true);

    User actual = userLoader.loadUser (userInfo);

    assertThat (actual).isNotNull ();
    assertThat (actual.getName ()).isEqualTo ("Etienne");
  }

}
