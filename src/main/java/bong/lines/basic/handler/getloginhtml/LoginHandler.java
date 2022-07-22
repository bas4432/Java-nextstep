package bong.lines.basic.handler.getloginhtml;

import bong.lines.basic.handler.Model.LoginUserDTO;
import bong.lines.basic.handler.getindexhtml.IndexHTMLHandler;
import bong.lines.basic.utill.IOUtils;
import bong.lines.basic.utill.RequestUtil;
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

                if(line == null){
                    return;
                }

                if (line != null && line.contains("GET") && line.contains("loginform.do")) {
                    log.debug("loginform.doLine-------->" + line);
                    String screenName = line.split(" ")[1]
                            .replace("/", "")
                            .replace(".do", "");
                    body = Objects.requireNonNull(
                                    IndexHTMLHandler.class
                                            .getResourceAsStream("/templates/user/" + screenName + ".html"))
                            .readAllBytes();
                }


                if (line != null && line.contains("POST") && line.contains("/user/create")) {
                    log.debug("==================================");

                    log.debug("PostLin------------->" + line);
                    log.debug("bufferedReader.readLine() ---------------->" +bufferedReader.readLine());

                    Map<String, String> headers = new HashMap<>();
                    try {
                        String str = "";
                        while ((str = bufferedReader.readLine()).length() > 0) {
                            System.out.println("str ------------->> " + str);
                            String[] headerTokens = str.split(": ");
                            System.out.println("headerToken[0]------>" + headerTokens[0]);
                            System.out.println("headerToken[1]------>" + headerTokens[1]);

                            if (headerTokens.length == 2) {
                                headers.put(headerTokens[0], headerTokens[1]);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    log.debug("content-length--------------->" + headers.get("Content-Length"));

                    String requestBody = IOUtils.readData(bufferedReader, Integer.parseInt(headers.get("Content-Length")));

                    log.debug("requsetBody-----------> " + requestBody);

                    String result = RequestUtil.RequestBody(requestBody);

                    body = result.getBytes();

                    log.debug(" ==================================== ");
                }

                if(line != null && line.contains("GET") && line.contains("/user/create")){

                    log.debug("GETLine-------------------->" + line);

                    String[] splited = line.split(" ");

                    log.debug("splited----------->" + splited[1]);

                    String path = splited[1];

                    int index = path.indexOf("?");

                    log.debug("index------------->" + index);

                    String realpath = path.substring(index + 1);

                    log.debug("realPath---------->" + realpath);

                    String result = RequestUtil.RequestBody(realpath);

                    body = result.getBytes();
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

