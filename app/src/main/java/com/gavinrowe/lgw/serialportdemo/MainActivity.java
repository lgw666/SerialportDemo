package com.gavinrowe.lgw.serialportdemo;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends SerialPortActivity {

    TextView mReception;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mReception = (TextView) findViewById(R.id.tv_reception);
        mReception.setMovementMethod(ScrollingMovementMethod.getInstance());
        EditText emission = (EditText) findViewById(R.id.EditTextEmission);
        emission.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                int i;
                CharSequence t = v.getText();
                char[] text = new char[t.length()];
                for (i = 0; i < t.length(); i++) {
                    text[i] = t.charAt(i);
                }
                try {
                    Log.e("TAG", "onEditorAction: " + new String(text));
                    mOutputStream.write(new String(text).getBytes());
                    mOutputStream.write('\n');
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

    @Override
    protected void onDataReceived(final byte[] buffer, final int size) {
        runOnUiThread(new Runnable() {
            public void run() {
                if (mReception != null) {
                    mReception.append(new String(buffer, 0, size));
                    int lineCount = mReception.getLineCount();
                    int lineHeight = mReception.getLineHeight();
                    int offset = lineCount * lineHeight;
                    int height = mReception.getHeight();
                    Log.e("TAG", "offset:" + offset
                            + "\nlineCount:" + lineCount
                            + "\nlineHeight:" + lineHeight
                            + "\nheight:" + height);

                    if (offset > height - lineHeight)
                        mReception.scrollTo(0, offset - height + lineHeight);
                }
            }
        });
    }
}
