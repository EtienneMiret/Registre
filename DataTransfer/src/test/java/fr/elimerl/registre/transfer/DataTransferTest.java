package fr.elimerl.registre.transfer;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;

public class DataTransferTest {

  @Test
  public void test () throws Exception {
    try (Connection connection = DriverManager.getConnection ("jdbc:hsqldb:mem:output", "SA", "")) {
      ScriptUtils.executeSqlScript (connection, new EncodedResource (
          new ClassPathResource ("schema.sql"), StandardCharsets.UTF_8
      ));
    }

    DataTransfer.main (new String[0]);
  }

}
