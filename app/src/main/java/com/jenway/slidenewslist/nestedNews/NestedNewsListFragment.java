package com.jenway.slidenewslist.nestedNews;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jenway.slidenewslist.MyApplication;
import com.jenway.slidenewslist.R;
import com.jenway.slidenewslist.base.BaseFragment;
import com.jenway.slidenewslist.dagger.DaggerNestedListInfoComponent;
import com.jenway.slidenewslist.dagger.NestedListInfoModule;
import com.jenway.slidenewslist.model.BaseEntity;
import com.jenway.slidenewslist.nestedNews.adapter.NestedListAdapter;
import com.jenway.slidenewslist.nestedNews.mvp.NestedNewsListContract;
import com.jenway.slidenewslist.nestedNews.mvp.NestedNewsListPresenterImp;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NestedNewsListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NestedNewsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NestedNewsListFragment extends BaseFragment<NestedNewsListPresenterImp> implements NestedNewsListContract.View {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private int lastPosition = 0;//RecyclerView last item positon
    private int lastOffset = 0;//RecyclerView offset
    private LinearLayoutManager manager;
    private NestedListAdapter mNestedListAdapter;
    private Button testToastButton;
    private Button testDialogButton;
    public NestedNewsListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NestedNewsListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NestedNewsListFragment newInstance(String param1, String param2) {
        NestedNewsListFragment fragment = new NestedNewsListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        //only for test
        if (MyApplication.DEBUG) {
            testToastButton = view.findViewById(R.id.test_toast_button);
            testToastButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showToast("test toast");
                }
            });
            testDialogButton = view.findViewById(R.id.test_dialog_button);
            testDialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.getTag().toString().equals("show")) {
                        view.setTag("hide");
                        mActivity.showDialog("test dialog");
                    } else {
                        view.setTag("show");
                        mActivity.dismissDialog();
                    }
                }
            });
        }
        mRecyclerView = view.findViewById(R.id.nested_rv);
        if (savedInstanceState != null && savedInstanceState.getStringArrayList("data") != null) {//to get status for mobile Portrait or Landscape
            mPresenter.setData(savedInstanceState.<BaseEntity>getParcelableArrayList("data"));
            setRecyclerView(mPresenter.getData());
            lastPosition = savedInstanceState.getInt("LAST_POSITION");
            lastOffset = savedInstanceState.getInt("LAST_OFFSET");
            setTheRecyclerViewPosition();
        } else {
            mPresenter.loadNewsListData();
        }
    }

    @Override
    public void setRecyclerView(final ArrayList<BaseEntity> data) {
        mNestedListAdapter = new NestedListAdapter(data, mActivity);
        manager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mNestedListAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        mActivity.getMenuInflater().inflate(R.menu.mymenu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.myrefreshbutton) {
            mPresenter.loadNewsListData();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_nested_news_list;
    }

    @Override
    protected void initInjector() {
        DaggerNestedListInfoComponent
                .builder()
                .appComponent(getAppComponent()).
                nestedListInfoModule(new NestedListInfoModule(this))
                .build()
                .inject(this);
    }


    //to save the status for mobile Portrait or Landscape
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("data", mPresenter.getData());
        outState.putInt("LAST_POSITION", lastPosition);
        outState.putInt("LAST_OFFSET", lastOffset);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (null != manager) {
            View topView = manager.getChildAt(0);
            lastOffset = topView.getTop();
            lastPosition = manager.getPosition(topView);
        }
        //Prevent memory leaks
        mPresenter.unSubscribe();
    }

    @Override
    public void showLoadingDialog(String msg) {
        mActivity.showDialog(msg);
    }

    @Override
    public void dismissLoadingDialog() {
        mActivity.dismissDialog();
    }

    @Override
    public void showToast(String message) {
        mActivity.showToast(message);
    }


    @Override
    public void setTheRecyclerViewPosition() {
        if (null != manager) {
            manager.scrollToPositionWithOffset(lastPosition, lastOffset);
        }
    }

    @Override
    public void onLoadInfoSuccess() {
        dismissLoadingDialog();
    }

    @Override
    public void onLoadInfoFail(String errorMsg) {
        dismissLoadingDialog();
        Toast.makeText(mActivity, errorMsg, Toast.LENGTH_SHORT).show();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
