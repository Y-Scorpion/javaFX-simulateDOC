package sample;

import java.io.*;
import java.nio.charset.Charset;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MainController {
    @FXML
    private TextArea console;
    @FXML
    private TextField textField;
    @FXML
    private Button button;

    public void initialize() {
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
//        System.setErr(ps);
        System.out.println("你好世界");
        button.setOnAction((actionEvent)->{
            this.testIpConfig();
        });


    }

    public void testIpConfig() {
        Runtime runtime = Runtime.getRuntime();
        String cmdText = textField.getText();
        System.out.println(cmdText); //input文本框输入
        textField.clear();
        button.setDisable(true); //点击按钮后将按钮暂时失效处理
        Process process = null;
        try {
            process = runtime.exec("cmd.exe /c "+cmdText);
            Charset charset = Charset.forName("gbk");
            Process finalProcess = process;
            new Thread(()->{  //开启异步线程 防止页面卡死
                try(InputStreamReader reader = new InputStreamReader(finalProcess.getInputStream(), charset)){
                    int read;
                    while((read = reader.read()) != -1){
                        System.out.print((char)read);
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }
                button.setDisable(false);  //在结束的时候将按钮接触失效状态
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        textField.requestFocus();
                    }
                });
            }).start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}