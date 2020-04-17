package com.example.stayathome.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.stayathome.R;
import com.example.stayathome.auth.LoginActivity;
import com.example.stayathome.databinding.FragmentHomeBinding;
import com.example.stayathome.models.GetRequestResponse;
import com.example.stayathome.models.Station;
import com.example.stayathome.models.TimeModel;
import com.example.stayathome.ui.request.RequestActivity;
import com.example.stayathome.utils.UserManager;
import com.example.stayathome.utils.ViewDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class HomeFragment extends Fragment implements HomeViewModel {


    private static final int CREATE_REQUEST = 1111;
    private FragmentHomeBinding fragmentHomeBinding;
    private RequestListAdapter adapter;
    private ViewDialog viewDialog;
    private HomePresenter presenter;
    private List<GetRequestResponse.RequestData> requestDataList = new ArrayList<>();
    private List<TimeModel> timeModelList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        fragmentHomeBinding.setHandler(this);
        fragmentHomeBinding.requestRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fragmentHomeBinding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (presenter != null) {
                    fragmentHomeBinding.swipeRefresh.setRefreshing(true);
                    presenter.getAllRequest();
                }
            }
        });
        getTimeList();
        return fragmentHomeBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewDialog = new ViewDialog(requireActivity());
        presenter = new HomePresenter();
        presenter.setView(this);
        viewDialog.showDialog();
        presenter.getAllRequest();
    }

    @SuppressLint("StaticFieldLeak")
    private void getTimeList() {
        new AsyncTask<Object, Object, String>() {
            @Override
            protected String doInBackground(Object... objects) {
                try {
                    StringBuilder builder = new StringBuilder();
                    InputStream json = requireActivity().getAssets().open("time.json");
                    BufferedReader in =
                            new BufferedReader(new InputStreamReader(json, "UTF-8"));
                    String str;

                    while ((str = in.readLine()) != null) {
                        builder.append(str);
                    }
                    in.close();
                    JSONArray array = new JSONArray(builder.toString());

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.optJSONObject(i);
                        TimeModel model = new TimeModel();
                        model.key = object.getInt("key");
                        model.value = object.getString("value");
                        timeModelList.add(model);
                    }
                    if (fragmentHomeBinding != null) {
                        adapter = new RequestListAdapter(requireActivity(), requestDataList, timeModelList);
                        fragmentHomeBinding.requestRecyclerView.setAdapter(adapter);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    @Override
    public void clickOnCreateRequest() {
        List<Station> stations = UserManager.getInstance().getUserPoliceStations();
        if (stations != null && stations.size() > 0) {
            startActivityForResult(new Intent(requireActivity(), RequestActivity.class), CREATE_REQUEST);
        } else {
            final AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).create();
            alertDialog.setTitle(getString(R.string.atert));
            alertDialog.setMessage(getString(R.string.alert_body));
            alertDialog.setCancelable(false);
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        }
    }

    @Override
    public void setHomeView(Response<GetRequestResponse> response) {
        viewDialog.hideDialog();
        fragmentHomeBinding.swipeRefresh.setRefreshing(false);
        if (response.code() == 401) {
            UserManager.getInstance().logOutUser();
            Intent intentMain = new Intent(requireActivity(), LoginActivity.class);
            intentMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intentMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intentMain);
            requireActivity().finish();
            return;
        }
        if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
            requestDataList.clear();
            fragmentHomeBinding.setHasRecords(response.body().getData().size() > 0);
            fragmentHomeBinding.executePendingBindings();
            if (response.body().getData().size() > 0) {
                requestDataList.addAll(response.body().getData());
            }
            adapter.notifyDataSetChanged();
        } else {
            //TODO:
        }
    }

    @Override
    public void onError(String error) {
        viewDialog.hideDialog();
        fragmentHomeBinding.swipeRefresh.setRefreshing(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_REQUEST && resultCode == Activity.RESULT_OK) {
            presenter.getAllRequest();
        }
    }
}
