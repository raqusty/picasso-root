package com.example.picasso;

import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class SampleGridViewActivity extends PicassoSampleActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_gridview_activity);

        GridView gv = findViewById(R.id.grid_view);
        gv.setAdapter(new SampleGridViewAdapter(this));
        gv.setOnScrollListener(new SampleScrollListener(this));


//        byte[] key = EncryptionUtil.getKey();
//        String name = "linzehao";
//        byte[] result =   EncryptionUtil.encryptionByKey(name.getBytes(),key,false);
//        Log.i("linzehao", new String(result));
//
//        byte[] result1 =   EncryptionUtil.encryptionByKey(result,key,true);
//        Log.i("linzehao",new String(result1));

    }
}
