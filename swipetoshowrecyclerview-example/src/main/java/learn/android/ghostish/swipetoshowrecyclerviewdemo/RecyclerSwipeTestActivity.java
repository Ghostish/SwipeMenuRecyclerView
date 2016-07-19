package learn.android.ghostish.swipetoshowrecyclerviewdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import adapter.SwipeItemImpl;
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
        List<SwipeItemImpl.Bean> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add(new SwipeItemImpl.Bean(String.valueOf(i), true));
        }
        SwipeItemImpl adapter = new SwipeItemImpl(this, rv, data, new int[]{Color.parseColor("#FFFF8C40"), Color.parseColor("#FFB7B2B4"), Color.RED}, new String[]{"Top", "Mark as Unread", "Delete"});
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);
    }
}
