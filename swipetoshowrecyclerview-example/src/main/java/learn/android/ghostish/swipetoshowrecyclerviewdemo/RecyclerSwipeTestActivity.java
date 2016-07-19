package learn.android.ghostish.swipetoshowrecyclerviewdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import adapter.SwipeMenuItemImpl;
import learn.android.ghostish.swipetoshowrecyclerview.SwipeToShowRecyclerView;

/**
 * Created by Kangel on 2016/6/29.
 */

public class RecyclerSwipeTestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        final SwipeToShowRecyclerView rv = (SwipeToShowRecyclerView) findViewById(R.id.recycler_view);
        List<SwipeMenuItemImpl.Bean> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add(new SwipeMenuItemImpl.Bean(String.valueOf(i), true));
        }
        SwipeMenuItemImpl adapter = new SwipeMenuItemImpl(this, rv, data);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);
    }
}
