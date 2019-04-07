package fr.elimerl.registre.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * This service is responsible for saving pictures to disk.
 */
public class PictureRegistry {

  @FunctionalInterface
  public interface DataSupplier {
    InputStream get () throws IOException;
  }

  /** Regexp pattern for the image/jpeg media-type. */
  private static final Pattern JPEG = Pattern.compile ("image/jpeg(\\W.*)?");

  /** Regexp pattern for the image/png media-type. */
  private static final Pattern PNG = Pattern.compile ("image/png(\\W.*)?");

  /** SLF4J logger for this class. */
  private static final Logger logger =
      LoggerFactory.getLogger (PictureRegistry.class);

  /** Path to the directory where to save pictures. */
  private Path pictureDirectory;

  /**
   * Save a given picture to disk. Support only JPEG and PNG, otherwise won’t
   * store anything and will return an empty optional.
   *
   * @param mediaType
   *     the media type of the data to store.
   * @param data
   *     an {@link InputStream} supplier. If called, the given
   *     input stream will be closed.
   * @return the filename of the new picture on success, an empty optional
   * on failure.
   */
  public Optional<String> savePicture (String mediaType, DataSupplier data) {
    UUID uuid = UUID.randomUUID ();
    String fileName;
    if (JPEG.matcher (mediaType).matches ()) {
      fileName = uuid + ".jpg";
    } else if (PNG.matcher (mediaType).matches ()) {
      fileName = uuid + ".png";
    } else {
      logger.warn ("Unsupported picture media-type: {}.", mediaType);
      return Optional.empty ();
    }
    try (InputStream inputStream = data.get ()) {
      Files.copy (inputStream, pictureDirectory.resolve (fileName));
      return Optional.of (fileName);
    } catch (Exception e) {
      logger.error ("Could not write picture to disk.", e);
      return Optional.empty ();
    }
  }

  /**
   * Define the path to the directory where to store pictures.
   *
   * @param pictureDirectory
   *     path to a writable directory.
   */
  public void setPictureDirectory (String pictureDirectory) {
    this.pictureDirectory = Paths.get (pictureDirectory);
  }

}
