package bong.lines.basic.utill;

import bong.lines.basic.handler.Model.LoginUserDTO;

import java.util.HashMap;
import java.util.Map;

public class RequestUtil {

    public static String RequestBody(String reqeustbody) {


        Map<String, String> values = new HashMap<String, String>();

        for (String param : reqeustbody.split("&")) {
            String pair[] = param.split("=");

            if (pair.length > 1) {
                values.put(pair[0], pair[1]);
            } else {
                values.put(pair[0], "");
            }
        }

        LoginUserDTO loginUserDTO = new LoginUserDTO(values.get("name"), values.get("email"),values.get("id"), values.get("password"));
        System.out.println("asdjakldjaklda::" + loginUserDTO.getName());

        Map<String, String> Json = new HashMap<String ,String>();

        Json.put("name", loginUserDTO.getName());
        Json.put("email", loginUserDTO.getEmail());
        Json.put("id", loginUserDTO.getId());
        Json.put("password", loginUserDTO.getPassword());

        System.out.println("ReqeustUTil          ");

        String JSONresult = "\"{\n"+"\"name\": \"" + Json.get("name") + "\",\n"+"\"email\": \"" + Json.get("email") + "\""+",\n"+"\"id\":\"" +Json.get("id") +"\""+",\n"+"\"password\":\""+Json.get("password")+"\""+"\n}\"";


        return JSONresult;
    };



}
