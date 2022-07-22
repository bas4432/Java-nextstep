package bong.lines.basic.utill;

import bong.lines.basic.handler.Model.LoginUserDTO;
import bong.lines.basic.handler.getindexhtml.IndexHTMLHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class RequestUtil {

    private static final Logger log = LoggerFactory.getLogger(IndexHTMLHandler.class);

    public static String RequestBody(String reqeustbody) {
        //reqeustbody: name=123&email=123%40asdasdas.co.kr&id=11&password=11

        Map<String, String> values = new HashMap<String, String>();

        for (String param : reqeustbody.split("&")) {
            String pair[] = param.split("=");
            //ex) name=123

            if (pair.length > 1) {
                values.put(pair[0], pair[1]);
            } else {
                values.put(pair[0], "");
            }
        }


        LoginUserDTO loginUserDTO = new LoginUserDTO(values.get("name"), values.get("email"),values.get("id"), values.get("password"));

        String JSONresult = "\"{\n"+"\"name\": \"" + loginUserDTO.getName() + "\",\n"+"\"email\": \"" + loginUserDTO.getEmail() + "\""+",\n"+"\"id\":\"" +loginUserDTO.getId() +"\""+",\n"+"\"password\":\""+loginUserDTO.getPassword()+"\""+"\n}\"";

        return JSONresult;
    };

}
