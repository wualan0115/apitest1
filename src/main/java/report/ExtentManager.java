package report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ResourceCDN;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;

public class ExtentManager {
    private static String reportLocation;
    private static ExtentReports extent;

    public static ExtentReports getInstance(){
        if(extent == null)
            createInstance();
        return extent;
    }

    public static ExtentReports createInstance() {
        String baseLocation=ExtentManager.class.getResource("/").getPath();
        System.out.println("编译后target的根路径"+baseLocation);
        File file=new File(baseLocation);
        String parentFile = new File(file.getParent()).getParent();
        System.out.println("项目的根路径"+parentFile);
        File reportDir = new File(parentFile+"/report");
        reportDir.mkdirs();
        reportLocation=reportDir.getAbsolutePath()+"/index.html";
        System.out.println("extentreport的报告路径"+reportLocation);

        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(reportLocation);
        htmlReporter.config().setTestViewChartLocation(ChartLocation.BOTTOM);
        htmlReporter.config().setChartVisibilityOnOpen(true);
        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setDocumentTitle(reportLocation);
        htmlReporter.config().setEncoding("utf-8");
        htmlReporter.config().setReportName(reportLocation);
        //ExtentReport容易报CDN错误，最终导致CSS加载不成功，报告生成失败
        htmlReporter.config().setResourceCDN(ResourceCDN.EXTENTREPORTS);

        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        return extent;
    }
}
