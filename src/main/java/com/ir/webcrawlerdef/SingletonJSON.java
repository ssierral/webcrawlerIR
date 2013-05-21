/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ir.webcrawlerdef;

import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author xsebastianx
 */
public class SingletonJSON {
    
    private JSONObject obj;
    
    private SingletonJSON() {
        obj = new JSONObject();
    }
    
    public static SingletonJSON getInstance() {
        return SingletonJSONHolder.INSTANCE;
    }
    
    private static class SingletonJSONHolder {

        private static final SingletonJSON INSTANCE = new SingletonJSON();
        
    }

    public JSONObject getObj() {
        return obj;
    }

    public void setObj(JSONObject obj) {
        this.obj = obj;
    }
    
    
}
