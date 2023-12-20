package com.example.sugangdb;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DeleteBasket extends StringRequest {

    // 서버 URL 설정 (PHP 파일 연동)
    final static private String URL = "http://15.165.171.57/BasketDelete.php"; //호스팅 주소 + php
    private Map<String, String> map;



    public DeleteBasket(String stu_id, String course_id, int div_id, Response.Listener<String> listener) { //문자형태로 보낸다는 뜻
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("stu_id", stu_id);
        map.put("course_id", course_id);
        map.put("div_id", String.valueOf(div_id));

        Log.d("DeleteBasket", "stu_id: " + stu_id + ", course_id: " + course_id + ", div_id: " + div_id);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
