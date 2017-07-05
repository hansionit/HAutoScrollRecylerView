package com.hansion.hautoscrollrecyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements HRecyclerViewAdapter.OnItemClickLitener {


    private final int[] mIds = {R.drawable.aaaa, R.drawable.bbbb, R.drawable.cccc, R.drawable.dddd,
            R.drawable.eeee, R.drawable.ffff, R.drawable.gggg, R.drawable.hhhh,
            R.drawable.iiii, R.drawable.jjjj, R.drawable.aaaa, R.drawable.bbbb, R.drawable.cccc, R.drawable.dddd,
            R.drawable.eeee, R.drawable.ffff, R.drawable.gggg, R.drawable.hhhh,
            R.drawable.iiii, R.drawable.jjjj};

    private HAutoScrollRecylerView mHRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHRecyclerView = (HAutoScrollRecylerView) findViewById(R.id.mHRecyclerView);

        HRecyclerViewAdapter adapter = new HRecyclerViewAdapter(this, mHRecyclerView, mIds);
        adapter.setOnItemClickLitener(this);
        mHRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.e("MainActivity", "-- 点击了 --" + position);
    }

}
