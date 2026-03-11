package guru.qa;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

import com.codeborne.selenide.Configuration;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class SelenideFilesTest {


  @BeforeAll
  static void beforeAll() {
    Configuration.browserSize = "1920x1080"; // Делаем окно большим, чтобы ничего не съезжало
    Configuration.pageLoadStrategy = "eager"; // Тесты запускаются быстрее
    Configuration.timeout = 5000; // Если элемент не появится за 5 секунд, то тест упадёт
    Configuration.holdBrowserOpen = true;  // После выполнения теста, браузер не закрывается автоматически.

  }


  @Test
  void downloadFileTest()  throws Exception {
    open("https://github.com/junit-team/junit5/blob/main/README.md");
    File downloaded =
        $("[href ='https://github.com/junit-team/junit-framework/raw/refs/heads/main/README.md']")
        .download();

    try (InputStream is = new FileInputStream(downloaded)) {
      byte[] data = is.readAllBytes();
      String dataAsString = new String(data, StandardCharsets.UTF_8);
      Assertions.assertTrue(dataAsString.contains("Contributions to JUnit are both welcomed and appreciated."));
    }

  }

  @Test
  void uploadFileTest() {
    open("https://fineuploader.com/demos.html");
    $("input[type='file']").uploadFromClasspath("cat.png.jpg");
    $(".qq-file-name").shouldHave(text("cat.png.jpg"));
  }
}


