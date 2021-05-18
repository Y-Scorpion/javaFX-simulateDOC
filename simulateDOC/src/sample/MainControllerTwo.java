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
        Service<String> service=new Service<String>() {
            @Override
            protected Task<String> createTask() {
                return new Task<String>() {
                    @Override
                    protected String call() throws Exception {
                        Process exec = runtime.exec("cmd.exe /c " + cmdText);
                        try(InputStreamReader reader = new InputStreamReader(exec.getInputStream(), Charset.forName("gbk"))){
                            BufferedReader br = new BufferedReader(reader);
                            String read;
                            while((read = br.readLine()) != null){
                                System.out.println(read);
                            }
                            br.close();
                        } catch (IOException e){
                            e.printStackTrace();
                        }

                        return "success";
                    }
                };
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                System.out.println("结束");
                button.setDisable(false);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        textField.requestFocus();
                    }
                });
            }
        };
        service.start();
    }
}