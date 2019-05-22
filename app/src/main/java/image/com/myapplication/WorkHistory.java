/*
package image.com.myapplication;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.doublepakka.doublepakkauser.R;
import com.doublepakka.doublepakkauser.adapter.WorkHistoryAdapter;
import com.doublepakka.doublepakkauser.model.WorkHistoryModel;
import com.doublepakka.doublepakkauser.utils.Config;
import com.doublepakka.doublepakkauser.utils.EndlessRecyclerOnScrollListener;
import com.doublepakka.doublepakkauser.utils.HidingScrollListener;
import com.doublepakka.doublepakkauser.utils.VolleySingleton;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WorkHistory extends Fragment {


    private RecyclerView workHistoryRecyclerView;
    private List<WorkHistoryModel> workHistoryModelList;
    private static final String TAG = "CompleteFragment";
    private ShimmerFrameLayout mShimmerViewContainer;
    private ProgressBar loadProgressBar;
    private RelativeLayout relativeLayout;
    private RelativeLayout.LayoutParams lp;
    LinearLayout linearLayoutProgress;
    private WorkHistoryAdapter workHistoryAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_history, container, false);

        workHistoryRecyclerView = view.findViewById(R.id.workHistoryRecycler);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.rel);
        loadProgressBar = view.findViewById(R.id.load_progress);
        linearLayoutProgress = view.findViewById(R.id.ll_progress);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            Drawable drawableProgress = DrawableCompat.wrap(loadProgressBar.getIndeterminateDrawable());
            DrawableCompat.setTint(drawableProgress, ContextCompat.getColor(getContext(), R.color.colorAccent));
            loadProgressBar.setIndeterminateDrawable(DrawableCompat.unwrap(drawableProgress));

        }


        mShimmerViewContainer = view.findViewById(R.id.shimmer_view);
        workHistoryModelList = new ArrayList<>();

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        workHistoryRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        workHistoryAdapter = new WorkHistoryAdapter(getActivity(), workHistoryModelList);
        workHistoryRecyclerView.setAdapter(workHistoryAdapter);

        workHistoryRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                uiUpdate(current_page);
            }
        });


        uiUpdate(1);

        workHistoryRecyclerView.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideViews();
            }

            @Override
            public void onShow() {
                showViews();
            }
        });

        return view;
    }


    private void uiUpdate(final int pagenumber) {

        if (pagenumber == 1) {
            linearLayoutProgress.setVisibility(View.GONE);
        } else {

            linearLayoutProgress.setVisibility(View.VISIBLE);
        }

        lp = (RelativeLayout.LayoutParams) workHistoryRecyclerView.getLayoutParams();
        lp.setMargins(0, 0, 0, 70);
        workHistoryRecyclerView.setLayoutParams(lp);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.JOB_STATUS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("WorkFragment", "onResponse: " + response);
                        try {
                            JSONObject rootJsonObject = new JSONObject(response);
                            String status = rootJsonObject.getString("status");
                            linearLayoutProgress.setVisibility(View.GONE);
                            lp.setMargins(0, 0, 0, 0);

                            if (status.equalsIgnoreCase("1")) {

                                JSONArray jsonArrayData = rootJsonObject.getJSONArray("data");


                                for (int i = 0; i < jsonArrayData.length(); i++) {

                                    JSONObject jsonObjectData = jsonArrayData.getJSONObject(i);

                                    workHistoryModelList.add(new WorkHistoryModel(
                                            jsonObjectData.optString("job_id"),
                                            jsonObjectData.optString("subservice_id"),
                                            jsonObjectData.optString("client_id"),
                                            jsonObjectData.optString("client_name"),
                                            jsonObjectData.optString("provider_id"),
                                            jsonObjectData.optString("contact_no"),
                                            jsonObjectData.optString("job_title"),
                                            jsonObjectData.optString("Job_description"),
                                            jsonObjectData.optString("job_image"),
                                            jsonObjectData.optString("address"),
                                            jsonObjectData.optString("latitude"),
                                            jsonObjectData.optString("longitude"),
                                            jsonObjectData.optString("created_date"),
                                            jsonObjectData.optString("expiry_time"),
                                            jsonObjectData.optString("job_status")
                                    ));

                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        workHistoryAdapter.notifyItemRangeInserted(pagenumber * 10, 10);

                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("status", "Completed");
                map.put("providerid", "77");
                map.put("pageno", pagenumber + "");
                Log.e("page numer", pagenumber + "");

                return map;
            }
        };
        RetryPolicy retryPolicy = new DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }


    private void hideViews() {

    }

    private void showViews() {
    }

}
*/
