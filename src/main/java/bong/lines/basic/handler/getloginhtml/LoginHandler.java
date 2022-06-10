package bong.lines.basic.handler.getloginhtml;

import bong.lines.basic.handler.Model.LoginUserDTO;
import bong.lines.basic.handler.getindexhtml.IndexHTMLHandler;
import bong.lines.basic.utill.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class LoginHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(IndexHTMLHandler.class);
    private Socket connection;

    public LoginHandler(Socket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}, Host Address : {}", connection.getInetAddress(), connection.getPort(), connection.getInetAddress().getHostAddress());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

            String line = " ";

            byte[] body = null;

            do {
                line = bufferedReader.readLine();

                if (line != null && line.contains("GET") && line.contains("loginform.do")) {
                    String screenName = line.split(" ")[1]
                            .replace("/", "")
                            .replace(".do", "");
                    body = Objects.requireNonNull(
                                    IndexHTMLHandler.class
                                            .getResourceAsStream("/templates/user/" + screenName + ".html"))
                            .readAllBytes();
                }

                if (line != null && line.contains("POST") && line.contains("/user/create")) {
                    System.out.println(" ==================================== ");

                    log.debug("------>" + line);
                    log.debug("<======" + bufferedReader.readLine());
                    Map<String, String> headers = new HashMap<>();
                    try {
                        String str = "";
                        while ((str = bufferedReader.readLine()).length() > 0) {
                            System.out.println("111111 => " + str);
                            String[] headerTokens = str.split(": ");
                            System.out.println("headerToken[0]-->>" + headerTokens[0]);
                            System.out.println("headerToken[1]-->>" + headerTokens[1]);

                            if (headerTokens.length == 2) {
                                headers.put(headerTokens[0], headerTokens[1]);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    System.out.println("content-legnth:::" + headers.get("Content-Length"));

                    String requsetBody = IOUtils.readData(bufferedReader, Integer.parseInt(headers.get("Content-Length")));

                    System.out.println("requsetBody = " + requsetBody);

                    Map<String, String> values = new HashMap<String ,String>();

                    for(String param : requsetBody.split("&")){
                        String pair[] = param.split("=");

                        if(pair.length>1){
                            values.put(pair[0], pair[1]);
                        }else{
                            values.put(pair[0], "");
                        }
                    }

                    LoginUserDTO loginUserDTO = new LoginUserDTO(values.get("name"), values.get("email"),values.get("id"), values.get("password"));

                    body = loginUserDTO.getName().getBytes();
                    //body = "hello world".getBytes();

                    System.out.println(" ==================================== ");
                }



            } while (body == null);

            DataOutputStream dos = new DataOutputStream(out);
              response200Header(dos, body.length);
              responseBody(dos, body);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {

        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8 \r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {

        try {
            dos.write(body, 0, body.length);
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (Exception exception) {
            exception.printStackTrace();
            log.error(exception.getMessage());
        }
    }
}

