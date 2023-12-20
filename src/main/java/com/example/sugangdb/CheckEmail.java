package com.example.sugangdb;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CheckEmail extends StringRequest {

    // 서버 URL 설정 (PHP 파일 연동)
    final static private String URL = "http://15.165.171.57/checkMail.php"; //호스팅 주소 + php
    private Map<String, String> map;



    public CheckEmail(String stu_id, String stu_email, Response.Listener<String> listener) { //문자형태로 보낸다는 뜻
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("stu_id", stu_id);
        map.put("stu_email", stu_email);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
