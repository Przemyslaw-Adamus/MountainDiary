package com.example.mountaindiary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Fragment_2 extends Fragment {
//#####################################################################################################################################
    private RecyclerView recyclerView;
    private ItemPeaksAdapter itemAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private GlobalData gd;
    private View view;
//#####################################################################################################################################
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//-------------------------------------------------------------------------------------------------------------------------------------
        view = inflater.inflate(R.layout.fragment_2_layout,container,false);
        gd = new GlobalData();
//-------------------------------------------------------------------------------------------------------------------------------------
        //initPeakList();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(recyclerView.getContext());
        itemAdapter = new ItemPeaksAdapter(gd.getGainsList());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(itemAdapter);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorAccent);

        reloadRecyclerView();
//-------------------------------------------------------------------------------------------------------------------------------------
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadRecyclerView();
            }
        });

        itemAdapter.setOnItemClickListener(new ItemPeaksAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                getActivity().finish();
                Intent memoryCard = new Intent(getContext(),MemoryCardActivity.class);
                memoryCard.putExtra("index", position);
                startActivity(memoryCard);
            }
        });

        itemAdapter.setOnItemLongClickListener(new ItemPeaksAdapter.OnItemLongClickListener() {

            @Override
            public void onLongClick(final int position) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Czy jesteś pewny?")
                        .setContentText(".. że chcesz usunąć wskazane zdobycie ?")
                        .setConfirmText("Tak")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                gd.deleteGain(position);
                                new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Yeaa...")
                                        .setContentText("Poprawnie usunięto wskazane zdobycie")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                reloadRecyclerView();
                                                sweetAlertDialog.dismissWithAnimation();
                                            }
                                        })
                                        .show();

                            }
                        })
                        .setCancelButton("Nie", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();

            }
        });
//-------------------------------------------------------------------------------------------------------------------------------------
        return view;
    }
//#####################################################################################################################################
    private void reloadRecyclerView() {
        itemAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }
//#####################################################################################################################################
}