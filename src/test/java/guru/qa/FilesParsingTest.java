package guru.qa;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

import com.codeborne.pdftest.PDF;
import java.io.File;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FilesParsingTest {

  @Test
  void pdfFileParsingTest()
    throws Exception {
      open("https://junit.org/junit5/docs/current/user-guide/");
      File downloaded;
    downloaded = $("[href*='junit-user-guide-5.10.1.pdf']").download();
    PDF pdf = new PDF(downloaded);
      Assertions.assertEquals("Stefan Behold, Sam Brannon, Johannes Link, Matthias Merges, Marc Philipp, Juliette de Rancour, Christian Stein", pdf.author);
    }
  }


