package com.example.administrator.smssend.data1;

/**
 * Created by Administrator on 2016/6/23.
 */

public class Data1ResponseItem
{
    /**
     * resultCode : 0000
     * type : 1
     * orderid : 51439122
     * smstype : 1
     * cmd : YpwsJssZQTHiM5kTMkE0pYw4s1ajyWquWZzGilWmVDYjPpdRYbBZxRmv0CIuZma7z2S59DqXa1+1eXDJtlenMGM0GZMvxe9mGT22W9e0s/HKJGdxefRUW63Pokgm8m1FV+9NYjBPs87TtWCvB4PBYOj5XVyL0Wa5mEwG
     * port : 1065842455
     * portnumber : 0
     * init_sms : TU0jV0xBTiNJd2w3d0c4eDF3ZGVwWnFKVFhOd21BPT0jMTAyMjUwOTM4I0JGM0EyMzM1MzA4NzFDRUQ=
     * init_sms_number : 10658424
     */

    private String resultCode;
    private int type;
    private String orderid;
    private String smstype;
    private String cmd;
    private String port;
    private String portnumber;
    private String init_sms;
    private String init_sms_number;

    public String getResultCode()
    {
        return resultCode;
    }

    public void setResultCode(String resultCode)
    {
        this.resultCode = resultCode;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public String getOrderid()
    {
        return orderid;
    }

    public void setOrderid(String orderid)
    {
        this.orderid = orderid;
    }

    public String getSmstype()
    {
        return smstype;
    }

    public void setSmstype(String smstype)
    {
        this.smstype = smstype;
    }

    public String getCmd()
    {
        return cmd;
    }

    public void setCmd(String cmd)
    {
        this.cmd = cmd;
    }

    public String getPort()
    {
        return port;
    }

    public void setPort(String port)
    {
        this.port = port;
    }

    public String getPortnumber()
    {
        return portnumber;
    }

    public void setPortnumber(String portnumber)
    {
        this.portnumber = portnumber;
    }

    public String getInit_sms()
    {
        return init_sms;
    }

    public void setInit_sms(String init_sms)
    {
        this.init_sms = init_sms;
    }

    public String getInit_sms_number()
    {
        return init_sms_number;
    }

    public void setInit_sms_number(String init_sms_number)
    {
        this.init_sms_number = init_sms_number;
    }
}
