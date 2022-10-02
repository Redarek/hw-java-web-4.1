import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class cardDeliveryTest {
    public String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }
    public void clearDateInput() {
        for (int i = 0; i < 8; i++) {
            $("[data-test-id='date'] input").sendKeys(Keys.BACK_SPACE);
        }
    }

    String planningDate = generateDate(4);

    @BeforeEach
    void setupTest(){
        open("http://localhost:9999");
    }

    @Test
    void happyPathTest() {
        $("[data-test-id='city'] input").setValue("Челябинск");
        clearDateInput();
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Тиньков-Тинькофф Олег");
        $("[data-test-id='phone'] input").setValue("+79997770011");
        $("[data-test-id='agreement']").click();
        $x("//span[@class=\"button__text\"]").click();
        $x("//*[contains(text(), \"Успешно!\")]").should(visible, Duration.ofSeconds(15));
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15))
                .shouldBe(visible);
    }

    @Test
    void testFormWithoutCheckbox() {
        $("[data-test-id='city'] input").setValue("Челябинск");
        clearDateInput();
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Тиньков Олег");
        $("[data-test-id='phone'] input").setValue("+79997770011");
        $x("//span[@class=\"button__text\"]").click();
        $("label.input_invalid");
    }

    @Test
    void nameTest() {
        $("[data-test-id='city'] input").setValue("Челябинск");
        clearDateInput();
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Tinkov-Tinkoff Oleg");
        $("[data-test-id='phone'] input").setValue("+79997770011");
        $("[data-test-id='agreement']").click();
        $x("//span[@class=\"button__text\"]").click();
        $x("//*[contains(text(),'Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.')]");
    }

    @Test
    void cityTest() {
        $("[data-test-id='city'] input").setValue("Токио");
        clearDateInput();
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Тиньков Олег");
        $("[data-test-id='phone'] input").setValue("+79997770011");
        $("[data-test-id='agreement']").click();
        $x("//span[@class=\"button__text\"]").click();
        $x("//*[contains(text(),\"Доставка в выбранный город недоступна\")]");
    }

    @Test
    void dateTest() {
        planningDate = generateDate(1);
        $("[data-test-id='city'] input").setValue("Челябинск");
        clearDateInput();
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Тиньков Олег");
        $("[data-test-id='phone'] input").setValue("+79997770011");
        $("[data-test-id='agreement']").click();
        $x("//span[@class=\"button__text\"]").click();
        $x("//*[contains(text(),\"Заказ на выбранную дату невозможен\")]");
    }

    @Test
    void phoneTest() {
        $("[data-test-id='city'] input").setValue("Челябинск");
        clearDateInput();
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Тиньков Олег");
        $("[data-test-id='phone'] input").setValue("89997770011");
        $("[data-test-id='agreement']").click();
        $x("//span[@class=\"button__text\"]").click();
        $x("//*[contains(text(),'Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.')]");
    }

    @Test
    void emptyFormTest() {
        $x("//span[@class=\"button__text\"]").click();
        $x("//*[contains(text(),'Поле обязательно для заполнения')]");
    }

}
