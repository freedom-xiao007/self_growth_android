package com.example.selfgrowth.ui.task;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.selfgrowth.R;
import com.example.selfgrowth.service.foregroud.TaskService;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

public class TaskFragment extends Fragment {

    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
    CollectionAdapter collectionAdapter;
    ViewPager2 viewPager;
    private final TaskService taskService = TaskService.getInstance();

    private final int activeColor = Color.parseColor("#FFFFFF");
    private final int normalColor = Color.parseColor("#666666");
    private final int normalSize = 20;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        List<String> groups = taskService.getAllGroup();
        if (groups.isEmpty()) {
            groups.add("无任务，请添加");
        }

        collectionAdapter = new CollectionAdapter(this, groups);
        viewPager = view.findViewById(R.id.pager);
        viewPager.setAdapter(collectionAdapter);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            //这里可以自定义TabView
            TextView tabView = new TextView(requireContext());

            int[][] states = new int[2][];
            states[0] = new int[]{android.R.attr.state_selected};
            states[1] = new int[]{};

            final String groupName = groups.get(position);
            int[] colors = new int[]{activeColor, normalColor};
            ColorStateList colorStateList = new ColorStateList(states, colors);
            tabView.setText(groupName);
            tabView.setTextSize(normalSize);
            tabView.setTextColor(colorStateList);

            tab.setCustomView(tabView);
        });
        //要执行这一句才是真正将两者绑定起来
        mediator.attach();
    }

    static class CollectionAdapter extends FragmentStateAdapter {

        private final List<String> groups;

        public CollectionAdapter(Fragment fragment, List<String> groups) {
            super(fragment);
            this.groups = groups;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            // Return a NEW fragment instance in createFragment(int)
            Fragment fragment = new TaskListFragment(groups.get(position));
            Bundle args = new Bundle();
            // Our object is just an integer :-P
            args.putInt(TaskListFragment.ARG_OBJECT, position + 1);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getItemCount() {
            return groups.size();
        }
    }
}

