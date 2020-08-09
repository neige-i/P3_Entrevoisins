package com.openclassrooms.entrevoisins.ui.neighbour_list;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ListNeighbourPagerAdapter extends FragmentStateAdapter {

    public ListNeighbourPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return NeighbourFragment.newInstance(position);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}