# YanRefreshListView
Listview的下拉刷新和上啦加载更多，以及上啦的开关


已经做android很久了，前几天感觉到现在还没自己写过ListView的下拉刷新和上啦加载更多
于是就看了一下网上的某个视频（注明：谭州学院kate，不注明是不是就侵权了。。），但是最后做完后发现其Listview的下拉刷新和上啦加载更多存在很多的问题：






1、如果项目需求不需要 上啦加载更多的时候没有关闭掉上啦的功能。


2、上啦加载更多的动画有点僵硬。

所以我自己动手写了这个小demo   希望可以帮助到诸位，如果发现有什么bug或者问题欢迎咨询和提议。。





新浪微博@Wang丶Yan


![image](https://github.com/yanshao/YanRefreshListView/blob/master/效果图.gif)


  *  Activity中的使用：
  

  RefreshListview.isOpenMoreLoading(false);//关闭上啦加载更多。默认开启
  
  //下拉和上啦的监听
  



RefreshListview.setonRefreshListener(
          new YanRefreshListView.onRefreshListener() {

            @Override
            public void refresh() {
                new  Handler(){


                    @Override
                    public void handleMessage(Message msg) {
                        RefreshListview.setOnRefreshComplete();
                    }
                }.sendEmptyMessageDelayed(0,3000);

            }

            @Override
            public void loadingMore() {
                new  Handler(){


                    @Override
                    public void handleMessage(Message msg) {
                        RefreshListview.setOnRefreshComplete();
                    }
                }.sendEmptyMessageDelayed(0,3000);
            }
        });

