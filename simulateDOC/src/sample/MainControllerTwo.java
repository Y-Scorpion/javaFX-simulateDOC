package sample;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.*;
import java.nio.charset.Charset;

import static java.lang.Thread.sleep;

public class MainControllerTwo {
    @FXML
    private TextArea console;
    @FXML
    private TextField textField;
    @FXML
    private Button button;

    private  Process exec;


    public void initialize() throws IOException {
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                String text = String.valueOf((char) b);
                Platform. runLater(() -> console. appendText(text));
            }
            @Override
            public void write(byte[] b, int off, int len) {
                String s = new String(b, off, len);
                Platform. runLater(() -> console. appendText(s));
            }
        }, true));
        System.setErr(System.out);
        System.out.println("你好世界");
        exec = Runtime.getRuntime().exec("cmd.exe");
        button.setOnAction((actionEvent)->{
            try {
                this.testIpConfig();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


    }

    public void testIpConfig() throws IOException {
        String cmdText = textField.getText();
        System.out.println(cmdText); //input文本框输入
        textField.clear();
//        button.setDisable(true); //点击按钮后将按钮暂时失效处理

        PrintWriter printWriter = new PrintWriter(exec.getOutputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(exec.getInputStream(), Charset.forName("gbk")));
        printWriter.println(cmdText);
        printWriter.flush();

        new Thread(()->{
            String read;
            try {
                while (((read = br.readLine()) != null)) {
                    System.out.println(read);
                }
//                System.out.println("a");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

//        Service<String> service=new Service<String>() {
//            @Override
//            protected Task<String> createTask() {
//                return new Task<String>() {
//                    @Override
//                    protected String call() throws IOException {
//
//                        return "success";
//                    }
//                };
//            }
//            @Override
//            protected void succeeded() {
//                super.succeeded();
//                System.out.println("结束");
//                button.setDisable(false);
//                Platform.runLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        textField.requestFocus();
//                    }
//                });
//            }
//        };
//        service.start();
    }
}