package com.moqian.pullsecret;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView authorIDTv;
    private Button authorIDCopy;

    private TextView authorPassTv;
    private Button authorPassCopy;

    private Button startAuthorForever;
    private Button startAuthorOne;

    ProgressDialog pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pro = new ProgressDialog(this);
        authorIDTv = (TextView) findViewById(R.id.author_id_text);
        authorIDCopy = (Button) findViewById(R.id.author_id_copy);
        authorIDCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copy(authorIDTv.getText().toString().trim());
                Toast.makeText(MainActivity.this, "复制成功！", Toast.LENGTH_SHORT).show();
            }
        });

        authorPassTv = (TextView) findViewById(R.id.author_pass_text);
        authorPassCopy = (Button) findViewById(R.id.author_pass_copy);
        authorPassCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copy(authorPassTv.getText().toString().trim());
                Toast.makeText(MainActivity.this, "复制成功！", Toast.LENGTH_SHORT).show();
            }
        });

        startAuthorForever = (Button) findViewById(R.id.start_forever_author);
        startAuthorForever.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //forever 10000 - 19999
                doClick(1);
//                long res = getRandom(1, 9999);
//                pullRSA((res + 10000) + "");

            }
        });
        startAuthorOne = (Button) findViewById(R.id.start_one_author);
        startAuthorOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //one 1 - 9999
                doClick(0);
//                long res = getRandom(1, 9999);
//                pullRSA(res + "");
            }
        });
    }

    private void doClick(final int whitch) {
        Handler handler = new Handler();
        pro.show();
        Runnable updateThread = new Runnable() {
            public void run() {
                long res = getRandom(1, 9999);
                if (whitch == 1) {
                    pullRSA((res + 10000) + "");
                } else {
                    pullRSA(res + "");
                }
                pro.cancel();
            }
        };

        handler.postDelayed(updateThread, 1000);
    }

    private void copy(String copy) {
        ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(copy);
    }

    private int getRandom(int start, int end) {
        Random random = new Random();
        int res = random.nextInt(end + 1) - start;
        return res;
    }

    private void pullRSA(String secret) {
        Log.e("tag","secret="+secret);

        Map<String, Object> keyMap = RSA.initKey();
        //公钥
        byte[] publicKey = RSA.getPublicKey(keyMap);

        //私钥
        byte[] privateKey = RSA.getPrivateKey(keyMap);

        byte[] publicKeys = Base64.encode(publicKey, Base64.DEFAULT);
        String sPublicKey = new String(publicKeys);

        byte[] code1 = RSA.encryptByPrivateKey(secret.getBytes(), privateKey);
        byte[] base64code1 = Base64.encode(code1, Base64.DEFAULT);

        String passKey = new String(base64code1);

        authorIDTv.setText(sPublicKey);
        authorPassTv.setText(passKey);
    }
}
