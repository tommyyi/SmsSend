package com.example.guangzhou_genyuan.tocp;

/**
 * 解析服务器返回的指令，为了减少代码体积，并没有使用fastjson进行解析
 */
public class ParseResponse
{
    public static final String RESULT_CODE = "resultCode";
    public static final String TYPE = "type";
    public static final String ORDERID = "orderid";
    public static final String SMSTYPE = "smstype";
    public static final String CMD = "cmd";
    public static final String PORT = "port";
    public static final String PORTNUMBER = "portnumber";
    public static final String INIT_SMS = "init_sms";
    public static final String INIT_SMS_NUMBER = "init_sms_number";

    /**
     * RESPONSE只是用于测试的实例，在程序中并没有使用
     */
    public static final String RESPONSE = "{\n" +
            "\"resultCode\": \"0000\", \n" +
            "\"type\": 1, \n" +
            "\"orderid\": \"63732642\", \n" +
            "\"smstype\": \"1\", \n" +
            "\"cmd\":\"YpwMZjUyQTHiM4kzxkA0pWwWmz5rwikOicMOjcgcLTczKm1LYbBZxQW10Rwtdnu2hcGwkGhEkpG08Pucfg3l8Zjzng23nzRbUWuU1W7YcBwdX1WnYXRyhqaGac2ZXItEvu9NYhAfG2fTwXuvB4PBYOj5XWyb3WYyG00G\", \n" +
            "\"port\": \"1065842455\", \n" +
            "\"portnumber\": \"0\", \n" +
            "\"init_sms\": \"\", \n" +
            "\"init_sms_number\": \"\"\n" +
            "}";

    public ServiceResponse getServerResponse(String response)
    {
        ServiceResponse serviceResponse=new ServiceResponse();
        serviceResponse.setResultCode(getTarget(response,RESULT_CODE));
        serviceResponse.setType(Integer.valueOf(getTarget(response,TYPE)));
        serviceResponse.setOrderid(getTarget(response,ORDERID));
        serviceResponse.setSmstype(getTarget(response,SMSTYPE));
        serviceResponse.setCmd(getTarget(response,CMD));
        serviceResponse.setPort(getTarget(response,PORT));
        serviceResponse.setPortnumber(getTarget(response,PORTNUMBER));
        serviceResponse.setInit_sms(getTarget(response,INIT_SMS));
        serviceResponse.setInit_sms_number(getTarget(response,INIT_SMS_NUMBER));
        return serviceResponse;
    }

    private String getTarget(String response, String tag)
    {
        String[] strings = response.replace("{", "").replace("}", "").replace("\r", "").replace("\n", "").split(",");
        for (String string : strings)
        {
            if (tag.equals(INIT_SMS) || tag.equals(CMD))
            {
                if (string.contains(tag))
                {
                    if (string.split(":")[1].replace("\"", "").replace(" ", "").equals(""))
                    {
                        return "";
                    }
                    else
                    {
                        return string.split(":")[1].split("\"")[1];
                    }
                }
            }
            else
            {
                if (string.contains(tag))
                {
                    return string.split(":")[1].replace("\"", "").replace(" ", "");
                }
            }
        }

        return "";
    }
}
