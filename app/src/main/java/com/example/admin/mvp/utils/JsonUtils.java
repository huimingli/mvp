package com.example.admin.mvp.utils;

import com.example.admin.mvp.model.BaseModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by admin on 2015/6/21.
 */
public class JsonUtils {
    public static String getModelName(String str) {
        String[] strArr = str.split("\\W");
        if (strArr.length > 0) {
            str = strArr[0];
        }
        return ucfirst(str);
    }
     public static String ucfirst (String str) {
        if (str != null && str != "") {
            str  = str.substring(0,1).toUpperCase()+str.substring(1);
        }
        return str;
    }
    public static List<String> getResult(JSONObject response) {
        List<String> list = new ArrayList<>();
        try {
            JSONObject firstEvent = new JSONObject(response.getString("result"));
            Iterator<String> it = firstEvent.keys();
            while (it.hasNext()) {
                // initialize
                String jsonKey = it.next();
                JSONArray modelJsonArray = firstEvent.optJSONArray(jsonKey);
                String modelName = JsonUtils.getModelName(jsonKey);
                // JSONObject
                if (modelJsonArray == null) {
                    JSONObject modelJsonObject = firstEvent.optJSONObject(jsonKey);
                    list.add(modelJsonObject.toString());
                    if (modelJsonObject == null) {
                        throw new Exception("Message result is invalid");
                    }
                    // JSONArray
                } else {
                    for (int i = 0; i < modelJsonArray.length(); i++) {
                        JSONObject modelJsonObject = modelJsonArray.optJSONObject(i);
                        list.add(modelJsonObject.toString());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if(list.isEmpty()){
                return null;
            }else{
                return  list;
            }
        }
    }

    public static BaseModel json2model (String modelClassName, JSONObject modelJsonObject) throws Exception  {
        // auto-load model class
        BaseModel modelObj = (BaseModel) Class.forName(modelClassName).newInstance();

        Class<? extends BaseModel> modelClass = modelObj.getClass();
        // auto-setting model fields
        Iterator<String> it = modelJsonObject.keys();
        while (it.hasNext()) {
            //属性名id
            String varField = it.next();
            //属性的值1
            String varValue = modelJsonObject.getString(varField);
            //类的全名
            Field field = modelClass.getDeclaredField(varField);

            field.setAccessible(true);

            //修改的
            if(varField.equals("id")||varField.equals("schedule_id")||varField.equals("number")
                    ||varField.equals("user_id")||varField.equals("movie_id")){
                field.set(modelObj, Integer.parseInt(varValue));
            }
            else if (varField.equals("airtime")) {
//					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
//							"yyyy-MM-dd HH:mm:ss");
//					Date date = null;
                try {
                    JSONObject object = new JSONObject(varValue);
                    Date date = new Date(Long.valueOf(object.getString("time")));
                    field.set(modelObj, date);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if(varField.equals("consumption")){
                field.set(modelObj, Double.parseDouble(varValue));
            }else if(varField.equals("showtime")||varField.equals("commitTime")||varField.equals("birthday")
                    ||varField.equals("collectionTime")){


                JSONObject object = new JSONObject(varValue);
                int year = object.getInt("year") + 1900;
                int month = object.getInt("month") + 1;
                int day = object.getInt("date");
                field.set(modelObj, year+"-"+month+"-"+day);

            }
            else {
                field.set(modelObj,varValue);
            }
        }
        return modelObj;
    }
    public static Object setResult (String result) throws Exception {

        if (result.length() > 0) {
            JSONObject jsonObject = null;
            jsonObject = new JSONObject(result);
            Iterator<String> it = jsonObject.keys();
            while (it.hasNext()) {
                // initialize
                String jsonKey = it.next();
                String modelName = getModelName(jsonKey);
                String modelClassName = "com.example.admin.mvp.model." + modelName;
                System.out.println("BaseMassage -- setResult() -- modeName: " + modelName);
                JSONArray modelJsonArray = jsonObject.optJSONArray(jsonKey);
                // JSONObject
                if (modelJsonArray == null) {
                    JSONObject modelJsonObject = jsonObject.optJSONObject(jsonKey);
                    if (modelJsonObject == null) {
                        throw new Exception("Message result is invalid");
                    }
                    return json2model(modelClassName,modelJsonObject);
                    // JSONArray
                } else {
                    ArrayList<BaseModel> modelList = new ArrayList<BaseModel>();
                    for (int i = 0; i < modelJsonArray.length(); i++) {
                        JSONObject modelJsonObject = modelJsonArray.optJSONObject(i);
                        modelList.add(json2model(modelClassName, modelJsonObject));
                    }
                    return modelList;
                }
            }
        }
        return null;
    }
}


