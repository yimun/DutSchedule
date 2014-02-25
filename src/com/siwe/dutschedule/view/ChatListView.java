package com.siwe.dutschedule.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.siwe.dutschedule.R;

public class ChatListView extends ListView implements OnScrollListener {

	private String TAG = "ChatListView";
	private LayoutInflater mInflater;
	private LinearLayout mHeadRootView;
	private int mHeadViewHeight;
	private int mHeadViewWidth;
	private OnRefreshListener refreshListener;
	private boolean isRefresh = false;

	public ChatListView(Context context) {
		super(context);
		init(context);
		// TODO Auto-generated constructor stub
	}

	public ChatListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		// TODO Auto-generated constructor stub
	}

	public ChatListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
		// TODO Auto-generated constructor stub
	}

	private void init(Context context) {
		mInflater = LayoutInflater.from(context);
		addHeadView();
		setOnScrollListener(this);

	}

	private void addHeadView() {
		mHeadRootView = (LinearLayout) mInflater.inflate(
				R.layout.head_chat_list, null);
		measureView(mHeadRootView);
		mHeadViewHeight = mHeadRootView.getMeasuredHeight();
		mHeadViewWidth = mHeadRootView.getMeasuredWidth();

		mHeadRootView.setPadding(0, -1 * mHeadViewHeight, 0, 0);
		mHeadRootView.invalidate();
		Log.v("size", "width:" + mHeadViewWidth + " height:" + mHeadViewHeight);
		addHeaderView(mHeadRootView, null, false);
		//mHeadRootView.setVisibility(View.GONE);

	}

	/**
	 * 测量HeadView宽高(注意：此方法仅适用于LinearLayout，请读者自己测试验证。)
	 * 
	 * @param pChild
	 * @date 2013-11-20 下午4:12:07
	 * @change JohnWatson
	 * @version 1.0
	 */
	private void measureView(View pChild) {
		if (pChild == null)
			log("child is null");
		ViewGroup.LayoutParams p = pChild.getLayoutParams();
		if (p == null) {
			log("null");
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;

		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		pChild.measure(childWidthSpec, childHeightSpec);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		// 判断是否移动到顶部
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& view.getFirstVisiblePosition() == 0) {
			if(!isRefresh)
				doRefresh();
		}
	}

	public void doRefresh() {
		isRefresh = true;
		mHeadRootView.setPadding(0, 0, 0, 0);
		if (refreshListener != null)
			refreshListener.onRefresh();
	}

	public void endRefresh() {
		if(!isRefresh)
			return;
		isRefresh = false;
		mHeadRootView.setPadding(0, -1 * mHeadViewHeight, 0, 0);
	}

	public void setOnRefreshListener(OnRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
	}

	public interface OnRefreshListener {
		public void onRefresh();
	}

	void log(String mess) {
		Log.i(TAG, mess);
	}
}
