package me.fangx.zhihu.ui.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import me.fangx.common.util.eventbus.EventCenter;
import me.fangx.common.widget.loading.ProgressView;
import me.fangx.haorefresh.HaoRecyclerView;
import me.fangx.haorefresh.LoadMoreListener;
import me.fangx.zhihu.R;
import me.fangx.zhihu.adapter.HomeListAdapter;
import me.fangx.zhihu.modle.bean.ArticleListBean;
import me.fangx.zhihu.modle.entity.ArticleListEntity;
import me.fangx.zhihu.presenter.HomePresenter;
import me.fangx.zhihu.utils.BaseUtil;
import me.fangx.zhihu.view.HomeListView;

/**
 * Created by fangxiao on 15/12/19.
 */
public class HomeListFragment extends BaseFragment implements HomeListView {

    @Bind(R.id.swiperefresh)
    SwipeRefreshLayout swiperefresh;
    @Bind(R.id.hao_recycleview)
    HaoRecyclerView hao_recycleview;

    private HomePresenter homePresenter;
    private HomeListAdapter homeListAdapter;
    private ArrayList<ArticleListBean> listData = new ArrayList<>();
    private int page = 1;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.home_list_layout;
    }


    @Override
    protected void initViewsAndEvents() {

        homeListAdapter = new HomeListAdapter(mContext, listData);
        hao_recycleview.setAdapter(homeListAdapter);

        swiperefresh.setColorSchemeResources(R.color.textBlueDark, R.color.textBlueDark, R.color.textBlueDark,
                R.color.textBlueDark);

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                homePresenter.loadList(page);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        hao_recycleview.setLayoutManager(layoutManager);

        //设置自定义加载中和到底了效果
        ProgressView progressView = new ProgressView(mContext);
        progressView.setIndicatorId(ProgressView.BallPulse);
        progressView.setIndicatorColor(0xff69b3e0);
        hao_recycleview.setFootLoadingView(progressView);

        TextView textView = new TextView(mContext);
        textView.setText("已经到底啦~");
        hao_recycleview.setFootEndView(textView);

        hao_recycleview.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void onLoadMore() {
                page++;
                homePresenter.loadList(page);
            }
        });


        homePresenter = new HomePresenter(mContext);
        homePresenter.attachView(this);
        //初次加载
        page = 1;
        homePresenter.loadList(page);

    }


    @Override
    public void refresh(ArticleListEntity data) {
        //注意此处
        hao_recycleview.refreshComplete();
        hao_recycleview.loadMoreComplete();
        swiperefresh.setRefreshing(false);
        listData.clear();
        listData.addAll(data.stories);
        homeListAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadMore(ArticleListEntity data) {

        if (BaseUtil.isEmpty(data.stories)) {
            hao_recycleview.loadMoreEnd();
        } else {
            listData.addAll(data.stories);
            homeListAdapter.notifyDataSetChanged();
            hao_recycleview.loadMoreComplete();
        }

    }


    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }


    @Override
    protected void onEventComming(EventCenter eventCenter) {

    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

}
