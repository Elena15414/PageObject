package ru.netology.pageobject.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.pageobject.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class TransferPage {
    private final SelenideElement amount = $("[data-test-id=amount] input");
    private final SelenideElement from = $("[data-test-id=from] input");
    // private final SelenideElement to = $("[data-test-id=to] input");
    private final SelenideElement actionTransfer = $("[data-test-id=action-transfer]");
    private final SelenideElement actionCancel = $("[data-test-id=action-cancel]");
    private final SelenideElement transferHead = $(byText("Пополнение карты"));
    private final SelenideElement errorMessage = $("[data-test-id=error-notification]");


    public TransferPage() {
        transferHead.shouldBe(Condition.visible);
    }

    public void transfer(DataHelper.CardInfo cardInfo, String amountToTransfer) {
        amount.setValue(amountToTransfer);
        from.setValue(cardInfo.getCardNumber());
        actionTransfer.click();
    }

    public void findErrorMessage(String expectedText) {
        errorMessage.shouldHave(exactText(expectedText), Duration.ofSeconds(15)).shouldBe(Condition.visible);
    }

    public DashBoardPage validTransfer(DataHelper.CardInfo cardInfo, String amountToTransfer) {
        transfer(cardInfo, amountToTransfer);
        return new DashBoardPage();
    }

    public void transferCancel(DataHelper.CardInfo cardInfo, String amountToTransfer) {
        amount.setValue(amountToTransfer);
        from.setValue(cardInfo.getCardNumber());
        actionCancel.click();
    }

}
