package com.yan.listview;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class MainActivity extends Activity {

    YanRefreshListView RefreshListview;
    String[] dataa={"adc","ggg","jjj","ttttt","yyy","adc","ggg","jjj","ttttt","yyy","ttttt","yyy","adc","ggg","jjj","ttttt","yyy","ttttt","yyy","adc","ggg","jjj","ttttt","yyy"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RefreshListview= (YanRefreshListView) findViewById(R.id.RefreshListVuew);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,dataa);
        RefreshListview.setAdapter(adapter);
        RefreshListview.isOpenMoreLoading(false);//关闭上啦加载更多。默认开启
        RefreshListview.setonRefreshListener(new YanRefreshListView.onRefreshListener() {
            @Override
            public void refresh() {
                new  Handler(){


                    @Override
                    public void handleMessage(Message msg) {
                        RefreshListview.setOnRefreshComplete();
                    }
                }.sendEmptyMessageDelayed(0,3000);

            }

            @Override
            public void loadingMore() {
                new  Handler(){


                    @Override
                    public void handleMessage(Message msg) {
                        RefreshListview.setOnRefreshComplete();
                    }
                }.sendEmptyMessageDelayed(0,3000);
            }
        });

    }
}
