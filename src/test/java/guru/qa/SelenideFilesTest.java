package guru.qa;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

import com.codeborne.selenide.Configuration;
import java.io.File;
import java.io.FileInputStream;
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



// тест скачивания файла
  // метод "download" - Selenide сам скачивает файл по этой ссылке и возвращает объект File

  @Test
  void downloadFileTest()  throws Exception {
    open("https://github.com/junit-team/junit5/blob/main/README.md");
    File downloaded =
        $(".react-blob-header-edit-and-raw-actions [href='https://github.com/junit-team/junit-framework/raw/refs/heads/main/README.md']")
        .download();

    // открываем InputStream
   //читаем все байты
   //превращаем в строку
   //проверяем, что внутри есть определённая фраза

    try (InputStream is = new FileInputStream(downloaded)) {
      byte[] data = is.readAllBytes();
      String dataAsString = new String(data, StandardCharsets.UTF_8);
      Assertions.assertTrue(dataAsString.contains("Contributions to JUnit are both welcomed and appreciated."));
    }
  }
  //Итог: тест проверяет, что по ссылке «Raw» действительно лежит настоящий README.md
  // от JUnit и в нём есть ожидаемый текст.



// тест загрузки файла
  // Selenide берёт файл cat.png.jpg, который лежит в папке src/test/resources
  @Test
  void uploadFileTest() {
    open("https://fineuploader.com/demos.html");
    $("input[type='file']").uploadFromClasspath("cat.png.jpg");
    $(".qq-file-name").shouldHave(text("cat.png.jpg"));
  }
}
// Проверяем, что в интерфейсе загрузчика появилось имя файла → .shouldHave(text("cat.png.jpg"))
// тест проверяет, что файл успешно выбран и отобразился в UI загрузчика.

