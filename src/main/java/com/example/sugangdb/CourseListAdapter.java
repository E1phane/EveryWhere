package com.example.sugangdb;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class CourseListAdapter extends BaseAdapter {
    private Context context;
    private List<Course> courseList;
    private Fragment parent;
    private String stu_id = CourseActivity.stu_id;
    private Schedule schedule;
    private List<String> courseIDList;
    private int stuAvailCredit;
    public static int totalCredit = 0;

    public CourseListAdapter(Context context, List<Course> courseList, Fragment parent) {
        this.context = context;
        this.courseList = courseList;
        this.parent = parent;
        schedule = new Schedule();
        courseIDList = new ArrayList<String>();
        new BackgroundTask().execute();
        totalCredit = 0;
    }

    @Override
    public int getCount() {
        return courseList.size();
    }

    @Override
    public Object getItem(int i) {
        return courseList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.course, null);
        TextView courseGrade = (TextView) v.findViewById(R.id.courseGrade);
        TextView courseTitle = (TextView) v.findViewById(R.id.courseTitle);
        TextView courseCredit = (TextView) v.findViewById(R.id.courseCredit);
        TextView courseDivide = (TextView) v.findViewById(R.id.courseDivide);
        TextView courseClass = (TextView) v.findViewById(R.id.courseClass);
        TextView courseAvail = (TextView) v.findViewById(R.id.courseAvail);//최대
        TextView coursePersonal = (TextView) v.findViewById(R.id.coursePersonal);//현재
        TextView courseProfessor = (TextView) v.findViewById(R.id.courseProfessor);
        TextView courseTime = (TextView) v.findViewById(R.id.courseTime);
        TextView courseDept = (TextView) v.findViewById(R.id.courseDept);
        TextView courseID = (TextView) v.findViewById(R.id.courseID);

        courseGrade.setText(courseList.get(i).getTarget_grade() + "학년");

        courseTitle.setText(courseList.get(i).getCourse_name());
        courseCredit.setText(courseList.get(i).getCredit() + "학점");
        courseDivide.setText("0" + courseList.get(i).getDiv_id());
        courseClass.setText(courseList.get(i).getClassification());
        courseAvail.setText(String.valueOf(courseList.get(i).getCurrent_count()));
        courseDept.setText(courseList.get(i).getDept_name());
        courseID.setText(courseList.get(i).getCourse_id());
        if(courseList.get(i).getAvail_count() == 0){
            coursePersonal.setText("인원 제한 없음");
        }
        else {
            coursePersonal.setText(String.valueOf(courseList.get(i).getAvail_count()));
        }
        if(courseList.get(i).getProf_name().equals("NULL")) {
            courseProfessor.setText("");
        }
        else {
            courseProfessor.setText(courseList.get(i).getProf_name() + " 교수님");
        }
        courseTime.setText(courseList.get(i).getCourse_time() + "");

        v.setTag(String.valueOf(courseList.get(i).getCourse_id()));

        Button addButton = (Button) v.findViewById(R.id.addButton);
        Button addBasketButton = (Button) v.findViewById(R.id.addBasketButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stu_id = CourseActivity.stu_id;
                boolean validate = schedule.validate(courseList.get(i).getCourse_time());

                if (!alreadyIn(courseIDList, courseList.get(i).getCourse_id())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                    builder.setMessage("이미 추가한 강의입니다.").setPositiveButton("다시 시도", null).create();
                    builder.show();
                }
                else if(totalCredit + courseList.get(i).getCredit() > 21) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                    builder.setMessage("21학점을 초과할 수 없습니다.").setPositiveButton("다시 시도", null).create();
                    builder.show();
                }
                else if((courseList.get(i).getCurrent_count() >= courseList.get(i).getAvail_count()) && courseList.get(i).getAvail_count() != 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                    builder.setMessage("수강 가능 인원 초과!").setPositiveButton("다시 시도", null).create();
                    builder.show();
                }
                else if(validate == false) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                    builder.setMessage("시간표가 중복됩니다.").setPositiveButton("다시 시도", null).create();
                    builder.show();
                }
                else {
                    Response.Listener<String> responseListner = (response) -> {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                                builder.setMessage("수강신청되었습니다.").setPositiveButton("확인", null).create();
                                builder.show();
                                courseIDList.add(courseList.get(i).getCourse_id());
                                schedule.addSchedule(courseList.get(i).getCourse_time());
                                totalCredit += courseList.get(i).getCredit();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                                builder.setMessage("강의 추가에 실패했습니다.").setNegativeButton("확인", null).create();
                                builder.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    };
                    AddRequest addRequest = new AddRequest(stu_id, courseList.get(i).getCourse_id(), courseList.get(i).getDiv_id(), responseListner);
                    RequestQueue queue = Volley.newRequestQueue(parent.getActivity());
                    queue.add(addRequest);
                }
            }
        });
        addBasketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stu_id = CourseActivity.stu_id;
                if (!alreadyIn(courseIDList, courseList.get(i).getCourse_id())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                    builder.setMessage("이미 추가한 강의입니다.").setPositiveButton("다시 시도", null).create();
                    Log.d("CourseListAdapter", "이미 추가된 강의입니다.");
                    builder.show();
                }
                else if(totalCredit + courseList.get(i).getCredit() > 27) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                    builder.setMessage("27학점을 초과할 수 없습니다.").setPositiveButton("다시 시도", null).create();
                    builder.show();
                }else if((courseList.get(i).getCurrent_count() >= courseList.get(i).getAvail_count()) && courseList.get(i).getAvail_count() != 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                    builder.setMessage("수강 가능 인원 초과!").setPositiveButton("다시 시도", null).create();
                    builder.show();
                }
                else {
                    Response.Listener<String> responseListener = (response) -> {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                                builder.setMessage("장바구니에 추가되었습니다.").setPositiveButton("확인", null).create();
                                builder.show();
                                courseIDList.add(courseList.get(i).getCourse_id());
                                schedule.addSchedule(courseList.get(i).getCourse_time());
                                totalCredit += courseList.get(i).getCredit();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                                builder.setMessage("강의 추가에 실패했습니다.").setNegativeButton("확인", null).create();
                                builder.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    };
                    AddBasket addBasket = new AddBasket(stu_id, courseList.get(i).getCourse_id(), courseList.get(i).getDiv_id(), responseListener);
                    RequestQueue queue = Volley.newRequestQueue(parent.getActivity());
                    queue.add(addBasket);
                }
            }
        });
        return v;
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
                Log.e("CourseListAdapter", "Error in doInBackground: " + e.getMessage());
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
                JSONObject jsonResponse = new JSONObject(result);
                if (jsonResponse.has("response")) {
                    JSONArray jsonArray = jsonResponse.getJSONArray("response");

                    // Check if the array is not empty
                    if (jsonArray.length() > 0) {
                        int count = 0;
                        String prof_name;
                        String course_time;
                        String course_id;
                        totalCredit = 0;

                        while (count < jsonArray.length()) {
                            JSONObject object = jsonArray.getJSONObject(count);
                            course_id = object.getString("course_id");
                            prof_name = object.getString("prof_name");
                            course_time = object.getString("course_time");
                            totalCredit += object.getInt("credit");
                            courseIDList.add(course_id);
                            schedule.addSchedule(course_time);
                            count++;
                        }
                    } else {
                        Log.d("CourseListAdapter", "No data in the response array");
                    }
                } else {
                    Log.d("CourseListAdapter", "No 'response' key in the JSON");
                }
            } catch (JSONException e) {
                Log.e("CourseListAdapter", "Error in onPostExecute: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public boolean alreadyIn(List<String> courseIDList, String courseId) {
        for(int i = 0; i < courseIDList.size(); i++) {
            if(courseIDList.get(i).equals(courseId)) {
                return false;
            }
        }
        return true;
    }
}
