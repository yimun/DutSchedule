package com.siwe.dutschedule.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/***
 * 自定义view
 * 
 * @author zhangjia
 * 
 */
public class MyViewGroup extends ViewGroup {
	private Scroller scroller;// 滑动
	private int distance;// 滑动距离

	private View menu_view, content_view;
	private int duration = 500;

	private CloseAnimation closeAnimation;

	public static boolean isMenuOpned = false;// 菜单是否打开

	public MyViewGroup(Context context) {
		super(context, null);
	}

	public void setCloseAnimation(CloseAnimation closeAnimation) {
		this.closeAnimation = closeAnimation;
	}

	public MyViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		scroller = new Scroller(context);
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	/*	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (changed) {
			menu_view = getChildAt(0);// 获取滑动菜单的view
			content_view = getChildAt(1);// 获得主页view

			// 相当于fill_parent
			content_view.measure(0, 0);
			content_view.layout(0, 0, getWidth(), getHeight());
			//	content_view.layout(0, 0,240, 320);
		}
	}
	 */

	//我们只需要在ViewGroup中的onMeasure方法里添加一个对子元素的遍历，并且在
	//onLayout中添加一个布局遍历就实现了简单的布局了。
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		// TODO Auto-generated method stub 
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		/*	int childCount = getChildCount();
		for(int i = 0; i < childCount; i ++){*/

		View v = getChildAt(1);

		v.measure(widthMeasureSpec, heightMeasureSpec);

		//	}

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		System.out.println("调用myViewGroup 的onLayout方法");
		menu_view = getChildAt(0);
		content_view = getChildAt(1);
		int childCount = getChildCount();

		for(int i = 0; i < childCount; i ++){

			View v = getChildAt(i);
			v.layout(l, t, r, b);
		}

		//.layout(int l, int t, int r, int b)

		menu_view.measure(0, distance);
		menu_view.layout(-distance, 0, distance, getHeight());
		content_view.layout(0, 0, getWidth(), getHeight());

		/*if(changed){
			System.out.println("changed");
			menu_view.measure(0, distance);
			menu_view.layout(-distance, 0, 0, getHeight());
			content_view.layout(0, 0, getWidth(), getHeight());
		}*/
	//	invalidate();

	}



	@Override
	public void computeScroll() {
		//		System.out.println("调用myViewGroup 的computeScroll()方法");
		if (scroller.computeScrollOffset()) {
			scrollTo(scroller.getCurrX(), scroller.getCurrY());
			postInvalidate();// 刷新
			if (closeAnimation != null)
				closeAnimation.closeMenuAnimation();
		}
	}

	void showMenu() {	
		isMenuOpned = true;
		scroller.startScroll(0, 0, -distance, 0, duration);

		invalidate();// 刷新
	}

	// 关闭菜单（执行自定义动画）
	void closeMenu(){		
		isMenuOpned = false;
		scroller.startScroll(getScrollX(), 0, distance, 0, duration);
		invalidate();// 刷新
	}

	// 关闭菜单（执行自定义动画）
	void closeMenu_1() {
		isMenuOpned = false;
		scroller.startScroll(getScrollX(), 0, distance - getWidth(), 0,
				duration);
		invalidate();// 刷新
	}

	// 关闭菜单（执行自定义动画）
	void closeMenu_2() {
		isMenuOpned = false;
		scroller.startScroll(getScrollX(), 0, getWidth(), 0, duration);
		invalidate();// 刷新
	}

	/***
	 * Menu startScroll(startX, startY, dx, dy)
	 * 
	 * dx=e1的减去e2的x,所以右移为负，左移动为正 dx为移动的距离，如果为正，则标识向左移动|dx|，如果为负，则标识向右移动|dx|
	 */
	void slidingMenu() {

		// 没有超过半屏
		if (getScrollX() > -getWidth() / 2) {
			scroller.startScroll(getScrollX(), 0, -getScrollX(), 0, duration);
			isMenuOpned = false;
		}
		// 超过半屏
		else if (getScrollX() <= -getWidth() / 2) {
			scroller.startScroll(getScrollX(), 0, -(distance + getScrollX()),
					0, duration);
			isMenuOpned = true;
		}

		invalidate();// 刷新
		Log.v("jj", "getScrollX()=" + getScrollX());
	}

}

abstract class CloseAnimation {
	// 点击list item 关闭menu动画
	public void closeMenuAnimation() {

	};
}
