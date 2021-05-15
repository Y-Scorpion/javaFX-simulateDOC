package sample;

import java.io.*;
import java.nio.charset.Charset;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class MainController {
    @FXML
    private TextArea console;
    private PrintStream ps;

    public void initialize() {
        ps = new ConsolePrint(console);
    }

    public void button(ActionEvent event) {
//        System.setOut(ps);
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                String text = String.valueOf((char) b);
                Platform.runLater(() -> {
                    console.appendText(text);
                });
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
        this.testIpConfig();

    }

    public void testIpConfig() {
        Runtime runtime = Runtime.getRuntime();
//        try {
//            runtime.exec("ipconfig");
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        Process process = null;
        try {
            process = runtime.exec("cmd.exe /c python ScanIp.py");
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
            }).start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//        InputStream inputStream = process.getInputStream();
//        BufferedReader br = null;
//        //cmd系统默认设置为gbk码，这里解析为gbk码
//        br = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("GBK")));
//        String line = null;
//        try {
//            while ((line = br.readLine()) != null) {
//                System.out.println(line);
//            }
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
    }

    public class ConsolePrint extends PrintStream {//可以正常解析utf8和gbk码
        TextArea console;

        public ConsolePrint(TextArea console) {
            super(new ByteArrayOutputStream());
            this.console = console;
        }

        @Override
        public void write(byte[] buf, int off, int len) {
            print(new String(buf, off, len));
        }

        @Override
        public void print(String s) {
            console.appendText(s);
        }
    }

    public class Console extends OutputStream {
        private TextArea console;

        public Console(TextArea console) {
            this.console = console;
        }

        public void appendText(String valueOf) {
            console.appendText(valueOf);
        }

        public void write(int b) throws IOException {
            appendText(String.valueOf((char) b));//这里解析非ascii码会出错乱码
        }
    }
}