package listeners;

import com.aventstack.extentreports.Status;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import reporters.ExtentReportManager;

public class LogListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        ExtentReportManager.startTest(result.getMethod().getMethodName(), result.getMethod().getDescription());
        ExtentReportManager.getTest().log(Status.INFO, "Test Started: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentReportManager.getTest().log(Status.PASS, "Test Passed: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentReportManager.getTest().log(Status.FAIL, "Test Failed: " + result.getMethod().getMethodName());
        ExtentReportManager.getTest().log(Status.FAIL, "Reason: " + result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentReportManager.getTest().log(Status.SKIP, "Test Skipped: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        ExtentReportManager.getTest().log(Status.WARNING, "Test Partially Failed: " + result.getMethod().getMethodName());
    }

    @Override
    public void onStart(ITestContext context) {
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReportManager.flush();
    }
}
