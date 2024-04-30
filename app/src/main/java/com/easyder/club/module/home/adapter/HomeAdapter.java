package com.easyder.club.module.home.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;
import com.easyder.club.module.home.vo.AdparamsBean;
import com.easyder.club.module.home.vo.NewsListVo;
import com.easyder.club.utils.OperateUtils;
import com.easyder.club.widget.FullJVideoPlayer;
import com.easyder.club.widget.GlideRoundTransform;
import com.sky.widget.autolayout.utils.AutoUtils;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Author: sky on 2020/11/13 15:22
 * Email: xcode126@126.com
 * Desc:
 */
public class HomeAdapter extends BaseQuickAdapter<NewsListVo.ArticlelistBean, BaseRecyclerHolder> {

    public final static int TYPE_NEWS_ONE = 1002;
    public final static int TYPE_NEWS_MULTI = 1003;
    public final static int TYPE_NEWS_VIDEO = 1004;

    public HomeAdapter() {
        super(R.layout.item_home_one);
    }

    @Override
    public void onViewDetachedFromWindow(BaseRecyclerHolder holder) {
        super.onViewDetachedFromWindow(holder);
        FrameLayout parent = holder.getView(R.id.layout_container);
        if (parent != null) {
            View view = parent.getChildAt(0);
            if (view != null && view instanceof JZVideoPlayer) {
                JZVideoPlayer.releaseAllVideos();
            }
        }
    }

    @Override
    protected int getDefItemViewType(int position) {
        NewsListVo.ArticlelistBean item = getItem(position);
        if (item.articlenewstype.equals("video")) {
            return TYPE_NEWS_VIDEO;
        }
        if (!TextUtils.isEmpty(item.titleimgs) && item.titleimgs.split(";").length > 1) {
            return TYPE_NEWS_MULTI;
        }
        if (!TextUtils.isEmpty(item.titleimgs) && item.titleimgs.split(";").length <= 1) {
            return TYPE_NEWS_ONE;
        }
        return super.getDefItemViewType(position);
    }

    @Override
    protected BaseRecyclerHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NEWS_ONE) {
            return createBaseViewHolder(getItemView(R.layout.item_home_one, parent));
        } else if (viewType == TYPE_NEWS_MULTI) {
            return createBaseViewHolder(getItemView(R.layout.item_home_mutile, parent));
        } else if (viewType == TYPE_NEWS_VIDEO) {
            return createBaseViewHolder(getItemView(R.layout.item_home_video, parent));
        }
        return super.onCreateDefViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerHolder holder, int positions) {
        switch (holder.getItemViewType()) {
            case TYPE_NEWS_ONE:
                convert(holder, mData.get(holder.getLayoutPosition() - getHeaderLayoutCount()));
                break;
            case TYPE_NEWS_MULTI:
                convertMulti(holder, mData.get(holder.getLayoutPosition() - getHeaderLayoutCount()));
                break;
            case TYPE_NEWS_VIDEO:
                convertVideo(holder, mData.get(holder.getLayoutPosition() - getHeaderLayoutCount()));
                break;
            default:
                super.onBindViewHolder(holder, positions);
                break;
        }
    }

    /**
     * convertVideo
     *
     * @param helper
     * @param item
     */
    private void convertVideo(BaseRecyclerHolder helper, NewsListVo.ArticlelistBean item) {
        helper.setText(R.id.tv_title, item.title);
//        helper.setImageManager(mContext, R.id.iv_image,OperateUtils.getFirstImage(item.titleimgs), new GlideRoundTransform(mContext, 10),
//                R.drawable.ic_placeholder_1, R.drawable.ic_placeholder_1);
        helper.setText(R.id.tv_visitor, String.format("%1$s%2$s%3$s %4$s", item.views, mContext.getString(R.string.a_unit_ci), mContext.getString(R.string.a_look), item.releasetime));

        FrameLayout layout_container = helper.getView(R.id.layout_container);
        layout_container.removeAllViews();
        AdparamsBean adparamsBean = JSON.parseObject(item.arparams, AdparamsBean.class);
        addJZVideoItem(helper, layout_container, OperateUtils.getFirstImage(item.titleimgs), adparamsBean.videourl);

//        AdparamsBean adparamsBean = JSON.parseObject(item.arparams, AdparamsBean.class);
//        FullJVideoPlayer player = helper.getView(R.id.mFullJVideoPlayer);
//        player.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AutoUtils.getPercentWidthSize(333)));//设置宽高
//        player.setUp(adparamsBean.videourl, JZVideoPlayer.SCREEN_LAYOUT_LIST, ""); //设置视频地址
//        ImageView thumbImageView = player.thumbImageView;
//        thumbImageView.setScaleType(ImageView.ScaleType.FIT_XY);
//        helper.setImageManager(mContext, player.thumbImageView, OperateUtils.getFirstImage(item.titleimgs), new GlideRoundTransform(mContext, 6),
//                R.drawable.ic_placeholder_2,R.drawable.ic_placeholder_2);//设置视频缩略图
//        player.setPressed(false);
    }

    /**
     * convertMulti
     *
     * @param helper
     * @param item
     */
    private void convertMulti(BaseRecyclerHolder helper, NewsListVo.ArticlelistBean item) {
        helper.setText(R.id.tv_title, item.title);
        helper.setText(R.id.tv_visitor, String.format("%1$s%2$s%3$s %4$s", item.views, mContext.getString(R.string.a_unit_ci), mContext.getString(R.string.a_look),item.releasetime));
        String[] split = item.titleimgs.split(";");
        helper.setImageManager(mContext, R.id.iv_one, split[0], new GlideRoundTransform(mContext, 10), R.drawable.ic_placeholder_1, R.drawable.ic_placeholder_1);
        helper.setImageManager(mContext, R.id.iv_two, split[1], new GlideRoundTransform(mContext, 10), R.drawable.ic_placeholder_1, R.drawable.ic_placeholder_1);
        if (split.length>2){
            helper.setImageManager(mContext, R.id.iv_three, split[2], new GlideRoundTransform(mContext, 10), R.drawable.ic_placeholder_1, R.drawable.ic_placeholder_1);
        }
    }

    @Override
    protected void convert(BaseRecyclerHolder helper, NewsListVo.ArticlelistBean item) {
        helper.setImageManager(mContext, R.id.iv_image, OperateUtils.getFirstImage(item.titleimgs), new GlideRoundTransform(mContext, 10),
                R.drawable.ic_placeholder_1, R.drawable.ic_placeholder_1);
        helper.setText(R.id.tv_title, item.title);
        helper.setText(R.id.tv_visitor, String.format("%1$s%2$s%3$s", item.views, mContext.getString(R.string.a_unit_ci), mContext.getString(R.string.a_look)));
        helper.setText(R.id.tv_time, item.releasetime);
    }


    /**
     * 添加视频Item项
     *
     * @param helper
     * @param container
     * @param img
     * @param video
     */
    private void addJZVideoItem(BaseRecyclerHolder helper, final FrameLayout container, String img, final String video) {
        final JZVideoPlayerStandard player = new FullJVideoPlayer(mContext);
        player.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AutoUtils.getPercentWidthSize(393)));//设置宽高
        player.setUp(video, JZVideoPlayer.SCREEN_LAYOUT_LIST, ""); //设置视频地址
        helper.setImageManager(mContext, player.thumbImageView, img, new GlideRoundTransform(mContext, 6),
                R.drawable.ic_placeholder_2,R.drawable.ic_placeholder_2);//设置视频缩略图
        player.setPressed(false);
        player.thumbImageView.setScaleType(ImageView.ScaleType.FIT_XY);
//        player.widthRatio = 16;//播放比例,可以设置为16:9,4:3
//        player.heightRatio = 9;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            player.setOutlineProvider(new JzViewOutlineProvider(AutoUtils.getPercentWidthSize(10)));
//            player.setClipToOutline(true);
//        }

        container.addView(player);
//        final TextView textView = getAdsText(playTime);
//        if (!TextUtils.isEmpty(playTime)) {
//            container.addView(textView);
//        }
//
//        player.setJzUserAction(new JZUserAction() {
//            @Override
//            public void onEvent(int type, String url, int screen, Object... objects) {
//                switch (type) {
//                    case JZUserAction.ON_CLICK_START_ICON:
//                        if (textView != null) {
//                            textView.setVisibility(View.GONE);
//                        }
//                        break;
//                    case JZUserAction.ON_AUTO_COMPLETE:
////                        if (onAdvPlayFinishListener != null) {
////                            onAdvPlayFinishListener.advPlayFinish(browseid, blAdv);
////                        }
//                        if (textView != null) {
//                            textView.setVisibility(View.VISIBLE);
//                        }
//                        break;
//                }
//            }
//        });
    }
}
