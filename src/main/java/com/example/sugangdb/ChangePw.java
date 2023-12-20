package com.example.sugangdb;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ChangePw extends StringRequest{

    // 서버 URL 설정 (PHP 파일 연동)
    final static private String URL = "http://15.165.171.57/ChangePw.php"; //호스팅 주소 + php
    private Map<String, String> map;



    public ChangePw(String id, String new_pw, Response.Listener<String> listener) { //문자형태로 보낸다는 뜻
        super(Request.Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("id", id);
        map.put("new_pw", new_pw);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }

}
