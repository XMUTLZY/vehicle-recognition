package xmut.class1.group3.vehiclerecognition.web.config;

import xmut.class1.group3.vehiclerecognition.http.response.admin.TestResultResponse;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class ImageTestRunnable implements Runnable {
    private String pythonProject;
    private String imageUrl;
    private CountDownLatch countDownLatch;
    volatile boolean flag = true;
    private final static TestResultResponse testResultResponse = new TestResultResponse();
    private static Map<String, String> map;
    private Thread thread;
    private final String exe = "python";
    private final String params = "--image";
    private String projectType;
    public ImageTestRunnable(String pythonProject, String imageUrl, CountDownLatch countDownLatch, String projectType){
        this.pythonProject = pythonProject;
        this.imageUrl = imageUrl;
        this.countDownLatch = countDownLatch;
        this.projectType = projectType;
    }
    public ImageTestRunnable() {

    }
    @Override
    public void run() {
        while (flag) {
            String[] exec = new String[]{exe, pythonProject, params, imageUrl};
            Process process = null;
            try {
                process = Runtime.getRuntime().exec(exec);
            } catch (IOException e) {
                e.printStackTrace();
            }
            InputStream is = process.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            String str = null;
            try {
//                str = new String(dis.readLine().getBytes("iso8859-1"),"utf-8");
                str = dis.readLine();
                System.out.println(str);
                map.put(projectType, str);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
        }
    }
    public void start() {
        thread = new Thread(this, pythonProject);
        thread.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.flag = false;
    }
    public TestResultResponse getTestResultResponse() {
        testResultResponse.setResultMap(map);
        return testResultResponse;
    }

    public void newMap() {
        map = new HashMap<>();
    }
}
