package com.txs.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.txs.mobilesafe.R;
import com.txs.mobilesafe.engine.AddressDao;

public class AToolsActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
        initUi();
        AddressDao.getAddress("13000000000");
    }

    private void initUi() {
        TextView query_adress = findViewById(R.id.tv_query_address);
        query_adress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { 
                Intent intent = new Intent(getApplicationContext(), QueryAddressActivity.class);
                startActivity(intent);
            }
        });
    }
}
