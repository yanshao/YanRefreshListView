package com.yan.listview;

import android.content.Context;
import android.content.IntentFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 作者：新浪微博@Wang丶Yan
 * QQ：512454068
 */
public class YanRefreshListView extends ListView implements OnScrollListener{
View  HeaderView,FooterView;
    int  StarY;
    int headerHerght,footerHeight;
    public static  final  int STAR_PULL_REFRESH=0;//默认啦啦刷新
    public static  final  int STAR_RELESE_REFRESH=1;//松开刷新
    public static  final  int STAR_REFRESING=2;//刷新中...
    public static  final  int STAR_DOWN_REFRESH=0;//默认上啦加载更多
    public static  final  int STAR_DOWN_RELESE_REFRESH=1;//松开加载
    public static  final  int STAR_LOADING=2;//加载中...
    private  int mCurrState=STAR_PULL_REFRESH;//记录当前下拉的状态
    private  int mCurrState2=STAR_DOWN_REFRESH;//记录当前上啦的状态

TextView headerText,footerText;
ImageView pullImage,footerImage;
   ProgressBar progress,footerprogress;
boolean  ismath=false;//判断是否占满一屏


    boolean isCloseMoreLod=true;//是否关闭上啦加载更多
    RotateAnimation rotateUp,rotateDown;
    public YanRefreshListView(Context context) {
        super(context);
        initHeader();
        initFooter();
    }

    public YanRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeader();
        initFooter();
    }

    public YanRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeader();
        initFooter();
    }




   private void  initHeader(){
       HeaderView= View.inflate(getContext(),R.layout.headeview,null);
       this.addHeaderView(HeaderView);
       headerText= (TextView) HeaderView.findViewById(R.id.headerText);
       pullImage= (ImageView) HeaderView.findViewById(R.id.pullImage);
       progress= (ProgressBar) HeaderView.findViewById(R.id.progress);

       HeaderView.measure(0,0);
       headerHerght=HeaderView.getMeasuredHeight();
       initAnim();
       HeaderView.setPadding(0,-(headerHerght),0,0);
    }
    private void  initFooter(){
        FooterView= View.inflate(getContext(),R.layout.footerview,null);
        this.addFooterView(FooterView);

        footerText= (TextView) FooterView.findViewById(R.id.headerText);
        footerImage= (ImageView) FooterView.findViewById(R.id.pullImage);
        footerprogress= (ProgressBar) FooterView.findViewById(R.id.progress);
        FooterView.measure(0,0);
        footerHeight=FooterView.getMeasuredHeight();

        FooterView.setPadding(0,-footerHeight,0,0);
        this.setOnScrollListener(this);
    }

    /**
     * 是否开启加载更多
     *
     */
    public void  isOpenMoreLoading(boolean isCloseMoreLoding){

            isCloseMoreLod=isCloseMoreLoding;

    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.e("000000", "ACTION_DOWN=="+ev.getRawY());
              StarY=(int)ev.getRawY();

              break;
            case MotionEvent.ACTION_UP:
                Log.e("000000", "ACTION_UP=="+ev.getRawY());
                StarY=-1;

                if (isLoadingMore){
                    if(mCurrState2==STAR_RELESE_REFRESH){
                        mCurrState2=STAR_LOADING;
                        getRefreshDownstate();
                        FooterView.setPadding(0,0,0,0);
                    }else if (mCurrState2==STAR_DOWN_REFRESH){
                        FooterView.setPadding(0,0,0,-footerHeight);
                    }
                    isLoadingMore=false;
                }else {
                    if (mCurrState == STAR_RELESE_REFRESH) {
                        mCurrState = STAR_REFRESING;
                        ReFreshState();
                        HeaderView.setPadding(0, 0, 0, 0);
                    } else if (mCurrState == STAR_PULL_REFRESH) {
                        HeaderView.setPadding(0, -headerHerght, 0, 0);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("000000", "ACTION_MOVE=="+ev.getRawY());
                if (isLoadingMore){//上啦加载更多
                    if (StarY == -1) {
                        StarY = (int) ev.getRawY();
                    }
                    int endY = (int) ev.getRawY();
                    int des = Math.abs(endY - StarY);
                    if (des > 10 && this.getLastVisiblePosition() == getCount()-1) {
                        int padding = des - footerHeight;
                        FooterView.setPadding(0, 0, 0, padding);
                        if (padding > 0 && mCurrState2 == STAR_DOWN_REFRESH) {
                            mCurrState2 = STAR_DOWN_RELESE_REFRESH;
                            getRefreshDownstate();
                        } else if (padding < 0 && mCurrState2 != STAR_DOWN_REFRESH) {
                            mCurrState2 = STAR_LOADING;
                            getRefreshDownstate();
                        }
                        // return true;
                    }
                }else {
                    if (StarY == -1) {
                        StarY = (int) ev.getRawY();
                    }
                    int endY = (int) ev.getRawY();
                    int des = endY - StarY;
                    if (des > 10 && this.getFirstVisiblePosition() == 0) {
                        int padding = des - headerHerght;
                        HeaderView.setPadding(0, padding, 0, 0);
                        if (padding > 0 && mCurrState == STAR_PULL_REFRESH) {
                            mCurrState = STAR_RELESE_REFRESH;
                            ReFreshState();
                        } else if (padding < 0 && mCurrState != STAR_PULL_REFRESH) {
                            mCurrState = STAR_PULL_REFRESH;
                            ReFreshState();
                        }
                        // return true;
                    }
                }
                break;

        }
        return super.onTouchEvent(ev);
    }

    /**
     * 刷新头部的状态
     */
    private void  ReFreshState(){
        switch (mCurrState){
            case   STAR_PULL_REFRESH:
                headerText.setText("下拉刷新");
                pullImage.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
               pullImage.startAnimation(rotateUp);
                break;
            case  STAR_REFRESING:
                headerText.setText("正在刷新");
                pullImage.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
               pullImage.clearAnimation();
                if (onRefreshListener!=null){
                    onRefreshListener.refresh();
                }
                break;
            case  STAR_RELESE_REFRESH:
                headerText.setText("松开刷新");
                pullImage.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                pullImage.startAnimation(rotateDown);

                break;


        }
    }

    /**
     * 刷新底部
     */
   private void  getRefreshDownstate(){
        switch (mCurrState2){
            case   STAR_DOWN_REFRESH:
                headerText.setText("上啦加载更多");
                pullImage.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                pullImage.startAnimation(rotateUp);
                break;
            case  STAR_LOADING:
                footerText.setText("正在加载中...");
                footerImage.setVisibility(View.GONE);
                footerprogress.setVisibility(View.VISIBLE);
                footerImage.clearAnimation();
                if (onRefreshListener!=null){
                    onRefreshListener.loadingMore();
                }
                break;

            case  STAR_DOWN_RELESE_REFRESH:
                footerText.setText("松开加载更多...");
                footerImage.setVisibility(View.VISIBLE);
                footerprogress.setVisibility(View.GONE);
                footerImage.startAnimation(rotateDown);

                break;

        }
    }

   private void initAnim(){
       //箭头朝上
       rotateUp=new RotateAnimation(0,-180,RotateAnimation.RELATIVE_TO_SELF,
               0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
       rotateUp.setDuration(500);
       rotateUp.setFillAfter(true);
       //箭头朝xia
      rotateDown=new RotateAnimation(-180,0,RotateAnimation.RELATIVE_TO_SELF,
               0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
       rotateDown.setDuration(500);
       rotateDown.setFillAfter(true);

    }

    /**
     * 刷新or加载完成
     */
    public void  setOnRefreshComplete(){

        if (isLoadingMore){
            mCurrState2=STAR_DOWN_REFRESH;
            footerText.setText("上啦加载更多");
            footerImage.setVisibility(View.GONE);
            footerImage.setVisibility(View.GONE);
            FooterView.setPadding(0,0,0,-footerHeight);
            isLoadingMore=false;
        }else {
            mCurrState = STAR_PULL_REFRESH;
            headerText.setText("下啦刷新");
            pullImage.setVisibility(View.GONE);
            progress.setVisibility(View.GONE);
            HeaderView.setPadding(0, -headerHerght, 0, 0);
        }
    }




    private onRefreshListener onRefreshListener;

    public void setonRefreshListener(onRefreshListener listener){
        this.onRefreshListener=listener;
    }


    public interface onRefreshListener{
        void refresh();
        void  loadingMore();
    }



    boolean isLoadingMore=false;
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {


        if (scrollState==SCROLL_STATE_IDLE||scrollState==SCROLL_STATE_FLING){
            if (getLastVisiblePosition()==getCount()-1&&!isLoadingMore){
                /*Log.e("yyyy","333333333");
                FooterView.setPadding(0,0,0,0);
                setSelection(getCount()-1);*/

                Log.e("yyyy","gaodu="+this.getHeight());
                if (ismath){
                    isLoadingMore=true;
                }else{
                    isLoadingMore=false;
                }

                Log.e("iii","isLoadingMore="+isLoadingMore);
                /*if (onRefreshListener!=null){
                    onRefreshListener.loadingMore();
                }*/
            }


        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.e("yyyy","gaodu222="+this.getHeight());

        //有更多
        if(totalItemCount > visibleItemCount){
            if (isCloseMoreLod){
                ismath=true;
            }else{
                ismath=false;
            }

            //不满一屏
        }else{
            ismath=false;
        }
    }
}
