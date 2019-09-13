package com.example.mountaindiary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fragment_1 extends Fragment {
//#####################################################################################################################################
    private RecyclerView recyclerView;
    private ItemMountainRangeAdapter itemAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private GlobalData gd;
    private View view;
    private Button buttonAddPeak;
//#####################################################################################################################################
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//-------------------------------------------------------------------------------------------------------------------------------------
        view = inflater.inflate(R.layout.fragment_1_layout,container,false);
        gd = new GlobalData();

//-------------------------------------------------------------------------------------------------------------------------------------
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorAccent);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(recyclerView.getContext());
        itemAdapter = new ItemMountainRangeAdapter(gd.getMountainRangeList());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(itemAdapter);
        buttonAddPeak = view.findViewById(R.id.buttonAddPeak);

//-------------------------------------------------------------------------------------------------------------------------------------
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadRecyclerView();
            }
        });

        itemAdapter.setOnItemClickListener(new ItemMountainRangeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int positionMenu) {

                final int[] imageIdArr = createOptionsImage(positionMenu);
                final Peak[] tabPeak = createOptions(positionMenu);
                //final String[] listItemArr = createOptions(positionMenu);

                final String CUSTOM_ADAPTER_IMAGE = "image";
                final String CUSTOM_ADAPTER_TEXT = "text";

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                //builder.setIcon();
                builder.setTitle(gd.getMountainRangeList().get(positionMenu).getTitle());
                builder.setNeutralButton(getString(R.string.returnn), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                List<Map<String, Object>> dialogItemList = new ArrayList<Map<String, Object>>();
                //int listItemLen = listItemArr.length;
                for(int i=0;i<tabPeak.length;i++)
                {
                    Map<String, Object> itemMap = new HashMap<String, Object>();
                    itemMap.put(CUSTOM_ADAPTER_IMAGE, imageIdArr[i]);
                    itemMap.put(CUSTOM_ADAPTER_TEXT, tabPeak[i].getTitle() + " - " + tabPeak[i].getHeight() + " m.n.p.m");

                    dialogItemList.add(itemMap);
                }
                SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(),dialogItemList,
                        R.layout.activity_alert_dialog_simple_adapter_row,
                        new String[]{CUSTOM_ADAPTER_IMAGE, CUSTOM_ADAPTER_TEXT},
                        new int[]{R.id.alertDialogItemImageView,R.id.alertDialogItemTextView});

                // Set the data adapter.
                builder.setAdapter(simpleAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int itemIndex) {
                        //Toast.makeText(getContext(),tabPeak[itemIndex].getTitle(),Toast.LENGTH_LONG).show();
                        getActivity().finish();
                        Intent intent = new Intent(getActivity(), PeakActivity.class);
                        intent.putExtra("id",gd.getPeaksList().indexOf(tabPeak[itemIndex]));
                        startActivity(intent);
                    }
                });

                builder.setCancelable(true);
                builder.create();
                builder.show();
            }
        });


        buttonAddPeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                Intent intent = new Intent(getActivity(), AddPeakActivity.class);
                startActivity(intent);
            }
        });
//-------------------------------------------------------------------------------------------------------------------------------------
        reloadRecyclerView();
        return view;
    }
//#####################################################################################################################################
    private void reloadRecyclerView() {
        itemAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    private Peak[] createOptions(int positionMenu) {
        int x=0;
        for(Peak peak : gd.getPeaksList()) {
            if (peak.getRangeId() == positionMenu) {
                x++;
            }
        }
        Peak[] peaks = new Peak[x];
        int i=0;
        for(Peak peak : gd.getPeaksList()){
            if(peak.getRangeId() == positionMenu){
                peaks[i] = peak;
                i++;
            }

        }
        return peaks;
    }

    private int[] createOptionsImage(int positionMenu) {

        int x=0;
        for(Peak peak : gd.getPeaksList()) {
            if (peak.getRangeId() == positionMenu) {
                x++;
            }
        }
        int[] imageIdArr = new int[x];
        int i=0;
        for(Peak peak : gd.getPeaksList()){
            if(peak.getRangeId() == positionMenu){
                if(peak.getGainId()>=0){
                    imageIdArr[i] = R.drawable.looted;
                }else{
                    imageIdArr[i] = R.drawable.not_looted;
                }
                i++;
            }
        }
        return  imageIdArr;
    }

    private boolean isLooted(int id) {
        for(Gain gain : gd.getGainsList()){
            if(gain.getPeakId() == id){
                return  true;
            }
        }
        return false;
    }
//#####################################################################################################################################
}