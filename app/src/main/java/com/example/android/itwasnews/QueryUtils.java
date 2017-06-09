package com.example.android.itwasnews;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class QueryUtils {
    private static final int CONN_READ_TIME = 10000;
    private static final int CONN_CONNECT_TIME = 15000;
    private static final String JSON_OBJECT_RESPONSE = "response";
    private static final String JSON_ARRAY_RESULTS = "results";
    private static final String JSON_KEY_WEB_TITLE = "webTitle";
    private static final String JSON_KEY_SECTION_NAME = "sectionName";
    private static final String JSON_KEY_WEB_URL = "webUrl";
    private static final String JSON_KEY_WEB_PUBLICATION_DATE = "webPublicationDate";
    private static final int DATE_LENGTH = 10;


    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<News> fetchNewsData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        jsonResponse = makeHttpRequest(url);
        List<News> newsFeed = extractFeatureFromJson(jsonResponse);

        return newsFeed;
    }


    private static URL createUrl(String reqeustUrl) {
        URL url = null;
        try {
            url = new URL(reqeustUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;


        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(CONN_READ_TIME);
            httpURLConnection.setConnectTimeout(CONN_CONNECT_TIME);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + httpURLConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Google Books JSON results.", e);
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    private static List<News> extractFeatureFromJson(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        List<News> newsFeed = new ArrayList<>();
        String section = "";
        String title = "";
        String date = "";
        String url = "";
        try {
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            if (baseJsonResponse.has(JSON_OBJECT_RESPONSE)) {
                JSONObject response = baseJsonResponse.getJSONObject(JSON_OBJECT_RESPONSE);
                if (response.has(JSON_ARRAY_RESULTS)) {
                    JSONArray results = response.getJSONArray(JSON_ARRAY_RESULTS);
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject result = results.getJSONObject(i);
                        section = result.getString(JSON_KEY_SECTION_NAME);
                        title = result.getString(JSON_KEY_WEB_TITLE);
                        date = result.getString(JSON_KEY_WEB_PUBLICATION_DATE);
                        if (date.length() > DATE_LENGTH) {
                            date = date.substring(0, DATE_LENGTH);
                        }
                        url = result.getString(JSON_KEY_WEB_URL);
                        newsFeed.add(new News(section, title, date, url));
                    }
                }
            } else {
                Log.v(LOG_TAG, "not find json Object");
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "JSON exception", e);
            e.printStackTrace();
        }
        return newsFeed;
    }

    public static String getSubtitle() {
        String fromDate = formatDateForSubtitle(addYearAndDays(new Date(), -10, -7));
        String toDate = formatDateForSubtitle(addYearAndDays(new Date(), -10, 0));
        return fromDate + "~" + toDate;
    }

    public static String getFromDate() {
        return formatDateForQuery(addYearAndDays(new Date(), -10, -7));
    }

    public static String getToDate() {
        return formatDateForQuery(addYearAndDays(new Date(), -10, 0));
    }

    private static Date addYearAndDays(Date date, int year, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, year);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    private static String formatDateForQuery(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    private static String formatDateForSubtitle(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy");
        return sdf.format(date);
    }
}
