package com.example.sugangdb;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private AutoResizeTextView monday[] = new AutoResizeTextView[14];
    private AutoResizeTextView tuesday[] = new AutoResizeTextView[14];
    private AutoResizeTextView wednesday[] = new AutoResizeTextView[14];
    private AutoResizeTextView thursday[] = new AutoResizeTextView[14];
    private AutoResizeTextView friday[] = new AutoResizeTextView[14];
    private Schedule schedule = new Schedule();

    @Override
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);
        monday[0] = (AutoResizeTextView) getView().findViewById(R.id.monday0);
        monday[1] = (AutoResizeTextView) getView().findViewById(R.id.monday1);
        monday[2] = (AutoResizeTextView) getView().findViewById(R.id.monday2);
        monday[3] = (AutoResizeTextView) getView().findViewById(R.id.monday3);
        monday[4] = (AutoResizeTextView) getView().findViewById(R.id.monday4);
        monday[5] = (AutoResizeTextView) getView().findViewById(R.id.monday5);
        monday[6] = (AutoResizeTextView) getView().findViewById(R.id.monday6);
        monday[7] = (AutoResizeTextView) getView().findViewById(R.id.monday7);
        monday[8] = (AutoResizeTextView) getView().findViewById(R.id.monday8);
        monday[9] = (AutoResizeTextView) getView().findViewById(R.id.monday9);
        monday[10] = (AutoResizeTextView) getView().findViewById(R.id.monday10);
        monday[11] = (AutoResizeTextView) getView().findViewById(R.id.monday11);
        monday[12] = (AutoResizeTextView) getView().findViewById(R.id.monday12);
        monday[13] = (AutoResizeTextView) getView().findViewById(R.id.monday13);
        tuesday[0] = (AutoResizeTextView) getView().findViewById(R.id.tuesday0);
        tuesday[1] = (AutoResizeTextView) getView().findViewById(R.id.tuesday1);
        tuesday[2] = (AutoResizeTextView) getView().findViewById(R.id.tuesday2);
        tuesday[3] = (AutoResizeTextView) getView().findViewById(R.id.tuesday3);
        tuesday[4] = (AutoResizeTextView) getView().findViewById(R.id.tuesday4);
        tuesday[5] = (AutoResizeTextView) getView().findViewById(R.id.tuesday5);
        tuesday[6] = (AutoResizeTextView) getView().findViewById(R.id.tuesday6);
        tuesday[7] = (AutoResizeTextView) getView().findViewById(R.id.tuesday7);
        tuesday[8] = (AutoResizeTextView) getView().findViewById(R.id.tuesday8);
        tuesday[9] = (AutoResizeTextView) getView().findViewById(R.id.tuesday9);
        tuesday[10] = (AutoResizeTextView) getView().findViewById(R.id.tuesday10);
        tuesday[11] = (AutoResizeTextView) getView().findViewById(R.id.tuesday11);
        tuesday[12] = (AutoResizeTextView) getView().findViewById(R.id.tuesday12);
        tuesday[13] = (AutoResizeTextView) getView().findViewById(R.id.tuesday13);
        wednesday[0] = (AutoResizeTextView) getView().findViewById(R.id.wednesday0);
        wednesday[1] = (AutoResizeTextView) getView().findViewById(R.id.wednesday1);
        wednesday[2] = (AutoResizeTextView) getView().findViewById(R.id.wednesday2);
        wednesday[3] = (AutoResizeTextView) getView().findViewById(R.id.wednesday3);
        wednesday[4] = (AutoResizeTextView) getView().findViewById(R.id.wednesday4);
        wednesday[5] = (AutoResizeTextView) getView().findViewById(R.id.wednesday5);
        wednesday[6] = (AutoResizeTextView) getView().findViewById(R.id.wednesday6);
        wednesday[7] = (AutoResizeTextView) getView().findViewById(R.id.wednesday7);
        wednesday[8] = (AutoResizeTextView) getView().findViewById(R.id.wednesday8);
        wednesday[9] = (AutoResizeTextView) getView().findViewById(R.id.wednesday9);
        wednesday[10] = (AutoResizeTextView) getView().findViewById(R.id.wednesday10);
        wednesday[11] = (AutoResizeTextView) getView().findViewById(R.id.wednesday11);
        wednesday[12] = (AutoResizeTextView) getView().findViewById(R.id.wednesday12);
        wednesday[13] = (AutoResizeTextView) getView().findViewById(R.id.wednesday13);
        thursday[0] = (AutoResizeTextView) getView().findViewById(R.id.thursday0);
        thursday[1] = (AutoResizeTextView) getView().findViewById(R.id.thursday1);
        thursday[2] = (AutoResizeTextView) getView().findViewById(R.id.thursday2);
        thursday[3] = (AutoResizeTextView) getView().findViewById(R.id.thursday3);
        thursday[4] = (AutoResizeTextView) getView().findViewById(R.id.thursday4);
        thursday[5] = (AutoResizeTextView) getView().findViewById(R.id.thursday5);
        thursday[6] = (AutoResizeTextView) getView().findViewById(R.id.thursday6);
        thursday[7] = (AutoResizeTextView) getView().findViewById(R.id.thursday7);
        thursday[8] = (AutoResizeTextView) getView().findViewById(R.id.thursday8);
        thursday[9] = (AutoResizeTextView) getView().findViewById(R.id.thursday9);
        thursday[10] = (AutoResizeTextView) getView().findViewById(R.id.thursday10);
        thursday[11] = (AutoResizeTextView) getView().findViewById(R.id.thursday11);
        thursday[12] = (AutoResizeTextView) getView().findViewById(R.id.thursday12);
        thursday[13] = (AutoResizeTextView) getView().findViewById(R.id.thursday13);
        friday[0] = (AutoResizeTextView) getView().findViewById(R.id.friday0);
        friday[1] = (AutoResizeTextView) getView().findViewById(R.id.friday1);
        friday[2] = (AutoResizeTextView) getView().findViewById(R.id.friday2);
        friday[3] = (AutoResizeTextView) getView().findViewById(R.id.friday3);
        friday[4] = (AutoResizeTextView) getView().findViewById(R.id.friday4);
        friday[5] = (AutoResizeTextView) getView().findViewById(R.id.friday5);
        friday[6] = (AutoResizeTextView) getView().findViewById(R.id.friday6);
        friday[7] = (AutoResizeTextView) getView().findViewById(R.id.friday7);
        friday[8] = (AutoResizeTextView) getView().findViewById(R.id.friday8);
        friday[9] = (AutoResizeTextView) getView().findViewById(R.id.friday9);
        friday[10] = (AutoResizeTextView) getView().findViewById(R.id.friday10);
        friday[11] = (AutoResizeTextView) getView().findViewById(R.id.friday11);
        friday[12] = (AutoResizeTextView) getView().findViewById(R.id.friday12);
        friday[13] = (AutoResizeTextView) getView().findViewById(R.id.friday13);

        new BackgroundTask().execute();
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            try {
                target = "http://15.165.171.57/ScheduleList.php?stu_id=" + URLEncoder.encode(CourseActivity.stu_id, "UTF-8");
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
                Log.d("CourseListAdapter", "BackgroundTask onPostExecute called with result: " + result);
                JSONObject jsonResponse = new JSONObject(result);

                // Check if the "response" key exists
                if (jsonResponse.has("response")) {
                    JSONArray jsonArray = jsonResponse.getJSONArray("response");

                    // Check if the array is not empty
                    if (jsonArray.length() > 0) {
                        int count = 0;
                        String prof_name;
                        String course_time;
                        String course_name;
                        String course_id;

                        while (count < jsonArray.length()) {
                            JSONObject object = jsonArray.getJSONObject(count);
                            course_id = object.getString("course_id");
                            prof_name = object.getString("prof_name");
                            course_time = object.getString("course_time");
                            course_name = object.getString("course_name");
                            schedule.addSchedule(course_time, course_name, prof_name);
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
            schedule.setting(monday, tuesday, wednesday, thursday, friday, getContext());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }
}