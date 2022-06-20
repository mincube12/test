package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends Activity {
    EditText edit;
    TextView text;
    String key = "F220607173";
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edit = (EditText) findViewById(R.id.edit);
        text = (TextView) findViewById(R.id.text);
    }

    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.button:

                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        data = getXmlData();
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                text.setText(data);
                            }
                        });
                    }
                }).start();
                break;
        }
    }

    String getXmlData() {
        StringBuffer buffer = new StringBuffer();

        String str = edit.getText().toString();
        String location = URLEncoder.encode(str);

        String queryUrl = "https://www.opinet.co.kr/api/aroundAll.do?code=" + key + "&x=314681.8&y=544837&radius=5000";

        try {
            URL url = new URL(queryUrl);
            InputStream is = url.openStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));

            String tag;

            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();

                        if (tag.equals("OIL"));
                        else if (tag.equals("OS_NM")) {
                            buffer.append("이름: ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        } else if (tag.equals("PRICE")) {
                            buffer.append("가격: ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        } else if (tag.equals("DISTANCE")) {
                            buffer.append("거리:");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        } else if (tag.equals("GIS_X_COOR")) {
                            buffer.append("X좌표:");
                            xpp.next();
                            buffer.append(xpp.getText());//cpNm
                            buffer.append("\n");
                        } else if (tag.equals("GIS_Y_COOR")) {
                            buffer.append("Y좌표:");
                            xpp.next();
                            buffer.append(xpp.getText());//
                            buffer.append("\n");
                        }
                        break;
                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag = xpp.getName();
                        if (tag.equals("OIL")) buffer.append("\n");

                        break;
                }

                eventType = xpp.next();
            }
        } catch (Exception e) {
        }
        buffer.append("파싱끝\n");
        return buffer.toString();
    }
}