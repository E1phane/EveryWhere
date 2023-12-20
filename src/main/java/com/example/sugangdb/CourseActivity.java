package com.example.sugangdb;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CourseActivity extends AppCompatActivity {

    private ListView noticeListView;
    private NoticeListAdapter adapter;
    private List<Notice> noticeList;
    private Button courseButton;
    private Button scheduleButton;
    private Button basketButton;
    private Button statisticsButton;
    private LinearLayout notice;

    public static String stu_id;
    public static String stu_name;
    public static String dept_name;
    public static int stu_grade;
    public static int stu_avail_credit;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        stu_id = getIntent().getStringExtra("stu_id");
        stu_name = getIntent().getStringExtra("stu_name");
        dept_name = getIntent().getStringExtra("dept_name");
        stu_grade = getIntent().getIntExtra("stu_grade", 0);
        stu_avail_credit = getIntent().getIntExtra("stu_avail_credit", 0);

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        Menu menu = navigationView.getMenu();

        TextView textViewItem1 = headerView.findViewById(R.id.user_name);   // 이름
        MenuItem item1 = menu.findItem(R.id.nav_item1);                    // 소속
        MenuItem item2 = menu.findItem(R.id.nav_item2);                    // 학번
        MenuItem item3 = menu.findItem(R.id.nav_item3);                    // 학년
        MenuItem item4 = menu.findItem(R.id.nav_item4);                    // 신청 가능 학점

        textViewItem1.setText(stu_name);
        item1.setTitle(dept_name);
        item2.setTitle(stu_id);
        item3.setTitle(stu_grade + "학년");
        item4.setTitle("신청 가능 학점 : " + stu_avail_credit + "학점");

        noticeListView = (ListView) findViewById(R.id.noticeListView);
        noticeList = new ArrayList<Notice>();
        // noticeList.add(new Notice("공지사항","작성자","날짜"));
        adapter = new NoticeListAdapter(getApplicationContext(), noticeList);
        noticeListView.setAdapter(adapter);

        courseButton = (Button) findViewById(R.id.courseButton);
        scheduleButton = (Button) findViewById(R.id.scheduleButton);
        basketButton = (Button) findViewById(R.id.basketButton);
        statisticsButton = (Button) findViewById(R.id.statisticsButton);
        notice = (LinearLayout) findViewById(R.id.notice);

        courseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notice.setVisibility(View.GONE);
                courseButton.setBackgroundColor(getResources().getColor(R.color.lilpa_dark));
                basketButton.setBackgroundColor(getResources().getColor(R.color.lilpa_700));
                statisticsButton.setBackgroundColor(getResources().getColor(R.color.lilpa_700));
                scheduleButton.setBackgroundColor(getResources().getColor(R.color.lilpa_700));
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new CourseFragment());
                fragmentTransaction.commit();
            }
        });
        basketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notice.setVisibility(View.GONE);
                courseButton.setBackgroundColor(getResources().getColor(R.color.lilpa_700));
                basketButton.setBackgroundColor(getResources().getColor(R.color.lilpa_dark));
                statisticsButton.setBackgroundColor(getResources().getColor(R.color.lilpa_700));
                scheduleButton.setBackgroundColor(getResources().getColor(R.color.lilpa_700));
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new BasketFragment());
                fragmentTransaction.commit();
            }
        });
        statisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notice.setVisibility(View.GONE);
                courseButton.setBackgroundColor(getResources().getColor(R.color.lilpa_700));
                basketButton.setBackgroundColor(getResources().getColor(R.color.lilpa_700));
                statisticsButton.setBackgroundColor(getResources().getColor(R.color.lilpa_dark));
                scheduleButton.setBackgroundColor(getResources().getColor(R.color.lilpa_700));
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new StatisticsFragment());
                fragmentTransaction.commit();
            }
        });
        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notice.setVisibility(View.GONE);
                courseButton.setBackgroundColor(getResources().getColor(R.color.lilpa_700));
                basketButton.setBackgroundColor(getResources().getColor(R.color.lilpa_700));
                statisticsButton.setBackgroundColor(getResources().getColor(R.color.lilpa_700));
                scheduleButton.setBackgroundColor(getResources().getColor(R.color.lilpa_dark));
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new ScheduleFragment());
                fragmentTransaction.commit();
            }
        });

        new BackgroundTask().execute();
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {

        String target;

        @Override
        protected void onPreExecute() {
            target = "http://15.165.171.57/NoticeList.php";
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }

        @Override
        public void onPostExecute(String result) {
            try {
                JSONArray jsonArray = new JSONArray(result);

                int count = 0;
                String noticeContent, noticeName, noticeDate;
                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    noticeContent = object.getString("noticeContent");
                    noticeName = object.getString("noticeName");
                    noticeDate = object.getString("noticeDate");

                    Notice notice = new Notice(noticeContent, noticeName, noticeDate);
                    noticeList.add(notice);
                    count++;
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        notice.setVisibility(View.VISIBLE);
        courseButton.setBackgroundColor(getResources().getColor(R.color.lilpa_700));
        basketButton.setBackgroundColor(getResources().getColor(R.color.lilpa_700));
        statisticsButton.setBackgroundColor(getResources().getColor(R.color.lilpa_700));
        scheduleButton.setBackgroundColor(getResources().getColor(R.color.lilpa_700));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 툴바 메뉴 아이템 클릭 이벤트 처리
        switch (item.getItemId()) {
            // 메뉴 아이템에 대한 동작 처리
        }
        return super.onOptionsItemSelected(item);
    }
}