package report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListner implements ITestListener {

    private static ExtentReports extent = ExtentManager.createInstance();
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult iTestResult) {
        System.out.println(iTestResult.getMethod().getMethodName()+"开始执行**************");
        ExtentTest extentTest = extent.createTest(iTestResult.getMethod().getMethodName(),iTestResult.getMethod().getDescription());
        test.set(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        System.out.println(iTestResult.getMethod().getMethodName()+" 运行通过************");
        test.get().pass("Test passed");

    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        System.out.println(iTestResult.getMethod().getMethodName()+" 运行失败*************");
        test.get().fail(iTestResult.getThrowable());

    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        System.out.println(iTestResult.getMethod().getMethodName()+"  跳过执行*************");
        test.get().skip(iTestResult.getThrowable());

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        System.out.println("onTestFailedButWithinSuccessPercentage for "+iTestResult.getMethod().getMethodName());

    }

    @Override
    public void onStart(ITestContext iTestContext) {

    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        extent.flush();

    }
}
