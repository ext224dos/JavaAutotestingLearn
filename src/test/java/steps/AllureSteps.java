package steps;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class AllureSteps {
    @Attachment(value = "Screenshot", type = "image/png")
    @Step("Capture screenshot")
    public byte[] captureScreenshot(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    @Step("Capture screenshot (spoiler)")
    public void captureScreenshotSpoiler(WebDriver driver) {
        Allure.addAttachment("Screenshot", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
    }

    @Step("Capture screenshot (extension)")
    public void captureScreenshotSpoiler() {
        Allure.addAttachment("Screenshot", new ByteArrayInputStream(((TakesScreenshot) BasePage.getDriver()).getScreenshotAs(OutputType.BYTES)));
    }

    @Step("Download file: {destination}")
    public void download(String link, File destination) throws IOException {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpUriRequestBase request = new HttpGet(link);
            client.execute(request, (HttpClientResponseHandler<byte[]>) response -> {
                Files.write(destination.toPath(), response.getEntity().getContent().readAllBytes());
                Allure.addAttachment(destination.getName(), Files.newInputStream(destination.toPath()));
                return null;
            });
        }
    }
}
