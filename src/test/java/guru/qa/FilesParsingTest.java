package guru.qa;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.opencsv.CSVReader;
import guru.qa.model.Glossary;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class FilesParsingTest {

  private final ClassLoader cl = FilesParsingTest.class.getClassLoader();
  private static final Gson gson = new Gson();




// скачивание и проверка PDF

  // Открыли страницу документации JUnit 5
  //Находим ссылку на PDF и скачиваем её с помощью .download()
  //Библиотека com.codeborne.pdftest открывает PDF
  //Проверяем, что поле author совпадает с ожидаемым
  @Test
  void pdfFileParsingTest()
    throws Exception {
      open("https://junit.org/junit5/docs/current/user-guide/");
      File downloaded;
    downloaded = $(".pdf-link [href='_exports/junit-user-guide-6.0.3.pdf#overview']").download();
    PDF pdf = new PDF(downloaded);
      Assertions.assertEquals("Stefan Behold, Sam Brannon, Johannes Link, Matthias Merges, Marc Philipp, Juliette de Rancour, Christian Stein", pdf.author);
    }



// скачивание и чтение Excel (.xls)

  // Скачиваем .xls файл
  //Библиотека com.codeborne.xlstest (или просто Apache POI под капотом) открывает его
  //Читаем конкретную ячейку → строка 3 (нумерация с 0), столбец 2 (C), лист 0
  //Проверяем, что там есть нужная фраза
  @Test
  void xlsFileParsingTest() throws Exception {
    open("https://excelvba.ru/programmes/Teachers?ysclid=lfcu77j9j9951587711");
    File downloaded = $(".field-items [href='https://ExcelVBA.ru/sites/default/files/teachers.xls']").download();
    XLS xls = new XLS(downloaded);

    String actualValue = xls.excel.getSheetAt(0).getRow(3).getCell(2).getStringCellValue();

    Assertions.assertTrue(actualValue.contains("Суммарное количество часов планируемое на штатную по всем разделам"));
  }



// чтение CSV из resources

  //Файл example.csv лежит в папке src/test/resources
  //Читаем его с помощью opencsv (CSVReader)
  //Проверяем количество строк и точное совпадение первых двух строк
  @Test
  void csvFileParsingTest() throws Exception {
    try (InputStream is = cl.getResourceAsStream("example.csv");
        CSVReader csvReader = new CSVReader(new InputStreamReader(is))) {

      List<String[]> data = csvReader.readAll();
      Assertions.assertEquals(2, data.size());
      Assertions.assertArrayEquals(
          new String[] {"Selenide", "https://selenide.org"},
          data.get(0)
      );
      Assertions.assertArrayEquals(
          new String[] {"JUnit 5", "https://junit.org"},
          data.get(1)
      );
    }
  }



  // просмотр содержимого ZIP

  //Берём sample.zip из resources
  //Просто выводим имена всех файлов внутри архива
  //Ничего не проверяет (это демонстрация)
  @Test
  void zipFileParsingTest() throws Exception {
    try (ZipInputStream zis = new ZipInputStream(cl.getResourceAsStream("sample.zip"))
    ) {
      ZipEntry entry;

      while ((entry = zis.getNextEntry()) != null) {
        System.out.println(entry.getName());
      }
    }
  }



  // чтение JSON «вручную»

  //Файл glossary.json из resources
  //Парсим с помощью Gson в JsonObject
  //Достаём значения по ключам (вложенность через .get())
  @Test
  void jsonFileParsingTest() throws Exception {
    try (Reader reader = new InputStreamReader(cl.getResourceAsStream("glossary.json"))
    ) {
      JsonObject actual = gson.fromJson(reader, JsonObject.class);

      Assertions.assertEquals("example glossary", actual.get("title").getAsString());
      Assertions.assertEquals(234234, actual.get("ID").getAsInt());

      JsonObject inner = actual.get("glossary").getAsJsonObject();

      Assertions.assertEquals("SGML", inner.get("SortAs").getAsString());
      Assertions.assertEquals("Standard Generalized Markup Language", inner.get("GlossTerm").getAsString());
    }
  }




// чтение JSON красиво (POJO)
  @Test
  void jsonFileParsingImprovedTest() throws Exception {
    try (Reader reader = new InputStreamReader(cl.getResourceAsStream("glossary.json"))
    ) {
      Glossary actual = gson.fromJson(reader, Glossary.class);

      Assertions.assertEquals("example glossary", actual.getTitle());
      Assertions.assertEquals(234234, actual.getID());
      Assertions.assertEquals("SGML", actual.getGlossary().getSortAs());
      Assertions.assertEquals("Standard Generalized Markup Language", actual.getGlossary().getGlossTerm());
    }
  }

}


