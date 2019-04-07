package fr.elimerl.registre.services;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;

/**
 * Test case for the {@link PictureRegistry} service.
 */
@RunWith (Parameterized.class)
public class PictureRegistryTest {

  @Parameters (name = "{0}")
  public static Iterable<Object[]> parameters () {
    return Arrays.asList (
        new Object[] {"image/jpeg", true},
        new Object[] {"image/png", true},
        new Object[] {"some-image/jpeg", false},
        new Object[] {"some-image/png", false},
        new Object[] {"image/jpeg2000", false},
        new Object[] {"image/pngg", false},
        new Object[] {"image/jpeg;leve=36", true},
        new Object[] {"image/png; foo=good", true}
    );
  }

  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder ();

  @Parameter (0)
  public String mediaType;

  @Parameter (1)
  public boolean shouldWrite;

  private PictureRegistry registry;

  private Path pictureDirectory;

  private InputStream stream;

  @Before
  public void setUp () throws Exception {
    String directory = temporaryFolder.newFolder ("pictures")
        .getAbsolutePath ();
    registry = new PictureRegistry ();
    registry.setPictureDirectory (directory);
    pictureDirectory = Paths.get (directory);
    stream = new ByteArrayInputStream (new byte[] {78, 32, 21});
  }

  @Test
  public void should_save_picture () {
    assumeTrue (shouldWrite);

    Optional<String> actual = registry.savePicture (mediaType, () -> stream);

    assertThat (actual).isPresent ();
    assertThat (pictureDirectory.resolve (actual.get ()))
        .hasBinaryContent (new byte[] {78, 32, 21});
  }

  @Test
  public void should_not_save_picture () throws Exception {
    assumeFalse (shouldWrite);

    Optional<String> actual = registry.savePicture (mediaType, () -> stream);

    assertThat (actual).isEmpty ();
    assertThat (Files.list (pictureDirectory)).isEmpty ();
  }

  @Test
  public void should_not_fail_on_IOException () {
    Optional<String> actual = registry.savePicture (mediaType, () -> {
      throw new IOException ("boohoo!");
    });

    assertThat (actual).isEmpty ();
  }

  @Test
  public void should_not_fail_on_RuntimeException () {
    Optional<String> actual = registry.savePicture (mediaType, () -> {
      throw new RuntimeException ("What?");
    });

    assertThat (actual).isEmpty ();
  }

}
