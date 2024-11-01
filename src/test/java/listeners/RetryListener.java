package listeners;

import config.ApiConfig;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import exceptions.ApiException;

public class RetryListener implements IRetryAnalyzer {

    private int retryCount = 0;
    private static final int maxRetryCount = ApiConfig.getMaxRetries();

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            return true;
        }
        return false;
    }
}
