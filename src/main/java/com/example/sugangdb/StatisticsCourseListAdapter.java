package com.example.sugangdb;

import android.content.Context;
import android.os.AsyncTask;
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
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class StatisticsCourseListAdapter extends BaseAdapter {
    private Context context;
    private List<Course> courseList;
    private Fragment parent;
    private String stu_id = CourseActivity.stu_id;

    public StatisticsCourseListAdapter(Context context, List<Course> courseList, Fragment parent) {
        this.context = context;
        this.courseList = courseList;
        this.parent = parent;
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
        View v = View.inflate(context, R.layout.statistics, null);
        TextView courseGrade = (TextView) v.findViewById(R.id.courseGrade);     // 대상 학년
        TextView courseTitle = (TextView) v.findViewById(R.id.courseTitle);     // 강의 제목
        TextView courseDivide = (TextView) v.findViewById(R.id.courseDivide);   // 강의 분반
        TextView coursePersonal = (TextView) v.findViewById(R.id.coursePersonal);   // 최대 인원
        TextView courseRate = (TextView) v.findViewById(R.id.courseRate);       // 현재인원 / 최대인원

        courseGrade.setText(courseList.get(i).getTarget_grade() + "학년");

        courseTitle.setText(courseList.get(i).getCourse_name());
        courseDivide.setText(courseList.get(i).getDiv_id() + "분반");
        if (courseList.get(i).getAvail_count() == 0) {
            coursePersonal.setText("∞");
            courseRate.setText("");
        } else {
            coursePersonal.setText("신청인원 : " + courseList.get(i).getCurrent_count() + " / " + courseList.get(i).getAvail_count());
            int rate = ((int) (((double) courseList.get(i).getCurrent_count() * 100 / courseList.get(i).getAvail_count()) + 0.5));
            courseRate.setText("경쟁률 : " + rate + "%");
            if (rate < 20) {
                courseRate.setTextColor(parent.getResources().getColor(R.color.colorSafe));
            } else if (rate <= 50) {
                courseRate.setTextColor(parent.getResources().getColor(R.color.lilpa_700));
            } else if (rate <= 100) {
                courseRate.setTextColor(parent.getResources().getColor(R.color.colorDanger));
            } else if (rate < 150) {
                courseRate.setTextColor(parent.getResources().getColor(R.color.colorWarning));
            } else {
                courseRate.setTextColor(parent.getResources().getColor(R.color.colorRed));
            }
        }

        v.setTag(courseList.get(i).getCourse_id());

        Button deleteButton = (Button) v.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
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
                            StatisticsFragment.totalCredit -= courseList.get(i).getCredit();
                            StatisticsFragment.credit.setText((StatisticsFragment.totalCredit + "학점"));
                            courseList.remove(i);
                            notifyDataSetChanged();
                        } else {
                            builder.setMessage("강의 삭제에 실패했습니다.").setNegativeButton("확인", null).create();
                            builder.show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                };
                DeleteRequest deleteRequest = new DeleteRequest(stu_id, courseList.get(i).getCourse_id(), courseList.get(i).getDiv_id(), responseListener);
                RequestQueue queue = Volley.newRequestQueue(parent.getActivity());
                queue.add(deleteRequest);
            }
        });
        return v;
    }
}