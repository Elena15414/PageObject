package ru.netology.pageobject.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.pageobject.page.DashBoardPage;
import ru.netology.pageobject.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.pageobject.data.DataHelper.*;

public class TestPageObject {
    DashBoardPage dashBoardPage;

    @BeforeEach
    void setup() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = getVerificationCode();
        dashBoardPage = verificationPage.validVerify(verificationCode);
    }

    @Test
    @DisplayName("Should Transfer Money From First Card To Second")
    void shouldTransferMoneyFromFirstCardToSecond() {
        var firstCardInfo = getFirstCardInfo();
        var lastCardInfo = getLastCardInfo();
        var firstCardBalance = dashBoardPage.getCardBalance(firstCardInfo);
        var lastCardBalance = dashBoardPage.getCardBalance(lastCardInfo);
        var amountTransfer = generateValidAmount(firstCardBalance);
        var expectedBalanceFirstCard = firstCardBalance - amountTransfer;
        var expectedBalanceLastCard = lastCardBalance + amountTransfer;
        var transferPage = dashBoardPage.selectCardToTransfer(lastCardInfo);
        dashBoardPage = transferPage.makeValidTransfer(String.valueOf(amountTransfer), firstCardInfo);
        var actualBalanceFirstCard = dashBoardPage.getCardBalance(firstCardInfo);
        var actualBalanceLastCard = dashBoardPage.getCardBalance(lastCardInfo);
        assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        assertEquals(expectedBalanceLastCard, actualBalanceLastCard);
    }

    @Test
    @DisplayName("Should Get Error Message If Amount More Balance")
    void shouldGetErrorMessageIfAmountMoreBalance() {
        var firstCardInfo = getFirstCardInfo();
        var lastCardInfo = getLastCardInfo();
        var firstCardBalance = dashBoardPage.getCardBalance(firstCardInfo);
        var lastCardBalance = dashBoardPage.getCardBalance(lastCardInfo);
        var amountTransfer = generateInvalidAmount(lastCardBalance);
        var transferPage = dashBoardPage.selectCardToTransfer(firstCardInfo);
        dashBoardPage = transferPage.makeValidTransfer(String.valueOf(amountTransfer), lastCardInfo);
        transferPage.findErrorMessage("Ошибка");
        var actualBalanceFirstCard = dashBoardPage.getCardBalance(firstCardInfo);
        var actualBalanceLastCard = dashBoardPage.getCardBalance(lastCardBalance);
        assertEquals(firstCardBalance, actualBalanceFirstCard);
        assertEquals(lastCardBalance, actualBalanceLastCard);
    }

}

