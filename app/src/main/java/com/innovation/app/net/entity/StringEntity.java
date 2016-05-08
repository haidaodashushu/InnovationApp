package com.innovation.app.net.entity;

import org.json.JSONException;

/**
 * @author WangZhengkui on 2016-05-09 00:58
 */
public class StringEntity extends BaseEntity{
    String string;
    @Override
    public void parser(String jsonStr) throws JSONException {
        string = jsonStr;
    }

    public String getString() {
        return string;
    }

    public StringEntity setString(String string) {
        this.string = string;
        return this;
    }
}
