package com.example.stayathome.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.stayathome.auth.LoginActivity;
import com.example.stayathome.databinding.FragmentHomeBinding;
import com.example.stayathome.models.GetRequestResponse;
import com.example.stayathome.models.TimeModel;
import com.example.stayathome.ui.request.RequestActivity;
import com.example.stayathome.utils.UserManager;
import com.example.stayathome.utils.ViewDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class HomeFragment extends Fragment implements HomeViewModel {


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
        String id = UserManager.getInstance().getUserPoliceStationId();
        if (id != null && !TextUtils.isEmpty(id))
            startActivity(new Intent(requireActivity(), RequestActivity.class));
        else {
            //TODO:Alert
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
}
