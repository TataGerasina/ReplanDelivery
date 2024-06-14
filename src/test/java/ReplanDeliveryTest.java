package ru.netology.web;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static com.codeborne.selenide.Selenide.*;
        import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReplanDeliveryTest {

    private String selectionDate(int addDate, String pattern) {
        return LocalDate.now().plusDays(addDate).format(DateTimeFormatter.ofPattern(pattern));
    }

    @Test
    void shouldTestSuccessAllForms() {
        open("http://localhost:9999");

        $("[data-test-id='city'] input").setValue("Казань");
        String planDate = selectionDate(4, "dd.MM.yyyy");

        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(planDate);
        $("[data-test-id='name'] input").setValue("Петров Иван");
        $("[data-test-id='phone'] input").setValue("+79000000000");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $(".notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Встреча успешно забронирована на " + planDate));
    }

    @Test
    void shouldTestEmptyCity() {
        open("http://localhost:9999");

        String planDate = selectionDate(4, "dd.MM.yyyy");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(planDate);
        $("[data-test-id='name'] input").setValue("Петров Иван");
        $("[data-test-id='phone'] input").setValue("+79000000000");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        var actualTextElement = $("[data-test-id='city'].input_invalid .input__sub");
        var actualText = actualTextElement.getText().trim();

        assertEquals("Поле обязательно для заполнения", actualText);
        assertTrue(actualTextElement.isDisplayed());
    }

    @Test
    void shouldTestEmptyName() {
        open("http://localhost:9999");

        $("[data-test-id='city'] input").setValue("Казань");
        String planDate = selectionDate(4, "dd.MM.yyyy");

        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(planDate);

        $("[data-test-id='phone'] input").setValue("+79000000000");
        $("[data-test-id='agreement']").click();
        $("button.button").click();

        var actualTextElement = $("[data-test-id='name'].input_invalid .input__sub");
        var actualText = actualTextElement.getText().trim();

        assertEquals("Поле обязательно для заполнения", actualText);
        assertTrue(actualTextElement.isDisplayed());
    }

    @Test
    void shouldTestEmptyPhone() {
        open("http://localhost:9999");

        $("[data-test-id='city'] input").setValue("Казань");
        String planDate = selectionDate(4, "dd.MM.yyyy");

        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(planDate);
        $("[data-test-id='name'] input").setValue("Петров Иван");

        $("[data-test-id='agreement']").click();
        $("button.button").click();

        var actualTextElement = $("[data-test-id='phone'].input_invalid .input__sub");
        var actualText = actualTextElement.getText().trim();

        assertEquals("Поле обязательно для заполнения", actualText);
        assertTrue(actualTextElement.isDisplayed());
    }

    @Test
    void shouldTestNotClickCheckbox() {
        open("http://localhost:9999");

        $("[data-test-id='city'] input").setValue("Казань");
        String planDate = selectionDate(4, "dd.MM.yyyy");

        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(planDate);
        $("[data-test-id='name'] input").setValue("Петров Иван");
        $("[data-test-id='phone'] input").setValue("+79000000000");

        $("button.button").click();

        var actualTextElement = $("[data-test-id='agreement'].input_invalid");
        var actualText = actualTextElement.getText().trim();

        assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных", actualText);
        assertTrue(actualTextElement.isDisplayed());
    }

    @Test
    void shouldTestNoCorrectedNameEng() {
        open("http://localhost:9999");

        $("[data-test-id='city'] input").setValue("Казань");
        String planDate = selectionDate(4, "dd.MM.yyyy");

        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(planDate);
        $("[data-test-id='name'] input").setValue("Ivan");
        $("[data-test-id='phone'] input").setValue("+79000000000");
        $("[data-test-id='agreement']").click();
        $("button.button").click();

        var actualTextElement = $("[data-test-id='name'].input_invalid .input__sub");
        var actualText = actualTextElement.getText().trim();

        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", actualText);
        assertTrue(actualTextElement.isDisplayed());
    }
    @Test
    void shouldTestNoCorrectedNameRus() {
        open("http://localhost:9999");

        $("[data-test-id='city'] input").setValue("Астрахань");
        String planDate = selectionDate(4, "dd.MM.yyyy");

        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(planDate);
        $("[data-test-id='name'] input").setValue("петров иван"); // с маленькой буквы фамилия и имя!!! БАГ
        $("[data-test-id='phone'] input").setValue("+79000000000");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $(".notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Встреча успешно забронирована на " + planDate));
    }

    @Test
    void shouldTestNoCorrectedCityRus() {
        open("http://localhost:9999");

        $("[data-test-id='city'] input").setValue("Урюпинск");
        String planDate = selectionDate(4, "dd.MM.yyyy");

        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(planDate);
        $("[data-test-id='name'] input").setValue("Петров Иван");
        $("[data-test-id='phone'] input").setValue("+79000000000");
        $("[data-test-id='agreement']").click();
        $("button.button").click();

        var actualTextElement = $("[data-test-id='city'].input_invalid .input__sub");
        var actualText = actualTextElement.getText().trim();

        assertEquals("Доставка в выбранный город недоступна", actualText);
        assertTrue(actualTextElement.isDisplayed());
    }

    @Test
    void shouldTestNoCorrectedCityEnglishWord() {
        open("http://localhost:9999");

        $("[data-test-id='city'] input").setValue("Amsterdam");
        String planDate = selectionDate(4, "dd.MM.yyyy");

        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(planDate);
        $("[data-test-id='name'] input").setValue("Петров Иван");
        $("[data-test-id='phone'] input").setValue("+79000000000");
        $("[data-test-id='agreement']").click();
        $("button.button").click();

        var actualTextElement = $("[data-test-id='city'].input_invalid .input__sub");
        var actualText = actualTextElement.getText().trim();

        assertEquals("Доставка в выбранный город недоступна", actualText);
        assertTrue(actualTextElement.isDisplayed());
    }

    @Test
    void shouldTestNoRealDate() {
        open("http://localhost:9999");

        $("[data-test-id='city'] input").setValue("Владивосток");
        String planDate = selectionDate(4, "09.02.1909");

        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(planDate);
        $("[data-test-id='name'] input").setValue("Петров Иван");
        $("[data-test-id='phone'] input").setValue("+79000000000");
        $("[data-test-id='agreement']").click();
        $("button.button").click();

        $("[data-test-id='date'] .input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldTestNoCorrectPhone() {
        open("http://localhost:9999");

        $("[data-test-id='city'] input").setValue("Казань");
        String planDate = selectionDate(4, "dd.MM.yyyy");

        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(planDate);
        $("[data-test-id='name'] input").setValue("Петров Иван");
        $("[data-test-id='phone'] input").setValue("+79000000000000");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $("[data-test-id='phone'] .input__sub")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

}