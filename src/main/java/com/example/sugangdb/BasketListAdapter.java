package com.example.sugangdb;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class BasketListAdapter extends BaseAdapter {
    private Context context;
    private List<Course> basketList;
    private Fragment parent;
    private String stu_id = CourseActivity.stu_id;
    private Schedule schedule;
    private List<String> basketIDList;

    public BasketListAdapter(Context context, List<Course> basketList, Fragment parent) {
        this.context = context;
        this.basketList = basketList;
        this.parent = parent;
        schedule = new Schedule();
        basketIDList = new ArrayList<String>();
        new BackgroundTask().execute();
    }

    @Override
    public int getCount() {
        return basketList.size();
    }

    @Override
    public Object getItem(int i) {
        return basketList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.basket, null);
        TextView basketGrade = (TextView) v.findViewById(R.id.basketGrade);
        TextView basketTitle = (TextView) v.findViewById(R.id.basketTitle);
        TextView basketCredit = (TextView) v.findViewById(R.id.basketCredit);
        TextView basketDivide = (TextView) v.findViewById(R.id.basketDivide);
        TextView basketClass = (TextView) v.findViewById(R.id.basketClass);
        TextView basketAvail = (TextView) v.findViewById(R.id.basketAvail);
        TextView basketPersonal = (TextView) v.findViewById(R.id.basketPersonal);
        TextView basketProfessor = (TextView) v.findViewById(R.id.basketProfessor);
        TextView basketTime = (TextView) v.findViewById(R.id.basketTime);

        basketGrade.setText(basketList.get(i).getTarget_grade() + "학년");

        basketTitle.setText(basketList.get(i).getCourse_name());
        basketCredit.setText(basketList.get(i).getCredit() + "학점");
        basketDivide.setText(basketList.get(i).getDiv_id() + "분반");
        basketClass.setText(basketList.get(i).getClassification());
        basketAvail.setText("현재 인원 : " + basketList.get(i).getCurrent_count() + "명");
        if(basketList.get(i).getAvail_count() == 0){
            basketPersonal.setText("인원 제한 없음");
        }
        else {
            basketPersonal.setText("제한 인원 : " + basketList.get(i).getAvail_count() + "명");
        }
        if(basketList.get(i).getProf_name().equals("NULL")) {
            basketProfessor.setText("");
        }
        else {
            basketProfessor.setText(basketList.get(i).getProf_name() + " 교수님");
        }
        basketTime.setText(basketList.get(i).getCourse_time() + "");

        v.setTag(String.valueOf(basketList.get(i).getCourse_id()));

        Button basketToSugang = (Button) v.findViewById(R.id.basketToSugang);
        Button deleteBasket = (Button) v.findViewById(R.id.deleteBasket);
        basketToSugang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTimeOverlap(basketList.get(i).getCourse_time())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                    builder.setMessage("기존 수강 중인 강의와 시간이 겹칩니다.").setNegativeButton("확인", null).create();
                    builder.show();
                } else {
                    Response.Listener<String> responseListener = (response) -> {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                            if (success) {
                                builder.setMessage("수강신청되었습니다.").setPositiveButton("확인", null).create();
                                builder.show();
                                StatisticsFragment.totalCredit -= basketList.get(i).getCredit();
                                StatisticsFragment.credit.setText((StatisticsFragment.totalCredit + "학점"));
                                basketList.remove(i);
                                notifyDataSetChanged();
                            } else {
                                builder.setMessage("수강신청에 실패했습니다.").setNegativeButton("확인", null).create();
                                builder.show();
                            }
                        } catch (Exception e) {

                        }
                    };
                    BasketToCourse basketToCourse = new BasketToCourse(stu_id, basketList.get(i).getCourse_id(), basketList.get(i).getDiv_id(), responseListener);
                    RequestQueue queue = Volley.newRequestQueue(parent.getActivity());
                    queue.add(basketToCourse);
                }
            }
        });

        deleteBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Response.Listener<String> responseListener = (response) -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                        if (success) {
                            builder.setMessage("강의가 삭제되었습니다.").setPositiveButton("확인", null).create();
                            builder.show();
                            StatisticsFragment.totalCredit -= basketList.get(i).getCredit();
                            StatisticsFragment.credit.setText((StatisticsFragment.totalCredit + "학점"));
                            basketList.remove(i);
                            notifyDataSetChanged();
                        } else {
                            builder.setMessage("강의 삭제에 실패했습니다.").setNegativeButton("확인", null).create();
                            builder.show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                };
                DeleteBasket deleteBasket = new DeleteBasket(stu_id, basketList.get(i).getCourse_id(), basketList.get(i).getDiv_id(), responseListener);
                RequestQueue queue = Volley.newRequestQueue(parent.getActivity());
                queue.add(deleteBasket);
            }
        });
        return v;
    }

    private boolean isTimeOverlap(String courseTime) {
        Schedule tempSchedule = new Schedule();  // 임시 Schedule 객체 생성
        tempSchedule.addSchedule(courseTime);  // 선택한 강의의 시간을 임시 Schedule에 추가

        // 현재 수강 중인 강의와의 시간 중복 여부 확인
        for (String existingCourseTime : basketIDList) {
            if (tempSchedule.validate(existingCourseTime)) {
                return true;  // 시간이 중복됨
            }
        }
        return false;  // 시간이 중복되지 않음
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            try {
                target = "http://15.165.171.57/ScheduleList.php?stu_id=" + URLEncoder.encode(stu_id, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        public void onProgressUpdate(Void... values){
            super.onProgressUpdate();
        }
        @Override
        public void onPostExecute(String result) {
            try {
                if (TextUtils.isEmpty(result)) {
                    Log.e("BackgroundTask", "Empty response");
                    return;
                }
                // 비동기 작업 완료 후에 basketIDList 초기화
                basketIDList = new ArrayList<>();

                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");

                int count = 0;
                String prof_name;
                String course_time;
                String course_id;

                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    course_id = object.getString("course_id");
                    prof_name = object.getString("prof_name");
                    course_time = object.getString("course_time");
                    basketIDList.add(course_id);
                    schedule.addSchedule(course_time);
                    count++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
