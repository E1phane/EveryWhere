package com.example.sugangdb;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AddRequest extends StringRequest {

    // 서버 URL 설정 (PHP 파일 연동)
    final static private String URL = "http://15.165.171.57/CourseAdd.php"; //호스팅 주소 + php
    private Map<String, String> map;



    public AddRequest(String stu_id, String course_id, int div_id, Response.Listener<String> listener) { //문자형태로 보낸다는 뜻
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("stu_id", stu_id);
        map.put("course_id", course_id);
        map.put("div_id", String.valueOf(div_id));

        Log.d("AddRequest", "stu_id: " + stu_id + ", course_id: " + course_id + ", div_id: " + div_id);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }

    @Override
    protected void deliverResponse(String response) {
        // 여기에 추가적인 로그를 출력하여 확인할 수 있도록 합니다.
        Log.d("AddRequest", "Server Response: " + response);

        // 나머지 코드는 그대로 유지합니다.
        super.deliverResponse(response);
    }
}
