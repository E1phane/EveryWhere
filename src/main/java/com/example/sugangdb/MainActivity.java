package com.example.sugangdb;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private EditText idInput, pwInput;
    private Button loginButton;
    private TextView pwFind, pwChange;
    static String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 상단 타이틀 제거
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //id 입력
        idInput = findViewById(R.id.mainIdInput);
        //pw 입력
        pwInput = findViewById(R.id.mainPwInput);
        //로그인 버튼
        loginButton = findViewById(R.id.mainLogin);
        //비밀번호 찾기
        pwFind = findViewById(R.id.mainPwFind);
        //비밀번호 변경
        //pwChange = findViewById(R.id.mainPwChange);

        //로그인 버튼 클릭
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String stu_id = idInput.getText().toString();
                String stu_pw = pwInput.getText().toString();

                Response.Listener<String> responseListener = response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {//성공시
                            String stu_name = jsonObject.getString("stu_name");
                            String dept_name = jsonObject.getString("dept_name");
                            int stu_grade = jsonObject.getInt("stu_grade");
                            int stu_avail_credit = jsonObject.getInt("stu_avail_credit");
                            Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, CourseActivity.class);
                            intent.putExtra("stu_id", stu_id);
                            intent.putExtra("stu_name", stu_name);
                            intent.putExtra("dept_name", dept_name);
                            intent.putExtra("stu_grade", stu_grade);
                            intent.putExtra("stu_avail_credit", stu_avail_credit);
                            MainActivity.this.startActivity(intent);
                        }
                        else {//실패시
                            Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
                            return;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "예외 1", Toast.LENGTH_SHORT).show();
                        return;
                    }
                };
                LoginRequest loginRequest = new LoginRequest(stu_id, stu_pw, responseListener);
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(loginRequest);
            }
        });
        //비밀번호 찾기 클릭
        pwFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 비밀번호 찾기 팝업
                showFindPopup();
            }
        });

    }

    private void showFindPopup() {
        // Layout Inflater를 사용하여 커스텀 레이아웃을 inflate
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pw_find_popup, null);
        // EditText 가져오기
        EditText editText1 = view.findViewById(R.id.pwIdInput);
        EditText editText2 = view.findViewById(R.id.pwEmInput);

        // AlertDialog.Builder를 사용하여 팝업 창 설정
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setTitle("비밀번호 찾기");

        // 확인 버튼 설정
        builder.setPositiveButton("확인", (dialog, which) -> {
            // 사용자가 입력한 텍스트 가져오기
            String stu_id = editText1.getText().toString();
            String stu_email = editText2.getText().toString();
            id = editText1.getText().toString();

            Response.Listener<String> responseListener = response -> {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {//성공시
                        Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_SHORT).show();
                        showChangePopup();
                    }
                    else {//실패시
                        Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
                        return;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "예외 1", Toast.LENGTH_SHORT).show();
                    return;
                }
            };

            CheckEmail checkEmail = new CheckEmail(stu_id, stu_email, responseListener);
            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
            queue.add(checkEmail);

            dialog.dismiss(); // 팝업 창 닫기
        });

        // 취소 버튼 설정
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // 팝업 창 닫기
            }
        });

        // 팝업 창 표시
        builder.show();
    }

    //비밀번호 변경 popup 띄우는 함수
    private void showChangePopup() {
        // Layout Inflater를 사용하여 커스텀 레이아웃을 inflate
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pw_change_popup, null);

        // EditText 가져오기
        EditText editText2 = view.findViewById(R.id.pwChange);
        EditText editText3 = view.findViewById(R.id.pwCheck);

        // AlertDialog.Builder를 사용하여 팝업 창 설정
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setTitle("비밀번호 변경");

        // 확인 버튼 설정
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 사용자가 입력한 텍스트 가져오기
                String new_pw = editText2.getText().toString(); //바꿀 비번
                String again_pw = editText3.getText().toString(); //2 다시 입력

                // 변경 비밀번호 입력 실패
                if (!Objects.equals(new_pw, again_pw)) {
                    showToast("바꿀 비밀번호가 다릅니다.");
                }
                else //비밀번호 변경 선공
                {
                    Response.Listener<String> responseListener = response -> {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {//성공시
                                Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_SHORT).show();
                            }
                            else {//실패시
                                Toast.makeText(getApplicationContext(), "원래 비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "예외 1", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    };
                    ChangePw changePw = new ChangePw(id, new_pw, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    queue.add(changePw);

                    //input1이 맞으면, 2로 비번을 변경하라는 리퀘스트 전달
                    showToast("비밀번호가 변경되었습니다.");
                }
                dialog.dismiss(); // 팝업 창 닫기
            }
        });

        // 취소 버튼 설정
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // 팝업 창 닫기
            }
        });

        // 팝업 창 표시
        builder.show();
    }

    //토스트 메시지 함수
    private void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }


}